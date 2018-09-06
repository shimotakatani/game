package game.engine.objects.units;

import game.consts.*;
import game.engine.Game;
import game.engine.mechanics.Impl.MotivationMechanic;
import game.engine.mechanics.Impl.MovableMechanic;
import game.engine.objects.GameMap;
import game.engine.objects.GameMapCell;
import game.engine.objects.MapCellForPath;
import game.engine.tactor.Tactor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * create time 26.02.2018
 *
 * Класс зайца, который ест траву
 * @author nponosov
 */
public class Rabbit extends GenericUnit{

    Random random = new Random();

    public int eatedGrass = 0;
    public int x = 0;
    public int y = 0;
    private int previousX = 0;
    private int previousY = 0;
    public int direction = DirectionConst.E;
    public Long clientId = 0L;
    public String name = "";
    public int tacticId = TacticTypeConst.RABBIT_ONE_RANGE_RANDOM_EAT;
    private List<MapCellForPath> path = new ArrayList<>();
    private int currentActionPicture = ActionConst.UNKNOWN;

    /*
    * Необходимость в сне. От MIN_NEED_SLEEP до MAX_NEED_SLEEP. MIN_NEED_SLEEP - не нужно, MAX_NEED_SLEEP - валится с ног.
    * */
    private int needSleeping = AnimalStatConst.MIN_NEED_SLEEP;
    /**
     * Спал ли заяц прошлый ход
     */
    private boolean lastSleep = false;


    private void eatGrass(GameMapCell cell, Tactor tactor){
        synchronized (cell){
            if (cell.plant != PlantTypeConst.NO_PLANT) {
                cell.plant = PlantTypeConst.NO_PLANT;
                cell.eatedAtTime = tactor.getInnerTime();
                eatedGrass++;
                this.setCurrentActionPicture(ActionConst.EAT);
            }
        }
    }

    public void doTact(Game game){
        if (MotivationMechanic.sleepNow(this, game)) {
            doSleep();
        } else {
            this.lastSleep = false;
            switch (this.tacticId) {
                case TacticTypeConst.RABBIT_RANGED_RANDOM_EAT:
                    doRangedRandomEatTactic(game);
                    break;
                case TacticTypeConst.RABBIT_ONE_RANGE_RANDOM_EAT:
                    doOneRangeRandomEatTactic(game);
                    break;
                default:
                    doOneRangeRandomEatTactic(game);
                    break;
            }
        }

    }

    private void doSleep(){
        this.setNeedSleeping(this.getNeedSleeping() + AnimalStatConst.AnimalTacticCost.SLEEP);
        this.lastSleep = true;
        this.setCurrentActionPicture(ActionConst.SLEEP);
    }

    private void doOneRangeRandomEatTactic(Game game){
        if (game.map.getCell(x, y).plant != PlantTypeConst.NO_PLANT){
            eatGrass(game.map.getCell(x, y), game.tactor);
        } else {
            changeDirection(game);
            goForvard(game.map.capacity);
            if (random.nextInt(2) == 1) {
                this.setNeedSleeping(this.getNeedSleeping() + AnimalStatConst.AnimalTacticCost.RANDOM);
            }

        }
    }

    private void setPathWithCalc(GameMap map, int startX, int startY, int endX, int endY, Rabbit thisRabbit){
        thisRabbit.path = MovableMechanic.findPathWidth(map, startX, startY, endX, endY);
    }

    private void doRangedRandomEatTactic(Game game){
        Rabbit thisRabbit = this;
        thisRabbit.path = new ArrayList<>();
        if (game.map.getCell(x, y).plant != PlantTypeConst.NO_PLANT){
            eatGrass(game.map.getCell(x, y), game.tactor);
        } else {
            GameMapCell nearestPlantCell = MovableMechanic.getNearestAnyPlant(game.map, x, y, CommonConst.DEFAULT_RABBIT_MAX_RANGE);
            if (nearestPlantCell != null) {
                ExecutorService executor = Executors.newFixedThreadPool(1);

                Future<?> future = executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        setPathWithCalc(game.map, x, y, nearestPlantCell.x, nearestPlantCell.y, thisRabbit);            //        <-- your job
                    }
                });

                executor.shutdown();            //        <-- reject all further submissions

                try {
                    future.get(1, TimeUnit.SECONDS);  //     <-- wait 8 seconds to finish
                } catch (InterruptedException e) {    //     <-- possible error cases
                    System.out.println("job was interrupted");
                } catch (ExecutionException e) {
                    System.out.println("caught exception: " + e.getCause());
                } catch (TimeoutException e) {
                    future.cancel(true);              //     <-- interrupt the job
                    System.out.println("timeout");
                }

                if (!path.isEmpty()) {
                    int maybeDirection = MovableMechanic.getDirectionByTwoCells(x, y, path.get(1).x, path.get(1).y);
                    if (maybeDirection >= DirectionConst.E && maybeDirection <= DirectionConst.SE){
                        direction = maybeDirection;
                        goForvard(game.map.capacity);
                        if (random.nextInt(2) == 1) {
                            this.setNeedSleeping(this.getNeedSleeping() + AnimalStatConst.AnimalTacticCost.WIDTH);
                        }
                        return;
                        //в самом конце верного пути надо return
                    }
                }
            }
            //default: то есть в случае любого косяка основного метода
            doOneRangeRandomEatTactic(game);
        }
    }

    private void changeDirection(Game game){
        direction = getDirectionToNearestGrass(game);
        if (direction > -1) return;
        do {
            direction = random.nextInt(DirectionConst.DIRECTION_SIZE);
        } while (!canGoTo(direction, game));
    }

    private boolean canGoTo(int direction, Game game){
        if (direction == DirectionConst.E || direction == DirectionConst.NE || direction == DirectionConst.SE){
            if (y == game.map.capacity) return false;
        }
        if (direction == DirectionConst.W || direction == DirectionConst.NW || direction == DirectionConst.SW){
            if (y == 0) return false;
        }
        if (direction == DirectionConst.S || direction == DirectionConst.SE || direction == DirectionConst.SW){
            if (x == game.map.capacity) return false;
        }
        if (direction == DirectionConst.N || direction == DirectionConst.NE || direction == DirectionConst.NW){
            if (x == 0) return false;
        }
        GameMapCell cell = MovableMechanic.getMapCellByDirection(game.map, direction, x, y);
        if (cell.ground == GroundTypeConst.WALL) return false;
        if (MovableMechanic.hasAnybodyOnCell(game, cell.x, cell.y)) return false;

        //возможно будут ещё условия
        return true;
    }

    private void goForvard(int capacity){
        switch (direction){
            case DirectionConst.E: setY(Math.min(capacity-1, y+1)); break;
            case DirectionConst.NE: {
                setY(Math.min(capacity-1, y+1));
                setX(Math.max(0, x-1));
                break;
            }
            case DirectionConst.N: setX(Math.max(0, x-1)); break;
            case DirectionConst.NW: {
                setY(Math.max(0, y-1));
                setX(Math.max(0, x-1));
                break;
            }
            case DirectionConst.W: setY(Math.max(0, y-1)); break;
            case DirectionConst.SW: {
                setY(Math.max(0, y-1));
                setX(Math.min(capacity-1, x+1));
                break;
            }
            case DirectionConst.S: setX(Math.min(capacity-1, x+1)); break;
            case DirectionConst.SE: {
                setY(Math.min(capacity-1, y+1));
                setX(Math.min(capacity-1, x+1));
                break;
            }
        }
        this.setCurrentActionPicture(ActionConst.MOVE);
    }

    /**
     * Получить направление к клетке с травой, если такая есть в радиусе 1 клетки
     * @param game - игра из которой дёргаем карту и зайцев
     * @return направление
     * @author nponosov
     */
    private int getDirectionToNearestGrass(Game game){
        int direction = -1;
        GameMapCell cell;
        ArrayList<Integer> directions = new ArrayList<>();
           while(directions.size() < DirectionConst.DIRECTION_SIZE){
                int i = random.nextInt(DirectionConst.DIRECTION_SIZE);
                if (directions.contains(i)) continue;
                directions.add(i);
                cell = MovableMechanic.getMapCellByDirection(game.map, i, x, y);
                if (MovableMechanic.hasAnybodyOnCell(game, cell.x, cell.y)) break;

                if (cell.plant != PlantTypeConst.NO_PLANT){
                    return i;
                }
            }
        return direction;
    }


    @Override
    public String toString() {
        return "\nЗаяц по имени " + name + " находится x:" + x + " y:" + y + "." + " Он съел " + eatedGrass + " пучков травы";
    }

    public void setX(int x){
        this.previousX = this.x;
        this.x = x;
    }

    public void setY(int y){
        this.previousY = this.y;
        this.y = y;
    }

    public int getEatedGrass() {
        return eatedGrass;
    }

    public void setEatedGrass(int eatedGrass) {
        this.eatedGrass = eatedGrass;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPreviousX() {
        return previousX;
    }

    public void setPreviousX(int previousX) {
        this.previousX = previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public void setPreviousY(int previousY) {
        this.previousY = previousY;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNeedSleeping() {
        return needSleeping;
    }

    public void setNeedSleeping(int needSleeping) {
        this.needSleeping = Math.max(Math.min(needSleeping, AnimalStatConst.MAX_NEED_SLEEP), AnimalStatConst.MIN_NEED_SLEEP);
    }

    public boolean isLastSleep() {
        return lastSleep;
    }

    public void setLastSleep(boolean lastSleep) {
        this.lastSleep = lastSleep;
    }

    public int getCurrentActionPicture() {
        return currentActionPicture;
    }

    public void setCurrentActionPicture(int currentActionPicture) {
        this.currentActionPicture = currentActionPicture;
    }
}

package game.engine.objects.units;

import game.consts.*;
import game.data.enums.ActionEnum;
import game.data.enums.PlantEnum;
import game.engine.Game;
import game.engine.mechanics.Impl.CostMechanic;
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
    private ActionEnum lastAction = ActionEnum.NO_ACTION;

    /**
     * Необходимость в сне. От MIN_NEED_SLEEP до MAX_NEED_SLEEP. MIN_NEED_SLEEP - не нужно, MAX_NEED_SLEEP - валится с ног.
     * */
    private int needSleeping = AnimalStatConst.MIN_NEED_SLEEP;

    //Текущая сытость. От 0 до maxFat
    private int fat = 100;
    //Максимальная сытость - пока константа, потом наверное должно мочь меняться
    private int maxFat = AnimalStatConst.MAX_FAT_RABBIT;

    //todo Тоже можно унести в MovableMechanic
    public void doOneRangeRandomEatTactic(Game game){
        changeDirection(game);
        goForvard(game.map.capacity);
        CostMechanic.setFromAction(this, ActionEnum.MOVE);
    }

    private void setPathWithCalc(GameMap map, int startX, int startY, int endX, int endY, Rabbit thisRabbit){
        thisRabbit.path = MovableMechanic.findPathWidth(map, startX, startY, endX, endY);
    }

    public void doRangedRandomEatTactic(Game game){
        Rabbit thisRabbit = this;
        thisRabbit.path = new ArrayList<>();
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

                if (!path.isEmpty() && path.size() > 1) {
                    int maybeDirection = MovableMechanic.getDirectionByTwoCells(x, y, path.get(1).x, path.get(1).y);
                    if (maybeDirection >= DirectionConst.E && maybeDirection <= DirectionConst.SE){
                        direction = maybeDirection;
                        goForvard(game.map.capacity);
                        CostMechanic.setFromAction(thisRabbit, ActionEnum.THINK_WIDTH);
                        CostMechanic.setFromAction(thisRabbit, ActionEnum.MOVE);
                        return;
                        //в самом конце верного пути надо return
                    }
                }
            }
            //default: то есть в случае любого косяка основного метода
            doOneRangeRandomEatTactic(game);
    }

    private void changeDirection(Game game){
        int oldDirection = direction;
        direction = getDirectionToNearestGrass(game);
        if (direction > -1) return;
        if (random.nextInt(5) == 0 || !MovableMechanic.canGoTo(this, oldDirection, game)) {
            do {
                direction = random.nextInt(DirectionConst.DIRECTION_SIZE);
            } while (!MovableMechanic.canGoTo(this, direction, game));
        } else {
            direction = oldDirection;
        }
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
        this.setLastAction(ActionEnum.MOVE);
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
        return getLastAction().equals(ActionEnum.SLEEP);
    }

    public boolean isLastRest() {
        return getLastAction().equals(ActionEnum.NO_ACTION);
    }

    public int getCurrentActionPicture() {
        return currentActionPicture;
    }

    public void setCurrentActionPicture(int currentActionPicture) {
        this.currentActionPicture = currentActionPicture;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = Math.max(Math.min(fat, this.getMaxFat()), 0);
    }

    public int getMaxFat() {
        return maxFat;
    }

    public void setMaxFat(int maxFat) {
        this.maxFat = maxFat;
    }

    public ActionEnum getLastAction() {
        return lastAction;
    }

    public void setLastAction(ActionEnum lastAction) {
        this.lastAction = lastAction;
    }
}

package game.engine.objects.units;

import game.consts.*;
import game.engine.Game;
import game.engine.mechanics.Impl.MovableMechanic;
import game.engine.objects.GameMapCell;
import game.engine.objects.MapCellForPath;
import game.engine.tactor.Tactor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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



    private void eatGrass(GameMapCell cell, Tactor tactor){
        synchronized (cell){
            if (cell.plant != PlantTypeConst.NO_PLANT) {
                cell.plant = PlantTypeConst.NO_PLANT;
                cell.eatedAtTime = tactor.getInnerTime();
                eatedGrass++;
            }
        }
    }

    public void doTact(Game game){
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

    private void doOneRangeRandomEatTactic(Game game){
        if (game.map.getCell(x, y).plant != PlantTypeConst.NO_PLANT){
            eatGrass(game.map.getCell(x, y), game.tactor);
        } else {
            changeDirection(game);
            goForvard(game.map.capacity);

        }
    }

    private void doRangedRandomEatTactic(Game game){
        if (game.map.getCell(x, y).plant != PlantTypeConst.NO_PLANT){
            eatGrass(game.map.getCell(x, y), game.tactor);
        } else {
            GameMapCell nearestPlantCell = MovableMechanic.getNearestAnyPlant(game.map, this.x, this.y, CommonConst.DEFAULT_RABBIT_MAX_RANGE);
            if (nearestPlantCell != null) {
                List<MapCellForPath> path = MovableMechanic.findPathWidth(game.map, x, y, nearestPlantCell.x, nearestPlantCell.y);
                if (!path.isEmpty()) {
                    int maybeDirection = MovableMechanic.getDirectionByTwoCells(x, y, path.get(0).x, path.get(0).y);
                    if (maybeDirection > 0 && maybeDirection < 9){
                        direction = maybeDirection;
                        goForvard(game.map.capacity);
                        return;
                        //в самом конце верного пути надо return
                    }
                }
            }
        }
        //default: то есть в случае любого косяка основного метода
        doOneRangeRandomEatTactic(game);
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
            if (x == game.map.capacity) return false;
        }
        if (direction == DirectionConst.W || direction == DirectionConst.NW || direction == DirectionConst.SW){
            if (x == 0) return false;
        }
        if (direction == DirectionConst.S || direction == DirectionConst.SE || direction == DirectionConst.SW){
            if (y == game.map.capacity) return false;
        }
        if (direction == DirectionConst.N || direction == DirectionConst.NE || direction == DirectionConst.NW){
            if (y == 0) return false;
        }
        GameMapCell cell = MovableMechanic.getMapCellByDirection(game.map, direction, x, y);
        if (cell.ground == GroundTypeConst.WALL) return false;
        if (MovableMechanic.hasAnybodyOnCell(game, cell.x, cell.y)) return false;

        //возможно будут ещё условия
        return true;
    }

    private void goForvard(int capacity){
        switch (direction){
            case DirectionConst.E: setX(Math.min(capacity-1, x+1)); break;
            case DirectionConst.NE: {
                setX(Math.min(capacity-1, x+1));
                setY(Math.max(0, y-1));
                break;
            }
            case DirectionConst.N: setY(Math.max(0, y-1)); break;
            case DirectionConst.NW: {
                setX(Math.max(0, x-1));
                setY(Math.max(0, y-1));
                break;
            }
            case DirectionConst.W: setX(Math.max(0, x-1)); break;
            case DirectionConst.SW: {
                setX(Math.max(0, x-1));
                setY(Math.min(capacity-1, y+1));
                break;
            }
            case DirectionConst.S: y = Math.min(capacity-1, y+1); break;
            case DirectionConst.SE: {
                setX(Math.min(capacity-1, x+1));
                setY(Math.min(capacity-1, y+1));
                break;
            }
        }
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
}

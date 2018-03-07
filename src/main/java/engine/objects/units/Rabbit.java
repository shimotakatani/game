package engine.objects.units;

import consts.ColorConst;
import consts.DirectionConst;
import engine.Game;
import engine.mechanics.Impl.MovableMechanic;
import engine.objects.GameMapCell;
import engine.tactor.Tactor;

import java.util.ArrayList;
import java.util.Random;

/**
 * create time 26.02.2018
 *
 * Класс кролика, который ест траву
 * @author nponosov
 */
public class Rabbit {

    Random random = new Random();

    public int eatedGrass = 0;
    public int x = 0;
    public int y = 0;
    public int previousX = 0;
    public int previousY = 0;
    public int direction = DirectionConst.E;
    public Long clientId = 0L;


    private void eatGrass(GameMapCell cell, Tactor tactor){
        if (cell.color == ColorConst.GREEN) {
            cell.color = ColorConst.WHITE;
            cell.eatedAtTime = tactor.getInnerTime();
            eatedGrass++;
        }
    }

    public void doTact(Game game){
        if (game.map.getCell(y, x).color == ColorConst.GREEN){
            eatGrass(game.map.getCell(y, x), game.tactor);
        } else {
            changeDirection(game);
            goForvard(game.map.capacity);

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
        if (cell.color == ColorConst.WALL) return false;
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

                if (cell.color == ColorConst.GREEN){
                    return i;
                }
            }
        return direction;
    }


    @Override
    public String toString() {
        return "\nRabbit x:" + x + " y:" + y;
    }

    public void setX(int x){
        this.previousX = this.x;
        this.x = x;
    }

    public void setY(int y){
        this.previousY = this.y;
        this.y = y;
    }
}

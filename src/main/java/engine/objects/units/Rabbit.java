package engine.objects.units;

import consts.ColorConst;
import consts.DirectionConst;
import engine.objects.GameMap;
import engine.objects.GameMapCell;
import engine.tactor.Tactor;

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
    public int direction = DirectionConst.E;


    private void eatGrass(GameMapCell cell, Tactor tactor){
        if (cell.color == ColorConst.GREEN) {
            cell.color = ColorConst.WHITE;
            cell.eatedAtTime = tactor.getInnerTime();
            eatedGrass++;
        }
    }

    public void doTact(GameMap map, Tactor tactor){
        if (map.getCell(y, x).color == ColorConst.GREEN){
            eatGrass(map.getCell(y, x), tactor);
        } else {
            changeDirection(map.capacity);
            goForvard(map.capacity);
        }
    }

    private void changeDirection(int capacity){
        do {
            direction = random.nextInt(8);
        } while (!canGoTo(direction, capacity));
    }

    private boolean canGoTo(int direction, int capacity){
        if (direction == DirectionConst.E || direction == DirectionConst.NE || direction == DirectionConst.SE){
            if (x == capacity) return false;
        }
        if (direction == DirectionConst.W || direction == DirectionConst.NW || direction == DirectionConst.SW){
            if (x == 0) return false;
        }
        if (direction == DirectionConst.S || direction == DirectionConst.SE || direction == DirectionConst.SW){
            if (y == capacity) return false;
        }
        if (direction == DirectionConst.N || direction == DirectionConst.NE || direction == DirectionConst.NW){
            if (y == 0) return false;
        }
        return true;
    }

    private void goForvard(int capacity){
        switch (direction){
            case DirectionConst.E: x = Math.min(capacity-1, x+1); break;
            case DirectionConst.NE: {
                x = Math.min(capacity-1, x+1);
                y = Math.max(0, y-1);
                break;
            }
            case DirectionConst.N: y = Math.max(0, y-1); break;
            case DirectionConst.NW: {
                x = Math.max(0, x-1);
                y = Math.max(0, y-1);
                break;
            }
            case DirectionConst.W: x = Math.max(0, x-1); break;
            case DirectionConst.SW: {
                x = Math.max(0, x-1);
                y = Math.min(capacity-1, y+1);
                break;
            }
            case DirectionConst.S: y = Math.min(capacity-1, y+1); break;
            case DirectionConst.SE: {
                x = Math.min(capacity-1, x+1);
                y = Math.min(capacity-1, y+1);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "\nRabbit x:" + x + " y:" + y + " \n";
    }
}

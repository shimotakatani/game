package game.engine.mechanics.Impl;

import game.consts.DirectionConst;
import game.engine.Game;
import game.engine.objects.GameMap;
import game.engine.objects.GameMapCell;
import game.engine.objects.units.Rabbit;

/**
 * create time 06.03.2018
 *
 * @author nponosov
 */
public class MovableMechanic {

    public static GameMapCell getMapCellByDirection(GameMap map, int direction, int x, int y){
        switch (direction){
            case DirectionConst.E: x = Math.min(map.capacity-1, x+1); break;
            case DirectionConst.NE: {
                x = Math.min(map.capacity-1, x+1);
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
                y = Math.min(map.capacity-1, y+1);
                break;
            }
            case DirectionConst.S: y = Math.min(map.capacity-1, y+1); break;
            case DirectionConst.SE: {
                x = Math.min(map.capacity-1, x+1);
                y = Math.min(map.capacity-1, y+1);
                break;
            }
        }
        return map.getCell(y,x);
    }

    public static boolean hasAnybodyOnCell(Game game, int x, int y){
        for (Rabbit rabbit : game.rabbits) {
            if (rabbit.x == x && rabbit.y == y) return true;
        }
        return false;
    }
}

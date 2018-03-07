package engine.mechanics.Impl;

import consts.ColorConst;
import engine.objects.GameMap;
import engine.objects.GameOptions;
import engine.objects.units.Rabbit;

import java.util.Random;

/**
 * create time 07.03.2018
 *
 * @author nponosov
 */
public class InitMechanic {

    public static void initMap(GameMap map, GameOptions options){

        for(int i = 0; i < map.capacity; i++){
            if (i % 2 == 0) {
                map.getCell(i,i).color = ColorConst.WALL;
            }

        }

    }

    public static void initRabbit(GameMap map, Rabbit rabbit){
        Random random = new Random();
        int x,y;
        do {
            x = random.nextInt(map.capacity);
            y = random.nextInt(map.capacity);
        } while (map.getCell(y,x).color == ColorConst.WALL);
        rabbit.x = x;
        rabbit.y = y;
    }
}

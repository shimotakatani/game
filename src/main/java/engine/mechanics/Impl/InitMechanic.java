package engine.mechanics.Impl;

import consts.ColorConst;
import consts.CommonConst;
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

        Random random = new Random();
        //Инициализация стен
        int wallX, wallY, wallLength;
        boolean isVerticalWall;
        int countWalls = random.nextInt(CommonConst.WALL_MAX_COUNT) + 5;

        for (int i = 0; i < countWalls; i++) {
            isVerticalWall = random.nextBoolean();
            wallX = random.nextInt(map.capacity);
            wallY = random.nextInt(map.capacity);
            wallLength = random.nextInt(CommonConst.WALL_MAX_LENGTH);
            for (int j = 0; j < wallLength; j++) {
                map.getCell(wallY,wallX).color = ColorConst.WALL;

                if (isVerticalWall) {
                    wallY = Math.min(wallY + 1, map.capacity-1);
                } else {
                    wallX = Math.min(wallX + 1, map.capacity-1);
                }
            }
        }

        //Другие инициализации карты

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

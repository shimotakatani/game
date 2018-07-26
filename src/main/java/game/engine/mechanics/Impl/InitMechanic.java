package game.engine.mechanics.Impl;

import game.consts.CommonConst;
import game.consts.GroundTypeConst;
import game.consts.PlantTypeConst;
import game.engine.objects.GameMap;
import game.engine.objects.GameOptions;
import game.engine.objects.units.Rabbit;

import java.util.Random;

/**
 * create time 07.03.2018
 *
 * @author nponosov
 */
public class InitMechanic {

    /**
     * Инициализация карты
     * @param map - инициализируемая карта
     * @param options - параметры инициализации
     */
    public static void initMap(GameMap map, GameOptions options){

        Random random = new Random();

        //Инициализация начальной травы
        int grassX, grassY, grassCount;
        grassCount = random.nextInt(CommonConst.DISPERS_COUNT_GRASS * 2 + 1) + CommonConst.MIDDLE_COUNT_GRASS - CommonConst.DISPERS_COUNT_GRASS;

        for (int i = 0; i < grassCount; i++) {
            grassX = random.nextInt(map.capacity);
            grassY = random.nextInt(map.capacity);
            setRangedMapPlant(map, grassX, grassY, PlantTypeConst.GREEN, CommonConst.RANGE_GRASS);
        }

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
                map.getCell(wallX,wallY).ground = GroundTypeConst.WALL;

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
        } while (map.getCell(x,y).ground == GroundTypeConst.WALL);
        rabbit.x = x;
        rabbit.y = y;
    }

    /**
     * Принудительно затираем карту определённым цветом в радиусе
     * @param map - карта в которой затираем
     * @param centerX - номер столбца центра
     * @param centerY - номер строки центра
     * @param plantType - цвет
     * @param range - радиус
     */
    private static void setRangedMapPlant(GameMap map, int centerX, int centerY, int plantType, int range){
        int cellX, cellY;
        for (int i = 0; i < range * 2 + 1; i++) {
            for (int j = 0; j < range * 2 + 1; j++) {
                cellX = centerX + j - range;
                cellY = centerY + i - range;
                if ((cellX > 0 && cellX < map.capacity) && (cellY > 0 && cellY < map.capacity)){
                    if (range*range > ((i-range) * (i-range) + (j-range) * (j-range))) {
                        map.getCell(cellX,cellY).plant = plantType;
                    }
                }
            }
        }
    }
}

package engine.actions;

import consts.ColorConst;
import consts.CommonConst;
import engine.objects.GameMap;
import engine.objects.GameMapCell;
import engine.tactor.Tactor;

import java.util.Random;

/**
 * create time 27.02.2018
 *
 * @author nponosov
 */
public class GrassUp {

    public static void grassUp(GameMap map, Tactor tactor){
        Random random = new Random();
        random.setSeed(tactor.getInnerTime());
        //todo вставить включение по опциям игры
        randomGrassUp(random, map);
        uptimeGrassUp(map, tactor);
    }

    private static void randomGrassUp(Random random, GameMap map){
        if (random.nextInt(CommonConst.RANDOM_NUMBER_FOR_GRASS) == 0) {
            int x = random.nextInt(map.capacity);
            int y = random.nextInt(map.capacity);
            map.getCell(x, y).color = ColorConst.GREEN;
        }
    }

    private static void uptimeGrassUp(GameMap map, Tactor tactor){
        for (int i = 0; i < map.capacity; i++){

            for (GameMapCell mapCell : map.getRow(i)) {
                if ((mapCell.eatedAtTime + CommonConst.EAT_UP_TIME < tactor.getInnerTime()) && (mapCell.color != ColorConst.GREEN)){
                    mapCell.color = ColorConst.GREEN;
                }
            }
        }
    }
}

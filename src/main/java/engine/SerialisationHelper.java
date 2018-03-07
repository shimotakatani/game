package engine;

import consts.ColorConst;
import consts.CommonConst;
import engine.objects.GameMap;
import engine.objects.GameMapCell;

import java.util.Vector;

/**
 * create time 07.03.2018
 *
 * @author nponosov
 */
public class SerialisationHelper {


    public static String getGameSerialization(Game game) {
        GameMap tempMap = getMapCut(game.map, game.rabbit.x, game.rabbit.y);
        StringBuilder builder = new StringBuilder();

        builder.append(getMapSerialization(tempMap));
        builder.append(game.rabbit.toString());
        return builder.toString();
    }

    public static String getMapSerialization(GameMap map){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < map.capacity; i++){
            Vector<GameMapCell> row = map.getRow(i);
            for (int j = 0; j < map.capacity; j++) {
                builder.append(row.get(j).color);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private static GameMap getMapCut(GameMap map, int centerX, int centerY){
        GameMap resultMap = new GameMap(CommonConst.MAX_RANGE_RABBIT * 2 + 1);

        for (int i = 0; i < resultMap.capacity; i++) {
            for (int j = 0; j < resultMap.capacity; j++) {
                if (centerX - CommonConst.MAX_RANGE_RABBIT + j < 0) { //отрезаем края
                    setColorForOutMapCell(resultMap.getCell(j,i),Math.abs(centerX - CommonConst.MAX_RANGE_RABBIT + j + 1));
                    continue;
                }
                if (centerX - CommonConst.MAX_RANGE_RABBIT + j > map.capacity -1) {  //отрезаем края
                    setColorForOutMapCell(resultMap.getCell(j,i),Math.abs(centerX - CommonConst.MAX_RANGE_RABBIT + j - map.capacity));
                    continue;
                }
                if (centerY - CommonConst.MAX_RANGE_RABBIT + i < 0) { //отрезаем края
                    setColorForOutMapCell(resultMap.getCell(j,i),Math.abs(centerY - CommonConst.MAX_RANGE_RABBIT + i + 1));
                    continue;
                }
                if (centerY - CommonConst.MAX_RANGE_RABBIT + i > map.capacity -1) { //отрезаем края
                    setColorForOutMapCell(resultMap.getCell(j,i),Math.abs(centerY - CommonConst.MAX_RANGE_RABBIT + i - map.capacity));
                    continue;
                }
                if (i == CommonConst.MAX_RANGE_RABBIT && j == CommonConst.MAX_RANGE_RABBIT) {  //вставляем зайца
                    resultMap.getCell(j,i).color = ColorConst.RABBIT;
                    continue;
                }
                resultMap.getCell(j,i).color = map.getCell(centerY - CommonConst.MAX_RANGE_RABBIT + i, centerX - CommonConst.MAX_RANGE_RABBIT + j).color;

            }
        }

        return resultMap;
    }

    private static void setColorForOutMapCell(GameMapCell cell, int outRange){
        if (outRange == 0) {
            cell.color = ColorConst.WALL;
        } else {
            cell.color = ColorConst.GREEN;
        }
    }
}

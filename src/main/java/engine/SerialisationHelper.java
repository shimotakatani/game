package engine;

import consts.ColorConst;
import engine.objects.GameMapCell;

import java.util.Vector;

/**
 * create time 07.03.2018
 *
 * @author nponosov
 */
public class SerialisationHelper {


    public static String getGameSerialization(Game game) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < game.map.capacity; i++){
            Vector<GameMapCell> row = game.map.getRow(i);
            for (int j = 0; j < game.map.capacity; j++) {
                if ( ( j == game.rabbit.x ) && ( i == game.rabbit.y )){
                    builder.append(ColorConst.RABBIT);
                } else {
                    builder.append(row.get(j).color);
                }
            }
            builder.append("\n");
        }

        builder.append(game.rabbit.toString());
        return builder.toString();
    }
}

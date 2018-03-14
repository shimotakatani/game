package game.engine.objects;

import game.consts.ColorConst;
import game.consts.CommonConst;

/**
 * create time 26.02.2018
 *
 * Класс для ячейки карты
 * @author nponosov
 */
public class GameMapCell {

    public int color = ColorConst.WHITE;
    public long eatedAtTime = CommonConst.MAX_LENGTH_OF_LIFE;
    public int previousColor = ColorConst.WHITE;
    public int x = 0;
    public int y = 0;

    public GameMapCell(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setColor(int newColor){
        this.previousColor = this.color;
        this.color = newColor;
    };
}

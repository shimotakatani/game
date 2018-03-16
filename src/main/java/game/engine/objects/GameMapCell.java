package game.engine.objects;

import game.consts.CommonConst;
import game.consts.GroundTypeConst;
import game.consts.PlantTypeConst;

/**
 * create time 26.02.2018
 *
 * Класс для ячейки карты
 * @author nponosov
 */
public class GameMapCell {

    public int plant = PlantTypeConst.NO_PLANT;
    public int ground = GroundTypeConst.WHITE;
    public int color = GroundTypeConst.WHITE;
    public long eatedAtTime = CommonConst.MAX_LENGTH_OF_LIFE;
    public int previousColor = GroundTypeConst.WHITE;
    public int x = 0;
    public int y = 0;

    public GameMapCell(int x, int y){
        this.x = x;
        this.y = y;
        this.color = GroundTypeConst.WHITE;
    }
}

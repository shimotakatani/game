package engine.objects;

import consts.ColorConst;
import consts.CommonConst;

/**
 * create time 26.02.2018
 *
 * Класс для ячейки карты
 * @author nponosov
 */
public class GameMapCell {

    public int color = ColorConst.GREEN;
    public long eatedAtTime = CommonConst.MAX_LENGTH_OF_LIFE;
}

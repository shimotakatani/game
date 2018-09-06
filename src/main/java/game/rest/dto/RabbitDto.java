package game.rest.dto;

import game.consts.ActionConst;

/**
 * create time 20.03.2018
 *
 * @author nponosov
 */
public class RabbitDto {
    public int eatedGrass;
    public int x;
    public int y;
    public int direction;
    public Long clientId = 0L;
    public String name = "";
    public int tacticId = 0;
    public int needSleep = 0;
    public int currentAction = ActionConst.UNKNOWN;
}

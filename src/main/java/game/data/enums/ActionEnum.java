package game.data.enums;

import game.consts.ActionConst;

public enum ActionEnum {

    UNKNOWN(ActionConst.UNKNOWN, ActionConst.ActionEnergyCost.UNKNOWN, ActionConst.ActionSleepCost.UNKNOWN),
    NO_ACTION(ActionConst.NO_ACTION, ActionConst.ActionEnergyCost.NO_ACTION, ActionConst.ActionSleepCost.NO_ACTION),
    EAT(ActionConst.EAT, ActionConst.ActionEnergyCost.EAT, ActionConst.ActionSleepCost.EAT),
    MOVE(ActionConst.MOVE, ActionConst.ActionEnergyCost.MOVE, ActionConst.ActionSleepCost.MOVE),
    SLEEP(ActionConst.SLEEP, ActionConst.ActionEnergyCost.SLEEP, ActionConst.ActionSleepCost.SLEEP),
    THINK_WIDTH(ActionConst.THINK_WIDTH, ActionConst.ActionEnergyCost.THINK_WIDTH, ActionConst.ActionSleepCost.THINK_WIDTH);

    public int number;
    public int energyCost;
    public int sleepCost;

    ActionEnum(int number, int energyCost, int sleepCost){
        this.number = number;
        this.energyCost = energyCost;
        this.sleepCost = sleepCost;
    }

    public static ActionEnum getByNumber(int number){
        ActionEnum[] array = ActionEnum.values();
        for (int i =0; i< array.length; i++) {
            if (array[i].number == number) return array[i];
        }
        return UNKNOWN;
    }

}

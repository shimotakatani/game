package game.engine.mechanics.Impl;

import game.consts.ActionConst;
import game.data.enums.ActionEnum;
import game.engine.objects.units.Rabbit;

import java.util.Random;

public class CostMechanic {

    public static void setRabbitEnergy(Rabbit rabbit, ActionEnum action){

        Random random = new Random();

        int energy = (random.nextInt(ActionConst.ActionEnergyCost.BASE * 2) + action.energyCost - ActionConst.ActionEnergyCost.BASE) / 100;
        rabbit.setFat(rabbit.getFat() - energy);
    }

    public static void setRabbitSleep(Rabbit rabbit, ActionEnum action){

        Random random = new Random();

        int sleep = (random.nextInt(ActionConst.ActionSleepCost.BASE * 2) + action.sleepCost - ActionConst.ActionSleepCost.BASE) / 100;
        rabbit.setNeedSleeping(rabbit.getNeedSleeping() + sleep);
    }

    public static void setFromAction(Rabbit rabbit, ActionEnum action){
        setRabbitEnergy(rabbit, action);
        setRabbitSleep(rabbit, action);
    }
}

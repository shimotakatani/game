package game.engine.actions.Impl;

import game.consts.ActionConst;
import game.consts.AnimalStatConst;
import game.data.enums.ActionEnum;
import game.engine.Game;
import game.engine.actions.SleepAction;
import game.engine.mechanics.Impl.CostMechanic;
import game.engine.mechanics.Impl.MotivationMechanic;
import game.engine.objects.units.GenericUnit;
import game.engine.objects.units.Rabbit;

import java.util.Random;

public class SleepActionImpl extends GenericGoapActionImpl implements SleepAction {

    @Override
    public void doAction(Game game, GenericUnit actor) {
        if (actor instanceof Rabbit) {
            Rabbit rabbit = (Rabbit) actor;
            CostMechanic.setFromAction(rabbit, ActionEnum.SLEEP);
            rabbit.setLastAction(ActionEnum.SLEEP);
            rabbit.setCurrentActionPicture(ActionConst.SLEEP);
        } else {
            super.doAction(game, actor);
        }
    }

    @Override
    public Double calcCost(Game game, GenericUnit actor) {
        if (actor instanceof Rabbit) {
            Rabbit rabbit = (Rabbit) actor;
            //Безусловный сон при максимальной сонливости
            if (rabbit.getNeedSleeping() == AnimalStatConst.ABSOLUTELY_NEED_SLEEP) return -100.0;

            //Большая вероятность уснуть если всё нормально и большая сонливость
            if (rabbit.getNeedSleeping() > AnimalStatConst.ALMOSTLY_NEED_SLEEP) {
                Random random = new Random();
                int seed = random.nextInt(AnimalStatConst.BASE_RANDOM_SLEEPING);
                if (seed < AnimalStatConst.RANDOM_START_SLEEPING_PERCENT) return -5.0;
            }

            //Продолжение сна если всё нормально и сонливость не низкая
            if (rabbit.getNeedSleeping() > AnimalStatConst.CONTINUE_NEED_SLEEP && rabbit.isLastSleep()) {
                Random random = new Random();
                int seed = random.nextInt(AnimalStatConst.BASE_RANDOM_SLEEPING);
                if (seed < AnimalStatConst.RANDOM_SLEEPING_PERCENT) return -5.0;
            }

            // В любом другом случае не спим
            return 90.0;
        } else {
            return super.calcCost(game, actor);
        }
    }

    @Override
    public boolean isAvailable(Game game, GenericUnit actor) {
        if (actor instanceof Rabbit) {
            Rabbit rabbit = (Rabbit) actor;
            return ! MotivationMechanic.hasDanger(rabbit, game);
        } else {
            return super.isAvailable(game, actor);
        }
    }
}

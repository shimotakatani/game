package game.engine.actions.Impl;

import game.consts.ActionConst;
import game.consts.AnimalStatConst;
import game.data.enums.ActionEnum;
import game.engine.Game;
import game.engine.actions.RestAction;
import game.engine.mechanics.Impl.CostMechanic;
import game.engine.mechanics.Impl.MotivationMechanic;
import game.engine.objects.units.GenericUnit;
import game.engine.objects.units.Rabbit;

import java.util.Random;

public class RestActionImpl extends GenericGoapActionImpl implements RestAction {

    @Override
    public void doAction(Game game, GenericUnit actor) {
        if (actor instanceof Rabbit) {
            Rabbit rabbit = (Rabbit) actor;
            CostMechanic.setFromAction(rabbit, ActionEnum.NO_ACTION);
            rabbit.setLastAction(ActionEnum.NO_ACTION);
            rabbit.setCurrentActionPicture(ActionConst.NO_ACTION);
        } else {
            super.doAction(game, actor);
        }
    }

    @Override
    public Double calcCost(Game game, GenericUnit actor) {
        if (actor instanceof Rabbit) {
            Rabbit rabbit = (Rabbit) actor;
            if (rabbit.getFat() == AnimalStatConst.ABSOLUTELY_NEED_REST) return -95.0;

            //Большая вероятность отдыхать если всё нормально и большая сытость
            if (rabbit.getFat() > AnimalStatConst.ALMOSTLY_NEED_REST ) {
                Random random = new Random();
                int seed = random.nextInt(AnimalStatConst.BASE_RANDOM_REST);
                if (seed < AnimalStatConst.RANDOM_START_REST_PERCENT) return -3.0;
            }

            //Продолжение отдыха если всё нормально и энергия не низкая
            if (rabbit.getFat() > AnimalStatConst.CONTINUE_NEED_REST && rabbit.isLastRest()) {
                Random random = new Random();
                int seed = random.nextInt(AnimalStatConst.BASE_RANDOM_REST);
                if (seed < AnimalStatConst.RANDOM_REST_PERCENT) return -3.0;
            }

            // В любом другом случае не отдыхаем
            return 1.6;
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

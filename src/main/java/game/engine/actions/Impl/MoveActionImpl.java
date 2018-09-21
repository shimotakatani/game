package game.engine.actions.Impl;

import game.consts.AnimalStatConst;
import game.consts.DirectionConst;
import game.consts.TacticTypeConst;
import game.engine.Game;
import game.engine.actions.MoveAction;
import game.engine.mechanics.Impl.MovableMechanic;
import game.engine.objects.units.GenericUnit;
import game.engine.objects.units.Rabbit;

public class MoveActionImpl extends GenericGoapActionImpl implements MoveAction {

    @Override
    public void doAction(Game game, GenericUnit actor) {
        if (actor instanceof Rabbit) {
            Rabbit rabbit = (Rabbit) actor;
            switch (rabbit.tacticId) {
                case TacticTypeConst.RABBIT_RANGED_RANDOM_EAT:
                    rabbit.doRangedRandomEatTactic(game);
                    break;
                case TacticTypeConst.RABBIT_ONE_RANGE_RANDOM_EAT:
                    rabbit.doOneRangeRandomEatTactic(game);
                    break;
                default:
                    rabbit.doOneRangeRandomEatTactic(game);
                    break;
            }
        } else {
            super.doAction(game, actor);
        }
    }

    //Потом переписать, когда будет планирование на несколько ходов и стоимость будет зависеть от расстояния
    @Override
    public Double calcCost(Game game, GenericUnit actor) {
        if (actor instanceof Rabbit) {
            Rabbit rabbit = (Rabbit) actor;
            return 0.0 + (rabbit.getFat()/rabbit.getMaxFat()) + (rabbit.getNeedSleeping()/AnimalStatConst.ABSOLUTELY_NEED_SLEEP);
        } else {
            return super.calcCost(game, actor);
        }
    }

    @Override
    public boolean isAvailable(Game game, GenericUnit actor) {
        if (actor instanceof Rabbit) {
            Rabbit rabbit = (Rabbit) actor;
            for (int i = 0; i < DirectionConst.DIRECTION_SIZE; i++) {
                if (MovableMechanic.canGoTo(rabbit, i, game)) return true;
            }
            return false;
        } else {
            return super.isAvailable(game, actor);
        }
    }
}

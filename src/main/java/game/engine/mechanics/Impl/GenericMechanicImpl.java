package game.engine.mechanics.Impl;

import game.engine.Game;
import game.engine.actions.GenericGoapAction;
import game.engine.actions.Impl.EatActionImpl;
import game.engine.actions.Impl.MoveActionImpl;
import game.engine.actions.Impl.RestActionImpl;
import game.engine.actions.Impl.SleepActionImpl;
import game.engine.mechanics.GenericMechanic;
import game.engine.objects.units.Rabbit;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * create time 22.02.2018
 *
 * Реализация общей механики
 * @author nponosov
 */
public class GenericMechanicImpl implements GenericMechanic {

    private Set<GenericGoapAction> actions = new HashSet<>();
    public GenericMechanicImpl(){
        actions.add(new EatActionImpl());
        actions.add(new SleepActionImpl());
        actions.add(new RestActionImpl());
        actions.add(new MoveActionImpl());
    }

    public void doTact(Game game, Rabbit rabbit){
        List<GenericGoapAction> availableActions = actions.stream()
                .filter(genericGoapAction -> genericGoapAction.isAvailable(game, rabbit))
                .collect(Collectors.toList());

        if (availableActions.isEmpty()) return;

        GenericGoapAction minCostAction = availableActions.get(0);
        for (GenericGoapAction action:
             availableActions) {
            if (minCostAction.calcCost(game, rabbit) > action.calcCost(game, rabbit)) minCostAction = action;
        }

        if (minCostAction != null) minCostAction.doAction(game, rabbit);
    }
}

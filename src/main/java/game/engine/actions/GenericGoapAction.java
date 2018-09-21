package game.engine.actions;

import game.engine.Game;
import game.engine.objects.units.GenericUnit;

public interface GenericGoapAction extends GenericAction {

    Double calcCost(Game game, GenericUnit actor);

    boolean isAvailable(Game game, GenericUnit actor);
}

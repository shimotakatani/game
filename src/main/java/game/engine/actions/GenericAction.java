package game.engine.actions;

import game.engine.Game;
import game.engine.objects.units.GenericUnit;

/**
 * create time 22.02.2018
 *
 * Общее действие
 * @author nponosov
 */
public interface GenericAction {

    void doAction(Game game, GenericUnit actor);
}

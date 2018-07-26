package game.engine.actions;

import game.engine.Game;
import game.engine.objects.units.GenericUnit;

/**
 * Абстрактный класс для действий GOAP
 *
 * create time 07.04.2018
 *
 * @author nponosov
 */
public abstract class GenericGoapAction implements GenericAction{

    private ActionPrecondition precondition;

    private Double cost;

    @Override
    public void doAction(Game game, GenericUnit actor) {
        //Писать выполняемые действия сюда
    }

    public Double calcCost(Game game, GenericUnit actor){
        //Здесь писать функцию стоимости, которая может зависеть от состояния мира и состояния актора действия
        cost = 1.0;
        return cost;
    }


}

package engine.commands;

import engine.Game;
import engine.objects.units.Rabbit;

/**
 * create time 07.03.2018
 *
 * @author nponosov
 */
public class StartGameCommand extends AbstractCommand{

    public static void execute(Game game, Long clientId){

        if (game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(clientId)).count() == 0){
            Rabbit rabbit = new Rabbit();
            rabbit.clientId = clientId;
            game.rabbits.add(rabbit);
        }
    }
}

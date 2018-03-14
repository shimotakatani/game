package game.engine.commands;

import game.engine.Game;
import game.engine.objects.units.Rabbit;

/**
 * create time 09.03.2018
 *
 * @author nponosov
 */
public class NameCommand extends AbstractCommand{

    public static void execute(Game game, Long clientId, String name){

        if (game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(clientId)).count() != 0){
            Rabbit rabbit = game.rabbits.stream().filter(rabbit1 -> rabbit1.clientId.equals(clientId)).findFirst().get();
            if (name.length() > 0) {
                rabbit.name = name;
            }
        }
    }
}
package engine.commands;

import engine.Game;
import engine.objects.units.Rabbit;

import java.util.Random;

/**
 * create time 07.03.2018
 *
 * @author nponosov
 */
public class StartGameCommand extends AbstractCommand{

    public static void execute(Game game, Long clientId){

        if (game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(clientId)).count() == 0){
            Random random = new Random();
            Rabbit rabbit = new Rabbit();
            rabbit.clientId = clientId;
            rabbit.name = clientId + "";
            rabbit.setX(random.nextInt(game.map.capacity));
            rabbit.setY(random.nextInt(game.map.capacity));
            game.rabbits.add(rabbit);
        }
    }
}

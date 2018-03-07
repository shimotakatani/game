package engine.commands;

import engine.Game;
import engine.objects.units.Rabbit;

import java.util.stream.Collectors;

/**
 * create time 07.03.2018
 *
 * @author nponosov
 */
public class EndGameCommand extends AbstractCommand {

    public static void execute(Game game, Long clientId){

        game.rabbits = game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(clientId)).collect(Collectors.toList());
    }
}

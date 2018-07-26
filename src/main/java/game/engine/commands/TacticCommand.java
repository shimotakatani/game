package game.engine.commands;

import game.engine.Game;
import game.engine.objects.units.Rabbit;

public class TacticCommand extends AbstractCommand{

    public static void execute(Game game, Long clientId, int tacticId){

        if (game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(clientId)).count() != 0){
            Rabbit rabbit = game.rabbits.stream().filter(rabbit1 -> rabbit1.clientId.equals(clientId)).findFirst().get();
            rabbit.tacticId = tacticId;
        }
    }
}

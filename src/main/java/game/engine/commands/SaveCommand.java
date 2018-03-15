package game.engine.commands;

import game.data.entity.RabbitEntity;
import game.data.repositories.RabbitRepository;
import game.data.transformers.RabbitTransformer;
import game.engine.Game;
import game.engine.objects.units.Rabbit;

import java.util.List;

/**
 * create time 15.03.2018
 *
 * @author nponosov
 */
public class SaveCommand extends AbstractCommand {

    public static void execute(Game game, RabbitRepository repository){

        if (!game.rabbits.isEmpty()){
            game.rabbits.forEach(rabbit -> {
                List<RabbitEntity> entities = repository.findByClientId(rabbit.clientId);
                RabbitEntity entity = entities.isEmpty() ? new RabbitEntity() : entities.get(0);
                RabbitTransformer.objectToEntity(rabbit, entity);
                repository.save(entity);
            });
        }
    }
}

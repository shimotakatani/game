package game.engine.commands;

import game.data.entity.MapEntity;
import game.data.entity.RabbitEntity;
import game.data.repositories.MapRepository;
import game.data.repositories.RabbitRepository;
import game.data.transformers.MapTransformer;
import game.data.transformers.RabbitTransformer;
import game.engine.Game;

import java.util.List;

/**
 * create time 15.03.2018
 *
 * @author nponosov
 */
public class SaveCommand extends AbstractCommand {

    public static void execute(Game game, RabbitRepository rabbitRepository, MapRepository mapRepository){

        if (!game.rabbits.isEmpty()){
            game.rabbits.forEach(rabbit -> {
                List<RabbitEntity> entities = rabbitRepository.findByClientId(rabbit.clientId);
                RabbitEntity entity = entities.isEmpty() ? new RabbitEntity() : entities.get(0);
                RabbitTransformer.objectToEntity(rabbit, entity);
                rabbitRepository.save(entity);
            });
        }
        if (game.map != null) {
            List<MapEntity> mapEntityList = mapRepository.findByCapacity(game.map.capacity);
            MapEntity mapEntity = mapEntityList.isEmpty() ? new MapEntity() : mapEntityList.get(0);
            MapTransformer.objectToEntity(game.map, mapEntity);
            mapRepository.save(mapEntity);
        }
    }
}

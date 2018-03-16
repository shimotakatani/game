package game.engine.commands;

import game.data.entity.GameEntity;
import game.data.entity.MapEntity;
import game.data.entity.RabbitEntity;
import game.data.repositories.CommonRepository;
import game.data.repositories.GameRepository;
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

    public static void execute(Game game, CommonRepository commonRepository){

        //Сохраняем зайцев
        if (!game.rabbits.isEmpty()){
            game.rabbits.forEach(rabbit -> {
                List<RabbitEntity> entities = commonRepository.rabbitRepository.findByClientId(rabbit.clientId);
                RabbitEntity entity = entities.isEmpty() ? new RabbitEntity() : entities.get(0);
                RabbitTransformer.objectToEntity(rabbit, entity);
                commonRepository.rabbitRepository.save(entity);
            });
        }
        //Сохраняем карту
        if (game.map != null) {
            List<MapEntity> mapEntityList = commonRepository.mapRepository.findByCapacity(game.map.capacity);
            MapEntity mapEntity = mapEntityList.isEmpty() ? new MapEntity() : mapEntityList.get(0);
            MapTransformer.objectToEntity(game.map, mapEntity);
            commonRepository.mapRepository.save(mapEntity);
        }

        //Сохраняем игру(по первости только время из неё
        GameEntity gameEntity;
        if (commonRepository.gameRepository.findAll().iterator().hasNext()) {
            gameEntity = commonRepository.gameRepository.findAll().iterator().next();
        } else {
            gameEntity = new GameEntity();
        }
        gameEntity.gameTime = game.tactor.getInnerTime();
        commonRepository.gameRepository.save(gameEntity);
    }
}

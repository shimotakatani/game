package game.helper;

import game.consts.CommonConst;
import game.data.entity.GameEntity;
import game.data.entity.MapEntity;
import game.data.repositories.CommonRepository;
import game.data.repositories.GameRepository;
import game.data.repositories.MapRepository;
import game.data.repositories.RabbitRepository;
import game.data.transformers.MapTransformer;
import game.engine.Game;
import game.engine.SerialisationHelper;
import game.engine.objects.GameMap;
import game.engine.objects.GameOptions;
import game.rest.dto.MessageDto;

import java.util.ArrayList;
import java.util.List;

/**
 * create time 14.03.2018
 *
 * @author nponosov
 */
public class GameHelper {

    public static GameOptions startArgs;

    public static Game game;

    public static void startServer(CommonRepository commonRepository){
        if (game == null) {
            startArgs = new GameOptions();
            List<MapEntity> mapEntityList = new ArrayList<>();
            commonRepository.mapRepository.findAll().iterator().forEachRemaining(mapEntityList::add);
            if (mapEntityList.stream().filter(mapEntity -> mapEntity.capacity == CommonConst.MAP_CAPACITY).count() == 1) {
                MapEntity map = mapEntityList.stream().filter(mapEntity -> mapEntity.capacity == CommonConst.MAP_CAPACITY).findFirst().get();
                GameMap gameMap = new GameMap(CommonConst.MAP_CAPACITY);
                MapTransformer.entityToObject(gameMap, map);
                startArgs.startMap = gameMap;
            }
            Iterable<GameEntity> gameEntityIterable = commonRepository.gameRepository.findAll();
            if (gameEntityIterable.iterator().hasNext()){
                startArgs.startTime = gameEntityIterable.iterator().next().gameTime;
            }
            game = new Game(null, startArgs, commonRepository);
            game.run();
        }
    }

    public static MessageDto test(){
        return SerialisationHelper.getGameSerialization(game,0L);
    }
}

package game.helper;

import game.engine.Game;
import game.engine.SerialisationHelper;
import game.engine.objects.GameOptions;
import game.rest.MainResource;
import game.rest.dto.MessageDto;

import java.util.logging.Logger;

/**
 * create time 14.03.2018
 *
 * @author nponosov
 */
public class GameHelper {

    public static GameOptions startArgs;

    public static Game game;

    public static void startServer(){
        startArgs = new GameOptions();
        game = new Game(null, startArgs);
        game.run();
    }

    public static MessageDto test(){
        return new MessageDto(SerialisationHelper.getGameSerialization(game,0L), 1);
    }
}

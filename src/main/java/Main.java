import engine.Game;
import engine.SerialisationHelper;
import engine.objects.GameOptions;
import logger.SparkUtils;
import org.apache.log4j.BasicConfigurator;
import rest.MainResource;

import org.apache.log4j.Logger;
import rest.dto.MessageDto;

import static spark.Spark.get;
import static spark.Spark.port;

/**
 * create time 27.02.2018
 *
 * Метод для запуска всех служб и основных процессов
 * @author nponosov
 */
public class Main {
    public static Logger logger = Logger.getLogger(MainResource.class);

    public static GameOptions startArgs = new GameOptions();

    public static Game game = new Game(logger, startArgs);

    public static void main(String[] args) {


        BasicConfigurator.configure();

        SparkUtils.createServerWithRequestLog(logger);
        port(8090);

        get("/game", (req, res) -> test());
        //MainResource.publicResource();

//        Game game = new Game();
        Thread gameThread = new Thread(game);

        gameThread.start();

    }

    private static MessageDto test(){
        return new MessageDto(SerialisationHelper.getGameSerialization(game), 1);
    }
}

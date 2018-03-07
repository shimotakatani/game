import engine.Game;
import engine.SerialisationHelper;
import engine.commands.EndGameCommand;
import engine.commands.StartGameCommand;
import engine.objects.GameOptions;
import logger.SparkUtils;
import org.apache.log4j.BasicConfigurator;
import rest.MainResource;

import org.apache.log4j.Logger;
import rest.dto.MessageDto;
import spark.Request;

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

        get("/test", (req, res) -> test());
        get("/game", (req, res) -> game(req));
        get("/start", (req, res) -> start(req));
        get("/end", (req, res) -> end(req));
        //MainResource.publicResource();

//        Game game = new Game();
        Thread gameThread = new Thread(game);

        gameThread.start();

    }

    private static MessageDto test(){
        return new MessageDto(SerialisationHelper.getGameSerialization(game,0L), 1);
    }

    private static MessageDto game(Request request){
        Long clientId = 0L;
        String paramString = "chatId=";
        if (request.raw().getQueryString().contains(paramString)) {
            String chatIdString = request.raw().getQueryString().substring(request.raw().getQueryString().indexOf(paramString) + paramString.length());
            try {
                if (chatIdString.length() > 0) {
                    clientId = Long.parseLong(chatIdString);
                }
            } catch (NumberFormatException e) {
                logger.info("Ошибка получения chatId");
            }
        }
        return new MessageDto(SerialisationHelper.getGameSerialization(game, clientId), 1);
    }

    private static MessageDto start(Request request){
        Long clientId = 0L;
        String paramString = "chatId=";
        if (request.raw().getQueryString().contains(paramString)) {
            String chatIdString = request.raw().getQueryString().substring(request.raw().getQueryString().indexOf(paramString) + paramString.length());
            try {
                if (chatIdString.length() > 0) {
                    clientId = Long.parseLong(chatIdString);
                    StartGameCommand.execute(game, clientId);
                }
            } catch (NumberFormatException e) {
                logger.info("Ошибка получения chatId");
            }
        }
        //todo дописать обратное сообщение
        return new MessageDto(SerialisationHelper.getGameSerialization(game, clientId), 1);
    }

    private static MessageDto end(Request request){
        Long clientId = 0L;
        String chatIdString = request.params().get("chatId");
        if (chatIdString.length() > 0) {
            clientId = Long.parseLong(chatIdString);
            EndGameCommand.execute(game, clientId);
        }
        //todo дописать обратное сообщение
        return new MessageDto(SerialisationHelper.getGameSerialization(game, clientId), 1);
    }

}

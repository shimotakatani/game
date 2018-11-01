package game.rest;

import game.data.repositories.CommonRepository;
import game.data.repositories.GameRepository;
import game.data.repositories.MapRepository;
import game.data.repositories.RabbitRepository;
import game.engine.SerialisationHelper;
import game.engine.commands.EndGameCommand;
import game.engine.commands.NameCommand;
import game.engine.commands.SaveCommand;
import game.engine.commands.StartGameCommand;
import game.helper.GameHelper;
import game.socket.SubscribeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import game.rest.dto.MessageDto;


/**
 * create time 27.02.2018
 *
 * @author nponosov
 */
@RestController
@CrossOrigin
public class BotResource {

    @Autowired
    private RabbitRepository rabbitRepository;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    public SubscribeListener subscribeListener;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public MessageDto test(){
        return GameHelper.test();
    }

    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public MessageDto game(@RequestParam("chatId") Long clientId){
        return SerialisationHelper.getGameSerialization(GameHelper.game, clientId);
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public MessageDto start(@RequestParam("chatId") Long clientId){
        StartGameCommand.execute(GameHelper.game, clientId);
        return SerialisationHelper.getGameSerialization(GameHelper.game, clientId);
    }

    @RequestMapping(value = "/end", method = RequestMethod.GET)
    public MessageDto end(@RequestParam("chatId") Long clientId){
        EndGameCommand.execute(GameHelper.game, clientId);
        return SerialisationHelper.getGameSerialization(GameHelper.game, clientId);
    }

    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public MessageDto name(@RequestParam("chatId") Long clientId, @RequestParam("name") String name){
        NameCommand.execute(GameHelper.game, clientId, name);
        return new MessageDto(SerialisationHelper.getRabbitSerilization(GameHelper.game, clientId), 1L);
    }

    @RequestMapping(value = "/score", method = RequestMethod.GET)
    public MessageDto score(@RequestParam("chatId") Long clientId){
        return new MessageDto(SerialisationHelper.getGeneralScoreSerilization(GameHelper.game, clientId), 1L);
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public MessageDto save(){
        SaveCommand.execute(GameHelper.game, commonRepository);
        return new MessageDto("Сохранение зайцев успешно завершено", 1L);
    }

    @RequestMapping(value = "/startServer", method = RequestMethod.GET)
    public MessageDto startServer(){
        GameHelper.startServer(commonRepository, subscribeListener);
        return new MessageDto("Игра на сервере запущена", 1L);
    }


}

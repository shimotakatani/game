package game.rest;

import game.data.repositories.RabbitRepository;
import game.engine.SerialisationHelper;
import game.engine.commands.EndGameCommand;
import game.engine.commands.NameCommand;
import game.engine.commands.SaveCommand;
import game.engine.commands.StartGameCommand;
import game.helper.GameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import game.rest.dto.MessageDto;


/**
 * create time 27.02.2018
 *
 * @author nponosov
 */
@RestController
public class MainResource {

    @Autowired
    private RabbitRepository rabbitRepository;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public MessageDto test(){
        return GameHelper.test();
    }

    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public MessageDto game(@RequestParam("chatId") Long clientId){
        return new MessageDto(SerialisationHelper.getGameSerialization(GameHelper.game, clientId), 1);
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public MessageDto start(@RequestParam("chatId") Long clientId){
        StartGameCommand.execute(GameHelper.game, clientId);
        return new MessageDto(SerialisationHelper.getGameSerialization(GameHelper.game, clientId), 1);
    }

    @RequestMapping(value = "/end", method = RequestMethod.GET)
    public MessageDto end(@RequestParam("chatId") Long clientId){
        EndGameCommand.execute(GameHelper.game, clientId);
        return new MessageDto(SerialisationHelper.getGameSerialization(GameHelper.game, clientId), 1);
    }

    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public MessageDto name(@RequestParam("chatId") Long clientId, @RequestParam("name") String name){
        NameCommand.execute(GameHelper.game, clientId, name);
        return new MessageDto(SerialisationHelper.getRabbitSerilization(GameHelper.game, clientId), 1);
    }

    @RequestMapping(value = "/score", method = RequestMethod.GET)
    public MessageDto score(@RequestParam("chatId") Long clientId){
        return new MessageDto(SerialisationHelper.getGeneralScoreSerilization(GameHelper.game, clientId), 1);
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public MessageDto save(){
        SaveCommand.execute(GameHelper.game, rabbitRepository);
        return new MessageDto("Сохранение зайцев успешно завершено", 1);
    }

    @RequestMapping(value = "/startServer", method = RequestMethod.GET)
    public MessageDto startServer(){
        GameHelper.startServer(rabbitRepository);
        return new MessageDto("Игра на сервере запущена", 1);
    }


}

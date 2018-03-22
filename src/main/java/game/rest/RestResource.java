package game.rest;

import game.data.repositories.CommonRepository;
import game.data.transformers.MapTransformer;
import game.data.transformers.RabbitTransformer;
import game.engine.SerialisationHelper;
import game.engine.commands.EndGameCommand;
import game.engine.commands.NameCommand;
import game.engine.commands.SaveCommand;
import game.engine.commands.StartGameCommand;
import game.engine.objects.GameMap;
import game.engine.objects.units.Rabbit;
import game.helper.GameHelper;
import game.rest.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * create time 20.03.2018
 *
 * @author nponosov
 */
@RestController
@CrossOrigin
public class RestResource {
    @Autowired
    private CommonRepository commonRepository;

    @RequestMapping(value = "/rest/test", method = RequestMethod.GET)
    public MessageDto test(){
        return GameHelper.test();
    }

    @RequestMapping(value = "/rest/game", method = RequestMethod.GET)
    public AllDto game(@RequestParam("chatId") Long clientId){

        AllDto dto = new AllDto();

        List<Rabbit> rabbits =  GameHelper.game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(clientId)).collect(Collectors.toList());
        if (rabbits.size() != 1) return dto;
        int tempX = rabbits.get(0).x;
        int tempY = rabbits.get(0).y;
        GameMap tempMap = SerialisationHelper.getMapCut(GameHelper.game.map, tempX, tempY);

        GameDto gameDto = new GameDto();
        gameDto.innerTime = GameHelper.game.tactor.getInnerTime();

        MapDto mapDto = new MapDto();
        MapTransformer.objectToDto(tempMap, mapDto);

        List<RabbitDto> rabbitDtoList = new ArrayList<>();
        for (Rabbit rabbit : GameHelper.game.rabbits) {
            RabbitDto rabbitDto = new RabbitDto();
            RabbitTransformer.objectToDto(rabbit, rabbitDto);
            rabbitDtoList.add(rabbitDto);
        }

        dto.gameDto = gameDto;
        dto.mapDto = mapDto;
        dto.rabbitDtoList = rabbitDtoList;

        return dto;
    }

    @RequestMapping(value = "/rest/start", method = RequestMethod.GET)
    public MessageDto start(@RequestParam("chatId") Long clientId){
        StartGameCommand.execute(GameHelper.game, clientId);
        return new MessageDto("Ваша игра успешно запущена", clientId);
    }

    @RequestMapping(value = "/rest/end", method = RequestMethod.GET)
    public MessageDto end(@RequestParam("chatId") Long clientId){
        EndGameCommand.execute(GameHelper.game, clientId);
        return SerialisationHelper.getGameSerialization(GameHelper.game, clientId);
    }

    @RequestMapping(value = "/rest/name", method = RequestMethod.GET)
    public MessageDto name(@RequestParam("chatId") Long clientId, @RequestParam("name") String name){
        NameCommand.execute(GameHelper.game, clientId, name);
        return new MessageDto(SerialisationHelper.getRabbitSerilization(GameHelper.game, clientId), 1L);
    }

    @RequestMapping(value = "/rest/score", method = RequestMethod.GET)
    public MessageDto score(@RequestParam("chatId") Long clientId){
        return new MessageDto(SerialisationHelper.getGeneralScoreSerilization(GameHelper.game, clientId), 1L);
    }

    @RequestMapping(value = "/rest/save", method = RequestMethod.GET)
    public MessageDto save(){
        SaveCommand.execute(GameHelper.game, commonRepository);
        return new MessageDto("Сохранение зайцев успешно завершено", 1L);
    }

    @RequestMapping(value = "/rest/startServer", method = RequestMethod.GET)
    public MessageDto startServer(){
        GameHelper.startServer(commonRepository);
        return new MessageDto("Игра на сервере запущена", 1L);
    }

    @RequestMapping(value = "/rest/all", method = RequestMethod.GET)
    public AllDto all(){
        AllDto dto = new AllDto();

        GameDto gameDto = new GameDto();
        gameDto.innerTime = GameHelper.game.tactor.getInnerTime();

        MapDto mapDto = new MapDto();
        MapTransformer.objectToDto(GameHelper.game.map, mapDto);

        List<RabbitDto> rabbitDtoList = new ArrayList<>();
        for (Rabbit rabbit : GameHelper.game.rabbits) {
            RabbitDto rabbitDto = new RabbitDto();
            RabbitTransformer.objectToDto(rabbit, rabbitDto);
            rabbitDtoList.add(rabbitDto);
        }

        dto.gameDto = gameDto;
        dto.mapDto = mapDto;
        dto.rabbitDtoList = rabbitDtoList;

        return dto;
    }
}

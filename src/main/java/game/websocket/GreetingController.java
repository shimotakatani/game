package game.websocket;

import game.consts.CommonConst;
import game.data.repositories.CommonRepository;
import game.data.transformers.MapTransformer;
import game.data.transformers.RabbitTransformer;
import game.engine.SerialisationHelper;
import game.engine.objects.GameMap;
import game.engine.objects.units.Rabbit;
import game.helper.GameHelper;
import game.rest.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * create time 03.05.2018
 *
 * @author nponosov
 */
@Controller
public class GreetingController {

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public AllDto greeting(MessageDto message) throws Exception {

        AllDto dto = new AllDto();

        if (GameHelper.game == null) return dto;
        List<Rabbit> rabbits =  GameHelper.game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(0L)).collect(Collectors.toList());
        if (rabbits.size() != 1) return dto;
        int tempX = rabbits.get(0).x;
        int tempY = rabbits.get(0).y;
        GameMap tempMap = SerialisationHelper.getMapCut(GameHelper.game.map, tempX, tempY, CommonConst.MAX_RANGE_RABBIT);

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

    @Scheduled(fixedRate = 1000)
    @SendTo("/topic/greetings")
    public void sendMessage(){
        try {
            AllDto dto = this.greeting(new MessageDto());
            this.template.convertAndSend("/topic/greetings", dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package game.socket;

import game.consts.CommonConst;
import game.data.transformers.MapTransformer;
import game.data.transformers.RabbitTransformer;
import game.engine.Game;
import game.engine.SerialisationHelper;
import game.engine.objects.GameMap;
import game.engine.objects.units.Rabbit;
import game.helper.GameHelper;
import game.helper.SocketHelper;
import game.rest.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

    public final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public SubscribeListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        System.out.println("Received a new message");
        messagingTemplate.convertAndSend("/topic/{helloId}", "Last known error count");
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        System.out.println("Received a new web socket connection.");

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        if(userId != null) {
            System.out.println("User Disconnected : " + userId);

            MessageDto chatMessage = new MessageDto();
            chatMessage.chatId = Long.getLong(userId);
            SocketHelper.activeUserId.remove(Long.getLong(userId));

            messagingTemplate.convertAndSend("/topic/" + userId, chatMessage);
        }
    }

    public void sendToSubscribedUsers(){
        for (Long userId: SocketHelper.activeUserId) {

            // Отправка основных данных по сокетам
            AllDto dto = new AllDto();

            List<Rabbit> rabbits =  GameHelper.game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(userId)).collect(Collectors.toList());
            if (rabbits.size() != 1) return;
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

            messagingTemplate.convertAndSend("/topic/"+ userId, dto);


            // Добавить покаддровую отправку миникарты
        }
    }
}

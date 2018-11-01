package game.socket;

import game.engine.Game;
import game.helper.GameHelper;
import game.helper.SocketHelper;
import game.rest.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

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
        Game game = GameHelper.game;
        for (Long userId: SocketHelper.activeUserId) {
            MessageDto chatMessage = new MessageDto();
            chatMessage.chatId = userId;
            chatMessage.message = "every second message";
            messagingTemplate.convertAndSend("/topic/"+ userId, chatMessage);
        }
    }
}

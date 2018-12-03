package game.socket;


import game.helper.SocketHelper;
import game.rest.dto.MessageDto;
import game.rest.dto.SocketMessageDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = { "*" }, maxAge = 6000)
public class SocketController {

    @MessageMapping("/{helloId}")
    @SendTo("/topic/{helloId}")
    public SocketMessageDto addUser(MessageDto message,
                                     @DestinationVariable Long helloId) throws Exception {
        System.out.println("greeting : receive: " + message.toString());
        SocketHelper.activeUserId.add(helloId);
        return new SocketMessageDto("Hello, " + helloId + "!");
    }

    @MessageMapping("/delete/{helloId}")
    @SendTo("/topic/{helloId}")
    public SocketMessageDto deleteUser(MessageDto message,
                                    @DestinationVariable Long helloId) throws Exception {
        System.out.println("greeting : receive: " + message.toString());
        SocketHelper.activeUserId.remove(helloId);
        return new SocketMessageDto("By, " + helloId + "!");
    }

}
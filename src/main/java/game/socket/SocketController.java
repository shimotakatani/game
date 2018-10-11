package game.socket;


import game.rest.dto.MessageDto;
import game.rest.dto.SocketMessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

@Controller
@CrossOrigin
public class SocketController {

    @MessageMapping("/{helloId}")
    @SendTo("/topic/{helloId}")
    public SocketMessageDto greeting(MessageDto message) throws Exception {
        Thread.sleep(1000); // simulated delay
        System.out.println("receive: " + message.toString());
        return new SocketMessageDto("Hello, " + message.chatId + "!");
    }

}
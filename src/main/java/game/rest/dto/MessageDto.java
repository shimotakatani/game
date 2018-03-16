package game.rest.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Класс для передачи сообщений телеграмм-боту
 *
 * @author nponosov
 */
public class MessageDto {

    public String message;
    public Long chatId;
    public String mapString = "";
    public String timeString = "";

    public MessageDto(){

    }

    public MessageDto(String message, Long chatId){
        this.message = message;
        this.chatId = chatId;
    }

    @Override
    public String toString(){
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
        System.out.println("json " + jsonString);
        return jsonString;
    }
}
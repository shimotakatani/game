package rest.dto;

/**
 * Класс для передачи сообщений телеграмм-боту
 *
 * @author nponosov
 */
public class MessageDto {

    public String message;
    public int chatId;

    public MessageDto(){

    }

    public MessageDto(String message, int chatId){
        this.message = message;
        this.chatId = chatId;
    }

    @Override
    public String toString(){
        return this.message + " " + this.chatId;
    }
}
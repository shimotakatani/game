package game.rest.dto;

public class SocketMessageDto {

    private String content;

    public SocketMessageDto() {
    }

    public SocketMessageDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

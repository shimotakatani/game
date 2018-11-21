package game.rest.dto;

public class MapCadrDto {

    public Long number = 0L;
    public Long capacity = 0L;
    public String mapString = "";
    public boolean finalCadr = false;
    public Long mapTime = 0L;

    public MapCadrDto(){

    }

    public MapCadrDto(String mapString, Long capacity, Long number, boolean finalCadr, Long currentTime){
        this.mapString = mapString;
        this.capacity = capacity;
        this.number = number;
        this.finalCadr = finalCadr;
        this.mapTime = currentTime;
    }
}

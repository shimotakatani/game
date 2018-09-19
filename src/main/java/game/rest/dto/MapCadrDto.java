package game.rest.dto;

public class MapCadrDto {

    public Long number = 0L;
    public Long capacity = 0L;
    public String mapString = "";
    public boolean finalCadr = false;

    public MapCadrDto(){

    }

    public MapCadrDto(String mapString, Long capacity, Long number, boolean finalCadr){
        this.mapString = mapString;
        this.capacity = capacity;
        this.number = number;
        this.finalCadr = finalCadr;
    }
}

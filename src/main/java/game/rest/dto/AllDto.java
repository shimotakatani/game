package game.rest.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * create time 20.03.2018
 *
 * @author nponosov
 */
public class AllDto {

    public GameDto gameDto;

    public MapDto mapDto;

    public List<RabbitDto> rabbitDtoList = new ArrayList<>();
}

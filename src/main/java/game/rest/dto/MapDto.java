package game.rest.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * create time 20.03.2018
 *
 * @author nponosov
 */
public class MapDto {

    public Integer capacity;

    public List<MapCellDto> cells = new ArrayList<>();
}

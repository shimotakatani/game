package game.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * create time 22.03.2018
 *
 * @author nponosov
 */
@JsonIgnoreProperties
public class MapRequestDto {

    public int centerX;

    public int centerY;

    public int radius;
}

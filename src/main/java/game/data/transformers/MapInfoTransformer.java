package game.data.transformers;

import game.data.entity.MapEntity;
import game.rest.dto.MapInfoDto;

public class MapInfoTransformer {

    public static void entityToDto(MapEntity entity, MapInfoDto dto){
        if (dto == null) return;
        dto.capacity = entity.capacity;
        dto.id = entity.getId();
    }
}

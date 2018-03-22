package game.data.transformers;

import game.data.entity.MapCellEntity;
import game.engine.objects.GameMapCell;
import game.rest.dto.MapCellDto;

/**
 * create time 16.03.2018
 *
 * @author nponosov
 */
public class MapCellTransformer {

    public static void objectToEntity(GameMapCell object, MapCellEntity entity){
        if (entity == null) return;
        entity.x = object.x;
        entity.y = object.y;
        entity.ground = object.ground;
        entity.plant = object.plant;
        entity.eatedAtTime = object.eatedAtTime;
    }

    public static void entityToObject(GameMapCell object, MapCellEntity entity){
        if (object == null) return;
        object.x = entity.x;
        object.y = entity.y;
        object.ground = entity.ground;
        object.plant = entity.plant;
        object.eatedAtTime = entity.eatedAtTime;
    }

    public static void objectToDto(GameMapCell object, MapCellDto dto){
        if (dto == null) return;
        dto.x = object.x;
        dto.y = object.y;
        dto.ground = object.ground;
        dto.plant = object.plant;
        dto.eatedAtTime = object.eatedAtTime;
    }
}

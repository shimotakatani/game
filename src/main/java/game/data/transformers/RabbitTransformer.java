package game.data.transformers;

import game.data.entity.RabbitEntity;
import game.engine.objects.units.Rabbit;
import game.rest.dto.RabbitDto;

/**
 * Трансформер для сущности зайца в объект заяц
 * create time 15.03.2018
 *
 * @author nponosov
 */
public class RabbitTransformer {

    public static void objectToEntity(Rabbit object, RabbitEntity entity){
        if (entity == null) return;
        entity.clientId = object.clientId;
        entity.direction = object.direction;
        entity.x = object.x;
        entity.y = object.y;
        entity.eatedGrass = object.eatedGrass;
        entity.name = object.name;
        entity.tacticId = object.tacticId;
    }

    public static void entityToObject(Rabbit object, RabbitEntity entity){
        if (object == null) return;
        object.clientId = entity.clientId;
        object.direction = entity.direction;
        object.x = entity.x;
        object.y = entity.y;
        object.eatedGrass = entity.eatedGrass;
        object.name = entity.name;
        object.tacticId = entity.tacticId;
    }

    public static void objectToDto(Rabbit object, RabbitDto dto){
        if (dto == null) return;
        dto.clientId = object.clientId;
        dto.direction = object.direction;
        dto.x = object.x;
        dto.y = object.y;
        dto.eatedGrass = object.eatedGrass;
        dto.name = object.name;
        dto.tacticId = object.tacticId;
    }
}

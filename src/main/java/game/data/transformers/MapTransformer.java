package game.data.transformers;

import game.data.entity.MapCellEntity;
import game.data.entity.MapEntity;
import game.engine.objects.GameMap;
import game.engine.objects.GameMapCell;
import game.rest.dto.MapCellDto;
import game.rest.dto.MapDto;

/**
 * create time 16.03.2018
 *
 * @author nponosov
 */
public class MapTransformer {

    public static void objectToEntity(GameMap object, MapEntity entity){
        if (entity == null) return;
        entity.capacity = object.capacity;
        boolean hasCell;
        for (int i = 0; i < object.capacity; i++) {
            for (int j = 0; j < object.capacity; j++) {
                MapCellEntity cellEntity = entity.getMapCellByXY(i, j);
                if (cellEntity == null) {
                    cellEntity = new MapCellEntity();
                    hasCell = false;
                } else {
                    hasCell = true;
                }
                MapCellTransformer.objectToEntity(object.getCell(i, j), cellEntity);
                cellEntity.mapEntity = entity;
                if (!hasCell) entity.mapCellEntityList.add(cellEntity);
            }
        }
    }

    public static void entityToObject(GameMap object, MapEntity entity){
        if (object == null) return;
        object.capacity = entity.capacity;
        object.setId(entity.getId());
        for (int i = 0; i < object.capacity; i++) {
            for (int j = 0; j < object.capacity; j++) {
                MapCellEntity cellEntity = entity.getMapCellByXY(i, j);
                if (cellEntity == null) continue;
                object.getCell(i, j).ground = cellEntity.ground;
                object.getCell(i, j).plant = cellEntity.plant;
                object.getCell(i, j).eatedAtTime = cellEntity.eatedAtTime;
            }
        }
    }

    public static void objectToDto(GameMap object, MapDto dto){
        if (dto == null) return;
        dto.capacity = object.capacity;
        for (int i = 0; i < object.capacity; i++) {
            for (int j = 0; j < object.capacity; j++) {
                MapCellDto cellDto = new MapCellDto();
                MapCellTransformer.objectToDto(object.getCell(i, j), cellDto);
                dto.cells.add(cellDto);
            }
        }
    }
}

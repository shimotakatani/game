package game.data.transformers;

import game.data.entity.MapCellEntity;
import game.data.entity.MapEntity;
import game.engine.objects.GameMap;
import game.engine.objects.GameMapCell;

/**
 * create time 16.03.2018
 *
 * @author nponosov
 */
public class MapTransformer {

    public static void objectToEntity(GameMap object, MapEntity entity){
        if (entity == null) return;
        entity.setId(object.getId());
        entity.capacity = object.capacity;
        boolean hasCell;
        for (int i = 0; i < object.capacity; i++) {
            for (int j = 0; j < object.capacity; j++) {
                MapCellEntity cellEntity = entity.getMapCellByXY(j, i);
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
                MapCellEntity cellEntity = entity.getMapCellByXY(j, i);
                if (cellEntity == null) continue;
                object.getCell(i, j).color = cellEntity.color;
                object.getCell(i, j).eatedAtTime = cellEntity.eatedAtTime;
            }
        }
    }
}

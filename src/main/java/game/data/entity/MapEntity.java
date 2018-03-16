package game.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * create time 16.03.2018
 *
 * @author nponosov
 */
@Entity
@Table(name = "map")
public class MapEntity extends AbstractEntity {

    @Column(name = "capacity")
    public Integer capacity;

    @OneToMany(mappedBy = "mapEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<MapCellEntity> mapCellEntityList = new ArrayList<>();

    public MapCellEntity getMapCellByXY(int x, int y){
        if (mapCellEntityList.isEmpty()) return null;
        for (MapCellEntity cellEntity : mapCellEntityList) {
            if (cellEntity.x == x && cellEntity.y == y) return cellEntity;
        }
        return null;
    }

}

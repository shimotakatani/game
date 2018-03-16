package game.data.entity;

import javax.persistence.*;

/**
 * create time 16.03.2018
 *
 * @author nponosov
 */
@Entity
@Table(name = "map_cell")
public class MapCellEntity extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "map_id")
    public MapEntity mapEntity;

    @Column(name = "ground")
    public int color;

    @Column(name = "eated_at_time")
    public long eatedAtTime;

    @Column(name = "x")
    public int x = 0;

    @Column(name = "y")
    public int y = 0;

}

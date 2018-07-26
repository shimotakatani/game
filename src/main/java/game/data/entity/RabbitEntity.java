package game.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * create time 15.03.2018
 *
 * @author nponosov
 */
@Entity
@Table(name = "rabbit")
public class RabbitEntity extends AbstractEntity{

    @Column(name = "eated_grass")
    public int eatedGrass;

    @Column(name = "x")
    public int x;

    @Column(name = "y")
    public int y;

    @Column(name = "direction")
    public int direction;

    @Column(name = "clientid")
    public Long clientId;

    @Column(name = "name")
    public String name;

    @Column(name = "direction")
    public int tacticId;

    public RabbitEntity(){

    }
}

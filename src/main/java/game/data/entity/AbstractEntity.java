package game.data.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Абстрактная сущность с id
 * create time 15.03.2018
 *
 * @author nponosov
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = -3009157732242241606L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

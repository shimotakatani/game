package game.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * create time 16.03.2018
 *
 * @author nponosov
 */
@Entity
@Table(name = "game")
public class GameEntity extends AbstractEntity {

    /**
     * Количество тактов внутреннего времени с начала игры
     */
    @Column(name = "tacts_from_start")
    public Long gameTime;
}

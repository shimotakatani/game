package game.data.repositories;

import game.data.entity.GameEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * create time 16.03.2018
 *
 * @author nponosov
 */
public interface GameRepository extends CrudRepository<GameEntity, Long> {

}

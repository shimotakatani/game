package game.data.repositories;

import game.data.entity.RabbitEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

/**
 * create time 15.03.2018
 *
 * @author nponosov
 */
public interface RabbitRepository extends CrudRepository<RabbitEntity, Long> {

        List<RabbitEntity> findByClientId(Long clientId);

}

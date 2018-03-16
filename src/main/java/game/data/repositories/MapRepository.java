package game.data.repositories;

import game.data.entity.MapEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNullApi;

import java.util.List;

/**
 * create time 16.03.2018
 *
 * @author nponosov
 */
public interface MapRepository  extends CrudRepository<MapEntity, Long> {

    List<MapEntity> findByCapacity(Integer capacity);
}

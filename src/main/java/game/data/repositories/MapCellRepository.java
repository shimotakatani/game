package game.data.repositories;

import game.data.entity.MapCellEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * create time 16.03.2018
 *
 * @author nponosov
 */
public interface MapCellRepository extends CrudRepository<MapCellEntity, Long> {

}

package game.data.repositories;

import game.data.entity.SessionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SessionRepository extends CrudRepository<SessionEntity, Long> {

    List<SessionEntity> getByUsernameAndActiveToTimeAfter(String username, Long activeTime);
}

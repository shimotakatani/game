package game.data.repositories;

import game.data.entity.HistorySessionEntity;
import org.springframework.data.repository.CrudRepository;

public interface HistorySessionRepository  extends CrudRepository<HistorySessionEntity, Long> {
}

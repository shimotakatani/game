package game.data.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * create time 16.03.2018
 *
 * @author nponosov
 */
@Service
public class CommonRepository {

    @Autowired
    public RabbitRepository rabbitRepository;

    @Autowired
    public MapRepository mapRepository;

    @Autowired
    public GameRepository gameRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public SessionRepository sessionRepository;

    @Autowired
    public HistorySessionRepository historySessionRepository;
}

package game;

import game.data.repositories.*;
import game.helper.GameHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * create time 14.03.2018
 *
 * @author nponosov
 */
@SpringBootApplication
@EnableScheduling
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(CommonRepository commonRepository) {
        return (args) -> {

            GameHelper.startServer(commonRepository);
        };
    }
}

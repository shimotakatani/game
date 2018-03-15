package game.engine;

import game.consts.CommonConst;
import game.data.entity.RabbitEntity;
import game.data.repositories.RabbitRepository;
import game.data.transformers.RabbitTransformer;
import game.engine.actions.GrassUp;
import game.engine.mechanics.Impl.InitMechanic;
import game.engine.objects.GameMap;
import game.engine.objects.GameOptions;
import game.engine.objects.GameStats;
import game.engine.objects.units.Rabbit;
import game.engine.tactor.Tactor;
import org.apache.log4j.Logger;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * create time 22.02.2018
 *
 * Класс для запуска игры(симуляции)
 * @author nponosov
 */
public class Game implements Runnable {

    public GameOptions startArgs;
    public GameStats stats;
    public Tactor tactor;

    public List<Rabbit> rabbits = new ArrayList<>();
    public GameMap map;
    public Logger logger;

    public Game(Logger logger, GameOptions startArgs, RabbitRepository repository){
        this.logger = logger;
        this.startArgs = startArgs;
        this.stats = new GameStats();
        this.tactor = new Tactor();

        List<RabbitEntity> rabbitsFromDB = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(rabbitsFromDB::add);
        rabbitsFromDB.forEach(rabbitFromDB -> {
            Rabbit rabbit = new Rabbit();
            RabbitTransformer.entityToObject(rabbit, rabbitFromDB);
            rabbits.add(rabbit);
        });
        this.map = new GameMap(CommonConst.MAP_CAPACITY);
        InitMechanic.initMap(this.map, this.startArgs);
        InitMechanic.initRabbit(this.map, this.rabbits.get(0));
    }

    @Override
    public void run() {

        while (!stats.isFinish) {
            //Обработка шагов игры
            tactor.nextTact(); //переходим на следующий шаг
            if (!rabbits.isEmpty()) {
                rabbits.forEach(rabbit -> rabbit.doTact(this));
            }
            GrassUp.grassUp(map, tactor);
            try {
                Thread.sleep(CommonConst.SLEEP_TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


}
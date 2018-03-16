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

    /**
     * Стартовые аргументы
     */
    public GameOptions startArgs;
    /**
     * Изменяемые опции игры (например, завершать ли её следующим тактом
     */
    public GameStats stats;
    /**
     * Класс для доступа к внутреннему времени игры
     */
    public Tactor tactor;

    /**
     * Зайцы(раз пока игра соредоточенна только на них
     */
    public List<Rabbit> rabbits = new ArrayList<>();

    /**
     * Игровая карта
     */
    public GameMap map;

    /**
     * Для спама
     */
    public Logger logger;

    /**
     * Конструктор + инициализация игры
     * Подумать, целесообразно ли вместе держать
     * @param logger - для спама
     * @param startArgs - объект для стартовых аргументов(первой карта пошла, потом и константы можно будет перетащить)
     * @param repository - Репозиторий для вытаскивания зайцев, наверное потом надо буедт какой-то 1 бин для всех репозиториев
     * @author nponosov
     */
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
        if (startArgs.startMap != null) {
            this.map = startArgs.startMap;
        } else {
            this.map = new GameMap(CommonConst.MAP_CAPACITY);
            InitMechanic.initMap(this.map, this.startArgs);
        }
        if (!this.rabbits.isEmpty()) {
            this.rabbits.forEach(rabbit -> InitMechanic.initRabbit(this.map, rabbit));
        }
    }

    /**
     * Основной цикл игры
     * @author nponosov
     */
    @Override
    public void run() {

        while (!stats.isFinish) {
            //Обработка шагов игры
            tactor.nextTact(); //переходим на следующий шаг
            if (!rabbits.isEmpty()) {
                rabbits.parallelStream().forEach(rabbit -> rabbit.doTact(this));
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
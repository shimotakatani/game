package engine;

import consts.CommonConst;
import engine.actions.GrassUp;
import engine.mechanics.Impl.InitMechanic;
import engine.objects.GameMap;
import engine.objects.GameOptions;
import engine.objects.GameStats;
import engine.objects.units.Rabbit;
import engine.tactor.Tactor;
import org.apache.log4j.Logger;

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

    public Game(Logger logger, GameOptions startArgs){
        this.logger = logger;
        this.startArgs = startArgs;
        this.stats = new GameStats();
        this.tactor = new Tactor();

        this.rabbits.add(new Rabbit());
        this.map = new GameMap(CommonConst.MAP_CAPACITY);
        InitMechanic.initMap(this.map, this.startArgs);
        InitMechanic.initRabbit(this.map, this.rabbits.get(0));
    }

    @Override
    public void run() {

        while (!stats.isFinish) {
            //Обработка шагов игры
            tactor.nextTact(); //переходим на следующий шаг
            rabbits.forEach(rabbit -> rabbit.doTact(this));
            GrassUp.grassUp(map, tactor);
            try {
                Thread.sleep(CommonConst.SLEEP_TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


}
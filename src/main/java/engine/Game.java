package engine;

import consts.ColorConst;
import consts.CommonConst;
import engine.actions.GrassUp;
import engine.mechanics.Impl.InitMechanic;
import engine.objects.GameMap;
import engine.objects.GameMapCell;
import engine.objects.GameOptions;
import engine.objects.GameStats;
import engine.objects.units.Rabbit;
import engine.tactor.Tactor;
import org.apache.log4j.Logger;

import java.util.Vector;

/**
 * create time 22.02.2018
 *
 * Класс для запуска игры(симуляции)
 * @author nponosov
 */
public class Game implements Runnable {

    GameOptions startArgs;
    GameStats stats;
    Tactor tactor;

    Rabbit rabbit;
    GameMap map;
    Logger logger;

    public Game(Logger logger, GameOptions startArgs){
        this.logger = logger;
        this.startArgs = startArgs;
        this.stats = new GameStats();
        this.tactor = new Tactor();

        this.rabbit = new Rabbit();
        this.map = new GameMap(CommonConst.MAP_CAPACITY);
        InitMechanic.initMap(this.map, this.startArgs);
        InitMechanic.initRabbit(this.map, this.rabbit);
    }

    @Override
    public void run() {

        while (!stats.isFinish) {
            //Обработка шагов игры
            tactor.nextTact(); //переходим на следующий шаг
            rabbit.doTact(map, tactor);
            GrassUp.grassUp(map, tactor);
            try {
                Thread.sleep(CommonConst.SLEEP_TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


}
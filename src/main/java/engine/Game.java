package engine;

import engine.objects.GameMap;
import engine.objects.GameOptions;
import engine.objects.GameStats;
import engine.objects.units.Rabbit;
import engine.tactor.Tactor;

/**
 * create time 22.02.2018
 *
 * Класс для запуска игры(симуляции)
 * @author nponosov
 */
public class Game implements Runnable {

    GameOptions startArgs = new GameOptions();
    GameStats stats = new GameStats();
    Tactor tactor = new Tactor();

    Rabbit rabbit = new Rabbit();
    GameMap map = new GameMap(5);


    @Override
    public void run() {

        while (!stats.isFinish) {
            //Обработка шагов игры
            tactor.nextTact(); //переходим на следующий шаг
            rabbit.doTact(map);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public String getGameSerilization() {
        return map.getMapSerilization();
    }
}
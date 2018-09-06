package game.engine.mechanics.Impl;

import game.consts.AnimalStatConst;
import game.engine.Game;
import game.engine.objects.units.Rabbit;

import java.util.Random;

public class MotivationMechanic {

    public static boolean hasDanger(Rabbit rabbit, Game game){
        //Добавить потом здесь логику, которая необходима для прерывания сна
        return false;
    }

    public static boolean sleepNow(Rabbit rabbit, Game game){

        //Безусловный сон при максимальной сонливости
        if (rabbit.getNeedSleeping() == AnimalStatConst.ABSOLUTELY_NEED_SLEEP) return true;

        //Большая вероятность уснуть если всё нормально и большая сонливость
        if (rabbit.getNeedSleeping() > AnimalStatConst.ALMOSTLY_NEED_SLEEP && !hasDanger(rabbit, game)) {
            Random random = new Random();
            int seed = random.nextInt(AnimalStatConst.BASE_RANDOM_SLEEPING);
            if (seed < AnimalStatConst.RANDOM_SLEEPING_PERCENT) return true;
        }

        //Продолжение сна если всё нормально и сонливость не низкая
        if (rabbit.getNeedSleeping() > AnimalStatConst.CONTINUE_NEED_SLEEP && !hasDanger(rabbit, game) && rabbit.isLastSleep()) {
            Random random = new Random();
            int seed = random.nextInt(AnimalStatConst.BASE_RANDOM_SLEEPING);
            if (seed < AnimalStatConst.RANDOM_SLEEPING_PERCENT) return true;
        }

        // В любом другом случае не спим
        return false;
    }
}

package consts;

/**
 * create time 22.02.2018
 *
 * Константы уровня приложения
 * @author nponosov
 */
public class CommonConst {

    //время через которое в любом случае отрастает трава
    public static final long EAT_UP_TIME = 1280L;
    //знаменатель вероятности, с которой обновляем случайно выбранную лужайку травы
    public static final int RANDOM_NUMBER_FOR_GRASS = 20;
    //Размерность карты
    public static final int MAP_CAPACITY = 20;
    //Время сна между шагами(тактами) игры
    public static final long SLEEP_TIME_OUT = 1000L;
    //Максимальное количество стен
    public static final int WALL_MAX_COUNT = 10;
    //Максимальная длинна стены
    public static final int WALL_MAX_LENGTH = 8;
}

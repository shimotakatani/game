package game.consts;

/**
 * create time 22.02.2018
 *
 * Константы уровня приложения
 * @author nponosov
 */
public class CommonConst {

    //время через которое в любом случае отрастает трава
    public static final long EAT_UP_TIME = 12800L;
    //знаменатель вероятности, с которой обновляем случайно выбранную лужайку травы
    public static final int RANDOM_NUMBER_FOR_GRASS = 5;
    //Размерность карты
    public static final int MAP_CAPACITY = 200;
    //Время сна между шагами(тактами) игры
    public static final long SLEEP_TIME_OUT = 1000L;
    //Максимальное количество стен
    public static final int WALL_MAX_COUNT = 60;
    //Максимальная длинна стены
    public static final int WALL_MAX_LENGTH = 20;
    //Максимальное время жизни объектов мира в тактах
    public static final long MAX_LENGTH_OF_LIFE = 200000000000L;
    //Зайчья дальность видимости
    public static final int MAX_RANGE_RABBIT = 6;
    //Начальное среднее количество кочек травы
    public static final int MIDDLE_COUNT_GRASS = 20;
    //Дисперсия начального среднего количества кочек травы
    public static final int DISPERS_COUNT_GRASS = 15;
    //Радиус начальных кочек травы
    public static final int RANGE_GRASS = 6;
    //Интервал сохранения (для начала примерно четверть дня)
    public static final long SAVE_INTERVAL = 21500L;
    //Максимальный радиус карты для отправки по рест
    public static final int MAX_MAP_RADIUS_TO_SEND_BY_REST = 25;
    //Максимальный радиус зайца
    public static final int DEFAULT_RABBIT_MAX_RANGE = 5;
}

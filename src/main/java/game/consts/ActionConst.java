package game.consts;

public class ActionConst {

    public static final int UNKNOWN = 0;
    public static final int EAT = 1;
    public static final int MOVE = 2;
    public static final int SLEEP = 3;
    public static final int NO_ACTION = 4;
    public static final int THINK_WIDTH = 5;

    public class ActionEnergyCost{

        //БАЗА вероятности
        public static final int BASE = 100;

        public static final int UNKNOWN = 0;
        public static final int EAT = 12;
        public static final int MOVE = 50;
        public static final int SLEEP = 10;
        public static final int NO_ACTION = 15;
        public static final int THINK_WIDTH = 25;
    }

    public class ActionSleepCost{

        //БАЗА вероятности
        public static final int BASE = 100;

        public static final int UNKNOWN = 0;
        public static final int EAT = 25;
        public static final int MOVE = 30;
        public static final int SLEEP = -500;
        public static final int NO_ACTION = 5;
        public static final int THINK_WIDTH = 10;
    }
}

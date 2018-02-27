package engine.tactor;

/**
 * create time 22.02.2018
 *
 * Класс для обработки тактов движка. Управляет внутренним временем
 * @author nponosov
 */
public class Tactor {

    private long innerTime = 0L;

    public long getInnerTime() {
        return innerTime;
    }

    // подумать над закрытием метода
    // возможно пригодится для старта не с начала
    public void setInnerTime(long innerTime) {
        this.innerTime = innerTime;
    }

    public void nextTact(){
        innerTime++;
    }
}

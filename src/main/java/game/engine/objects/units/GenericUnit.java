package game.engine.objects.units;

import game.engine.actions.GenericGoapAction;

/**
 * create time 07.04.2018
 *
 * @author nponosov
 */
public abstract class GenericUnit {

    /**
     * Текущее действие
     */
    private GenericGoapAction currentAction;

    /**
     * В процессе выполнения текущего действия?
     */
    private boolean inAction;



    public GenericGoapAction getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(GenericGoapAction currentAction) {
        this.currentAction = currentAction;
    }

    public boolean isInAction() {
        return inAction;
    }

    public void setInAction(boolean inAction) {
        this.inAction = inAction;
    }
}

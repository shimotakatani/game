package game.engine.actions.Impl;

import game.consts.ActionConst;
import game.consts.PlantTypeConst;
import game.data.enums.ActionEnum;
import game.data.enums.PlantEnum;
import game.engine.Game;
import game.engine.actions.EatAction;
import game.engine.mechanics.Impl.CostMechanic;
import game.engine.objects.GameMapCell;
import game.engine.objects.units.GenericUnit;
import game.engine.objects.units.Rabbit;
import game.engine.tactor.Tactor;

public class EatActionImpl extends GenericGoapActionImpl implements EatAction {
    @Override
    public void doAction(Game game, GenericUnit actor) {
        if (actor instanceof Rabbit) {
            Rabbit rabbit = (Rabbit) actor;
            GameMapCell cell = game.map.getCell(rabbit.x, rabbit.y);
            Tactor tactor = game.tactor;
            synchronized (cell){
                if (cell.plant != PlantTypeConst.NO_PLANT) {
                    rabbit.setFat(rabbit.getFat() + PlantEnum.getByNumberOfTitle(cell.plant).plantCost);
                    cell.plant = PlantTypeConst.NO_PLANT;
                    cell.eatedAtTime = tactor.getInnerTime();
                    CostMechanic.setFromAction(rabbit, ActionEnum.EAT);
                    rabbit.setLastAction(ActionEnum.EAT);
                    rabbit.eatedGrass++;
                    rabbit.setCurrentActionPicture(ActionConst.EAT);
                }
            }
        } else {
            super.doAction(game, actor);
        }
    }

    @Override
    public Double calcCost(Game game, GenericUnit actor) {
        if (actor instanceof Rabbit){
            Rabbit rabbit = (Rabbit) actor;
            GameMapCell cell = game.map.getCell(rabbit.x, rabbit.y);
            if (rabbit.getMaxFat() - rabbit.getFat() >= PlantEnum.getByNumberOfTitle(cell.plant).plantCost) {
                return 0.0 - PlantEnum.getByNumberOfTitle(cell.plant).plantCost;
            } else {
                return 100.0;
            }
        } else {
            return super.calcCost(game, actor);
        }
    }

    @Override
    public boolean isAvailable(Game game, GenericUnit actor) {
        if (actor instanceof Rabbit){
            Rabbit rabbit = (Rabbit) actor;
            GameMapCell cell = game.map.getCell(rabbit.x, rabbit.y);
            return cell.plant != PlantTypeConst.NO_PLANT;
        } else {
            return super.isAvailable(game, actor);
        }
    }
}

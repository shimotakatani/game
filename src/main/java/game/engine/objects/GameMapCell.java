package game.engine.objects;

import game.consts.CommonConst;
import game.consts.GroundTypeConst;
import game.consts.PlantTypeConst;

/**
 * create time 26.02.2018
 *
 * Класс для ячейки карты
 * @author nponosov
 */
public class GameMapCell {

    public int plant = PlantTypeConst.NO_PLANT;
    public int ground = GroundTypeConst.WHITE;
    public int color = GroundTypeConst.WHITE;
    public long eatedAtTime = CommonConst.MAX_LENGTH_OF_LIFE;
    public int previousColor = GroundTypeConst.WHITE;
    public int x = 0;
    public int y = 0;

    public GameMapCell(int x, int y){
        this.x = x;
        this.y = y;
        this.color = GroundTypeConst.WHITE;
    }

    public GameMapCell(){

    }

    public int getPlant() {
        return plant;
    }

    public void setPlant(int plant) {
        this.plant = plant;
    }

    public int getGround() {
        return ground;
    }

    public void setGround(int ground) {
        this.ground = ground;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getEatedAtTime() {
        return eatedAtTime;
    }

    public void setEatedAtTime(long eatedAtTime) {
        this.eatedAtTime = eatedAtTime;
    }

    public int getPreviousColor() {
        return previousColor;
    }

    public void setPreviousColor(int previousColor) {
        this.previousColor = previousColor;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

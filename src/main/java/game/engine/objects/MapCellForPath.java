package game.engine.objects;

/**
 * create time 07.04.2018
 *
 * @author nponosov
 */
public class MapCellForPath extends GameMapCell {

    private boolean opened = false;
    private boolean closed = false;
    private int g;
    private int f;
    private int previousX;
    private int previousY;

    public MapCellForPath(int x, int y) {
        super(x, y);
    }

    public void copyFromMapCell(GameMapCell mapCell){
        this.plant = mapCell.plant;
        this.ground = mapCell.ground;
        this.color = mapCell.color;
        this.x = mapCell.x;
        this.y = mapCell.y;
        this.previousX = this.x;
        this.previousY = this.y;
        this.eatedAtTime = mapCell.eatedAtTime;
    }

    public MapCellForPath(GameMapCell mapCell){
        super();
        this.copyFromMapCell(mapCell);
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getPreviousX() {
        return previousX;
    }

    public void setPreviousX(int previousX) {
        this.previousX = previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public void setPreviousY(int previousY) {
        this.previousY = previousY;
    }
}

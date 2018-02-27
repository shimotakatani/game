package engine.objects;

import java.util.Vector;

/**
 * create time 26.02.2018
 *
 * Класс для представления карты игры
 * Подумать над совместным доступом
 * @author nponosov
 */
public class GameMap {

    public int capacity = 10;

    public Vector<Vector<GameMapCell>> rowsOfCells;

    public GameMap(int capacity){
        rowsOfCells = new Vector<>(capacity);
        for(int i = 0; i < capacity; i++){
            rowsOfCells.add(i, setNewVector(capacity));
        }
        this.capacity = capacity;
    }

    public GameMap(){
        int MAGIC_VECTOR_NUMBER = 10;
        rowsOfCells = new Vector<>();
        for(int i = 0; i < MAGIC_VECTOR_NUMBER; i++){
            rowsOfCells.add(i, setNewVector(MAGIC_VECTOR_NUMBER));
        }
    }

    private Vector<GameMapCell> setNewVector(int capacity){
        Vector<GameMapCell> newVector = new Vector<GameMapCell>(capacity);
        for(int i = 0; i < capacity; i++){
            newVector.add(i, new GameMapCell());
        }
        return newVector;
    }

    /**
     * Возвращаем строку из карты
     * @param i - номер строки
     * @return массив ячеек
     * @author nponosov
     */
    public Vector<GameMapCell> getRow(int i){
        return rowsOfCells.get(i);
    }

    /**
     * Возвращаем столбец из карты
     * @param j - номер столбца
     * @return массив ячеек
     * @author nponosov
     */
    public Vector<GameMapCell> getColumn(int j){
        Vector<GameMapCell> columnOfCells = new Vector<>();
        rowsOfCells.forEach(rowOfCells -> columnOfCells.add(rowOfCells.get(j)));
        return columnOfCells;
    }

    /**
     * Получить ячейку по координатам
     * @param i - номер строки
     * @param j - номер столбца
     * @return ячейка
     * @author nponosov
     */
    public GameMapCell getCell(int i, int j){
        return getRow(i).get(j);
    }

    public synchronized String getMapSerilization(){
        StringBuilder builder = new StringBuilder();
        for (Vector<GameMapCell> rowsOfCell : rowsOfCells) {
            for (GameMapCell gameMapCell : rowsOfCell) {
                builder.append(gameMapCell.color);
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}

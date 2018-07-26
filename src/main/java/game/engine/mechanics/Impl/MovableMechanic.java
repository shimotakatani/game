package game.engine.mechanics.Impl;

import game.consts.DirectionConst;
import game.consts.GroundTypeConst;
import game.engine.Game;
import game.engine.objects.GameMap;
import game.engine.objects.GameMapCell;
import game.engine.objects.MapCellForPath;
import game.engine.objects.units.Rabbit;

import java.util.*;
import java.util.stream.Collectors;

/**
 * create time 06.03.2018
 *
 * @author nponosov
 */
public class MovableMechanic {

    public static GameMapCell getMapCellByDirection(GameMap map, int direction, int x, int y){
        switch (direction){
            case DirectionConst.E: x = Math.min(map.capacity-1, x+1); break;
            case DirectionConst.NE: {
                x = Math.min(map.capacity-1, x+1);
                y = Math.max(0, y-1);
                break;
            }
            case DirectionConst.N: y = Math.max(0, y-1); break;
            case DirectionConst.NW: {
                x = Math.max(0, x-1);
                y = Math.max(0, y-1);
                break;
            }
            case DirectionConst.W: x = Math.max(0, x-1); break;
            case DirectionConst.SW: {
                x = Math.max(0, x-1);
                y = Math.min(map.capacity-1, y+1);
                break;
            }
            case DirectionConst.S: y = Math.min(map.capacity-1, y+1); break;
            case DirectionConst.SE: {
                x = Math.min(map.capacity-1, x+1);
                y = Math.min(map.capacity-1, y+1);
                break;
            }
        }
        return map.getCell(x,y);
    }

    public static boolean hasAnybodyOnCell(Game game, int x, int y){
        synchronized (game.map.getCell(x, y)){
            for (Rabbit rabbit : game.rabbits) {
                if (rabbit.x == x && rabbit.y == y) return true;
            }
            return false;
        }
    }

    public static List<GameMapCell> getNeighbors(GameMap map, int centerX, int centerY){

        List<GameMapCell> neighbors = new ArrayList<>();

        GameMapCell cell;
        for(int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1 ; j++) {
                cell = map.getExistedCell(centerX + i, centerY + j);
                if (cell.ground != GroundTypeConst.WALL) {
                    if (!neighbors.contains(cell) && (cell.x != centerX || cell.y != centerY)) neighbors.add(cell);
                }
            }
        }

        return neighbors;
    }

    private static boolean hasVisited(List<MapCellForPath> visited, int x, int y){
        int i = 0;
        if (visited.size() > 0) {
            MapCellForPath currentCell = visited.get(i);
            if (currentCell.x == x && currentCell.y ==y) return true;
            while (i < visited.size()) {
                i++;
                if (i != visited.size()) {
                    currentCell = visited.get(i);
                    if (currentCell.x == x && currentCell.y == y) return true;
                }
            }
        }
        return false;
    }

    /**
     * Поиск в ширину
     * @param map - карта на которой ищем, например зайцы будут искать на маленькой вырезке(19х19 по дефолту)
     * @param startX - x начальной точки
     * @param startY - y начальной точки
     * @param endX - х конечной точки
     * @param endY - y конечной точки
     * @return Последовательность клеток, которые надо посетить для достижение конечной точки
     */
    public static List<MapCellForPath> findPathWidth(GameMap map, int startX, int startY, int endX, int endY){

        List<MapCellForPath> path = new ArrayList<>();

        List<MapCellForPath> visited = new ArrayList<>();
        Queue<MapCellForPath> queue = new LinkedList<MapCellForPath>();

        MapCellForPath start = new MapCellForPath(map.getExistedCell(startX, startY));

        queue.add(start);
        visited.add(start);
        start.setPreviousX(start.getX());
        start.setPreviousY(start.getY());

        MapCellForPath currentCell = start;

        while ( ! queue.isEmpty()) {
            currentCell = queue.poll();

            if (currentCell.getX() == endX && currentCell.getY() == endY) break;

            List<GameMapCell> neith = getNeighbors(map, currentCell.getX(), currentCell.getY());
            for (GameMapCell gameMapCell : neith) {
                if (!hasVisited(visited, gameMapCell.getX(), gameMapCell.getY())) {
                    if (!((gameMapCell.getX() == currentCell.getPreviousX()) && (gameMapCell.getY() == currentCell.getPreviousY()))) {
                        MapCellForPath next = new MapCellForPath(gameMapCell);
                        next.setPreviousX(currentCell.getX());
                        next.setPreviousY(currentCell.getY());
                        queue.add(next);
                        visited.add(next);
                    }
                }
            }
        }

        if (!(currentCell.getX() == endX && currentCell.getY() == endY)) return Collections.EMPTY_LIST;

        path.add(currentCell);
        int previousX, previousY;
        while (currentCell.getX() != startX || currentCell.getY() != startY) {
            previousX = currentCell.getPreviousX();
            previousY = currentCell.getPreviousY();
            MapCellForPath item = visited.get(0);
            int i = 0;
            currentCell = null;
            while (!(item.getX() == previousX && item.getY() == previousY) && i < visited.size()){
                i++;
                if (item.getX() == previousX && item.getY() == previousY) {
                    currentCell = item;
                } else if (i != visited.size()){
                    item = visited.get(i);
                }
            }
            if (i != visited.size()){
                currentCell = visited.get(i);
            }
            if (currentCell == null) break;
            path.add(currentCell);
        }
        Collections.reverse(path);

        return path;
    }

    public static List<GameMapCell> findPathDeixtra(GameMap map, int startX, int startY, int endX, int endY){
        List<GameMapCell> findedPath = new ArrayList<>();

        List<MapCellForPath> path = new ArrayList<>();



        return findedPath;
    }

    public static List<GameMapCell> findPathAStar(GameMap map, int startX, int startY, int endX, int endY){
        List<GameMapCell> findedPath = new ArrayList<>();

        List<MapCellForPath> path = new ArrayList<>();



        return findedPath;
    }
}

package game.engine;

import game.consts.AnimalTypeConst;
import game.consts.CommonConst;
import game.consts.GroundTypeConst;
import game.consts.PlantTypeConst;
import game.engine.objects.GameMap;
import game.engine.objects.GameMapCell;
import game.engine.objects.units.Rabbit;
import game.engine.objects.units.RabbitComporator;
import game.rest.dto.MessageDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * create time 07.03.2018
 *
 * @author nponosov
 */
public class SerialisationHelper {


    public static MessageDto getGameSerialization(Game game, Long clientId) {
        MessageDto messageDto = new MessageDto();
        List<Rabbit> rabbits =  game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(clientId)).collect(Collectors.toList());
        if (rabbits.size() != 1) return new MessageDto("Не был найден заяц для текущего чата", 1L);
        int tempX = rabbits.get(0).x;
        int tempY = rabbits.get(0).y;
        GameMap tempMap = getMapCut(game.map, tempX, tempY, CommonConst.MAX_RANGE_RABBIT);

        game.rabbits.forEach(rabbit -> {
            if ((Math.abs(tempX - rabbit.x) <= CommonConst.MAX_RANGE_RABBIT) && (Math.abs(tempY - rabbit.y) <= CommonConst.MAX_RANGE_RABBIT) ) {
                tempMap.getCell( rabbit.x - tempX + CommonConst.MAX_RANGE_RABBIT, rabbit.y - tempY + CommonConst.MAX_RANGE_RABBIT).color = AnimalTypeConst.RABBIT;
            }
        });
        StringBuilder builder = new StringBuilder();

        messageDto.mapString = getMapSerialization(tempMap);
        messageDto.timeString = "\n Сейчас " + game.tactor.getInnerTime() + " мгновений";
        builder.append(rabbits.get(0).toString());
        messageDto.message = builder.toString();
        return messageDto;
    }

    public static String getMapSerialization(GameMap map){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < map.capacity; i++){
            Vector<GameMapCell> row = map.getRow(i);
            for (int j = 0; j < map.capacity; j++) {
                if (i == CommonConst.MAX_RANGE_RABBIT && j == CommonConst.MAX_RANGE_RABBIT) {  //вставляем зайца
                    builder.append(AnimalTypeConst.RABBIT);
                    continue;
                }
                builder.append(getColorForSerilization(row.get(j)));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public static String getMapColorSerialization(GameMap map, Game game, Long cadr){
        StringBuilder builder = new StringBuilder();
        HashMap<Integer, Integer> rabbitColors = new HashMap<>();
        game.rabbits.forEach(rabbit -> {
            rabbitColors.put(rabbit.y * map.capacity + rabbit.x, 1);
        });
        int startLine = new Long(cadr*CommonConst.MAX_CADR_LENGTH).intValue();
        for (int i = startLine; i < startLine + CommonConst.MAX_CADR_LENGTH; i++){
            Vector<GameMapCell> row = map.getRow(i);
            for (int j = 0; j < map.capacity; j++) {
                if (rabbitColors.containsKey(i * map.capacity + j )) {
                    builder.append(AnimalTypeConst.RABBIT);
                } else {
                    builder.append(getColorForSerilization(row.get(j)));
                }
            }
        }
        return builder.toString();
    }

    public static GameMap getMapCut(GameMap map, int centerX, int centerY, int radius){
        GameMap resultMap = new GameMap(radius * 2 + 1);

        for (int i = 0; i < resultMap.capacity; i++) {
            for (int j = 0; j < resultMap.capacity; j++) {
                if (centerY - radius + j < 0) { //отрезаем края
                    setColorForOutMapCell(resultMap.getCell(i,j),Math.abs(centerY - radius + j + 1));
                    continue;
                }
                if (centerY - radius + j > map.capacity -1) {  //отрезаем края
                    setColorForOutMapCell(resultMap.getCell(i,j),Math.abs(centerY - radius + j - map.capacity));
                    continue;
                }
                if (centerX - radius + i < 0) { //отрезаем края
                    setColorForOutMapCell(resultMap.getCell(i,j),Math.abs(centerX - radius + i + 1));
                    continue;
                }
                if (centerX - radius + i > map.capacity -1) { //отрезаем края
                    setColorForOutMapCell(resultMap.getCell(i,j),Math.abs(centerX - radius + i - map.capacity));
                    continue;
                }
                resultMap.getCell(i,j).ground = map.getCell(centerX - radius + i, centerY - radius + j).ground;
                resultMap.getCell(i,j).plant = map.getCell(centerX - radius + i, centerY - radius + j).plant;
            }
        }

        return resultMap;
    }

    //Выбор цвета ячеек по приоритету цветов земли и растений
    private static int getColorForSerilization(GameMapCell cell){
        //if (cell.color != GroundTypeConst.WHITE) return cell.color;
        //Самая приоритетная - стена
        if (cell.ground == GroundTypeConst.WALL) return GroundTypeConst.WALL;
        //Далее растения на клетке
        if (cell.plant != PlantTypeConst.NO_PLANT) return cell.plant;
        //Самая неприоритетная - земля
        return cell.ground;
    }

    private static void setColorForOutMapCell(GameMapCell cell, int outRange){
        if (outRange == 0) {
            cell.ground = GroundTypeConst.WALL;
        } else {
            cell.plant = PlantTypeConst.GREEN;
        }
    }

    public static String getRabbitSerilization(Game game, Long clientId){
        if (game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(clientId)).count() != 0){
            return game.rabbits.stream().filter(rabbit1 -> rabbit1.clientId.equals(clientId)).findFirst().get().name;
        } else {
            return "У вас нет ещё зайца. Обязательно заведите.";
        }
    }

    public static String getRabbitScoreSerilization(Game game, Long clientId){
        if (game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(clientId)).count() != 0){
            return game.rabbits.stream().filter(rabbit1 -> rabbit1.clientId.equals(clientId)).findFirst().get().eatedGrass + "";
        } else {
            return "У вас нет ещё зайца. Обязательно заведите.";
        }
    }

    public static String getGeneralScoreSerilization(Game game, Long clientId){

        List<Rabbit> rabbits =  new ArrayList();
        rabbits.addAll(game.rabbits);
        rabbits.sort(new RabbitComporator());

        StringBuilder scoreBuilder = new StringBuilder();
        for (int i = 0; i < rabbits.size(); i++) {
            scoreBuilder.append(i+1).append(". ")
                    .append(rabbits.get(i).name)
                    .append(" съел ")
                    .append(rabbits.get(i).eatedGrass)
                    .append(" пучков травы\n");
        }

        if (game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(clientId)).count() != 0){
            Rabbit yourRabbit = game.rabbits.stream().filter(rabbit1 -> rabbit1.clientId.equals(clientId)).findFirst().get();
            return scoreBuilder.append("\nВаш заяц ")
                    .append(yourRabbit.name)
                    .append(" съел ")
                    .append(yourRabbit.eatedGrass)
                    .append(" пучков травы и находится на ")
                    .append(rabbits.indexOf(yourRabbit) + 1)
                    .append(" месте.\n")
                    .toString();
        } else {
            return scoreBuilder.append("\nУ вас нет ещё зайца. Обязательно заведите.").toString();
        }
    }
}

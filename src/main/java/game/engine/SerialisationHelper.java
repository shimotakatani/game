package game.engine;

import game.consts.ColorConst;
import game.consts.CommonConst;
import game.engine.objects.GameMap;
import game.engine.objects.GameMapCell;
import game.engine.objects.units.Rabbit;
import game.engine.objects.units.RabbitComporator;
import game.rest.dto.MessageDto;

import java.util.ArrayList;
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
        GameMap tempMap = getMapCut(game.map, tempX, tempY);

        game.rabbits.forEach(rabbit -> {
            if ((Math.abs(tempX - rabbit.x) <= CommonConst.MAX_RANGE_RABBIT) && (Math.abs(tempY - rabbit.y) <= CommonConst.MAX_RANGE_RABBIT) ) {
                tempMap.getCell(rabbit.y - tempY + CommonConst.MAX_RANGE_RABBIT, rabbit.x - tempX + CommonConst.MAX_RANGE_RABBIT).setColor(ColorConst.RABBIT);
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
                builder.append(row.get(j).color);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private static GameMap getMapCut(GameMap map, int centerX, int centerY){
        GameMap resultMap = new GameMap(CommonConst.MAX_RANGE_RABBIT * 2 + 1);

        for (int i = 0; i < resultMap.capacity; i++) {
            for (int j = 0; j < resultMap.capacity; j++) {
                if (centerY - CommonConst.MAX_RANGE_RABBIT + j < 0) { //отрезаем края
                    setColorForOutMapCell(resultMap.getCell(j,i),Math.abs(centerY - CommonConst.MAX_RANGE_RABBIT + j + 1));
                    continue;
                }
                if (centerY - CommonConst.MAX_RANGE_RABBIT + j > map.capacity -1) {  //отрезаем края
                    setColorForOutMapCell(resultMap.getCell(j,i),Math.abs(centerY - CommonConst.MAX_RANGE_RABBIT + j - map.capacity));
                    continue;
                }
                if (centerX - CommonConst.MAX_RANGE_RABBIT + i < 0) { //отрезаем края
                    setColorForOutMapCell(resultMap.getCell(j,i),Math.abs(centerX - CommonConst.MAX_RANGE_RABBIT + i + 1));
                    continue;
                }
                if (centerX - CommonConst.MAX_RANGE_RABBIT + i > map.capacity -1) { //отрезаем края
                    setColorForOutMapCell(resultMap.getCell(j,i),Math.abs(centerX - CommonConst.MAX_RANGE_RABBIT + i - map.capacity));
                    continue;
                }
                if (i == CommonConst.MAX_RANGE_RABBIT && j == CommonConst.MAX_RANGE_RABBIT) {  //вставляем зайца
                    resultMap.getCell(j,i).color = ColorConst.RABBIT;
                    continue;
                }
                resultMap.getCell(j,i).color = map.getCell(centerY - CommonConst.MAX_RANGE_RABBIT + j, centerX - CommonConst.MAX_RANGE_RABBIT + i).color;

            }
        }

        return resultMap;
    }

    private static void setColorForOutMapCell(GameMapCell cell, int outRange){
        if (outRange == 0) {
            cell.color = ColorConst.WALL;
        } else {
            cell.color = ColorConst.GREEN;
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

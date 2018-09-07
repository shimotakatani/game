package game.data.enums;

import game.consts.PlantTypeConst;

public enum PlantEnum {

    NO_PLANT(PlantTypeConst.NO_PLANT, PlantTypeConst.PlantCost.NO_PLANT),
    GREEN(PlantTypeConst.GREEN, PlantTypeConst.PlantCost.GREEN);


    public int numberOfTile;
    public int plantCost;
    PlantEnum(int numberOfTyle, int plantCost){
        this.numberOfTile = numberOfTyle;
        this.plantCost = plantCost;
    }

    public static PlantEnum getByNumberOfTitle(int numberOfTile){
        PlantEnum[] array = PlantEnum.values();
        for (int i =0; i< array.length; i++) {
            if (array[i].numberOfTile == numberOfTile) return array[i];
        }
        return NO_PLANT;
    }
}

package game.rest;

import game.consts.CommonConst;
import game.data.entity.MapEntity;
import game.data.repositories.CommonRepository;
import game.data.transformers.MapInfoTransformer;
import game.data.transformers.MapTransformer;
import game.data.transformers.RabbitTransformer;
import game.engine.SerialisationHelper;
import game.engine.commands.*;
import game.engine.mechanics.Impl.MovableMechanic;
import game.engine.objects.GameMap;
import game.engine.objects.MapCellForPath;
import game.engine.objects.units.Rabbit;
import game.helper.GameHelper;
import game.rest.dto.*;
import game.socket.SubscribeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static game.consts.CommonConst.MAP_CAPACITY;
import static game.consts.CommonConst.MAX_CADR_LENGTH;
import static game.consts.CommonConst.MAX_MAP_RADIUS_TO_SEND_BY_REST;

/**
 * create time 20.03.2018
 *
 * @author nponosov
 */
@RestController
@CrossOrigin(origins = { "*" }, maxAge = 6000)
//todo добавить перехватчик на авторизованность
public class RestResource {
    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    public SubscribeListener subscribeListener;

    /**
     * Метод для проверки работоспособности рест апи
     * @return серилизованный объект игры
     */
    @RequestMapping(value = "/rest/test", method = RequestMethod.GET)
    public MessageDto test(){
        return GameHelper.test();
    }

    /**
     * Получить собранные данные по игре для данного клиента
     * @param clientId - идентификатор клиента
     * @return данные по основным аспектам игры
     */
    @RequestMapping(value = "/rest/game", method = RequestMethod.GET)
    public AllDto game(@RequestParam("chatId") Long clientId){

        AllDto dto = new AllDto();

        List<Rabbit> rabbits =  GameHelper.game.rabbits.stream().filter(rabbit -> rabbit.clientId.equals(clientId)).collect(Collectors.toList());
        if (rabbits.size() != 1) return dto;
        int tempX = rabbits.get(0).x;
        int tempY = rabbits.get(0).y;
        GameMap tempMap = SerialisationHelper.getMapCut(GameHelper.game.map, tempX, tempY, CommonConst.MAX_RANGE_RABBIT);

        GameDto gameDto = new GameDto();
        gameDto.innerTime = GameHelper.game.tactor.getInnerTime();

        MapDto mapDto = new MapDto();
        MapTransformer.objectToDto(tempMap, mapDto);

        List<RabbitDto> rabbitDtoList = new ArrayList<>();
        for (Rabbit rabbit : GameHelper.game.rabbits) {
            RabbitDto rabbitDto = new RabbitDto();
            RabbitTransformer.objectToDto(rabbit, rabbitDto);
            rabbitDtoList.add(rabbitDto);
        }

        dto.gameDto = gameDto;
        dto.mapDto = mapDto;
        dto.rabbitDtoList = rabbitDtoList;

        return dto;
    }

    /**
     * Метод для запуска игры с определённым идентификатором клиента
     * @param clientId - идентификатор клиента
     * @return сообщение по запуску игры
     */
    @RequestMapping(value = "/rest/start", method = RequestMethod.GET)
    public MessageDto start(@RequestParam("chatId") Long clientId){
        StartGameCommand.execute(GameHelper.game, clientId);
        return new MessageDto("Ваша игра успешно запущена", clientId);
    }

    /**
     * Метод для окончания игры определённого клиента
     * @param clientId - идентификатор клиента
     * @return серилизованный объект игры
     */
    @RequestMapping(value = "/rest/end", method = RequestMethod.GET)
    public MessageDto end(@RequestParam("chatId") Long clientId){
        EndGameCommand.execute(GameHelper.game, clientId);
        return SerialisationHelper.getGameSerialization(GameHelper.game, clientId);
    }

    /**
     * Переименование зайца
     * @param clientId - идентификатор клиента
     * @param name - новое имя зайца
     * @return данные по зайцу
     */
    @RequestMapping(value = "/rest/name", method = RequestMethod.GET)
    public MessageDto name(@RequestParam("chatId") Long clientId, @RequestParam("name") String name){
        NameCommand.execute(GameHelper.game, clientId, name);
        return new MessageDto(SerialisationHelper.getRabbitSerilization(GameHelper.game, clientId), 1L);
    }

    /**
     * Получить счёт по игре
     * @param clientId - идентификатор клиента
     * @return счёт по игре
     */
    @RequestMapping(value = "/rest/score", method = RequestMethod.GET)
    public MessageDto score(@RequestParam("chatId") Long clientId){
        return new MessageDto(SerialisationHelper.getGeneralScoreSerilization(GameHelper.game, clientId), 1L);
    }

    /**
     * Сохранение состояния игры
     * Автоматическое сохранение происходит как правило раз в 6 часов(пока, меняется в конфиге)
     * @return сообщение об успешном сохранении
     */
    @RequestMapping(value = "/rest/save", method = RequestMethod.GET)
    public MessageDto save(){
        SaveCommand.execute(GameHelper.game, commonRepository);
        return new MessageDto("Сохранение зайцев успешно завершено", 1L);
    }

    /**
     * Запуск игры, но только если она не запущена.
     * Сейчас немного бесполезная функция, в перспективе будет доступна из админки для управления разными играми.
     * @return сообщение об успешном запуске игры.
     */
    @RequestMapping(value = "/rest/startServer", method = RequestMethod.GET)
    public MessageDto startServer(){
        GameHelper.startServer(commonRepository, subscribeListener);
        return new MessageDto("Игра на сервере запущена", 1L);
    }

    /**
     * Получение всего состояние текущей игры
     * !!!!Не работает, так как карта не влазит в пакет.
     * @return всё состояние, но по факту не отработает.
     */
    @RequestMapping(value = "/rest/all", method = RequestMethod.GET)
    public AllDto all(){
        AllDto dto = new AllDto();

        GameDto gameDto = new GameDto();
        gameDto.innerTime = GameHelper.game.tactor.getInnerTime();

        MapDto mapDto = new MapDto();
        MapTransformer.objectToDto(GameHelper.game.map, mapDto);

        List<RabbitDto> rabbitDtoList = new ArrayList<>();
        for (Rabbit rabbit : GameHelper.game.rabbits) {
            RabbitDto rabbitDto = new RabbitDto();
            RabbitTransformer.objectToDto(rabbit, rabbitDto);
            rabbitDtoList.add(rabbitDto);
        }

        dto.gameDto = gameDto;
        dto.mapDto = mapDto;
        dto.rabbitDtoList = rabbitDtoList;

        return dto;
    }

    /**
     * Получить кусок карты по заданным параметрам
     * @param requestDto - объект параметров для выборки куска карты
     * @return кусок карты
     */
    @RequestMapping(value = "/rest/mapCut", method = RequestMethod.POST)
    public MapDto getMapCut(@RequestBody MapRequestDto requestDto){

        MapDto mapDto = new MapDto();

        if (GameHelper.game == null) return mapDto;
        if (requestDto == null) return mapDto;
        if (requestDto.centerX < 0 || requestDto.centerX >= GameHelper.game.map.capacity) return mapDto;
        if (requestDto.centerY < 0 || requestDto.centerY >= GameHelper.game.map.capacity) return mapDto;
        if (requestDto.radius < 0) return mapDto;

        GameMap mapCut = SerialisationHelper.getMapCut(GameHelper.game.map, requestDto.centerX, requestDto.centerY, Math.min(requestDto.radius, MAX_MAP_RADIUS_TO_SEND_BY_REST));
        MapTransformer.objectToDto(mapCut, mapDto);
        return mapDto;
    }

    /**
     * Получить список зайцев
     * @return список зайцев
     */
    @RequestMapping(value = "/rest/rabbit/list", method = RequestMethod.GET)
    public List<RabbitDto> getRabbitList(){

        List<RabbitDto> rabbitDtoList = new ArrayList<>();
        for (Rabbit rabbit : GameHelper.game.rabbits) {
            RabbitDto rabbitDto = new RabbitDto();
            RabbitTransformer.objectToDto(rabbit, rabbitDto);
            rabbitDtoList.add(rabbitDto);
        }

        return rabbitDtoList;
    }

    /**
     * Получить текущее время
     * @return текущее время
     */
    @RequestMapping(value = "/rest/now", method = RequestMethod.GET)
    public Long getInnerTime(){
        return GameHelper.game.tactor.getInnerTime();
    }

    /**
     * Получить путь, построенный из точки начало в точку конец
     * @param startX - х начала
     * @param startY - у начала
     * @param endX - х конца
     * @param endY - у конца
     * @return страна
     */
    @RequestMapping(value = "/rest/path", method = RequestMethod.GET)
    public MessageDto getPath(@RequestParam("startX") int startX, @RequestParam("startY") int startY, @RequestParam("endX") int endX, @RequestParam("endY") int endY){
        List<MapCellForPath> path = MovableMechanic.findPathWidth(GameHelper.game.map, startX, startY , endX, endY);
        StringBuilder pathBuilder = new StringBuilder("start -> ");
        path.forEach(mapCellForPath -> pathBuilder.append(mapCellForPath.x + ", " + mapCellForPath.y + " -> "));
        pathBuilder.append("end");
        return new MessageDto(pathBuilder.toString(), 0L);
    }

    /**
     * Установить тактику для конкретного зайца
     * @param clientId - идентификатор клиента
     * @param tacticId - идентификатор тактики
     * @return сообщение об успешной смене тактики
     */
    @RequestMapping(value = "/rest/setTactic", method = RequestMethod.GET)
    public MessageDto setTactic(@RequestParam("chatId") Long clientId, @RequestParam("tacticId") int tacticId){
        TacticCommand.execute(GameHelper.game, clientId, tacticId);
        return new MessageDto("Тактика успешно изменена на тактику с номером " + tacticId, clientId);
    }

    /**
     * Получить миникарту текущей игры покадрово
     * @param clientId - идентификатор клиента
     * @param cadr - номер кадра
     * @return кадр карты
     */
    @RequestMapping(value = "/rest/littleMap", method = RequestMethod.GET)
    public MapCadrDto getLittleMap(@RequestParam("chatId") Long clientId, @RequestParam("cadr") Long cadr ){
        String mapStr = SerialisationHelper.getMapColorSerialization(GameHelper.game.map, GameHelper.game, cadr);
        return new MapCadrDto(mapStr,  new Long(GameHelper.game.map.capacity), cadr, (cadr+1)*MAX_CADR_LENGTH >= MAP_CAPACITY, GameHelper.game.tactor.getInnerTime());
    }

    /**
     * Получить карту с конкретным id покадрово с более полной информацией
     * @param mapId - id карты
     * @param cadr - номер кадра
     * @return кадр карты
     */
    @RequestMapping(value = "/rest/map", method = RequestMethod.GET)
    public MapCadrDto getMapById(@RequestParam("id") Long mapId, @RequestParam("cadr") Long cadr ){
        if (cadr == null) cadr = 0L;

        String mapStr = "";
        MapEntity map = commonRepository.mapRepository.findById(mapId).orElse(null);

        if (map != null) {
            GameMap gameMap = new GameMap(map.capacity);
            MapTransformer.entityToObject(gameMap, map);
            mapStr = SerialisationHelper.getMapSerialization(gameMap,  cadr);
        }

        return new MapCadrDto(mapStr,
                new Long(map != null ? map.capacity : 0),
                cadr,
                map == null || (cadr+1)*MAX_CADR_LENGTH >= map.capacity,
                GameHelper.game.tactor.getInnerTime());
    }

    /**
     * Создать новую карту
     * @param capacity - размерность карты
     * @return сообщение о создании/несоздании карты
     */
    @RequestMapping(value = "/rest/map/create", method = RequestMethod.POST)
    public MessageDto postCreateMap(@RequestBody int capacity){
        if (capacity > 0) {
            GameMap gameMap = new GameMap(capacity);
            MapEntity map = new MapEntity();
            MapTransformer.objectToEntity(gameMap, map);
            commonRepository.mapRepository.save(map);
            return new MessageDto("Карта успешно создана", 0L);
        } else {
            return new MessageDto("Размерность меньше 1, карта не может быть создана", 0L);
        }
    }

    /**
     * Получить список карт
     * @return список карт
     */
    @RequestMapping(value = "/rest/map/list", method = RequestMethod.GET)
    public List<MapInfoDto> getMapList(){

        List<MapInfoDto> mapInfoDtoList = new ArrayList<>();
        List<MapEntity> mapEntityList = new ArrayList<>();
        commonRepository.mapRepository.findAll().iterator().forEachRemaining(mapEntityList::add);

        if ( ! mapEntityList.isEmpty()) {
            mapEntityList.forEach(mapEntity -> {
                MapInfoDto dto = new MapInfoDto();
                MapInfoTransformer.entityToDto(mapEntity, dto);
                mapInfoDtoList.add(dto);
            });
        }

        return mapInfoDtoList;
    }
}

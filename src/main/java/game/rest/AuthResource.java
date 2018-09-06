package game.rest;

import game.data.entity.SessionEntity;
import game.data.entity.UserEntity;
import game.data.repositories.CommonRepository;
import game.rest.dto.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@RestController
@CrossOrigin
public class AuthResource {

    @Autowired
    private CommonRepository commonRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public TokenDto getToken(@RequestParam("username") String username, @RequestParam("password") String password){
        List<UserEntity> userEntityList = commonRepository.userRepository.findByUsername(username);

        if (userEntityList.isEmpty()) return new TokenDto(""); //Не нашли такого пользователя

        if (userEntityList.get(0).getPassword().equals(password)){
            Calendar cal = Calendar.getInstance();
            cal.getTimeInMillis();
            List<SessionEntity> sessionEntityList = commonRepository.sessionRepository.getByUsernameAndActiveToTimeAfter(username, cal.getTimeInMillis());
            if (sessionEntityList.isEmpty()) {
                //Добавляем новую сессию
                SessionEntity sessionEntity = new SessionEntity(username);
                //в моменты сохранения ей выдастся токен
                sessionEntity = commonRepository.sessionRepository.save(sessionEntity);
                return new TokenDto(sessionEntity.getToken());
            } else {
                return new TokenDto(sessionEntityList.get(0).getToken());
            }
        }
        //Не нашли такого пользователя
        return new TokenDto("");
    }
}

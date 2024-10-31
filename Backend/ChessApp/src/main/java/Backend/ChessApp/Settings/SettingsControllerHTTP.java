package Backend.ChessApp.Settings;


import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SettingsControllerHTTP {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SettingsRepo settingsRepo;

    @GetMapping(path="/settings/findAll")
    public List<SettingsUserStates> findAllUserSettings() {return settingsRepo.findAll();}

    @GetMapping(path="/settings/{userName}")
    public SettingsUserStates findUserSettingsByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        return settingsRepo.findByUser(user);
    }
    @PostMapping(path="/settings/{userName}")
    public void createNewSettings(String userName, @RequestBody SettingsUserStates settingsUserStates){
        User user = userRepository.findByUserName(userName);

    }



}

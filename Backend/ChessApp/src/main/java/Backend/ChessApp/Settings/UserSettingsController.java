package Backend.ChessApp.Settings;


import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.aot.AbstractAotProcessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserSettingsController {
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
    public void createNewSettings(String userName){
        User user = userRepository.findByUserName(userName);
        if(user == null)
            return;
        SettingsUserStates sus = new SettingsUserStates(user);
        settingsRepo.save(sus);
    }
    @PostMapping(path="/settings")
    public void createAllNewSettings(@RequestBody SettingsUserStates settingsUserStates){
        if(settingsUserStates == null)
            return;
        settingsUserStates = new SettingsUserStates(settingsUserStates.getBoardTheme(),
                settingsUserStates.getPieceTheme(), settingsUserStates.isAppTheme(),
                settingsUserStates.isSounds(), settingsUserStates.isMoveHighlighting());
        settingsRepo.save(settingsUserStates);

    }

    @PutMapping(path="/settings/boardTheme/{user}")
    public short updateBoardTheme(@PathVariable User user, @RequestParam(value="boardTheme") short boardTheme){
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setBoardTheme(boardTheme);
        return boardTheme;
    }
    @PutMapping(path="/settings/pieceTheme/{user}")
    public short updatePieceTheme(@PathVariable User user, @RequestParam(value="boardTheme") short pieceTheme){
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setPieceTheme(pieceTheme);
        return pieceTheme;
    }
    @PutMapping(path="/settings/appTheme/{user}")
    public boolean updateAppTheme(@PathVariable User user, @RequestParam(value="boardTheme") boolean appTheme){
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setAppTheme(appTheme);
        return appTheme;
    }
    @PutMapping(path="/settings/sounds/{user}")
    public boolean updateSounds(@PathVariable User user, @RequestParam(value="boardTheme") boolean sounds){
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setSounds(sounds);
        return sounds;
    }
    @PutMapping(path="/settings/moveHighlighting/{user}")
    public boolean updateMoveHighlighting(@PathVariable User user, @RequestParam(value="boardTheme") boolean moveHighlighting){
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setMoveHighlighting(moveHighlighting);
        return moveHighlighting;
    }
    @DeleteMapping(path="/settings/clear")
    public String clearSettings(@PathVariable User user){
        SettingsUserStates sus = settingsRepo.findByUser(user);
        if(user == null)
            return "User not found";
        settingsRepo.delete(sus);
        sus = new SettingsUserStates(user);
        settingsRepo.save(sus);
        return "Settings cleared successfully";
    }



}

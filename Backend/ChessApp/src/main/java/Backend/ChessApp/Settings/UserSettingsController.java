package Backend.ChessApp.Settings;


import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.aot.AbstractAotProcessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "User Settings", description = "HTTP requests to update a users settings for the app")
public class UserSettingsController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SettingsRepo settingsRepo;

    @Operation(summary = "Returns all the different users settings from the database")
    @GetMapping(path="/settings/findAll")
    public List<SettingsUserStates> findAllUserSettings() {return settingsRepo.findAll();}

    @Operation(summary = "Find a specific users settings given their username.")
    @GetMapping(path="/settings/{userName}")
    public SettingsUserStates findUserSettingsByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        return settingsRepo.findByUser(user);
    }
    @Operation(summary = "Creates default settings for a user")
    @PostMapping(path="/settings/{userName}")
    public void createNewSettings(@PathVariable String userName){
        User user = userRepository.findByUserName(userName);
        if(user == null)
            return;
        SettingsUserStates sus = new SettingsUserStates(user);
        settingsRepo.save(sus);
    }
    @Operation(summary = "Sets the settings from a json body.")
    @PostMapping(path="/settings")
    public void createAllNewSettings(@RequestBody SettingsUserStates settingsUserStates){
        if(settingsUserStates == null)
            return;
        settingsUserStates = new SettingsUserStates(settingsUserStates.getBoardTheme(),
                settingsUserStates.getPieceTheme(), settingsUserStates.isAppTheme(),
                settingsUserStates.isSounds(), settingsUserStates.isMoveHighlighting(), settingsUserStates.isMenuMusic());
        settingsRepo.save(settingsUserStates);

    }
    @Operation(summary = "Given a username, updates the board theme for the user.")
    @PutMapping(path="/settings/boardTheme/{username}")
    public short updateBoardTheme(@PathVariable String username, @RequestParam(value="boardTheme") short boardTheme){
        User user = userRepository.findByUserName(username);
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setBoardTheme(boardTheme);
        settingsRepo.save(sus);
        return boardTheme;
    }
    @Operation(summary = "Given a username, updates the piece theme for the user.")
    @PutMapping(path="/settings/pieceTheme/{user}")
    public short updatePieceTheme(@PathVariable String username, @RequestParam(value="pieceTheme") short pieceTheme){
        User user = userRepository.findByUserName(username);
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setPieceTheme(pieceTheme);
        settingsRepo.save(sus);
        return pieceTheme;
    }
    @Operation(summary = "Given a username, updates the app theme for the user.")
    @PutMapping(path="/settings/appTheme/{user}")
    public boolean updateAppTheme(@PathVariable String username, @RequestParam(value="appTheme") boolean appTheme){
        User user = userRepository.findByUserName(username);
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setAppTheme(appTheme);
        settingsRepo.save(sus);
        return appTheme;
    }
    @Operation(summary = "Given a username, updates the sounds for the user.")
    @PutMapping(path="/settings/sounds/{user}")
    public boolean updateSounds(@PathVariable String username, @RequestParam(value="sounds") boolean sounds){
        User user = userRepository.findByUserName(username);
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setSounds(sounds);
        settingsRepo.save(sus);
        return sounds;
    }
    @Operation(summary = "Given a username, toggles the move highlighting for the user.")
    @PutMapping(path="/settings/moveHighlighting/{user}")
    public boolean updateMoveHighlighting(@PathVariable String username, @RequestParam(value="moveHighlighting") boolean moveHighlighting){
        User user = userRepository.findByUserName(username);
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setMoveHighlighting(moveHighlighting);
        settingsRepo.save(sus);
        return moveHighlighting;
    }
    @Operation(summary = "Given a username, toggles the move highlighting for the user.")
    @PutMapping(path = "/settings/menuMusic/{username}")
    public boolean updateMenuMusic(@PathVariable String username, @RequestParam(value="menuMusic") boolean menuMusic){
        User user = userRepository.findByUserName(username);
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setMenuMusic(menuMusic);
        settingsRepo.save(sus);
        return menuMusic;
    }
    @Operation(summary = "Resets the settings back to default.")
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

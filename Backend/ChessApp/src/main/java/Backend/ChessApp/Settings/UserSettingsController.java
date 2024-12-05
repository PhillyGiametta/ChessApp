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
//    @Operation(summary = "From username sets all new settings for a user.")
//    @PostMapping(path="/settings/{userName}")
//    public void createAllNewSettings(@PathVariable String userName, @RequestBody SettingsUserStates settingsUserStates){
//        User user = userRepository.findByUserName(userName);
//        if(settingsUserStates == null)
//            return;
//        settingsUserStates = new SettingsUserStates(settingsUserStates.getBoardTheme(),
//                settingsUserStates.getPieceTheme(), settingsUserStates.isAppTheme(),
//                settingsUserStates.isSounds(), settingsUserStates.isMoveHighlighting());
//        user.setSettings(settingsUserStates);
//        settingsRepo.save(settingsUserStates);
//
//    }
    @Operation(summary = "Given a user, updates the board theme for the user.")
    @PutMapping(path="/settings/boardTheme/{user}")
    public short updateBoardTheme(@PathVariable User user, @RequestParam(value="boardTheme") short boardTheme){
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setBoardTheme(boardTheme);
        return boardTheme;
    }
    @Operation(summary = "Given a user, updates the piece theme for the user.")
    @PutMapping(path="/settings/pieceTheme/{user}")
    public short updatePieceTheme(@PathVariable User user, @RequestParam(value="boardTheme") short pieceTheme){
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setPieceTheme(pieceTheme);
        return pieceTheme;
    }
    @Operation(summary = "Given a user, updates the app theme for the user.")
    @PutMapping(path="/settings/appTheme/{user}")
    public boolean updateAppTheme(@PathVariable User user, @RequestParam(value="boardTheme") boolean appTheme){
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setAppTheme(appTheme);
        return appTheme;
    }
    @Operation(summary = "Given a user, updates the sounds for the user.")
    @PutMapping(path="/settings/sounds/{user}")
    public boolean updateSounds(@PathVariable User user, @RequestParam(value="boardTheme") boolean sounds){
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setSounds(sounds);
        return sounds;
    }
    @Operation(summary = "Given a user, toggles the move highlighting for the user.")
    @PutMapping(path="/settings/moveHighlighting/{user}")
    public boolean updateMoveHighlighting(@PathVariable User user, @RequestParam(value="boardTheme") boolean moveHighlighting){
        SettingsUserStates sus = settingsRepo.findByUser(user);
        sus.setMoveHighlighting(moveHighlighting);
        return moveHighlighting;
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

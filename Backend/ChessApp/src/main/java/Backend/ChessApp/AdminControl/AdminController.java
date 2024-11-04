package Backend.ChessApp.AdminControl;

import Backend.ChessApp.Settings.SettingGameStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    //private GameSettingsService gameSettingsService;

    @PostMapping("/initializeGameSettings")
    public ResponseEntity<String> initializeGameSettings(@RequestBody SettingGameStates settings) {
        //gameSettingsService.setGameSettings(settings);
        return ResponseEntity.ok("Game settings initialized");
    }
}

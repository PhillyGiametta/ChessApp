package Backend.ChessApp.AdminControl;

import Backend.ChessApp.Game.ChessGame;
import Backend.ChessApp.Settings.GameSettingsService;
import Backend.ChessApp.Settings.SettingGameStates;
import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "HTTP methods for initializing the game settings most logic in websockets")
public class AdminController {

    @Autowired
    private GameSettingsService gameSettingsService;
    UserRepository userRepository;
    User user;
    ChessGame chessGame;

    @Operation(summary = "Initializes the game settings for the game.")
    @PostMapping("/initializeDefaultSettings")
    public ResponseEntity<String> defaultSettings(){
        gameSettingsService.initializeDefaultSettings(chessGame);
        return ResponseEntity.ok("Game settings initialized");
    }

    @Operation(summary = "Changes the setting for a single user.")
    @PostMapping("/initializeGameSettings/{userName}")
    public ResponseEntity<String> initializeGameSettings(@PathParam("userName") String userName, @RequestBody SettingGameStates settings) {
        user = userRepository.findByUserName(userName);
        gameSettingsService.updateGameSettings(user, chessGame, settings);
        return ResponseEntity.ok("Game settings initialized");
    }
}

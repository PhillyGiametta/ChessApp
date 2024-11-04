package Backend.ChessApp.Game;

import Backend.ChessApp.Settings.GameSettingsContoller;
import Backend.ChessApp.Settings.SettingGameStates;
import Backend.ChessApp.Settings.SettingsRepo;
import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.aot.AbstractAotProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Hashtable;
import java.util.Map;

@ServerEndpoint("/game/{userName}")
@Controller
public class ChessGameController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SettingsRepo settingsRepo;

    SettingGameStates gameSettings;
    private static Map<ChessGame, User> chessGameUserMap = new Hashtable<>();
    private static Map<User, ChessGame> userChessGameMap = new Hashtable<>();
    private static Map<User, Session> userSessionMap = new Hashtable<>();
    private static Map<Session, User> sessionUserMap = new Hashtable<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {
        if (gameSettings == null) {
            // Handle case where game settings are not initialized, perhaps a default
            initializeDefaultSettings();
        }
        // Add player to the game, and share settings with their session
    }

    private void initializeDefaultSettings() {
        this.gameSettings = new SettingGameStates((short) 10, 5, true, false, 30);
    }

    @OnMessage
    public void InitiateSettings(Session session, @RequestBody SettingGameStates settingGameStates){

    }

}

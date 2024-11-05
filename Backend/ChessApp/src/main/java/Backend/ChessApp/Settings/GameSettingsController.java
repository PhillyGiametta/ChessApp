package Backend.ChessApp.Settings;

import org.json.JSONObject;
import Backend.ChessApp.Game.ChessGame;
import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

/*
       This is a controller allowing settings to sync to the user, game, and other variables. This will handle mostly the game settings,
       User Settings will be handled in either separate class, or rather using http requests since they do not need to be live updating
 */
@Controller
@ServerEndpoint("/settings/{userName}")
public class GameSettingsController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private SettingsRepo settingsRepo;
    @Autowired
    private GameSettingsService gameSettingsService;

    private final Logger logger = LoggerFactory.getLogger(GameSettingsController.class);

    // Session and User mappings
    private static final Map<Session, User> sessionUserMap = new Hashtable<>();
    private static final Map<User, Session> userSessionMap = new Hashtable<>();

    // Game and Settings mappings
    private static final Map<ChessGame, SettingGameStates> gameSettingsMap = new Hashtable<>();
    private static final Map<User, ChessGame> userGameMap = new Hashtable<>();
    private static final Map<ChessGame, List<Session>> gameSessionMap = new Hashtable<>();

    private final ChessGame chessGame = new ChessGame();
    private User adminUser;

    public GameSettingsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {
        User user = userRepository.findByUserName(userName);

        sessionUserMap.put(session, user);
        userSessionMap.put(user, session);
        userGameMap.put(user, chessGame);

        // Set the first user as the Admin
        if (adminUser == null) {
            adminUser = user;
            initializeDefaultSettings();
        }

        // Add user session to game
        gameSessionMap.computeIfAbsent(chessGame, k -> new ArrayList<>()).add(session);

        // Send the current settings to the user
        sendSettings(session, gameSettingsMap.get(chessGame));
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        User user = sessionUserMap.get(session);

        if (user != null && user.equals(adminUser)) {
            // Only the admin can update settings
            ChessGame game = userGameMap.get(user);
            SettingGameStates settings = gameSettingsMap.get(game);

            updateSettings(settings, message);
            broadcastSettings(game, settings);
        } else {
            assert user != null;
            logger.warn("Unauthorized settings change attempt by user: {}", user.getUserName());
        }
    }

    @OnClose
    public void onClose(Session session) {
        User user = sessionUserMap.remove(session);
        if (user != null) {
            userSessionMap.remove(user);
            removeUserFromGame(session, user);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("WebSocket error for session: " + session.getId(), throwable);
    }

    private void initializeDefaultSettings() {
        SettingGameStates defaultSettings = new SettingGameStates((short) 30, 1, true, false, 30);
        gameSettingsMap.put(chessGame, defaultSettings);
    }

    private void sendSettings(Session session, SettingGameStates settings) {
        try {
            String settingsMessage = "Settings: " + settings.toString(); // Format this as needed
            session.getBasicRemote().sendText(settingsMessage);
        } catch (IOException e) {
            logger.warn("Failed to send settings", e);
        }
    }

    private void removeUserFromGame(Session session, User user) {
        ChessGame game = userGameMap.get(user);
        List<Session> sessions = gameSessionMap.get(game);

        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                gameSessionMap.remove(game);
                gameSettingsMap.remove(game);
            }
        }

        if (user.equals(adminUser)) {
            adminUser = null; // Clear admin if they leave
            assignNewAdmin(game);
        }
    }

    private void broadcastSettings(ChessGame game, SettingGameStates settings) {
        List<Session> sessions = gameSessionMap.get(game);
        if (sessions != null) {
            for (Session s : sessions) {
                sendSettings(s, settings);
            }
        }
    }

    private void updateSettings(SettingGameStates settings, String message) {
        try {
            JSONObject json = new JSONObject(message);

            if (json.has("timeController")) {
                settings.timeController = (short) json.getInt("timeController");
            }
            if (json.has("incrementTimer")) {
                settings.incrementTimer = json.getInt("incrementTimer");
            }
            if (json.has("allowUndos")) {
                settings.allowUndos = json.getBoolean("allowUndos");
            }
            if (json.has("enableLimitMoveTime")) {
                settings.enableLimitMoveTime = json.getBoolean("enableLimitMoveTime");
            }
            if (json.has("limitMoveTime")) {
                settings.limitMoveTime = json.getInt("limitMoveTime");
            }

            // Log the updated settings for debugging
            logger.info("Settings updated: " + settings.toString());

        } catch (Exception e) {
            logger.error("Failed to parse and update settings", e);
        }
    }

    private void assignNewAdmin(ChessGame game) {
        List<Session> sessions = gameSessionMap.get(game);
        if (sessions != null && !sessions.isEmpty()) {
            Session newAdminSession = sessions.get(0);
            User newAdminUser = sessionUserMap.get(newAdminSession);
            adminUser = newAdminUser;

            try {
                newAdminSession.getBasicRemote().sendText("You are now the Admin.");
            } catch (IOException e) {
                logger.warn("Failed to notify new admin", e);
            }
        }
    }
}



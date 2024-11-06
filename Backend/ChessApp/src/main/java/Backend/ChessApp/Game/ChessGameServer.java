package Backend.ChessApp.Game;

import Backend.ChessApp.AdminControl.AdminRepo;
import Backend.ChessApp.Game.Board.Position;
import Backend.ChessApp.Settings.GameSettingsController;
import Backend.ChessApp.Settings.GameSettingsService;
import Backend.ChessApp.Settings.SettingGameStates;
import Backend.ChessApp.Settings.SettingsRepo;
import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@ServerEndpoint("/game/{userName}")
@Component
public class ChessGameServer {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SettingsRepo settingsRepo;
    @Autowired
    private GameSettingsService gameSettingsService;
    @Autowired
    private AdminRepo adminRepo;

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

    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) throws IOException {
        User user = userRepository.findByUserName(userName);

        if(user == null){
            session.close();
            return;
        }
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
    public void updateSettings(Session session, String message) {
        User user = sessionUserMap.get(session);
        JSONObject json = new JSONObject(message);
        if(json.getString("type").equals("chessMove")){
            moveOnBoard(session, message);
            return;
        }

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

    public void moveOnBoard(Session session, String message){
        User user = sessionUserMap.get(session);
        JSONObject json = new JSONObject(message);
        int row = json.getInt("rowStart");
        int col = json.getInt("colStart");
        Position positionStart = new Position(row,col);
        int row2 = json.getInt("rowEnd");
        int col2 = json.getInt("colEnd");
        Position positionEnd = new Position(row2, col2);
        List<Position> p = chessGame.getLegalMovesForPieceAt(positionStart);
        if(p.contains(positionEnd)){
            chessGame.makeMove(positionStart, positionEnd);
        }


    }

    @OnClose
    public void onClose(Session session) throws IOException {
        User user = sessionUserMap.remove(session);
        if (user != null) {
            userSessionMap.remove(user);
            sessionUserMap.remove(session);
            removeUserFromGame(session, user);
            session.close();
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("WebSocket error for session: " + session.getId(), throwable);
    }

    private void initializeDefaultSettings() {
        gameSettingsService.initializeDefaultSettings(chessGame);
        gameSettingsMap.put(chessGame, gameSettingsService.getSettings(chessGame));
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
                settings.setTimeController((short)(json.getInt("timeController")));
            }
            if (json.has("incrementTimer")) {
                settings.setIncrementTimer(json.getInt("incrementTimer"));
            }
            if (json.has("allowUndos")) {
                settings.setAllowUndos(json.getBoolean("allowUndos"));
            }
            if (json.has("enableLimitMoveTime")) {
                settings.setEnableLimitMoveTime(json.getBoolean("enableLimitMoveTime"));
            }
            if (json.has("limitMoveTime")) {
                settings.setLimitMoveTime(json.getInt("limitMoveTime"));
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
            adminUser = sessionUserMap.get(newAdminSession);

            try {
                newAdminSession.getBasicRemote().sendText("You are now the Admin.");
            } catch (IOException e) {
                logger.warn("Failed to notify new admin", e);
            }
        }
    }
}


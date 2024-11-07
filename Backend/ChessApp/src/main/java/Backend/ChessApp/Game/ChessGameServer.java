package Backend.ChessApp.Game;

import Backend.ChessApp.AdminControl.Admin;
import Backend.ChessApp.AdminControl.AdminRepo;
import Backend.ChessApp.Game.Board.Position;
import Backend.ChessApp.Settings.GameSettingsController;
import Backend.ChessApp.Settings.GameSettingsService;
import Backend.ChessApp.Settings.SettingGameStates;
import Backend.ChessApp.Settings.SettingsRepo;
import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;

import static Backend.ChessApp.Game.Pieces.PieceColor.WHITE;

@Controller
@Component
@ServerEndpoint(value = "/game/{userName}", configurator = SpringConfigurator.class)
public class ChessGameServer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameSettingsService gameSettingsService;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private SettingsRepo settingsRepo;

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
            logger.info("user is null");
            session.close();
            return;
        }
        sessionUserMap.put(session, user);
        userSessionMap.put(user, session);
        userGameMap.put(user, chessGame);
        logger.info("[onOpen] User {} joined", userName);
        // Set the first user as the Admin
        if (adminUser == null) {
            adminUser = user;
            logger.info("{} is now the admin", user.getUserName());
            initializeDefaultSettings();
        }
        chessGame.blackTimer = new Timer(gameSettingsService.getSettings(chessGame).getTimeController());
        chessGame.whiteTimer = new Timer(gameSettingsService.getSettings(chessGame).getTimeController());
        // Add user session to game
        gameSessionMap.computeIfAbsent(chessGame, k -> new ArrayList<>()).add(session);
        assignTeams(chessGame);

        // Send the current settings to the user
        sendSettings(session, gameSettingsMap.get(chessGame));
    }

    @OnMessage
    public void OnMessage(Session session, String message) {

        JSONObject json = new JSONObject(message);

        if(json.getString("type").equals("chessMove")){
            moveOnBoard(session, message);
            return;
        }
        else if(json.getString("type").equals("settings")){
            updateSettings(session, message);
            return;
        }
        else if(json.getString("type").equals("start")){
            startGame(session);
        }
        else if(json.getString("type").equals("end")){
            endGame(session);
        }
    }

    public void moveOnBoard(Session session, String message){
        if (chessGame.getGameActive() != ChessGame.GameActive.GAME_ACTIVE) {
            try {
                session.getBasicRemote().sendText("The game has ended. No further moves are allowed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return; // Stop processing the move
        }
        if(chessGame.getCurrentPlayerColor()){
            chessGame.blackTimer.start();
        }
        else{
            chessGame.whiteTimer.start();
        }
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
        if(chessGame.getCurrentPlayerColor()){
            chessGame.blackTimer.stop();
        }
        else
            chessGame.whiteTimer.stop();
    }

    public void updateSettings(Session session, String message){
        User user = sessionUserMap.get(session);
        if (user != null && user.equals(adminUser)) {
            // Only the admin can update settings
            ChessGame game = userGameMap.get(user);
            SettingGameStates settings = gameSettingsMap.get(game);

            updateSettings(settings, message);
            chessGame.whiteTimer = new Timer(settings.getTimeController());
            chessGame.blackTimer = new Timer(settings.getTimeController());
            sendSettings(session, settings);
        } else {
            assert user != null;
            logger.warn("Unauthorized settings change attempt by user: {}", user.getUserName());
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        User user = sessionUserMap.remove(session);
        if (user != null) {
            String userName = user.getUserName();
            userSessionMap.remove(user);
            sessionUserMap.remove(session);
            removeUserFromGame(session, user);
            logger.info("User {} has left", userName);
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
            String settingsMessage = "Settings: " + settings.toString();
            session.getBasicRemote().sendText(settingsMessage);
        } catch (IOException e) {
            logger.warn("Failed to send settings", e);
        }
    }

    private void startGame(Session session){
        if(sessionUserMap.get(session) == adminUser){
            chessGame.setGameActive(ChessGame.GameActive.GAME_ACTIVE);
        }
    }

    private void assignTeams(ChessGame chessGame) throws IOException {
        List<User> whiteTeam = new ArrayList<>();
        List<User> blackTeam = new ArrayList<>();
        for(int i = 0; i < gameSessionMap.get(chessGame).size(); i++) {
            if (i % 2 == 0) {
                whiteTeam.add(sessionUserMap.get(gameSessionMap.get(chessGame).get(i)));
                gameSessionMap.get(chessGame).get(i).getBasicRemote().sendText("You are now white team");
            }
             else {
                blackTeam.add(sessionUserMap.get(gameSessionMap.get(chessGame).get(i)));
                gameSessionMap.get(chessGame).get(i).getBasicRemote().sendText("You are now black team");
            }
        }
        chessGame.setWhiteTeam(whiteTeam);
        chessGame.setBlackTeam(blackTeam);
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

    private void endGame(Session session){
        if(sessionUserMap.get(session) == adminUser)
            chessGame.setGameActive(ChessGame.GameActive.GAME_ENDED);
    }
}


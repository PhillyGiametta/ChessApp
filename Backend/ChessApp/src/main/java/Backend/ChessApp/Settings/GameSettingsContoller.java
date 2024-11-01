package Backend.ChessApp.Settings;


import Backend.ChessApp.Game.ChessGame;
import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

/*
       This is a controller allowing settings to sync to the user, game, and other variables. This will handle mostly the game settings,
       User Settings will be handled in either seperate class, or rather using http requests since they do not need to be live updating
 */

@ServerEndpoint("/settings/{userName}")
@Controller
public class GameSettingsContoller {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(GameSettingsContoller.class);
    private final ChessGame chessGame = new ChessGame();

    private static Map<Session, User> sessionUserMap = new Hashtable<>();
    private static Map<User, Session> userSessionMap = new Hashtable<>();
    private static Map<ChessGame, SettingGameStates> chessGameSettingsMap = new Hashtable<>();
    private static Map<User, ChessGame> userGameMap = new Hashtable<>();
    private static Map<ChessGame, List<Session>> chessGameSessionMap = new Hashtable<>();

    public GameSettingsContoller(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {
        User user = userRepository.findByUserName(userName);
        sessionUserMap.put(session, user);
        userSessionMap.put(user, session);
        userGameMap.put(user, chessGame);

        // Initialize settings if the game doesn't have any
        ChessGame game = getGameForUser(user);
        chessGameSettingsMap.putIfAbsent(game, new SettingGameStates((short) 5, 0, true, false, 30));

        sendSettings(session, chessGameSettingsMap.get(game));
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        User user = sessionUserMap.get(session);
        if (user != null) {
            ChessGame game = getGameForUser(user);
            SettingGameStates settings = chessGameSettingsMap.get(game);

            // Update settings based on the parsed message content (consider using JSON for structure)
            updateSettings(settings, message);

            // Broadcast updated settings to all game participants
            broadcastSettings(game, settings);
        }
    }


    @OnClose
    public void onClose(Session session) {
        User user = sessionUserMap.get(session);
        if (user != null) {
            removeUserFromGame(session, user);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("WebSocket error for session: " + session.getId(), throwable);
    }

    private void sendSettings(Session session, SettingGameStates settings) {
        try {
            String settingsMessage = "Settings: " + settings.toString(); // Format this as needed
            session.getBasicRemote().sendText(settingsMessage);
        } catch (IOException e) {
            logger.warn("Failed to send settings", e);
        }
    }

    private void removeUserFromGame(Session session, User user){
        userSessionMap.remove(user);
        sessionUserMap.remove(session);


    }


    private void broadcastSettings(ChessGame game, SettingGameStates settings) {
        List<Session> sessions = chessGameSessionMap.get(game);
        if (sessions != null) {
            for (Session s : sessions) {
                sendSettings(s, settings);
            }
        }
    }


    private void updateSettings(SettingGameStates settings, String message) {
        // Logic to parse and update settings based on the received message content
        // Example: update settings fields based on parsed values from message
    }

    private ChessGame getGameForUser(User user) {
        return userGameMap.get(user);
    }


    private void sendWelcomeMessage(Session session, String userName) {
        try {
            session.getBasicRemote().sendText("Welcome, " + userName + "!");
        } catch (IOException e) {
            logger.warn("Failed to send welcome message", e);
        }
    }
}



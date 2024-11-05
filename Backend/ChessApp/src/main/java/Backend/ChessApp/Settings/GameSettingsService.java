package Backend.ChessApp.Settings;

import Backend.ChessApp.Game.ChessGame;
import Backend.ChessApp.Users.User;
import jakarta.websocket.Session;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameSettingsService {

    private final Map<ChessGame, SettingGameStates> gameSettingsMap = new ConcurrentHashMap<>();
    private final Map<ChessGame, User> gameAdminMap = new ConcurrentHashMap<>();
    private final Map<ChessGame, List<Session>> gameSessionsMap = new ConcurrentHashMap<>();
    private final Map<Session, User> sessionUserMap = new ConcurrentHashMap<>();

    // Default game settings
    public void initializeDefaultSettings(ChessGame game) {
        SettingGameStates defaultSettings = new SettingGameStates((short) 30, 10, true, false, 30);
        gameSettingsMap.putIfAbsent(game, defaultSettings);
    }

    // Set Admin for a game
    public void setAdminUser(ChessGame game, User adminUser) {
        gameAdminMap.put(game, adminUser);
    }

    public User getAdminUser(ChessGame game) {
        return gameAdminMap.get(game);
    }

    // Retrieve settings for a specific game
    public SettingGameStates getSettings(ChessGame game) {
        return gameSettingsMap.get(game);
    }

    // Update settings for a specific game
    public boolean updateGameSettings(User requestingUser, ChessGame game, SettingGameStates updatedSettings) {
        if (isAdmin(requestingUser, game)) {
            gameSettingsMap.put(game, updatedSettings);
            broadcastSettings(game, updatedSettings);
            return true;
        }
        return false;
    }

    // Register a user session for a specific game
    public void addSessionToGame(ChessGame game, Session session) {
        gameSessionsMap.computeIfAbsent(game, k -> new ArrayList<>()).add(session);
    }

    // Remove a user session from a game
    public void removeSessionFromGame(ChessGame game, Session session) {
        List<Session> sessions = gameSessionsMap.get(game);
        if (sessions != null) {
            sessions.remove(session);
        }
    }

    // Broadcast settings to all users in a game
    private void broadcastSettings(ChessGame game, SettingGameStates settings) {
        List<Session> sessions = gameSessionsMap.get(game);
        if (sessions != null) {
            for (Session session : sessions) {
                try {
                    String settingsMessage = "Settings: " + settings.toString(); // Format as needed
                    session.getBasicRemote().sendText(settingsMessage);
                } catch (IOException e) {
                    // Handle error logging
                }
            }
        }
    }

    // Assign a new Admin if the current Admin leaves
    public void reassignAdminIfNecessary(ChessGame game) {
        List<Session> sessions = gameSessionsMap.get(game);
        if (sessions != null && !sessions.isEmpty()) {
            Session newAdminSession = sessions.get(0);
            User newAdminUser = getUserFromSession(newAdminSession);
            gameAdminMap.put(game, newAdminUser);
            sendAdminNotification(newAdminSession);
        }
    }
    public boolean isAdmin(User user, ChessGame game) {
        return user.equals(gameAdminMap.get(game));
    }
    private Session findSessionForUser(ChessGame game, User user) {
        List<Session> sessions = gameSessionsMap.get(game);
        if (sessions != null) {
            for (Session session : sessions) {
                if (sessionUserMap.get(session).equals(user)) {
                    return session;
                }
            }
        }
        return null;
    }



    // Helper method to send admin notification
    private void sendAdminNotification(Session session) {
        try {
            session.getBasicRemote().sendText("You are now the Admin.");
        } catch (IOException e) {
            // Handle error logging
        }
    }
    private void sendBootNotification(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
            session.close(); // Optionally close the session after notification
        } catch (IOException e) {
            // Handle error logging
        }
    }
    public boolean bootPlayer(User requestingUser, ChessGame game, User targetUser) {
        if (isAdmin(requestingUser, game)) {
            Session targetSession = findSessionForUser(game, targetUser);
            if (targetSession != null) {
                sendBootNotification(targetSession, "You have been removed from the game.");
                removeSessionFromGame(game, targetSession);
                return true;
            }
        }
        return false;
    }

    // Helper method to retrieve user from session
    private User getUserFromSession(Session session) {
        // Assuming session has a way to retrieve the user
        // Implement your logic here
        return null;
    }
}
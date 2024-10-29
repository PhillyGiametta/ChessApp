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
import java.util.Map;
import org.slf4j.Logger;

/*
       This is a controller allowing settings to sync to the user, game, and other variables. This will handle mostly the game settings,
       User Settings will be handled in either seperate class, or rather using http requests since they do not need to be live updating
 */
@ServerEndpoint("/settings/{userName}")
@Controller
public class SettingsController {
    UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(SettingsController.class);
    private static Map <Session, User> sessionUserMap = new Hashtable<>();
    private static Map <User, Session> userSessionMap = new Hashtable<>();
    private static Map <ChessGame, Session> chessGameSessionMap = new Hashtable<>();
    private static Map <Session, ChessGame> sessionChessGameMap = new Hashtable<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) {
        User user = userRepository.findByUserName(userName); // Assuming User has a constructor with userName
        sessionUserMap.put(session, user);
        userSessionMap.put(user, session);

        try {
            session.getBasicRemote().sendText("Welcome, " + userName + "!");
        } catch (IOException e) {
            logger.info("IOException");
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        User user = sessionUserMap.get(session);
        if (user != null) {
            // Here, parse the message and handle settings or game updates
            System.out.println("Received message from " + user.getUserName() + ": " + message);

            // Example: Broadcast message to other users
            for (Session s : sessionUserMap.keySet()) {
                if (!s.equals(session)) { // avoid sending back to the sender
                    s.getBasicRemote().sendText(user.getUserName() + " says: " + message);
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        User user = sessionUserMap.get(session);
        if (user != null) {
            sessionUserMap.remove(session);
            userSessionMap.remove(user);

            ChessGame game = sessionChessGameMap.get(session);
            if (game != null) {
                chessGameSessionMap.remove(game);
                sessionChessGameMap.remove(session);
            }
            System.out.println("Session closed for user: " + user.getUserName());
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error for session: " + session.getId());
        logger.info("ERROR: websocket failed");
    }
}

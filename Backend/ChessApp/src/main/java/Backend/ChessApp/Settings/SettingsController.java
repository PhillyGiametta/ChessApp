package Backend.ChessApp.Settings;


import Backend.ChessApp.Game.ChessGame;
import Backend.ChessApp.Users.User;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/*
       This is a controller allowing settings to sync to the user, game, and other variables
 */
@ServerEndpoint("/settings/{userName}")
@Controller
public class SettingsController {

    private static Map <Session, User> sessionUserMap = new Hashtable<>();
    private static Map <User, Session> userSessionMap = new Hashtable<>();
    private static Map <ChessGame, Session> chessGameSessionMap = new Hashtable<>();
    private static Map <Session, ChessGame> sessionChessGameMap = new Hashtable<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName){

    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

    }

    @OnClose
    public void onClose(Session session) throws IOException {

    }

    @OnError
    public void onError(Session session, Throwable throwable){

    }
}

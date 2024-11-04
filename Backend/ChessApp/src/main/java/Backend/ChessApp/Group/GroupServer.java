package Backend.ChessApp.Group;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@ServerEndpoint("/group/{lobbyId}/{username}")
@Component
public class GroupServer {
    private static Map<Session, String > sessionUsernameMap = new Hashtable< >();
    private static Map < String, Session > usernameSessionMap = new Hashtable < > ();
    private static Map <Integer, Group> groupsMap = new Hashtable < > ();

    
}

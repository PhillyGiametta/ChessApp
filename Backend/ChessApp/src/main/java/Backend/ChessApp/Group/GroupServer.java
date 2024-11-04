package Backend.ChessApp.Group;

import Backend.ChessApp.Users.User;
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

    private final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("groupId") int groupId, @PathParam("username") User user) throws IOException{

        logger.info("[onOpen]: " + user + " joined group: " + groupId);

        Group group = groupsMap.computeIfAbsent(groupId, id -> new Group(id)); //If lobby doesn't exist, create it

        if(!group.addUser(user)){
            session.close();
            return;
        }

        sessionUsernameMap.put(session, user.getUserName());
        usernameSessionMap.put(user.getUserName(), session);
        
    }
}

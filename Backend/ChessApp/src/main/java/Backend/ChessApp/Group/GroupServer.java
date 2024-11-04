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

@ServerEndpoint("/group/{groupName}/{username}")
@Component
public class ChatServer {
    private static Map<String, Map<Session, String>> groupSessions = new Hashtable<>();
    private static Map<Session, String > sessionUsernameMap = new Hashtable< >();
    private static Map <String, Session > usernameSessionMap = new Hashtable < > ();

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("groupName") String groupName, @PathParam("username") String username) throws IOException {

        // server side log
        logger.info("[onOpen] User {} joined group {}", username, groupName);

        //init group if it doesnt exist
        groupSessions.computeIfAbsent(groupName, k -> new Hashtable<>());

        //add user to group
        groupSessions.get(groupName).put(session, username);
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);

        //Notify chat
        broadcastToGroup(groupName,username + " has joined.");
        logger.info("[onOpen] Current group size: {}", groupSessions.get(groupName).size());

    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("groupName") String groupName) throws IOException {

        // get the username by session
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onMessage] " + username + ": " + message);

        broadcastToGroup(groupName,username + ": " + message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("groupName") String groupName) throws IOException {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onClose] " + username);

        if(groupSessions.containsKey(groupName)){
            groupSessions.get(groupName).remove(session);
            broadcastToGroup(groupName, "User " + username + " has left the group.");
        }

        // remove user from mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }

    private void broadcastToGroup(String groupName, String message) {
        Map<Session, String> group = groupSessions.get(groupName);
        if(group != null) {
            group.keySet().forEach(session -> {
                try{
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.info("[broadcastToGroup Exception] " + e.getMessage());
                }
            });
        }
    }
}

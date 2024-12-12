package Backend.ChessApp.Group;

import Backend.ChessApp.AdminControl.Admin;
import Backend.ChessApp.AdminControl.AdminRepo;
import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ServerEndpoint("/group/{groupName}/{username}/{joinCode}")
@Controller
public class GroupServer {

    private static Map<String, Map<Session, String>> groupSessions = new Hashtable<>();
    private static Map<Session, String > sessionUsernameMap = new Hashtable< >();
    private static Map <String, Session > usernameSessionMap = new Hashtable < > ();

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(GroupServer.class);

    private static UserRepository userRepository;

    private static GroupRepository groupRepository;

    private static GroupService groupService;

    private static AdminRepo adminRepo;

    @Autowired
    public void setGroupService(GroupService serv) {
        groupService = serv;
    }

    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepository = repo;
    }

    @Autowired
    public void setGroupRepository(GroupRepository repo) {
        groupRepository = repo;
    }

    @Autowired
    public void setAdminRepo(AdminRepo repo){
        adminRepo = repo;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("groupName") String groupName, @PathParam("username") String username, @PathParam("joinCode") String joinCode) throws IOException {
        // server side log
        logger.info("[onOpen] User {} joined group {}", username, groupName);

        User user = userRepository.findByUserName(username);
        if(user == null){
            session.close();
            return;
        }

        Group group = groupRepository.findBygroupName(groupName);
        logger.info("group = " + group.getGroupName());

        //init group if it doesnt exist
        groupSessions.computeIfAbsent(groupName, k -> new Hashtable<>());

        if(group.isPrivate() && !group.getJoinCode().equals(joinCode)){
            session.close();
            return;
        }

        if(groupService.addUserToGroup(group, user)){
            //add user to group
            groupSessions.get(groupName).put(session, username);
            sessionUsernameMap.put(session, username);
            usernameSessionMap.put(username, session);

            //assign admin
            Admin admin = group.getAdminId();

            //update repos
            adminRepo.save(admin);
            groupRepository.save(group);
            userRepository.save(user);
        }else{
            session.close();
            return;
        }

        //Notify chat
        broadcastToGroup(groupName,username + " has joined.");
        broadcastPlayerList(groupName);
        logger.info("[onOpen] Current group size: {}", groupSessions.get(groupName).size());
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("groupName") String groupName) throws IOException {
        // get the username by session
        String username = sessionUsernameMap.get(session);

        User user = userRepository.findByUserName(username);
        Group group = groupRepository.findBygroupName(groupName);

        // server side log
        DateTimeFormatter d = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        broadcastToGroup(groupName,"[" + d.format(now) + "] " + username + ": " + message);
        logger.info("[onMessage] " + username + ": " + message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("groupName") String groupName) throws IOException {

        String username = sessionUsernameMap.get(session);

        // Server side log
        logger.info("[onClose] " + username);

        // Remove user from mappings
        groupSessions.remove(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        session.close();

        // Remove the user from the group and delete the group if empty
        if(groupService.removeUserFromGroupAndDeleteIfEmpty(groupName, username)){
            return;
        }
        broadcastPlayerList(groupName);
        broadcastToGroup(groupName, "User " + username + " has left the group.");
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

    private void broadcastPlayerList(String groupName){
        Map<Session, String> group = groupSessions.get(groupName);

        if(group != null) {
            List<String> playerList = group.values().stream().toList();
            String message = "Players: " + String.join(", ", playerList);

            group.keySet().forEach(session -> {
                try{
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.info("[broadcastPlayerList Exception] " + e.getMessage());
                }
            });
        }
    }
}

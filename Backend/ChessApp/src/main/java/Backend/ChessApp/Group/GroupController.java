package Backend.ChessApp.Group;

import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    //Get all groups
    @GetMapping(path = "/groups")
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    //Get specific group
    @GetMapping(path = "/groups/{groupId}")
    Group getGroupById(@PathVariable int groupId) {
        return groupRepository.findById(groupId);
    }

    //Create
    @PostMapping(path = "/groups")
    public Group createGroup(@RequestBody Group group) {
        return groupRepository.save(group);
    }

    @DeleteMapping(path = "/groups/{groupId}")
    public void deleteGroup(@PathVariable int groupId) {
        groupRepository.deleteById(groupId);
    }

    //Start game
    @PostMapping(path = "/groups/{groupId}/start")
    public ResponseEntity<String> startGame(@PathVariable int groupId, @RequestParam String username){
        Group group = groupRepository.findById(groupId);
        User user = userRepository.findByUserName(username);

        //Make sure only the leader can start the game
        if(!group.isLeader(user)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Leader must start the game");
        }
//        gameService.startGame(group); Waiting for game implementation
        return ResponseEntity.ok("Game is starting");
    }

}

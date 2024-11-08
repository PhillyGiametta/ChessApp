package Backend.ChessApp.Group;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;

    //Get all groups
    @GetMapping(path = "/groups")
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    //Get specific group
    @GetMapping(path = "/groups/{groupName}")
    Group getGroupById(@PathVariable String groupName) {
        return groupRepository.findBygroupName(groupName);
    }

    //Create
    @PostMapping(path = "/groups")
    public Group createGroup(@RequestBody Group group) {
        return groupRepository.save(group);
    }

//    @DeleteMapping(path = "/groups/{id}")
//    public void deleteGroup(@PathVariable int id) {
//        groupRepository.deleteById(id);
//    }


    @DeleteMapping(path = "/groups/{groupName}")
    public void deleteGroupBygroupName(@PathVariable String groupName) {
        groupRepository.deleteBygroupName(groupName);
    }
}

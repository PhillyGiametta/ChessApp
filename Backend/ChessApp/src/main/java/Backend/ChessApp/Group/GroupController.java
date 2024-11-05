package Backend.ChessApp.Group;

import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping(path = "/groups/{id}")
    Group getGroupById(@PathVariable int id) {
        return groupRepository.findById(id);
    }

    //Create
    @PostMapping(path = "/groups")
    public Group createGroup(@RequestBody Group group) {
        return groupRepository.save(group);
    }

    @DeleteMapping(path = "/groups/{id}")
    public void deleteGroup(@PathVariable int id) {
        groupRepository.deleteById(id);
    }

}

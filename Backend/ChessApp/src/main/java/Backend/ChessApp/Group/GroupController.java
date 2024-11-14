package Backend.ChessApp.Group;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Fetches all groups")
    @GetMapping(path = "/groups")
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    //Get specific group
    @Operation(summary = "fetches specific group given the groupName")
    @GetMapping(path = "/groups/{groupName}")
    Group getGroupById(@PathVariable String groupName) {
        return groupRepository.findBygroupName(groupName);
    }

    //Create
    @Operation(summary = "Creates a group")
    @PostMapping(path = "/groups")
    public Group createGroup(@RequestBody Group group) {
        return groupRepository.save(group);
    }

//    @DeleteMapping(path = "/groups/{id}")
//    public void deleteGroup(@PathVariable int id) {
//        groupRepository.deleteById(id);
//    }

    @Operation(summary = "Deletes a group given the groupName")
    @DeleteMapping(path = "/groups/{groupName}")
    public void deleteGroupBygroupName(@PathVariable String groupName) {
        groupRepository.deleteBygroupName(groupName);
    }
}

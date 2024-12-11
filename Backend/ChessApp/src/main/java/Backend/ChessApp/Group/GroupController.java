package Backend.ChessApp.Group;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "Groups", description = "Groups related HTTP methods")
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;

    //Get all groups
    @Operation(summary = "Fetches all groups")
    @GetMapping(path = "/groups")
    @ResponseBody
    public List<Group> getAllGroups() {
        JSONObject json = new JSONObject();
        List<Group> groups = new ArrayList<>();
        Iterable<Group> groupsAll = groupRepository.findAll();
        for(Group group : groupsAll) {
            groups.add(group);
        }
        return groups;
    }

    //Get specific group
    @Operation(summary = "fetches specific group given the groupName")
    @GetMapping(path = "/groups/{groupName}")
    Group getGroupById(@PathVariable String groupName) {
        return groupRepository.findBygroupName(groupName);
    }

    //Get group by join code
    @GetMapping(path = "/groups/{joinCode}")
    Group getGroupByJoinCode(@PathVariable String joinCode){
        return groupRepository.findByjoinCode(joinCode);
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

package Backend.ChessApp.Group;

import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Group addUserToGroup(int groupId, User user){
        Group group = groupRepository.findById(groupId);
        if(group != null && !group.isFull()){
            group.addUser(user);
            groupRepository.save(group);
        }
        return group;
    }

    @Transactional
    public Group removeUserFromGroup(int groupId, User user){
        Group group = groupRepository.findById(groupId);
        if(group != null){
            group.removeUser(user);
            groupRepository.save(group);
        }
        return group;
    }

    public Group getGroup(int groupId){
        return groupRepository.findById(groupId);
    }
}

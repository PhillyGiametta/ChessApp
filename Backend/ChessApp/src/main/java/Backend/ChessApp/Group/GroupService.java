package Backend.ChessApp.Group;

import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void removeUserFromGroupAndDeleteIfEmpty(String groupName, String username) {
        Group group = groupRepository.findBygroupName(groupName);
        User user = userRepository.findByUserName(username);
        group.removeUser(user);

        // Save the updated group and user
        groupRepository.save(group);
        userRepository.save(user);

        // Check if the group is empty and delete if necessary
        if (group.getUsers().isEmpty()) {
            groupRepository.deleteById(group.getGroupId());
        }
    }
}

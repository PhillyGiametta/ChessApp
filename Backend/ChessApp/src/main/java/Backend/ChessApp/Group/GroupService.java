package Backend.ChessApp.Group;

import Backend.ChessApp.AdminControl.AdminRepo;
import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Backend.ChessApp.AdminControl.Admin;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepo adminRepo;

    @PersistenceContext
    private EntityManager em;

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
            if(group.getAdminId() != null){
                adminRepo.delete(group.getAdminId());
            }
            groupRepository.delete(group);
        }else{
            throw new IllegalStateException("Group is not empty");
        }
    }

    @Transactional
    public boolean addUserToGroup(Group group, User user) {
        if(group.getAdminId() == null){
            Admin admin = new Admin();
            admin.setUser(user);
            admin.setGroup(group);

            user.setAdmin(admin);

            adminRepo.save(admin);
            group.setAdmin(admin);
        }
        group.getUsers().add(user);
        groupRepository.save(group);
        return true;
    }
}

package Backend.ChessApp.Group;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    Group findById(int id);
    Group findBygroupName(String groupName);
    void deleteById(int id);
    void deleteBygroupName(String groupName);
}

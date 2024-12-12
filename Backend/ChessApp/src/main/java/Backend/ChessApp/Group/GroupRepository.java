package Backend.ChessApp.Group;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    Group findById(int id);
    Group findBygroupName(String groupName);
    Group findByjoinCode(String joinCode);
    void deleteById(int id);
    @Transactional
    void deleteBygroupName(String groupName);
}

package Backend.ChessApp.Users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(int id);
    void deleteById(int id);
    User findByUserName(String userName);
    User findByUserEmail(String userEmail);
    boolean existsByUserName(String userName);
    boolean existsByUserEmail(String userEmail);
    User findByPasswordResetToken(String token);
}



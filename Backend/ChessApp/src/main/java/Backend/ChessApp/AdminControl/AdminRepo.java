package Backend.ChessApp.AdminControl;

import Backend.ChessApp.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin, Integer> {
    Admin findById(int id);
}

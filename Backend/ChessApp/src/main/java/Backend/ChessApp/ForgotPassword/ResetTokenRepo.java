package Backend.ChessApp.ForgotPassword;
import Backend.ChessApp.Users.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetTokenRepo extends JpaRepository<User, String> {

}

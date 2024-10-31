package Backend.ChessApp.Settings;

import Backend.ChessApp.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepo extends JpaRepository<SettingsUserStates, User> {


}

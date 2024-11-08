package Backend.ChessApp.Settings;

import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepo extends JpaRepository<SettingsUserStates, User> {

    SettingsUserStates findByUser(User user);

}

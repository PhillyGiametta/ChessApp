package Backend.ChessApp.Users;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User findById(int id);

    User findByUserEmail(String userEmail);

    User findByUserName(String userName);

    void save(User user);

    void deleteById(int id);

    void deleteAll();

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserName(String userName);
}

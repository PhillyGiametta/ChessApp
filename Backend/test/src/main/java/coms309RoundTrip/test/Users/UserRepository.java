package coms309RoundTrip.test.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);
    void deleteById(int id);
    User findByUserName(String userName);
    User findByUserEmail(String userEmail);
}


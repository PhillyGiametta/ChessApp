package coms309RoundTrip.test.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private UserRepository userRepository;
    private JpaRepository jpaRepository;

    @GetMapping(path = "/users")
    List<User> getAllUser() {return userRepository.findAll();}

    @GetMapping(path = "/users/{id}")
    User getUser(@PathVariable int id) {return userRepository.findById(id);}

}

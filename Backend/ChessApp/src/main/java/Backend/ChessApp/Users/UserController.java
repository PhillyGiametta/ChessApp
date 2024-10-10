package Backend.ChessApp.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JpaRepository jpaRepository;

    private String Dsuccess = "{\"User was deleted\": \"successfully\"}";
    private String Postsuccess = "{\"User was created\": \"successfully\"}";
    private String Dfail = "{\"User was deleted\": \"Unsuccessfully\"}";

    @GetMapping(path = "/users")
    List<User> getAllUser() {return userRepository.findAll();}

    @GetMapping(path = "/users/{id}")
    User getUser1(@PathVariable int id) {return userRepository.findById(id);}

    @GetMapping(path = "users/{userName}")
    User getUser2(@PathVariable String userName) {return userRepository.findByUserName(userName);}

    @GetMapping(path = "users/{userEmail}")
    User getUser3(@PathVariable String userEmail) {return userRepository.findByUserEmail(userEmail);}

    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if(user == null){
            return "User is invalid";
        }
        user = new User(user.getUserName(), user.getUserEmail() ,user.getUserPassword());
        userRepository.save(user);
        return Postsuccess;
    }

    @PutMapping(path = "/users/{userName}")
    User updateUser(@PathVariable String userName, @RequestBody User updated){
        User user = userRepository.findByUserName(userName);
        if(user == null){
            return null;
        }
        userRepository.save(updated);
        return userRepository.findByUserName(userName);
    }

    @PutMapping(path = "/users/{userEmail}")
    User updateUser2(@PathVariable String userEmail, @RequestBody User updated){
        User user = userRepository.findByUserEmail(userEmail);
        if(user == null){
            return null;
        }
        userRepository.save(updated);
        return userRepository.findByUserEmail(userEmail);
    }

    @PutMapping(path = "/users/{userId}")
    User updateUser(@PathVariable int id, @RequestBody User updated){
        User user = userRepository.findById(id);
        if(user == null){
            return null;
        }
        userRepository.save(updated);
        return userRepository.findById(id);
    }

    @Transactional
    @DeleteMapping(path = "/users/{userId}")
    String deleteUser(@PathVariable int userId){
        userRepository.deleteById(userId);
        return Dsuccess;
    }

    @DeleteMapping(path = "/users/deleteAllUsers")
    String deleteAllUsers() {
        userRepository.deleteAll();
        return Dsuccess;
    }


}
package Backend.ChessApp.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    private String Dsuccess = "{\"User was deleted\": \"successfully\"}";
    private String Postsuccess = "{\"User was created\": \"successfully\"}";
    private String Dfail = "{\"User was deleted\": \"Unsuccessfully\"}";

    //Gets all users
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    //Get specific user
    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

    @GetMapping(path = "/users/{userName}")
    User getUserByUsername(@PathVariable String userName) {
        return userRepository.findByUserName(userName);
    }

    @GetMapping(path = "/users/{userEmail}")
    User getUserByUserEmail(@PathVariable String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

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
    User updateUserByUsername(@PathVariable String userName, @RequestBody User updated){
        User user = userRepository.findByUserName(userName);
        if(user == null){
            return null;
        }
        userRepository.save(updated);
        return userRepository.findByUserName(userName);
    }

    @PutMapping(path = "/users/{userEmail}")
    User updateUserByUserEmail(@PathVariable String userEmail, @RequestBody User updated){
        User user = userRepository.findByUserEmail(userEmail);
        if(user == null){
            return null;
        }
        userRepository.save(updated);
        return userRepository.findByUserEmail(userEmail);
    }

    @PutMapping(path = "/users/{userId}")
    User updateUserByUserId(@PathVariable int userId, @RequestBody User updated){
        User user = userRepository.findById(userId);
        if(user == null){
            return null;
        }
        userRepository.save(updated);
        return userRepository.findById(userId);
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
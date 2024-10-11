package Backend.ChessApp.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
  //  @Autowired
  //  private JpaRepository jpaRepository;

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

    @GetMapping(path = "users/userName/{userName}")
    User getUserByUsername(@PathVariable String userName) {
        return userRepository.findByUserName(userName);
    }

    @GetMapping(path = "users/userEmail/{userEmail}")
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

    @PutMapping(path = "/users/userName/{userName}")
    User updateUserByUsername(@PathVariable String userName, @RequestParam(value = "userPassword") String password){
        User user = userRepository.findByUserName(userName);
        if(user == null){
            return null;
        }
        user.setUserPassword(password);
        userRepository.save(user);
        return userRepository.findByUserName(userName);
    }

    @PutMapping(path = "/users/userEmail/{userEmail}")
    User updateUserByUserEmail(@PathVariable String userEmail, @RequestParam(value = "userPassword") String password){
        User user = userRepository.findByUserEmail(userEmail);
        if(user == null){
            return null;
        }
        user.setUserPassword(password);
        userRepository.save(user);
        return userRepository.findByUserEmail(userEmail);
    }


    @PutMapping(path = "/users/{userId}")
    User updateUserByUserId(@PathVariable int userId, @RequestParam(value = "userPassword") String password){
        User user = userRepository.findById(userId);
        if(user == null){
            return null;
        }
        user.setUserPassword(password);
        userRepository.save(user);
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
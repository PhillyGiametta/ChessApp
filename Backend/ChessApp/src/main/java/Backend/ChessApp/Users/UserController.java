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

    private final String Dsuccess = "{\"User was deleted\": \"successfully\"}";
    private final String Postsuccess = "{\"User was created\": \"successfully\"}";
    private final String Dfail = "{\"User was deleted\": \"Unsuccessfully\"}";

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
        if(user == null || user.getUserName() == null || user.getUserPassword() == null || user.getUserEmail() == null)
            return "User is invalid";
        else if(userRepository.existsByUserEmail(user.getUserEmail()))
            return "Email already in use";
        else if (userRepository.existsByUserName(user.getUserName()))
            return "Username already in use";

        user = new User(user.getUserName(), user.getUserEmail() ,user.getUserPassword());
        userRepository.save(user);
        return Postsuccess;
    }
    @PostMapping(path = "/users/CopyUser")

    @PutMapping(path = "/users/changePasswordFromUserName/{userName}")
    User updateUserByUsername(@PathVariable String userEmail, @RequestParam(value = "userPassword") String password){
        User user = userRepository.findByUserEmail(userEmail);
        if(user == null){
            return null;
        }
        user.setUserPassword(password);
        userRepository.save(user);
        return user;
    }

    @PutMapping(path = "/users/changeUserName/{userEmail}")
    User updateUserByUserEmail(@PathVariable String userEmail, @RequestParam(value = "userName") String userName){
        User user = userRepository.findByUserEmail(userEmail);
        if(user == null){
            return null;
        }
        user.setUserName(userName);
        userRepository.save(user);
        return user;
    }

    @PutMapping(path = "/users/changePassword/{userId}")
    User updateUserByUserId(@PathVariable int userId, @RequestParam(value = "userPassword") String password){
        User user = userRepository.findById(userId);
        if(user == null){
            return null;
        }
        user.setUserPassword(password);
        userRepository.save(user);
        return user;
    }

    @PutMapping(path = "/users/changeUserName/{userName}")
    User updateUserName(@PathVariable int userId, @RequestParam(value = "userName") String userName){
        User update = userRepository.findById(userId);
        if(update == null){
            return null;
        }
        update.setUserName(userName);
        userRepository.save(update);
        return update;
    }

    @Transactional
    @DeleteMapping(path = "/users/{userId}")
    String deleteUser(@PathVariable int userId){
        userRepository.deleteById(userId);
        return Dsuccess;
    }

    @Transactional
    @DeleteMapping(path = "/users/deleteByUsername/{userName}")
    String deleteUserByUsername(@PathVariable String userName){
        User user;
        user = userRepository.findByUserName(userName);
        if(user == null){
            return Dfail;
        }
        userRepository.deleteById(user.getUserId());
        return Dsuccess;
    }
    @Transactional
    @DeleteMapping(path = "/users/deleteByEmail/{userEmail}")
    String deleteUserByEmail(@PathVariable String userEmail){
        User user;
        user = userRepository.findByUserEmail(userEmail);
        if(user == null){
            return Dfail;
        }
        userRepository.deleteById(user.getUserId());
        return Dsuccess;
    }

    @DeleteMapping(path = "/users/deleteAllUsers")
    String deleteAllUsers() {
        userRepository.deleteAll();
        return Dsuccess;
    }

 }
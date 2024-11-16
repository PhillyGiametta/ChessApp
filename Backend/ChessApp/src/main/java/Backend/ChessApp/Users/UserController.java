package Backend.ChessApp.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "Users", description = "Class for HTTP calls for the users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
  //  @Autowired
  //  private JpaRepository jpaRepository;

    private String Dsuccess = "{\"User was deleted\": \"successfully\"}";
    private String Postsuccess = "{\"User was created\": \"successfully\"}";
    private String Dfail = "{\"User was deleted\": \"Unsuccessfully\"}";

    //Gets all users
    @Operation(summary = "Returns a list of all users in database.")
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    //Get specific user
    @Operation(summary = "Returns a user from the database given their id.")
    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

    @Operation(summary = "Returns a user from the database given their username.")
    @GetMapping(path = "users/userName/{userName}")
    User getUserByUsername(@PathVariable String userName) {
        return userRepository.findByUserName(userName);
    }

    @Operation(summary = "Returns a user from the database given their email.")
    @GetMapping(path = "users/userEmail/{userEmail}")
    User getUserByUserEmail(@PathVariable String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

    @Operation(summary = "creates a user from a JSON object, containing email, username, and password, auto assigns id.")
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

    @Operation(summary = "Changes the users password, from the given username.")
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

    @Operation(summary = "Changes the user password from the given id.")
    @PutMapping(path = "/users/{userId}")
    User updateUserByUserId(@PathVariable int userId, @RequestParam(value = "userPassword") String password){
        User user = userRepository.findById(userId);
        if(user == null){
            return null;
        }
        user.setUserPassword(password);
        userRepository.save(user);
        return user;
    }

    @Operation(summary = "Changes the users username from their id #.")
    @PutMapping(path = "/users/changeUserName/{userId}")
    User updateUserName(@PathVariable int userId, @RequestParam(value = "userName") String userName){
        User update = userRepository.findById(userId);
        if(update == null){
            return null;
        }
        update.setUserName(userName);
        userRepository.save(update);
        return update;
    }

    @Operation(summary = "Deletes a user from their userID.")
    @Transactional
    @DeleteMapping(path = "/users/{userId}")
    String deleteUser(@PathVariable int userId){
        userRepository.deleteById(userId);
        return Dsuccess;
    }

    @Operation(summary = "From the users username, allow to change all data, being password, email, and username")
    @PutMapping("/users/{userName}")
    public User updateProfile(@PathVariable String userName, @RequestBody User userDetails) {
        User user = userRepository.findByUserName(userName);

        if (user == null) {
            throw new RuntimeException("User not found with username: " + userName);
        }

        // Update user details
        user.setUserName(userDetails.getUserName());
        user.setUserEmail(userDetails.getUserEmail());
        user.setUserPassword(userDetails.getUserPassword());

        // Save the updated user
        userRepository.save(user);

        return user;
    }

    @Operation(summary = "Delete a user from the database given their username.")
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
    @Operation(summary = "Delete a user from the database given their email")
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
    @Operation(summary = "A dangerous command that will delete every user from the database")
    @DeleteMapping(path = "/users/deleteAllUsers")
    String deleteAllUsers() {
        userRepository.deleteAll();
        return Dsuccess;
    }

 }
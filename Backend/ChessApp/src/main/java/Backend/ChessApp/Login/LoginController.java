package Backend.ChessApp.Login;

import Backend.ChessApp.Users.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    UserRepository userRepository;

    //when user logs in
    @Operation(summary = "Verifies login information is correct allows user to log in")
    @PostMapping(path ="/login")
    String userLogin(@RequestBody User user){

        int id = userRepository.findByUserName(user.getUserName()).getUserId();
        String username = user.getUserName();
        String password = user.getUserPassword();

        if(username.equals(userRepository.findById(id).getUserName())
                && password.equals(userRepository.findById(id).getUserPassword())){
            return "Successfully logged in.";
        }
        return "Failed to login, username or password incorrect.";
    }

}

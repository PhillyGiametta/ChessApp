package com.coms309.Chess.login;

import com.coms309.Chess.users.User;
import com.coms309.Chess.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    UserRepository userRepository;

    //when user logs in
    @PostMapping(path ="/login")
    String userLogin(@RequestBody User user){

        int id = userRepository.findByUsername(user.getUsername()).getId();
        String username = user.getUsername();
        String password = user.getPassword();

        if(username.equals(userRepository.findById(id).getUsername())
                && password.equals(userRepository.findById(id).getPassword())){
            return "Successfully logged in.";
        }
        return "Failed to login, username or password incorrect.";
    }

}

package com.coms309.Chess.signup;

import com.coms309.Chess.users.User;
import com.coms309.Chess.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {
    @Autowired
    UserRepository userRepository;

    //when user signs up
    @PostMapping(path = "/signup")
    String createUser(@RequestBody User user){
        if(user == null){
            return "Signup failed";
        }
        String name = user.getUsername();
        if(userRepository.findByUsername(name) != null){
            return "username already taken, please select a new username";
        }
        userRepository.save(user);
        return "Successfully signed up";
    }

}

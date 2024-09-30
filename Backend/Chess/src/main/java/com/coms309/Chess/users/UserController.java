package com.coms309.Chess.users;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 * @author Ethan Roe
 *
 */

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    //Gets all users
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    //Get specific user
    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable int id){
        return userRepository.findById(id);
    }

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

    //when user signs up
    @PostMapping(path = "/signup")
    String createUser(@RequestBody User user){
        if(user == null){
            return "Signup failed";
        }
        userRepository.save(user);
        return "Successfully signed up";
    }

    //Update user
    @PutMapping(path = "/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User update){
        User temp = userRepository.findById(id);
        if(update == null){
            System.out.println("failure");
            return null;
        }
        userRepository.save(update);
        return userRepository.findById(id);
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return "deleted user";
    }

 }
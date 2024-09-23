package com.coms309.Chess.users;

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
//    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    //gets all users
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    //when user logs in
    @PostMapping("/login")
    String userLogin(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();
        User tempUser = userRepository.findById(user.getId());

        if(tempUser.getUsername().equals(user.getUsername()) && tempUser.getPassword().equals(user.getPassword())){
            return "Successfully logged in!";
        }
        return "Failed to login, username or password incorrect";
    }
    //when user signs up
    @PostMapping("/signup")
    String userSignup(@RequestBody User user){
        if(user == null){
            return failure;
        }
        userRepository.save(user);
        return success;
    }

    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User update){
        User temp = userRepository.findById(id);
        if(update == null){
            System.out.println(failure);
            return null;
        }
        userRepository.save(update);
        return userRepository.findById(id);
    }

 }
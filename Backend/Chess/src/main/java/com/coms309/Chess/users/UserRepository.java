package com.coms309.Chess.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author Ethan Roe
 *
 */

@SpringBootApplication
public interface UserRepository extends JpaRepository {
    User findById(int id);
}
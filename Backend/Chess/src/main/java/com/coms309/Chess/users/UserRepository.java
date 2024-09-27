package com.coms309.Chess.users;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author Ethan Roe
 *
 */

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);

    User deleteById(int id);

    User findByUsername(String username);
}
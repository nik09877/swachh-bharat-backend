package com.nikhil.swachhbharatbackend.repositories;

import com.nikhil.swachhbharatbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}

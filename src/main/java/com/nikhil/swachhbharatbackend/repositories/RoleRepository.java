package com.nikhil.swachhbharatbackend.repositories;

import com.nikhil.swachhbharatbackend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}

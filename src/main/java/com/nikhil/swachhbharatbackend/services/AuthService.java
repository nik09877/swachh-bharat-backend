package com.nikhil.swachhbharatbackend.services;

import com.nikhil.swachhbharatbackend.exceptions.AlreadyExistsException;
import com.nikhil.swachhbharatbackend.models.LoginRequest;
import com.nikhil.swachhbharatbackend.models.LoginResponse;
import com.nikhil.swachhbharatbackend.models.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

public interface AuthService {


    User register(User user, boolean isDriver) throws IllegalArgumentException, AlreadyExistsException;

    LoginResponse login(LoginRequest loginRequest) throws DisabledException, BadCredentialsException;
}

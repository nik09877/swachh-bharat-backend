package com.nikhil.swachhbharatbackend.services.implementation;

import com.nikhil.swachhbharatbackend.models.Role;
import com.nikhil.swachhbharatbackend.models.User;
import com.nikhil.swachhbharatbackend.repositories.RoleRepository;
import com.nikhil.swachhbharatbackend.repositories.UserRepository;
import com.nikhil.swachhbharatbackend.exceptions.NotFoundException;
import com.nikhil.swachhbharatbackend.services.UserProfileService;
import com.nikhil.swachhbharatbackend.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User updateUserProfile(User user) throws NotFoundException, IllegalArgumentException {
        if (user == null ||
                user.getName() == null || user.getName().isBlank() ||
                user.getUsername() == null || user.getUsername().isBlank() ||
                user.getMobNumber() == null || user.getMobNumber().isBlank() ||
                user.getAddress() == null || user.getAddress().isBlank())
            throw new IllegalArgumentException("Mandatory fields can't be empty!");

        else if (user.getPassword() != null && !user.getPassword().isBlank() && user.getPassword().length() < 8)
            throw new IllegalArgumentException("Password should be minimum 8 characters long!");
        else if(!Util.isValidPhoneNumber(user.getMobNumber()))
            throw new IllegalArgumentException("Provide a valid phone number");
        else {
            User temp = userRepository.findByUsername(user.getUsername());
            if (temp == null) {
                throw new NotFoundException("User doesn't exists!");
            } else {
                temp.setName(user.getName());
                temp.setUsername(user.getUsername());
                if(user.getPassword() != null && !user.getPassword().isBlank()) {
                    temp.setPassword(passwordEncoder.encode(user.getPassword()));
                }
                temp.setMobNumber(user.getMobNumber());
                temp.setAddress(user.getAddress());

                Role driverRole = roleRepository.findById("DRIVER_USER").isPresent() ? roleRepository.findById("DRIVER_USER").get() : null;

                if(temp.getRoles().contains(driverRole)){
                    temp.setPickupCities(user.getPickupCities());
                    System.out.println("temp : " + temp.toString());
                }
                return userRepository.save(temp);
            }
        }
    }

    @Override
    public User getUserProfile(String username) throws NotFoundException, IllegalArgumentException {
        return userRepository.findByUsername(username);
    }
}

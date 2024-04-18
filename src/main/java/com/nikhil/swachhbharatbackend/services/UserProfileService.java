package com.nikhil.swachhbharatbackend.services;

import com.nikhil.swachhbharatbackend.exceptions.NotFoundException;
import com.nikhil.swachhbharatbackend.models.User;

public interface UserProfileService {
    
    User updateUserProfile(User user) throws NotFoundException, IllegalArgumentException;
    User getUserProfile(String username) throws NotFoundException, IllegalArgumentException;

}

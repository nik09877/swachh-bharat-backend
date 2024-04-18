package com.nikhil.swachhbharatbackend.controllers;

import com.nikhil.swachhbharatbackend.configurations.JwtUtil;
import com.nikhil.swachhbharatbackend.exceptions.NotFoundException;
import com.nikhil.swachhbharatbackend.models.User;
import com.nikhil.swachhbharatbackend.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    JwtUtil jwtUtil;

    @PutMapping("/")
    public ResponseEntity<?> updateUserProfile(@RequestBody User user){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userProfileService.updateUserProfile(user));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token){
        try{
            if (token != null && token.startsWith("Bearer ")) {
                String username = jwtUtil.extractUsername(token.substring(7));
                return ResponseEntity.status(HttpStatus.OK).body(userProfileService.getUserProfile(username));
            }
            else
                throw new Exception("Invalid Token");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}

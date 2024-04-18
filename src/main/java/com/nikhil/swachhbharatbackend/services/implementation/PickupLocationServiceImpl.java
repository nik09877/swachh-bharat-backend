package com.nikhil.swachhbharatbackend.services.implementation;

import com.nikhil.swachhbharatbackend.models.User;
import com.nikhil.swachhbharatbackend.repositories.PickupLocationRepository;
import com.nikhil.swachhbharatbackend.exceptions.NotFoundException;
import com.nikhil.swachhbharatbackend.models.PickupLocation;
import com.nikhil.swachhbharatbackend.repositories.UserRepository;
import com.nikhil.swachhbharatbackend.services.PickupLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Service
public class PickupLocationServiceImpl implements PickupLocationService {

    private static final Logger LOGGER = Logger.getLogger(PickupLocationServiceImpl.class.getName());

    @Autowired
    private PickupLocationRepository pickupLocationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public PickupLocation addPickupLocation(PickupLocation pickupLocation) throws IllegalArgumentException, DateTimeParseException {
        if (Stream.of(pickupLocation.getLandmark(), pickupLocation.getStreet(),
                pickupLocation.getCity(), pickupLocation.getState(),
                pickupLocation.getCountry(), pickupLocation.getDateAdded(),
                pickupLocation.getUserId(),pickupLocation.getLatitude(),pickupLocation.getLongitude())
                .anyMatch((p) -> p == null || String.valueOf(p).isBlank())) {
            throw new IllegalArgumentException("Mandatory fields can't be empty!");
        } else if (pickupLocation.getDateCleaned() != null && !pickupLocation.getDateCleaned().isBlank()) {
            throw new IllegalArgumentException("Date Cleaned can be updated, only after adding the location first!");
        } else if (pickupLocation.isStatus()) {
            throw new IllegalArgumentException("Status can be set to true, only after adding the location first!");
        } else if (pickupLocation.getDriverId() != null && !String.valueOf(pickupLocation.getDriverId()).isBlank()) {
            throw new IllegalArgumentException("Driver ID can be set, only after adding the location first!");
        }

        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        try {
            LocalDateTime.parse(pickupLocation.getDateAdded(), f);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Invalid Date Time, it can't be parsed! Error in ", e.getParsedString() + " at ", e.getErrorIndex());
        }

        return pickupLocationRepository.save(pickupLocation);
    }

    @Override
    public List<PickupLocation> getPickupLocation() {
        return pickupLocationRepository.findAll();
    }

    @Override
    public List<PickupLocation> getPickupLocationByUserId(Long userId) {
//        return getPickupLocation().stream().filter(loc -> loc.getUserId().equals(userId)).toList();
        return pickupLocationRepository.findByUserId(userId);
    }

    @Override
    public List<PickupLocation> getPickupLocationByCities(String[] cities) {
//        return getPickupLocation().stream().filter(loc -> Arrays.stream(cities).anyMatch(c -> c.equals(loc.getCity()))).toList();
        return pickupLocationRepository.findByCityIn(cities);
    }

    @Transactional
    @Override
    public PickupLocation updatePickupLocation(Long pickLocId, PickupLocation pickupLocation) throws NotFoundException, IllegalArgumentException, DateTimeParseException {
        Optional<PickupLocation> temp = pickupLocationRepository.findById(pickLocId);
        if (temp.isPresent()) {
            if (Stream.of(pickupLocation.getLandmark(), pickupLocation.getStreet(),
                    pickupLocation.getCity(), pickupLocation.getState(),
                    pickupLocation.getCountry(), pickupLocation.getDateAdded(),
                    pickupLocation.getUserId(),pickupLocation.getLatitude(),pickupLocation.getLongitude()).anyMatch((p) -> p == null || String.valueOf(p).isBlank())) {
                throw new IllegalArgumentException("Mandatory fields can't be empty!");
            }
            try {
                DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime dateAdded = LocalDateTime.parse(pickupLocation.getDateAdded(), f);
                if (pickupLocation.getDateCleaned() != null) {
                    LocalDateTime dateCleaned = LocalDateTime.parse(pickupLocation.getDateCleaned(), f);
                    if (!dateAdded.isBefore(dateCleaned))
                        throw new IllegalArgumentException("Date Cleaned can't be before Date Added!");

                    //ADD COINS TO WALLET OF USER AND DRIVER
                    Optional<User> res = userRepository.findById(pickupLocation.getUserId());
                    if(res.isPresent()){
                        User user = res.get();
                        user.setCoinsEarned(user.getCoinsEarned()+20);
                        userRepository.save(user);
                    }
                    res = userRepository.findById(pickupLocation.getDriverId());
                    if(res.isPresent()){
                        User user = res.get();
                        user.setCoinsEarned(user.getCoinsEarned()+100);
                        userRepository.save(user);
                    }
                }
                return pickupLocationRepository.save(pickupLocation);
            } catch (DateTimeParseException e) {
                throw new DateTimeParseException("Invalid Date Time, it can't be parsed! Error in ", e.getParsedString() + " at ", e.getErrorIndex());
            }
        } else {
            throw new NotFoundException("ERR_NOT_FOUND");
        }
    }

    @Override
    public void deletePickupLocation(Long pickLocId) throws NotFoundException {
        Optional<PickupLocation> pickupLocation = pickupLocationRepository.findById(pickLocId);
        if (pickupLocation.isPresent()) {
            pickupLocationRepository.delete(pickupLocation.get());
        } else {
            throw new NotFoundException("ERR_NOT_FOUND");
        }
    }

//    @Override
//    public void saveImageWithPickupLocId(byte[] imageData, Long pickupLocId) throws NotFoundException {
//        Optional<PickupLocation> res = pickupLocationRepository.findById(pickupLocId);
//        if(res.isEmpty()){
//            throw new NotFoundException("PickupLocation Not Found for id "+pickupLocId);
//        }
//        PickupLocation pickupLocation = res.get();
//        pickupLocation.setImage(imageData);
//        pickupLocationRepository.save(pickupLocation);
//    }
}

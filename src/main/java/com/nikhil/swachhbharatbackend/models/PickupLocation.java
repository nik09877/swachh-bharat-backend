package com.nikhil.swachhbharatbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "swachh_bharat_pickup_locations")
public class PickupLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pickup_location_id")
    private long pickLocId;

    private double latitude;
    private double longitude;

    @Column(name = "landmark", nullable = false)
    private String landmark;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "city", nullable = false, length = 60)
    private String city;

    @Column(name = "state", nullable = false, length = 60)
    private String state;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "date_added", nullable = false, length = 30)
    private String dateAdded;

    @Column(name = "date_cleaned", length = 30)
    private String dateCleaned;

    @Column(name = "status", nullable = false, columnDefinition = "boolean default false")
    private boolean status;

//    @JsonIgnore
//    @Column(name = "image", nullable = true)
//    @Lob
//    private byte[] image;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "driver_id")
    private Long driverId;
}

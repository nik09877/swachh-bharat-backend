package com.nikhil.swachhbharatbackend.utils;

import java.util.regex.Pattern;

public class Util {
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Regular expression to match only digits
        String regex = "^[0-9]+$";
        return Pattern.matches(regex, phoneNumber);
    }
}

package com.gwt.sample.shared;

public class NumberVerifier {

    public static boolean isNumeric(String inputString) {
        if (inputString == null) {
            return false;
        }
        try {
            Integer.valueOf(inputString);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }


    public static boolean isValidNumberOfNumbers(String number) {
        Integer num = Integer.valueOf(number);
        if (num > 1000 || num < 0) {
            return false;
        }
        return true;
    }

}

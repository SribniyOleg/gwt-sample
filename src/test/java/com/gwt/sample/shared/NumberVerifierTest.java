package com.gwt.sample.shared;


import org.junit.Test;

import static com.gwt.sample.shared.NumberVerifier.isNumeric;
import static com.gwt.sample.shared.NumberVerifier.isValidNumberOfNumbers;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NumberVerifierTest {

    @Test
    public void shouldCheckIsNumericString() {
        //given
        String number = "10";
        //when
        boolean result = isNumeric(number);
        //then
        assertTrue(result);
    }

    @Test
    public void shouldCheckIsNumericStringWhenIsIncorrect() {
        //given
        String number = "incorrect";
        //when
        boolean result = isNumeric(number);
        //then
        assertFalse(result);
    }

    @Test
    public void shouldCheckIsValidNumberOfNumbersNotGreatThen1000() {
        //given
        String number = "56";
        //when
        boolean result = isValidNumberOfNumbers(number);
        //then
        assertTrue(result);
    }

    @Test
    public void shouldCheckIsValidNumberOfNumbersNotGreatThen1000WhenIsInvalid() {
        //given
        String number = "1001";
        //when
        boolean result = isValidNumberOfNumbers(number);
        //then
        assertFalse(result);
    }
}
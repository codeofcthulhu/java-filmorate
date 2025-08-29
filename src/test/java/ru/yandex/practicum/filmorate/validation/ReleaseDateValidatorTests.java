package ru.yandex.practicum.filmorate.validation;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReleaseDateValidatorTests {

    @Test
    void testEarlyDate() {
        ReleaseDateValidator validator = new ReleaseDateValidator();

        assertFalse(validator.isValid(LocalDate.of(1700, 12, 12), null));
    }

    @Test
    void testLaterDate() {
        ReleaseDateValidator validator = new ReleaseDateValidator();

        assertTrue(validator.isValid(LocalDate.of(2024, 12, 12), null));
    }

    @Test
    void testBirthDateOfCinema() {
        ReleaseDateValidator validator = new ReleaseDateValidator();

        assertTrue(validator.isValid(LocalDate.of(1895, 12, 28), null));
    }

}


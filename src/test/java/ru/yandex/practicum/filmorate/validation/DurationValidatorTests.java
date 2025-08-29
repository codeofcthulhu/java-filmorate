package ru.yandex.practicum.filmorate.validation;

import java.time.Duration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DurationValidatorTests {

    @Test
    void testPositiveDuration() {
        DurationValidator validator = new DurationValidator();

        assertTrue(validator.isValid(Duration.ofMinutes(5), null));
    }

    @Test
    void testNegativeDuration() {
        DurationValidator validator = new DurationValidator();

        assertFalse(validator.isValid(Duration.ofMinutes(-5), null));
    }

    @Test
    void testZeroDuration() {
        DurationValidator validator = new DurationValidator();

        assertFalse(validator.isValid(Duration.ofMinutes(0), null));
    }

}

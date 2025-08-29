package ru.yandex.practicum.filmorate.exception;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private String message;
    private Map<String, String> details;
}

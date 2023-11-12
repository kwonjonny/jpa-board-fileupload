package board.jpa.querydsl.util.validator;

import org.springframework.util.StringUtils;

import board.jpa.querydsl.exception.DataNotFoundException;

public class ValidationUtil {

    public static void validateNotEmnty(Object value, String errorMessage) {
        if (value == null) {
            throw new DataNotFoundException(errorMessage);
        }
        if (value instanceof String && !StringUtils.hasText((String) value)) {
            throw new DataNotFoundException(errorMessage);
        }
    }
}

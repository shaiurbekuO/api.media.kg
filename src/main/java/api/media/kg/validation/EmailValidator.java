package api.media.kg.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<EmailValidation, String> {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false;
        }
        return Pattern.matches(EMAIL_REGEX, email);
    }
}

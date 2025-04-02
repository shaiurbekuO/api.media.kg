package api.media.kg.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidation, String> {
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return (phoneNumber.startsWith("998") || phoneNumber.startsWith("996")) && phoneNumber.length() == 12;
    }
}

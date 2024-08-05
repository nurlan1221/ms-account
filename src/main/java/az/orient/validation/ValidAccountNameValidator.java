package az.orient.validation;

import az.orient.handler.FieldException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class ValidAccountNameValidator implements ConstraintValidator<ValidAccountName, String> {
    private boolean isNullable;

    @Override
    public void initialize(ValidAccountName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        isNullable = constraintAnnotation.isNullable();

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (isNullable) {
            if (value == null) {
                buildCustomValidation(constraintValidatorContext, "Account name could not be null");
                return false;
            }
        }
        if (value.matches("\\d+")) {
            buildCustomValidation(constraintValidatorContext, "Account name cannot consist only of numbers");
            return false;
        }
        if (!(value.matches("[a-zA-Z]+"))) {
            buildCustomValidation(constraintValidatorContext, "Account name can only contain alphabetic characters");
            return false;
        }
        if (value.length() < 4 || value.length() > 20) {
            buildCustomValidation(constraintValidatorContext, "Account name must be between 4 and 20 characters");
            return false;
        }
        return true;
    }

    private void buildCustomValidation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}

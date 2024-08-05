package az.orient.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ValidAccountNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAccountName {
    String message() default "Account name is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean isNullable() default false;
}

package az.orient.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidAccountNameValidatorTest {
    ValidAccountNameValidator validator = new ValidAccountNameValidator();

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;


    @Test
    void isValidOnlyDigitValueThenReturnFalse() {
        //setup
        String value="12345";
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
        //when
        boolean result=validator.isValid(value,constraintValidatorContext);
        //then
        assertFalse(result);
    }
    @Test
    void isValidGivenNotOnlyLetterValuesThenReturnFalse() {
        String value="12345@$%!";
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
        boolean result=validator.isValid(value,constraintValidatorContext);
        assertFalse(result);
    }
    @Test
    void isValidGivenOnlyLetterValuesThenReturnTrue() {
        String value="Nurlan";
        verify(constraintValidatorContext,never()).buildConstraintViolationWithTemplate(anyString());
        boolean result=validator.isValid(value,constraintValidatorContext);
        assertTrue(result);
    }

    @Test
    void isValidGivenValueLessThan4ThenReturnFalse() {
        String value="Ali";
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()))
        .thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
        boolean result=validator.isValid(value,constraintValidatorContext);
        assertFalse(result);
    }

    @Test
    void isValidGivenValueGreaterThan20ThenReturnFalse() {
        String value="Nurlandwwrrfdwiigcuwcbgiywgdiygiwwwww";
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()))
        .thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
        boolean result=validator.isValid(value,constraintValidatorContext);
        assertFalse(result);
    }

}
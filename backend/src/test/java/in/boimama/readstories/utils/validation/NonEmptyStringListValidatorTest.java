package in.boimama.readstories.utils.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NonEmptyStringListValidatorTest {

    private NonEmptyStringListValidator validator;
    private ConstraintValidatorContext context;

    private NonEmptyStringList constraintAnnotation;

    @BeforeEach
    void setUp() {
        validator = new NonEmptyStringListValidator();
        context = mock(ConstraintValidatorContext.class);
        constraintAnnotation = mock(NonEmptyStringList.class);

        // Mocking minLength and maxLength values
        when(constraintAnnotation.minLength()).thenReturn(2);
        when(constraintAnnotation.maxLength()).thenReturn(5);

        validator.initialize(constraintAnnotation);
    }

    @Test
    void isValid_WithValidList_ShouldReturnTrue() {
        List<String> validList = Arrays.asList("abc", "def", "ghi");

        assertTrue(validator.isValid(validList, context));
    }

    @Test
    void isValid_WithEmptyList_ShouldReturnFalse() {
        List<String> emptyList = Arrays.asList();

        assertFalse(validator.isValid(emptyList, context));
    }

    @Test
    void isValid_WithNullList_ShouldReturnFalse() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void isValid_WithNullElement_ShouldReturnFalse() {
        List<String> listWithNull = Arrays.asList("abc", null, "def");

        assertFalse(validator.isValid(listWithNull, context));
    }

    @Test
    void isValid_WithEmptyStringElement_ShouldReturnFalse() {
        List<String> listWithEmptyString = Arrays.asList("abc", "", "def");

        assertFalse(validator.isValid(listWithEmptyString, context));
    }

    @Test
    void isValid_WithElementBelowMinLength_ShouldReturnFalse() {
        List<String> listWithShortElement = Arrays.asList("abc", "def", "a");

        assertFalse(validator.isValid(listWithShortElement, context));
    }

    @Test
    void isValid_WithElementAboveMaxLength_ShouldReturnFalse() {
        List<String> listWithLongElement = Arrays.asList("abc", "def", "ijklmnop");

        assertFalse(validator.isValid(listWithLongElement, context));
    }
}

package in.boimama.readstories.utils.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class UuidListValidatorTest {

    private final UuidListValidator validator = new UuidListValidator();
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    @Test
    void isValid_WithValidUuids_ShouldReturnTrue() {
        List<String> validUuids = Arrays.asList("550e8400-e29b-41d4-a716-446655440000", "123e4567-e89b-12d3-a456-426614174001");

        assertTrue(validator.isValid(validUuids, context));
    }

    @Test
    void isValid_WithNullList_ShouldReturnTrue() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void isValid_WithEmptyList_ShouldReturnTrue() {
        assertTrue(validator.isValid(List.of(), context));
    }

    @Test
    void isValid_WithInvalidUuidFormat_ShouldReturnFalse() {
        List<String> invalidUuids = Arrays.asList("not_a_uuid", "invalid_format");

        assertFalse(validator.isValid(invalidUuids, context));
    }

    @Test
    void isValid_WithNullUuid_ShouldReturnFalse() {
        List<String> uuidsWithNull = Arrays.asList("550e8400-e29b-41d4-a716-446655440000", null);

        assertFalse(validator.isValid(uuidsWithNull, context));
    }
}
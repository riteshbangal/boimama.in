package in.boimama.readstories.utils.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.UUID;

public class UuidListValidator implements ConstraintValidator<UuidList, List<String>> {
    @Override
    public void initialize(UuidList constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Allow null list, handle empty list separately if needed
        }

        for (String uuidStr : value) {
            if (uuidStr == null) {
                return false; // Disallow null UUIDs in the list
            }
            try {
                UUID.fromString(uuidStr);
            } catch (IllegalArgumentException exception) {
                return false; // Not a valid UUID format
            }
        }

        return true;
    }
}

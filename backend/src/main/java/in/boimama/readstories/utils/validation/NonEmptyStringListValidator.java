package in.boimama.readstories.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class NonEmptyStringListValidator implements ConstraintValidator<NonEmptyStringList, List<String>> {

    private int minLength;
    private int maxLength;

    @Override
    public void initialize(NonEmptyStringList constraintAnnotation) {
        minLength = constraintAnnotation.minLength();
        maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(List<String> list, ConstraintValidatorContext context) {
        // Check if the list is not empty and each element is a non-empty string
        if (list == null || list.isEmpty()) {
            return false;
        }

        for (String element : list) {
            if (element == null
                    || element.trim().isEmpty()
                    || element.length() < minLength
                    || element.length() > maxLength) {
                return false;
            }
        }

        return true;
    }
}

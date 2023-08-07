package in.boimama.readstories.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NonEmptyStringListValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NonEmptyStringList {
    String message() default "List must not be empty and all elements must be non-empty strings";

    int minLength() default 1;

    int maxLength() default 100;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

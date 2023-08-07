package in.boimama.readstories.utils.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UuidListValidator.class)
public @interface UuidList {
    String message() default "Invalid UUID format in the list";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package com.nashtech.rookies.java05.AssetManagement.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = GreaterThanCurrentDayValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface GreaterThanCurrentDayConstraint {
    String message() default "Assigned Date only current or future date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

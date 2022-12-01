package com.nashtech.rookies.java05.AssetManagement.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Date;
import java.time.LocalDate;

public class GreaterThanCurrentDayValidator implements ConstraintValidator<GreaterThanCurrentDayConstraint, Date> {
    @Override
    public void initialize(GreaterThanCurrentDayConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        Date currentDay = Date.valueOf(LocalDate.now());
        return value.after(currentDay);
    }
}

package com.company.storehouse.validation;

import com.company.storehouse.controller.request.ImportProductRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateMatchingValidator implements ConstraintValidator<DateMatching, ImportProductRequest> {

    @Override
    public void initialize(DateMatching constraintAnnotation) {
    }

    @Override
    public boolean isValid(ImportProductRequest request, ConstraintValidatorContext context) {
        return request.getProducedAt().isBefore(request.getExpiredAt());
    }

}

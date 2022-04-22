package com.company.storehouse.validation;

import com.company.storehouse.controller.request.ImportProductRequest;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class MaxSizeConstraintValidator implements ConstraintValidator<MaxSizeConstraint, List<ImportProductRequest>> {

    @Value("${import.products.max}")
    private String maxProductsForImport;

    @Override
    public void initialize(MaxSizeConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<ImportProductRequest> value, ConstraintValidatorContext context) {
        return value.size() <= Integer.parseInt(maxProductsForImport);
    }

}

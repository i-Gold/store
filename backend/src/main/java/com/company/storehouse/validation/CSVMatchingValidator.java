package com.company.storehouse.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CSVMatchingValidator implements ConstraintValidator<CSVMatching, MultipartFile> {

    @Override
    public void initialize(CSVMatching constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile csvFile, ConstraintValidatorContext context) {
        return Objects.requireNonNull(csvFile.getOriginalFilename()).endsWith(".csv");
    }

}

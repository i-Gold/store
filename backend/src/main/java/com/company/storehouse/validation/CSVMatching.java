package com.company.storehouse.validation;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CSVMatchingValidator.class)
public @interface CSVMatching {

    String message() default "The uploading file name must is only 'INPUT' or 'OUTPUT' in uppercase!";

    Class[] groups() default {};

    Class[] payload() default {};

}

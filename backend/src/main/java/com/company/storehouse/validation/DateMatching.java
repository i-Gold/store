package com.company.storehouse.validation;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateMatchingValidator.class)
public @interface DateMatching {

    String message() default "Date of producing must be earlier than the expiration date";

    Class[] groups() default {};

    Class[] payload() default {};

}

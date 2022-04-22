package com.company.storehouse.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxSizeConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface MaxSizeConstraint {

    String message() default "The input list cannot contain more than 10 items.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

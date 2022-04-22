package com.company.storehouse.validation;

import com.company.storehouse.controller.request.RegistrationRequest;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@Component
public class RetypePasswordValidator implements ConstraintValidator<RetypePassword, RegistrationRequest> {

    @Override
    public void initialize(RetypePassword retypePassword) {
    }

    @Override
    public boolean isValid(RegistrationRequest request, ConstraintValidatorContext context) {
        if (!Objects.equals(request.getPassword(), request.getRetypePassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{com.naturalprogrammer.spring.different.passwords}")
                    .addPropertyNode("password").addConstraintViolation()
                    .buildConstraintViolationWithTemplate("{com.naturalprogrammer.spring.different.passwords}")
                    .addPropertyNode("retypePassword").addConstraintViolation();
            return false;
        }
        return true;
    }
}

package com.company.storehouse.controller.request;

import com.company.storehouse.model.enumeration.RoleName;
import com.company.storehouse.validation.Email;
import com.company.storehouse.validation.Enum;
import com.company.storehouse.validation.RetypePassword;
import com.company.storehouse.validation.UniqueEmail;
import com.company.storehouse.validation.UniqueUsername;
import com.company.storehouse.validation.Username;
import com.company.storehouse.validation.ValidationType;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@RetypePassword(groups = ValidationType.RegistrationValidation.class)
public class RegistrationRequest implements Serializable {
    private static final long serialVersionUID = 5997305228125728535L;

    @Email(groups = ValidationType.RegistrationValidation.class)
    @UniqueEmail(groups = ValidationType.RegistrationValidation.class)
    @Size(min = 4, groups = ValidationType.RegistrationValidation.class)
    private String email;

    @Size(min = 8, max = 16, groups = ValidationType.RegistrationValidation.class)
    private String password;

    @Size(min = 8, max = 16, groups = ValidationType.RegistrationValidation.class)
    private String retypePassword;

    @Enum(enumClass = RoleName.class, groups = ValidationType.RegistrationValidation.class)
    private String roleName;

    @Username(groups = ValidationType.RegistrationValidation.class)
    @UniqueUsername(groups = ValidationType.RegistrationValidation.class)
    private String username;

}

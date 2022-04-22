package com.company.storehouse.controller.request;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {
    private static final long serialVersionUID = -193546474788284400L;

    @ApiParam(required = true)
    private String username;

    @ApiParam(required = true)
    private String password;

}

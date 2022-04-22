package com.company.storehouse.controller.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenResponse implements Serializable {
    private static final long serialVersionUID = 2951669841414765531L;

    private String token;

    public TokenResponse(String token) {
        this.token = "Bearer " + token;
    }

}

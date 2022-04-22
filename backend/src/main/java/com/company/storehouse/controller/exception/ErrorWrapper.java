package com.company.storehouse.controller.exception;

import org.springframework.http.HttpStatus;

public interface ErrorWrapper {

    String getMessage();

    HttpStatus getStatus();

}

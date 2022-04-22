package com.company.storehouse.controller.exception.handler;

import com.company.storehouse.controller.exception.ErrorWrapper;
import com.company.storehouse.controller.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerControllerAdvice<T extends Throwable> {

    @RequestMapping(produces = "application/json")
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleException(HttpServletRequest httpServletRequest, T ex) throws T {
        log.error(String.valueOf(httpServletRequest), ex);
        ErrorResponse errorResponse = new ErrorResponse(ex, httpServletRequest);
        if (ex instanceof ErrorWrapper) {
            ErrorWrapper errorWrapper = (ErrorWrapper) ex;
            errorResponse.setStatus(errorWrapper.getStatus().value());
            errorResponse.setMessage(errorWrapper.getMessage());
        }
        return ResponseEntity.status(HttpStatus.valueOf(errorResponse.getStatus())).body(errorResponse);
    }

}

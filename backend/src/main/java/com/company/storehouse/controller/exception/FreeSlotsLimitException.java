package com.company.storehouse.controller.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FreeSlotsLimitException extends RuntimeException implements ErrorWrapper {
    private static final long serialVersionUID = 3890344939539163268L;

    private String message;
    private final HttpStatus status = HttpStatus.FORBIDDEN;

    public FreeSlotsLimitException(String message) {
        super(message);
        this.message = message;
    }

}

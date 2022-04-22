package com.company.storehouse.controller.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ApproveWaybillException extends RuntimeException implements ErrorWrapper {
    private static final long serialVersionUID = -9065455176974352617L;

    private String message;
    private final HttpStatus status = HttpStatus.FORBIDDEN;

    public ApproveWaybillException(String message) {
        super(message);
        this.message = message;
    }

}

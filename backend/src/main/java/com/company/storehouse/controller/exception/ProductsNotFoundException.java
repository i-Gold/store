package com.company.storehouse.controller.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProductsNotFoundException extends RuntimeException implements ErrorWrapper {
    private static final long serialVersionUID = 1490037408063617214L;

    private String message;
    private final HttpStatus status = HttpStatus.FORBIDDEN;

    public ProductsNotFoundException(String message) {
        super(message);
        this.message = message;
    }

}

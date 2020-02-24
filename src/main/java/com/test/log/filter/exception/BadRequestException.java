package com.test.log.filter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = -6605904807048267469L;
	
    public BadRequestException(String message) {
        super(message);
    }


}

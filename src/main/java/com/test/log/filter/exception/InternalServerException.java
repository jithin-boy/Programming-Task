package com.test.log.filter.exception;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {
		
		
	    /**
	 * 
	 */
	private static final long serialVersionUID = -1254648911935155371L;

		public InternalServerException(String message) {
	        super(message);
	    }

	    public InternalServerException(String message, Throwable cause) {
	        super(message, cause);
	    }
	}
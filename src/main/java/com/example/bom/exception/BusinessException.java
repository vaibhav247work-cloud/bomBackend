package com.example.bom.exception;

public class BusinessException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5748572598900923179L;

	public BusinessException(String message) {
        super(message);
    }
}

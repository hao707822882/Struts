package com.dangdang.struts3.core.exception;

public class NotFoundActionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundActionException(String content) {
		super(content);
	}
}

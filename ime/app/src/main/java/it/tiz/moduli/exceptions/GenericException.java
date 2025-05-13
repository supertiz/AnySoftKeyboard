package it.tiz.moduli.exceptions;

import java.io.Serial;

public class GenericException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 1L;
	
	public GenericException(Throwable throwable) {
		super(throwable);
	}
	
	public GenericException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
	public GenericException(String msg) {
		super(msg);
	}
	
}

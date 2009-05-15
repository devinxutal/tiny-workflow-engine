package edu.thu.thss.twe.exception;

public class TweException extends RuntimeException {

	public TweException() {
		super();
	}

	public TweException(String message, Throwable cause) {
		super(message, cause);
	}

	public TweException(String message) {
		super(message);
	}

	public TweException(Throwable cause) {
		super(cause);
	}
}

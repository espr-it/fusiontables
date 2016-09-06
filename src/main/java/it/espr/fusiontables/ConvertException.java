package it.espr.fusiontables;

public class ConvertException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConvertException() {
		super();
	}

	public ConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ConvertException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConvertException(String message) {
		super(message);
	}

	public ConvertException(Throwable cause) {
		super(cause);
	}

}

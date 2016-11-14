package it.espr.fusiontables.data;

public class DataConvertException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataConvertException(String message) {
		super(message);
	}

	public DataConvertException(String message, Throwable cause) {
		super(message, cause);
	}

}

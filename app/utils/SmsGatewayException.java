package utils;

public final class SmsGatewayException extends Exception {
	private static final long serialVersionUID = 1L;

	public SmsGatewayException() {
	}

	public SmsGatewayException(String message) {
		super(message);
	}

	public SmsGatewayException(Throwable cause) {
		super(cause);
	}

	public SmsGatewayException(String message, Throwable cause) {
		super(message, cause);
	}
}

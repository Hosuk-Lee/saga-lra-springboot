package hs.saga.config.rest.util;

public class DecodingFailureException extends RuntimeException {
    private static final long serialVersionUID = -2471072907351494922L;

    public DecodingFailureException() {
    }

    public DecodingFailureException(String message) {
        super(message);
    }

    public DecodingFailureException(Throwable cause) {
        super(cause);
    }

    public DecodingFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecodingFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

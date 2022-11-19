package com.nashtech.rookies.java05.AssetManagement.exceptions;

public class RepeatDataException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public RepeatDataException() {
        super();
    }

    public RepeatDataException(String message) {
        super(message);
    }

    public RepeatDataException(String message, Throwable cause) {
        super(message, cause);
    }
}

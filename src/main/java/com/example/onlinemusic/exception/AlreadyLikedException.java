package com.example.onlinemusic.exception;

public class AlreadyLikedException extends RuntimeException {
    public AlreadyLikedException() {
        super();
    }

    public AlreadyLikedException(String message) {
        super(message);
    }

    public AlreadyLikedException(Throwable cause) {
        super(cause);
    }

    public AlreadyLikedException(String message, Throwable cause) {
        super(message, cause);
    }
}

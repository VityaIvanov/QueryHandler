package com.kpi.ivanov;

final class ParseException extends RuntimeException {

    ParseException(String message) {
        super(message);
    }

    ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

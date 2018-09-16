package com.kpi.ivanov;

final class ParseException extends Exception {

    ParseException(String message) {
        super(message);
    }

    ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.company.website.exception;

/**
 * @author Dmitry Matrizaev
 * @since 27.04.2020
 */
public class CustomFormatException extends RuntimeException {

    public CustomFormatException(String message) {
        super(message);
    }

    public CustomFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}

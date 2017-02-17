package com.epam.task04.lib.controller.exception;

/**
 * Created by Yauheni_Borbut on 2/17/2017.
 */
public class ControllerRuntimeException extends RuntimeException {

    public ControllerRuntimeException() {
        super();
    }

    public ControllerRuntimeException(String message) {
        super(message);
    }

    public ControllerRuntimeException(String message, Exception e) {
        super(message, e);
    }

    public ControllerRuntimeException(Exception e) {
        super(e);
    }
}

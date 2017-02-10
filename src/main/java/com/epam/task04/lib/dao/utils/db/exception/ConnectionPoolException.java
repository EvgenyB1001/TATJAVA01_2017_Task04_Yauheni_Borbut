package com.epam.task04.lib.dao.utils.db.exception;

/**
 * Created by Yauheni_Borbut on 2/8/2017.
 */
public class ConnectionPoolException extends Exception {

    public ConnectionPoolException() {
        super();
    }

    public ConnectionPoolException(String message) {
        super(message);
    }

    public ConnectionPoolException(String message, Exception e) {
        super(message, e);
    }

    public ConnectionPoolException(Exception e) {
        super(e);
    }
}

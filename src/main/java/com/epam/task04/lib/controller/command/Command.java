package com.epam.task04.lib.controller.command;

import com.epam.task04.lib.bean.Request;
import com.epam.task04.lib.exception.InitializationException;

/**
 * Interface for commands
 */
public interface Command {

    /**
     * Method execute current request
     */
    String executeCommand(Request request) throws InitializationException;
}

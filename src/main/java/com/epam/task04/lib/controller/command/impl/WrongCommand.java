package com.epam.task04.lib.controller.command.impl;

import com.epam.task04.lib.bean.Request;
import com.epam.task04.lib.controller.command.Command;
import com.epam.task04.lib.exception.InitializationException;

/**
 * Class provides actions, if command is wrong
 */
public class WrongCommand implements Command {

    private final String UNKNOWN_COMMAND_TEXT = "Application doesn't provide such command";
    /**
     * Method just return response, that application doesn't provide that command
     */
    @Override
    public String executeCommand(Request request) throws InitializationException {
        return UNKNOWN_COMMAND_TEXT;
    }
}

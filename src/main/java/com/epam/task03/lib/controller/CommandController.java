package com.epam.task03.lib.controller;

import com.epam.task03.lib.bean.Category;
import com.epam.task03.lib.bean.CommandName;
import com.epam.task03.lib.bean.Request;
import com.epam.task03.lib.controller.command.Command;
import com.epam.task03.lib.exception.InitializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class provides actions to perform command according to request from user
 */
public class CommandController {

    private static final Logger logger = LogManager.getLogger();
    private CommandProvider provider = new CommandProvider();
    private static CommandController instance;

    private CommandController() {}

    /**
     * Singleton implementation
     */
    public static CommandController getInstance() {
        if (instance == null) {
            instance = new CommandController();
        }
        return instance;
    }

    private final String FAIL_RESPONSE = "Some errors during executing request. Try again";

    /**
     * Method gets request as argument and calls definite command to execute current request
     *
     * @param line line of parameters of request to execute
     */
    public String executeRequest(String line) {
        if (line == null) {
            return FAIL_RESPONSE;
        }

        String response;
        try {
            Request request = createRequest(line);
            Command command1 = provider.getCommand(request.getCommandName().name());
            response = command1.executeCommand(request);
        } catch (InitializationException e) {
            response = FAIL_RESPONSE;
            logger.error(e.getMessage());
        }

        return response;
    }

    /**
     * Method gets array of parameters, parses them, creates a request by this parameters and returns that
     * request
     *
     * @param requestLine line of arguments to create request
     * @return object of request
     * @throws InitializationException if array of parameters isn't initialized correctly
     */
    private Request createRequest(String requestLine) throws InitializationException {
        if (requestLine == null) {
            throw new InitializationException("Request isn't initialized. Request can't be created");
        }

        String[] current = requestLine.split(" ");
        if (current.length <= 1) {
            throw new InitializationException("Invalid text of request. Request can't be created");
        }

        Request request;
        try {
            request = new Request();
            CommandName commandName;

            String name = requestLine.substring(0, requestLine.indexOf(" "));
            String parameters = requestLine.substring(requestLine.indexOf(" ") + 1);
            commandName = CommandName.valueOf(name.toUpperCase());
            request.setCommandToRequest(commandName);
            String[] params = parameters.split(",");
            setRequestParams(request, params);
        } catch (IllegalArgumentException e) {
            throw new InitializationException(e);
        }
        return request;
    }

    /**
     * Method get object of request and initializes it with parameters according to command of request
     *
     * @param request current request to initialize
     * @throws InitializationException if there are exceptions with initialization
     */
    private void setRequestParams(Request request, String[] params) throws InitializationException{
        CommandName commandName = request.getCommandName();
        if (commandName.equals(CommandName.ADD) && params.length == 3) {
            request.setTitleToRequest(params[0]);
            Category category = Category.valueOf(params[1].toUpperCase());
            request.setCategoryToRequest(category);
            request.setDateToRequest(params[2]);
        } else if (commandName.equals(CommandName.FIND_BY_CATEGORY) && params.length == 1) {
            Category category = Category.valueOf(params[0].toUpperCase());
            request.setCategoryToRequest(category);
        } else if (commandName.equals(CommandName.FIND_BY_TITLE) && params.length == 1) {
            request.setTitleToRequest(params[0]);
        } else if (commandName.equals(CommandName.FIND_BY_DATE) && params.length == 1) {
            request.setDateToRequest(params[0]);
        } else {
            throw new InitializationException("Invalid parameters. Request can't be created");
        }
    }
}

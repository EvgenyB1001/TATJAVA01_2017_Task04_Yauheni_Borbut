package com.epam.task04.lib.controller;

import com.epam.task04.lib.bean.Category;
import com.epam.task04.lib.bean.CommandName;
import com.epam.task04.lib.bean.Request;
import com.epam.task04.lib.controller.command.Command;
import com.epam.task04.lib.controller.exception.ControllerRuntimeException;
import com.epam.task04.lib.exception.InitializationException;
import com.epam.task04.lib.service.NewsService;
import com.epam.task04.lib.service.exception.ServiceException;
import com.epam.task04.lib.service.factory.NewsServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class provides actions to perform command according to request from user. Work of this class starts
 * with method <code>init</code>, that initializes resources and ends with method <code>destroy</code>
 */
public class CommandController {

    private static final Logger logger = LogManager.getLogger();
    private CommandProvider provider = new CommandProvider();
    private static CommandController instance;


    private static final String REQUEST_INIT_EXCEPTION = "Request isn't initialized. Request can't be created";
    private static final String INPUT_TEXT_INIT_EXCEPTION = "Invalid text of request. Request can't be created";
    private static final String PARAMS_INIT_EXCEPTION = "Invalid parameters. Request can't be created";

    private static final String COMMAND_DELIMITER = " ";
    private static final String PARAMS_DELIMITER = ",";

    private static final String FAIL_RESPONSE = "Some errors during executing request. Try again";

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

    /**
     * Method initializes resources, that required to app work
     */
    public void init() {
        NewsServiceFactory factory = NewsServiceFactory.getInstance();
        NewsService newsService = factory.getNewsServiceImpl();
        try {
            newsService.init();
        } catch (ServiceException e) {
            logger.error(e);
            throw new ControllerRuntimeException(e);
        }
    }
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
            logger.error(e);
        }

        return response;
    }

    /**
     * Method frees resources, that required to app work
     */
    public void destroy() {
        NewsServiceFactory factory = NewsServiceFactory.getInstance();
        NewsService newsService = factory.getNewsServiceImpl();
        newsService.destroy();
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
            throw new InitializationException(REQUEST_INIT_EXCEPTION);
        }

        String[] current = requestLine.split(COMMAND_DELIMITER);
        if (current.length == 1) {
            throw new InitializationException(INPUT_TEXT_INIT_EXCEPTION);
        }

        Request request;
        try {
            request = new Request();
            CommandName commandName;

            String name = requestLine.substring(0, requestLine.indexOf(COMMAND_DELIMITER));
            String parameters = requestLine.substring(requestLine.indexOf(COMMAND_DELIMITER) + 1);
            commandName = CommandName.valueOf(name.toUpperCase());
            request.setCommandToRequest(commandName);
            String[] params = parameters.split(PARAMS_DELIMITER);
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
            throw new InitializationException(PARAMS_INIT_EXCEPTION);
        }
    }
}

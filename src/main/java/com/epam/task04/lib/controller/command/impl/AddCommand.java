package com.epam.task04.lib.controller.command.impl;

import com.epam.task04.lib.bean.Request;
import com.epam.task04.lib.controller.command.Command;
import com.epam.task04.lib.exception.InitializationException;
import com.epam.task04.lib.service.NewsService;
import com.epam.task04.lib.service.exception.ServiceException;
import com.epam.task04.lib.service.factory.NewsServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Class provides actions to perform add command
 */
public class AddCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private final String SUCCESS_RESPONSE = "News successfully added";
    private final String FAIL_RESPONSE = "Some errors during adding news";

    private final String REQUEST_INIT_EXCEPTION = "Request isn't initialized";

    /**
     * Method gets request as argument, resend it and return response of command execution
     *
     * @param request params of request to execute
     * @return response of execution
     * @throws InitializationException if <code>request</code> isn't initialized
     */
    @Override
    public String executeCommand(Request request) throws InitializationException {
        if (request == null) {
            throw new InitializationException(REQUEST_INIT_EXCEPTION);
        }

        NewsServiceFactory factory = NewsServiceFactory.getInstance();
        NewsService newsService = factory.getNewsServiceImpl();
        String response;
        try {
            newsService.init();
            newsService.addNews(request);
            response = SUCCESS_RESPONSE;
        } catch (IllegalArgumentException | ServiceException e) {
            response = FAIL_RESPONSE;
            logger.error(e);
        } finally {
            newsService.destroy();
        }
        return response;
    }
}
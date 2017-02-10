package com.epam.task04.lib.controller.command.impl;

import com.epam.task04.lib.bean.News;
import com.epam.task04.lib.bean.Request;
import com.epam.task04.lib.controller.command.Command;
import com.epam.task04.lib.exception.InitializationException;
import com.epam.task04.lib.service.NewsService;
import com.epam.task04.lib.service.exception.ServiceException;
import com.epam.task04.lib.service.factory.NewsServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Class provides actions to perform find by date command
 */
public class FindByTitleCommand implements Command {

    private static final Logger logger = LogManager.getLogger();
    private final String FAIL_RESPONSE = "Some errors during searching news";
    private final String RESPONSE_HEADER = "Found news: ";
    private final String PARAMS_DELIMITER = " | ";
    private final String NEWS_DELIMITER = " ; ";

    private final String REQUEST_INIT_EXCEPTION = "Request isn't initialized";

    /**
     * Method gets request as argument, resend it and return response of command execution
     *
     * @param request request to execute
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
            ArrayList<News> result = newsService.findNewsByTitle(request);
            StringBuilder builder = new StringBuilder();
            response = RESPONSE_HEADER + result.size();
            for(News news : result) {
                builder.append(news.getTitle()).append(PARAMS_DELIMITER).
                        append(news.getCategory()).append(PARAMS_DELIMITER).
                        append(news.getDate()).append(NEWS_DELIMITER);
            }

            response += builder.toString();
        } catch (ServiceException e) {
            response = FAIL_RESPONSE;
            logger.error(e.getMessage());
        } finally {
            newsService.destroy();
        }

        return response;
    }
}

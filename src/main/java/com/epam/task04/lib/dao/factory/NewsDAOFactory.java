package com.epam.task04.lib.dao.factory;

import com.epam.task04.lib.dao.NewsDAO;
import com.epam.task04.lib.dao.impl.DBNewsDAOImpl;

/**
 * Class creates objects of DAO layer
 */
public class NewsDAOFactory {

    /**
     * Instance of class
     */
    private static NewsDAOFactory instance;

    private final DBNewsDAOImpl daoNews = new DBNewsDAOImpl();

    private NewsDAOFactory() {}

    /**
     * Singleton implementation
     */
    public static NewsDAOFactory getInstance() {
        if (instance == null) {
            instance = new NewsDAOFactory();
        }
        return instance;
    }

    /**
     * Method returns object of NewsDAOTxtImpl
     *
     * @return object of DAO class
     */
    public NewsDAO getNewsDAO() {
        return daoNews;
    }
}

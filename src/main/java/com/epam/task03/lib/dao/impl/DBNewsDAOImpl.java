package com.epam.task03.lib.dao.impl;

import com.epam.task03.lib.bean.Category;
import com.epam.task03.lib.bean.News;
import com.epam.task03.lib.bean.Request;
import com.epam.task03.lib.dao.NewsDAO;
import com.epam.task03.lib.dao.exception.DAOException;
import com.epam.task03.lib.dao.utils.db.ConnectionPool;
import com.epam.task03.lib.dao.utils.db.exception.ConnectionPoolException;
import com.epam.task03.lib.exception.InitializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class provides action with database to write and read news to it
 */
public class DBNewsDAOImpl implements NewsDAO {

    private static final Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    private final String DB_NAME = "news";
    private final String INSERT_QUERY = "INSERT INTO " + DB_NAME + " (title, category, date) VALUES (?, ?, ?)";
    private final String SELECT_TITLE_QUERY = "SELECT * FROM " + DB_NAME + " WHERE title = ?";
    private final String SELECT_CATEGORY_QUERY = "SELECT * FROM " + DB_NAME + " WHERE category = ?";
    private final String SELECT_DATE_QUERY = "SELECT * FROM " + DB_NAME + " WHERE date = ?";

    /**
     * Method writes current news, got as argument, to database
     *
     * @param news current news to write
     * @throws DAOException if there are exceptions in DAO
     */
    @Override
    public void addNews(News news) throws DAOException {
        if (news == null || news.getTitle() == null || news.getDate() == null || news.getCategory() == null) {
            throw new DAOException("News aren't initialized. They can't be added");
        }
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            pool.initConnectionPool();
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getCategory().name().toLowerCase());
            preparedStatement.setString(3, news.getDate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("SQL exception during executing request", e);
        } catch (ConnectionPoolException e2) {
            throw new DAOException("Exception in connection pool", e2);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * Method creates and executes query, and return list of news selected by category from request, got as argument
     *
     * @param request request to select news
     * @return list of news
     * @throws DAOException if there are exceptions in DAO
     */
    @Override
    public ArrayList<News> getNewsByCategory(Request request) throws DAOException {
        if (request == null || request.getCategory() == null) {
            throw new DAOException("Request isn't initialized. It can't be performed");
        }

        ArrayList<News> result = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            pool.initConnectionPool();
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_CATEGORY_QUERY);
            preparedStatement.setString(1, request.getCategory().name().toLowerCase());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString(2);
                String category = resultSet.getString(3);
                String date = resultSet.getString(4);
                News news = new News(title, Category.valueOf(category.toUpperCase()), date);
                result.add(news);
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("SQL exception during executing request", e);
        } catch (InitializationException e) {
            e.printStackTrace();
            throw new DAOException("Error while creating news");
        } catch (ConnectionPoolException e2) {
            e2.printStackTrace();
            throw new DAOException("Exception in connection pool", e2);
        } finally {

            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }

            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }

            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * Method creates and executes query, and return list of news selected by title from request, got as argument
     *
     * @param request request to select news
     * @return list of news
     * @throws DAOException if there are exceptions in DAO
     */
    @Override
    public ArrayList<News> getNewsByTitle(Request request) throws DAOException {
        if (request == null || request.getTitle() == null) {
            throw new DAOException("Request isn't initialized. It can't be performed");
        }

        ArrayList<News> result = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            pool.initConnectionPool();
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_TITLE_QUERY);
            preparedStatement.setString(1, request.getTitle());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString(2);
                String category = resultSet.getString(3);
                String date = resultSet.getString(4);
                News news = new News(title, Category.valueOf(category.toUpperCase()), date);
                result.add(news);
            }

            return result;
        } catch (SQLException e) {
            throw new DAOException("SQL exception during executing request", e);
        } catch (InitializationException e) {
            throw new DAOException("Error while creating news");
        } catch (ConnectionPoolException e2) {
            throw new DAOException("Exception in connection pool", e2);
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * Method creates and executes query, and return list of news selected by date from request, got as argument
     *
     * @param request request to select news
     * @return list of news
     * @throws DAOException if there are exceptions in DAO
     */
    @Override
    public ArrayList<News> getNewsByDate(Request request) throws DAOException {
        if (request == null || request.getDate() == null) {
            throw new DAOException("Request isn't initialized. It can't be performed");
        }

        ArrayList<News> result = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            pool.initConnectionPool();
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_DATE_QUERY);
            preparedStatement.setString(1, request.getDate());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString(2);
                String category = resultSet.getString(3);
                String date = resultSet.getString(4);
                News news = new News(title, Category.valueOf(category.toUpperCase()), date);
                result.add(news);
            }

            return result;
        } catch (SQLException e) {
            throw new DAOException("SQL exception during executing request", e);
        } catch (InitializationException e) {
            throw new DAOException("Error while creating news");
        } catch (ConnectionPoolException e2) {
            throw new DAOException("Exception in connection pool", e2);
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }
}

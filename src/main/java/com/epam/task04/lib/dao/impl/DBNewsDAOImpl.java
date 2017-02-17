package com.epam.task04.lib.dao.impl;

import com.epam.task04.lib.bean.Category;
import com.epam.task04.lib.bean.News;
import com.epam.task04.lib.bean.Request;
import com.epam.task04.lib.dao.NewsDAO;
import com.epam.task04.lib.dao.exception.DAOException;
import com.epam.task04.lib.dao.utils.db.ConnectionPool;
import com.epam.task04.lib.dao.utils.db.exception.ConnectionPoolException;
import com.epam.task04.lib.exception.InitializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class provides action with database to write and read news to it
 */
public class DBNewsDAOImpl implements NewsDAO {

    private static final Logger logger = LogManager.getLogger();

    private static final String INSERT_QUERY = "INSERT INTO news (title, category, date) VALUES (?, ?, ?)";
    private static final String SELECT_TITLE_QUERY = "SELECT * FROM news WHERE title = ?";
    private static final String SELECT_CATEGORY_QUERY = "SELECT * FROM news WHERE category = ?";
    private static final String SELECT_DATE_QUERY = "SELECT * FROM news WHERE date = ?";

    private static final String INIT_EXCEPTION_NEWS = "Error while creating news";
    private static final String SQL_EXCEPTION = "SQL exception during executing request";
    private static final String CONNECTON_POOL_EXCEPTION = "Exception in connection pool";

    /**
     * Method create pool of connections, that are required to application
     */
    @Override
    public void init() throws DAOException {
        try {
            ConnectionPool.getInstance().initConnectionPool();
        } catch (ConnectionPoolException e) {
            throw new DAOException(e);
        }

    }

    /**
     * Method writes current news, got as argument, to database
     *
     * @param news current news to write
     * @throws DAOException if there are exceptions in DAO
     */
    @Override
    public void addNews(News news) throws DAOException {

        ConnectionPool pool = ConnectionPool.getInstance();

        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = pool.takeConnection();
            preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getCategory().name().toLowerCase());
            preparedStatement.setString(3, news.getDate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(SQL_EXCEPTION, e);
        } catch (ConnectionPoolException e2) {
            throw new DAOException(CONNECTON_POOL_EXCEPTION, e2);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
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

        ConnectionPool pool = ConnectionPool.getInstance();
        ArrayList<News> result = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
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

        } catch (SQLException e) {
            throw new DAOException(SQL_EXCEPTION, e);
        } catch (InitializationException e) {
            throw new DAOException(INIT_EXCEPTION_NEWS, e);
        } catch (ConnectionPoolException e2) {
            throw new DAOException(CONNECTON_POOL_EXCEPTION, e2);
        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }

        return result;
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

        ConnectionPool pool = ConnectionPool.getInstance();
        ArrayList<News> result = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
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


        } catch (SQLException e) {
            throw new DAOException(SQL_EXCEPTION, e);
        } catch (InitializationException e) {
            throw new DAOException(INIT_EXCEPTION_NEWS, e);
        } catch (ConnectionPoolException e2) {
            throw new DAOException(CONNECTON_POOL_EXCEPTION, e2);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }


        return result;
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

        ConnectionPool pool = ConnectionPool.getInstance();
        ArrayList<News> result = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
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

        } catch (SQLException e) {
            throw new DAOException(SQL_EXCEPTION, e);
        } catch (InitializationException e) {
            throw new DAOException(INIT_EXCEPTION_NEWS, e);
        } catch (ConnectionPoolException e2) {
            throw new DAOException(CONNECTON_POOL_EXCEPTION, e2);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }

        return result;
    }

    /**
     * Method closes all connections in connection pool
     */
    @Override
    public void destroy() {
        ConnectionPool.getInstance().clearConnections();
    }
}

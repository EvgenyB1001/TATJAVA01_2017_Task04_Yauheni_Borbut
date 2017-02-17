package com.epam.task04.lib.dao.impl;

import com.epam.task04.lib.bean.Category;
import com.epam.task04.lib.bean.News;
import com.epam.task04.lib.bean.Request;
import com.epam.task04.lib.dao.exception.DAOException;
import com.epam.task04.lib.dao.utils.db.ConnectionPool;
import org.testng.Assert;
import org.testng.annotations.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Yauheni_Borbut on 2/1/2017.
 */
public class NewsDAOTxtImplTest {

    DBNewsDAOImpl newsDAO;

    @BeforeTest
    public void init() throws Exception {
        ConnectionPool.getInstance().initConnectionPool();
    }

    @BeforeMethod
    public void setUp(){
        newsDAO = new DBNewsDAOImpl();
    }

    @AfterMethod
    public void setDown() {
        newsDAO = null;
    }

    @AfterTest
    public void destroy() {
        ConnectionPool.getInstance().clearConnections();
    }

    @DataProvider(name = "Illegal objects of news")
    public Object[][] getIllegalNews() {
        return new Object[][] {
                {new News()},
        };
    }

    @DataProvider(name = "Illegal objects of request")
    public Object[][] getIllegalRequests() {
        return new Object[][] {
                {new Request()},
        };
    }

    @Test
    public void tstPositiveAddNews() throws Exception {
        News news = new News("News", Category.BOOK, "12:21:2334");
        newsDAO.addNews(news);
    }

    @Test
    public void tstPositiveGetNewsByDate() throws Exception {
        Request request = new Request();
        request.setDateToRequest("12:21:2034");
        newsDAO.getNewsByDate(request);
    }

    @Test
    public void tstPositiveGetNewsByCategory() throws Exception {
        Request request = new Request();
        request.setCategoryToRequest(Category.BOOK);
        newsDAO.getNewsByCategory(request);
    }

    @Test
    public void tstPositiveGetNewsByTitle() throws Exception {
        Request request = new Request();
        request.setTitleToRequest("Some title");
        newsDAO.getNewsByTitle(request);
    }

    @Test(dataProvider = "Illegal objects of news", expectedExceptions = DAOException.class)
    public void tstNegativeAddNews(News news) throws Exception {
        newsDAO.addNews(news);
    }

    @Test(dataProvider = "Illegal objects of request", expectedExceptions = DAOException.class)
    public void tstNegativeGetNewsByTitle(Request request) throws Exception {
        newsDAO.getNewsByTitle(request);
    }

    @Test(dataProvider = "Illegal objects of request", expectedExceptions = DAOException.class)
    public void tstNegativeGetNewsByCategory(Request request) throws Exception {
        newsDAO.getNewsByCategory(request);
    }

    @Test(dataProvider = "Illegal objects of request", expectedExceptions = DAOException.class)
    public void tstNegativeGetNewsByDate(Request request) throws Exception {
        newsDAO.getNewsByDate(request);
    }

    @Test
    public void tstPositiveSearchByCategory() throws Exception {
        Connection connection = ConnectionPool.getInstance().takeConnection();
        Statement statement = connection.createStatement();
        int state = statement.executeUpdate("INSERT INTO news (title, category, date) VALUES ('Some title', 'book', '12:12:2005')");
        if (state == 0) {
            Assert.fail();
        }

        Request request = new Request();
        request.setCategoryToRequest(Category.BOOK);
        ArrayList<News> result = newsDAO.getNewsByCategory(request);
        int countRightResults = 0;
        for(News currNews  : result) {
            if (currNews.getCategory().equals(request.getCategory())) {
                countRightResults++;
            }
        }
        connection.close();
        Assert.assertTrue(countRightResults == result.size());
    }

    @Test
    public void tstPositiveSearchByTitle() throws Exception {
        Connection connection = ConnectionPool.getInstance().takeConnection();
        Statement statement = connection.createStatement();
        int state = statement.executeUpdate("INSERT INTO news (title, category, date) VALUES ('Some title', 'book', '12:12:2005')");
        if (state == 0) {
            Assert.fail();
        }

        Request request = new Request();
        request.setTitleToRequest("Some title");
        ArrayList<News> result = newsDAO.getNewsByTitle(request);
        int countRightResults = 0;
        for(News currNews  : result) {
            if (currNews.getTitle().equals(request.getTitle())) {
                countRightResults++;
            }
        }

        connection.close();
        Assert.assertTrue(countRightResults == result.size());
    }

    @Test
    public void tstPositiveSearchByDate() throws Exception {
        Connection connection = ConnectionPool.getInstance().takeConnection();
        Statement statement = connection.createStatement();
        int state = statement.executeUpdate("INSERT INTO news (title, category, date) VALUES ('Some title', 'book', '12:12:2005')");
        if (state == 0) {
            Assert.fail();
        }

        Request request = new Request();
        request.setDateToRequest("12:12:2005");
        ArrayList<News> result = newsDAO.getNewsByDate(request);
        int countRightResults = 0;
        for(News currNews  : result) {
            if (currNews.getDate().equals(request.getDate())) {
                countRightResults++;
            }
        }

        connection.close();
        Assert.assertTrue(countRightResults == result.size());
    }
}
package com.epam.task04.lib.dao.impl;

import com.epam.task04.lib.bean.Category;
import com.epam.task04.lib.bean.News;
import com.epam.task04.lib.bean.Request;
import com.epam.task04.lib.dao.exception.DAOException;
import org.testng.annotations.*;

/**
 * Created by Yauheni_Borbut on 2/1/2017.
 */
public class NewsDAOTxtImplTest {

    DBNewsDAOImpl newsDAO;

    @BeforeMethod
    public void setUp(){
        newsDAO = new DBNewsDAOImpl();
    }

    @AfterMethod
    public void tearDown() {
        newsDAO = null;
    }

    @DataProvider(name = "Illegal objects of news")
    public Object[][] getIllegalNews() {
        return new Object[][] {
                {new News()},
                {null}
        };
    }

    @DataProvider(name = "Illegal objects of request")
    public Object[][] getIllegalRequests() {
        return new Object[][] {
                {new Request()},
                {null}
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
}
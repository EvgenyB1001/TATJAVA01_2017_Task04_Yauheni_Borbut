package com.epam.task03.lib.dao.utils.db.properties;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class provides resources from .properties file
 */
public class DBResourceManager {

    private static DBResourceManager instance;

    private ResourceBundle bundle = ResourceBundle.getBundle("database", Locale.ENGLISH);

    /**
     * Singleton implementation
     */
    public static DBResourceManager getInstance() {
        if (instance == null) {
            instance = new DBResourceManager();
        }

        return  instance;
    }

    /**
     * Method returns values according to key. got as argument
     *
     * @param key value of key to return string
     * @return String of value by key
     */
    public String getString(String key) {
        return bundle.getString(key);
    }
}

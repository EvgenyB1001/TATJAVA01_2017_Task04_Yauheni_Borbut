package com.epam.task04.lib.dao.utils.db.resource;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class provides resources from .resource file
 */
public class DBResourceManager {

    private static DBResourceManager instance;

    private final String PROPERTIES_FILE_NAME = "database";

    private ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME, Locale.ENGLISH);

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

package com.li.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author li
 */
public class Config {
    public static Properties getProperties() {
        Properties properties = new Properties();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("config.properties");
        try {
            InputStreamReader reader = new InputStreamReader(inputStream,"UTF-8");
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}

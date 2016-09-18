package ua.artcode.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by serhii on 9/18/16.
 */
public class PropertiesHolder {

    private static final Properties PROP = init();

    private static Properties init(){
        Properties properties = new Properties();

        try {
            properties.load(PropertiesHolder.class
                    .getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static String get(String key) {
        return PROP.getProperty(key);
    }
}

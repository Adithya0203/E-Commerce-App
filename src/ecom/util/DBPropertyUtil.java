package ecom.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getPropertyString() {
        Properties properties = new Properties();
        String connectionString = null;

        try (FileInputStream input = new FileInputStream("db.properties")) {
            // Load the properties file
            properties.load(input);

            // Fetch connection details from the property file
            String hostname = properties.getProperty("hostname");
            String dbname = properties.getProperty("dbname");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String port = properties.getProperty("port");

            // Build the connection string
            connectionString = "jdbc:mysql://" + hostname + ":" + port + "/" + dbname + 
                               "?user=" + username + "&password=" + password;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return connectionString;
    }
}

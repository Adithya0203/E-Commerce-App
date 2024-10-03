package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getPropertyString(String propertyFileName) {
        Properties properties = new Properties();
        StringBuilder connectionString = new StringBuilder();

        try (InputStream inputStream = new FileInputStream("src/main/resources/" + propertyFileName)) {
            properties.load(inputStream);
            String hostname = properties.getProperty("hostname");
            String dbname = properties.getProperty("dbname");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String port = properties.getProperty("port");

            // Constructing the connection string
            connectionString.append("jdbc:mysql://")
                    .append(hostname)
                    .append(":")
                    .append(port)
                    .append("/")
                    .append(dbname)
                    .append("?user=")
                    .append(username)
                    .append("&password=")
                    .append(password);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return connectionString.toString();
    }
}

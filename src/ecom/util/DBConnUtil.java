package ecom.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {
    
    // Database connection parameters
    private static final String hostname = "localhost";
    private static final String dbname = "e-com";
    private static final String username = "root";
    private static final String password = "Swetha@2003";
    private static final String port = "3306";

    // Static method to get a new connection
    public static Connection getConnection() {
        try {
            // Create the connection string
            String connectionString = "jdbc:mysql://" + hostname + ":" + port + "/" + dbname;
            // Establish and return a new connection
            return DriverManager.getConnection(connectionString, username, password);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions
            return null;
        }
    }
}
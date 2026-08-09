package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Manages the database connection for the HR System.
 * Provides a centralized method to obtain a MySQL database connection.
 * Update USER and PASSWORD to match your MySQL setup before running.
 */
public class DBConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/hr_system";
    private static final String USER = "root";   // Change to your MySQL username
    private static final String PASSWORD = "root"; // Change to your MySQL password

    /**
     * Returns a new connection to the hr_system database.
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

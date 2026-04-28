package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBSetup {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void setupDatabase() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = con.createStatement()) {

            st.executeUpdate("CREATE DATABASE IF NOT EXISTS hr_system");
            st.executeUpdate("USE hr_system");

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50),
                    password VARCHAR(50),
                    email VARCHAR(100)
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS employees (
                    emp_id VARCHAR(20) PRIMARY KEY,
                    full_name VARCHAR(100),
                    department VARCHAR(50),
                    email VARCHAR(100),
                    phone VARCHAR(20),
                    join_date DATE
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS leave_requests (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    emp_id VARCHAR(20),
                    leave_type VARCHAR(50),
                    start_date DATE,
                    end_date DATE,
                    status VARCHAR(20)
                )
            """);

            System.out.println("Database setup complete!");

        } catch (Exception e) {
            System.out.println("Setup error: " + e.getMessage());
        }
    }
}

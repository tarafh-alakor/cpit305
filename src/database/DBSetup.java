package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * DBSetup - Creates and initializes all database tables for the HR System.
 * This class runs once at application startup to ensure all required tables exist.
 */
public class DBSetup {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    /**
     * Creates the hr_system database and all required tables if they don't exist.
     * Tables created: users, employees, leave_requests, contracts.
     */
    public static void setupDatabase() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = con.createStatement()) {

            st.executeUpdate("CREATE DATABASE IF NOT EXISTS hr_system");
            st.executeUpdate("USE hr_system");

            // Users table: stores login credentials
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50),
                    password VARCHAR(50),
                    email VARCHAR(100)
                )
            """);

            // Employees table: stores employee information
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

            // Leave requests table: stores employee leave requests
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS leave_requests (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    emp_name VARCHAR(100),
                    leave_type VARCHAR(50),
                    start_date DATE,
                    end_date DATE,
                    total_days INT,
                    status VARCHAR(20) DEFAULT 'Pending'
                )
            """);

            // Contracts table: stores employee contracts
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS contracts (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    emp_name VARCHAR(100),
                    contract_type VARCHAR(50),
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

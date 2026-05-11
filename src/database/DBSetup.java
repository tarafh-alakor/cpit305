package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBSetup {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/";
    private static final String USER = "HRsystemDB";
    private static final String PASSWORD = "HR_system";

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
                    leave_id INT AUTO_INCREMENT PRIMARY KEY,
                    emp_name VARCHAR(100),
                    leave_type VARCHAR(50),
                    leave_date DATE,
                    total_days INT,
                    status VARCHAR(20)
                )
            """);
            
            var rs = st.executeQuery("SELECT COUNT(*) FROM leave_requests");
            rs.next();
            if (rs.getInt(1) == 0) {

            st.executeUpdate("""
                INSERT INTO leave_requests
                (emp_name, leave_type, leave_date, total_days, status)
                VALUES
                ('Rakan Faisal', 'Sick', '2026-05-01', 3, 'Pending'),            
                ('Latifa Khalid', 'Annual', '2026-05-05', 7, 'Pending'),            
                ('Maha Saud', 'Unpaid', '2026-05-10', 1, 'Pending'),            
                ('Sara Ahmed', 'Sick', '2026-05-15', 4, 'Pending'),            
                ('Faisal Mohammed', 'Annual', '2026-05-20', 10, 'Pending')
            """);
            }
            
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS contracts (
                    contract_id INT AUTO_INCREMENT PRIMARY KEY,
                    emp_id VARCHAR(20),
                    contract_type VARCHAR(50),
                    start_date DATE,
                    end_date DATE,
                    status VARCHAR(20),
                    FOREIGN KEY (emp_id) REFERENCES employees(emp_id)
                )
            """);

            System.out.println("Database setup complete!");

        } catch (Exception e) {
            System.out.println("Setup error: " + e.getMessage());
        }
    }
}

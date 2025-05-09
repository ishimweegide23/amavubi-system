package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/amavubi_fanhub_db?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Using the correct driver for 5.1.49
            Class.forName("com.mysql.jdbc.Driver"); 
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database Connected Successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("⚠ Error: MySQL JDBC Driver Not Found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("⚠ Error: Failed to Connect to Database!");
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("🔌 Database Connection Closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
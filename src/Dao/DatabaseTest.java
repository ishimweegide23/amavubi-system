package Dao;

import Dao.DatabaseConnection;
import java.sql.Connection;

public class DatabaseTest {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getConnection();
        
        // Close connection after testing
        DatabaseConnection.closeConnection(conn);
    }
}

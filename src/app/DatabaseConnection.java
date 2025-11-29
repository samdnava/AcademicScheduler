package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // 1. Database Credentials
    private static final String URL = "jdbc:postgresql://localhost:5432/scheduler";
    private static final String USER = "samn";
    private static final String PASSWORD = "1738";

    // 2. The Method to Connect
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("CRITICAL ERROR: Could not connect to database.");
            e.printStackTrace();
            return null;
        }
    }

    // 3. Test it
    static void main(String[] args) {
        Connection conn = connect();
        if (conn != null) {
            System.out.println("SUCCESS: Connected to PostgresSQL!");
        } else {
            System.out.println("FAILURE: Connection was null.");
        }
    }
}

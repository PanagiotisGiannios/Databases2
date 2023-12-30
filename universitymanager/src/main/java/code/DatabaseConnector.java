package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/university";

    public static Connection connect(String userId, String password) throws SQLException {
        return DriverManager.getConnection(URL, userId, password);
    }
}/** */

package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static Connection connect(String[] info) throws SQLException {      
        return DriverManager.getConnection(info[2], info[0], info[1]);
    }
}

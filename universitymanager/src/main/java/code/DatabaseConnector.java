package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static Connection connect(int user) throws SQLException {
        String[] info = selectUser(user);
        return DriverManager.getConnection(info[2], info[0], info[1]);
    }

    private static String[] selectUser(int num){
        String[] res = new String[3];
        if(num == 0){
            res[0] = "root";
            res[1] = "1234";
            res[2] = "jdbc:mysql://localhost:3306/university";
        }
        else if(num == 1){
            res[0] = "root";
            res[1] = "!Sql12345Sql!";
            res[2] = "jdbc:mysql://localhost:3306/university";
        }
        return res;
    }
}

package code;

import java.sql.*;

public class MainApp {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnector.connect()) {
            // Do database operations here
            System.out.println("Komple");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

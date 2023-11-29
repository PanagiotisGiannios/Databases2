package code;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // TODO: Remove in final build
        // Create Scanner to select user from console
        Scanner in = new Scanner(System.in);

        System.out.println("Select user:\n 0 : Pan \n 1 : Fragk\n");
        int user = in.nextInt();
        while (!(user == 1 || user == 0)) {
            user = in.nextInt();
        }
        // Close the scanner since it will no longer be used
        in.close();

        //Establish a connection with the database using the info of the selected user
        try (Connection connection = DatabaseConnector.connect(user)) {
            Statement stmnt = connection.createStatement();
            ResultSet res = stmnt.executeQuery("select * from employee");
            while (res.next()) {
                
                System.out.println(res.getString("firstname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }   
}

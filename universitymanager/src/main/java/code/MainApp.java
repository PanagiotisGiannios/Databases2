package code;

import java.sql.*;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        //Create Scanner to select user from console, Remove in final build?
        Scanner in = new Scanner(System.in);

        System.out.println("Select user:\n 0 : Pan \n 1 : Fragk\n");
        int user = in.nextInt();
        //Close the scanner since it will no longer be used
        in.close();
        //Establish a connection with the database using the info of the selected user
        try (Connection connection = DatabaseConnector.connect(selectUser(user))) {
            Statement stmnt = connection.createStatement();
            ResultSet res = stmnt.executeQuery("select * from employee");
            while (res.next()) {
                
                System.out.println(res.getString("firstname"));
            }
            System.out.println("Komple");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static String[] selectUser(int num){
        String[] res = new String[3];
        if(num == 0){
            res[0] = "root";
            res[1] = "1234";
            res[2] = "jdbc:mysql://localhost:3306/university";
        }
        else if(num == 1){
            //Change USERNAME,PASSWORD and URL if needed
            res[0] = "USERNAME";
            res[1] = "PASSWORD";
            res[2] = "jdbc:mysql://localhost:3306/university";
        }
        return res;
    }
}

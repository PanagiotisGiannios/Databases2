package code;

import java.sql.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPage extends Application {

    // TODO: In the final version delete those + PanButton + FragkButton
    private final static String USERNAME = "root";
    private final static String PANPASS = "1234";
    private final static String FRAGKPASS = "!Sql12345Sql!";
    private String path = System.getProperty("user.dir") + "/photos/";

    @Override
    public void start(Stage primaryStage) {
        // TODO: Set application icon
        // Image logo = new Image(getClass().getResourceAsStream(path + "logo.png"));
        // primaryStage.getIcons().add(logo);

        // Add a welcome text at the top
        Text welcomeText = new Text("Welcome to University Management");
        welcomeText.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");

        // Add the explanatory text under the welcome
        Text explanatoryText = new Text("\n\nWrite the username and password of your DataBase");
        explanatoryText.setStyle("-fx-font-size: 14;");

        // Set up the user ID text field
        TextField userIdTextField = new TextField();
        userIdTextField.setPromptText("User ID");

        // Set up the password text field
        PasswordField passwordTextField = new PasswordField();
        passwordTextField.setPromptText("Password");

        // Set up the login button
        Button loginButton = new Button("Login");

        // Set up 2 quick buttons
        Button PanButton = new Button("Panagiotis");
        Button FragkButton = new Button("Fragkiskos");

        // Create a label for displaying errors
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Create a vertical box to hold the login components
        VBox loginBox = new VBox(10);
        loginBox.setPadding(new Insets(20));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.getChildren().addAll(welcomeText, explanatoryText, userIdTextField, passwordTextField, loginButton, errorLabel, PanButton, FragkButton);

        // Set up the scene and stage
        Scene scene = new Scene(loginBox, 500, 500);
        primaryStage.setTitle("University Management");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set up the login button action
        loginButton.setOnAction(e -> {
            String userId = userIdTextField.getText();
            String password = passwordTextField.getText();

            // Establish a connection with the database using the crudentials
            try (Connection connection = DatabaseConnector.connect(userId, password)) {
                // Successfully logged in, close login page and do the rest of the program 
                // TODO: add the rest.
                primaryStage.close();
                doStaff(connection);
            } catch (Exception e1) {
                // Incorrect credentials, show an error message
                e1.printStackTrace();
                errorLabel.setText("Invalid username or password. Please try again.");
            }
        });

        PanButton.setOnAction(e -> {
            String userId = USERNAME;
            String password = PANPASS;

            // Establish a connection with the database using the crudentials
            try (Connection connection = DatabaseConnector.connect(userId, password)) {
                primaryStage.close();
                doStaff(connection);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        );

        FragkButton.setOnAction(e -> {
            String userId = USERNAME;
            String password = FRAGKPASS;

            // Establish a connection with the database using the crudentials
            try (Connection connection = DatabaseConnector.connect(userId, password)) {
                primaryStage.close();
                doStaff(connection);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        );
    }

    private void doStaff(Connection connection) throws SQLException {
        // Print query
        Statement stmnt = connection.createStatement();
        ResultSet res = stmnt.executeQuery("select * from employee");
        while (res.next()) {                
            System.out.println(res.getString("firstname"));
        }
    }
}

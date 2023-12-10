package code;

import java.sql.Connection;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPage extends Page {

    // TODO: In the final version delete those + PanButton + FragkButton
    private final static String USERNAME = "root";
    private final static String PANPASS = "1234";
    private final static String FRAGKPASS = "!Sql12345Sql!";

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadLogo();
        loadBackground("uniPage.png");
        loginPageSetup();
        createScene();
    }

    private void loginPageSetup() {
        Text welcomeText = new Text("Welcome to University Management\n\n");
        welcomeText.setStyle("-fx-font-family: 'Irish Grover'; -fx-font-size: 40;");
    
        // Set up the user ID text field
        TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("Username");
        usernameTextField.setMaxWidth(400);

        // Set up the password text field
        PasswordField passwordTextField = new PasswordField();
        passwordTextField.setPromptText("Password");
        passwordTextField.setMaxWidth(400);

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
        loginBox.getChildren().addAll(welcomeText, usernameTextField, passwordTextField, loginButton, errorLabel, PanButton, FragkButton);
        root.getChildren().add(loginBox);
        
        // Set up the login button action
        loginButton.setOnAction(e -> {
            String userId = usernameTextField.getText();
            String password = passwordTextField.getText();

            // Establish a connection with the database using the credentials
            try (Connection connection = DatabaseConnector.connect(userId, password)) {
                // Successfully logged in, close login page and do the rest of the program 
                showMainMenu(primaryStage);
                
            } catch (Exception e1) {
                // Incorrect credentials, show an error message
                // Otherwise show stackTrace
                passwordTextField.clear();
                System.out.println(e1.getLocalizedMessage().substring(0,13));
                if(e1.getLocalizedMessage().substring(0,13).equals("Access denied"))
                    errorLabel.setText("Invalid username or password. Please try again.");                
                else
                    e1.printStackTrace();                
            }
        });

        // Activate the loginButton when Enter is pressed
        loginBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginButton.fire(); 
            }
        });

        // TODO: Delete from here
        PanButton.setOnAction(e -> {
            String userId = USERNAME;
            String password = PANPASS;

            // Establish a connection with the database using the crudentials
            try (Connection connection = DatabaseConnector.connect(userId, password)) {
                showMainMenu(primaryStage);
                
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
                showMainMenu(primaryStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        );
    }

    private void showMainMenu(Stage primaryStage) {
        try {
            MainMenu mainMenu = new MainMenu();
            mainMenu.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

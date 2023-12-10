package code;

import java.io.File;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPage extends Application {

    // TODO: In the final version delete those + PanButton + FragkButton
    private final static String USERNAME = "root";
    private final static String PANPASS = "1234";
    private final static String FRAGKPASS = "!Sql12345Sql!";
    private String path = System.getProperty("user.dir") + "\\images\\";
    private String pagePath = path + "\\pages\\";

    @Override
    public void start(Stage primaryStage) {
        /*
         * Add Icon at the top left of the window
         * In order to add the icon we first need to transform it to
         * URI in order for the Image constructor to work, We first 
         * get the path to the logo as a File and then we transform
         * it to a URI and then a String so that it can be used.
         */
        File imageFile = new File(path + "university.png");
        Image logo = new Image(imageFile.toURI().toString());
        primaryStage.getIcons().add(logo);

        // Load the blankPage image as the background
        File backgroundFile = new File(pagePath + "uniPage.png");
        Image backgroundImage  = new Image(backgroundFile.toURI().toString());

        // Create a background image
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );

        // Set the background to a StackPane
        StackPane root = new StackPane();
        root.setMinSize(800, 600);  // Set a minimum size
        root.setBackground(new Background(background));

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
        
        // Set up the scene and stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("University Management");
        primaryStage.setScene(scene);
        primaryStage.show();
        
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
                usernameTextField.clear();
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
        //TODO: To here
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

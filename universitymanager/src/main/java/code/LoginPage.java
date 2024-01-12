package code;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPage extends Page {

    // TODO: In the final version delete those + PanButton + FragkButton
    private final static String USERNAME = "root";
    private final static String PANPASS = "1234";
    private final static String FRAGKPASS = "!Sql12345Sql!";
    private final static String BACKGROUND_COL_LIGHT = "151,198,154";
    //private final static String BACKGROUND_COL_DARK = "120,157,122";
    @Override
    public void start(Stage primaryStage) {
        Page.primaryStage = primaryStage;
        loadLogo();
        loadBackground("uniPage.png");
        loginPageSetup();
        createScene();
    }

    private void loginPageSetup() {        
        Text welcomeText = new Text("Welcome to University Management\n\n");        
        welcomeText.setStyle("-fx-font-family: 'Irish Grover'; -fx-font-size: 40;");

        // Set up the username text field
        TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("Username");
        usernameTextField.setMaxWidth(400);
        usernameTextField.setFocusTraversable(false);
        
        // Set up the password text field
        PasswordField passwordTextField = new PasswordField();
        passwordTextField.setPromptText("Password");
        passwordTextField.setMaxWidth(400);
        passwordTextField.setFocusTraversable(false);

        //Create the login button where the cursor also changes when hovered above it.
        Button loginButton = new Button("Login");
        loginButton.setCursor(Cursor.HAND);
        loginButton.setFont(new Font(15));
        
        //Create two buttons so that we don't have to type our passwords every time
        Button PanButton = new Button("Panagiotis");
        PanButton.setCursor(Cursor.HAND);
        Button FragkButton = new Button("Fragkiskos");
        FragkButton.setCursor(Cursor.HAND);

        // Create a label for displaying errors
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;" + "-fx-background-color: rgba(" + BACKGROUND_COL_LIGHT + "," + "0.8);");
        
        //Create two vertical boxes to hold the login components        
        VBox welcomeTextBox = new VBox();        
        welcomeTextBox.setPadding(new Insets(70, 20, 0, 20));
        welcomeTextBox.setAlignment(Pos.TOP_CENTER);
        welcomeTextBox.getChildren().addAll(welcomeText);

        VBox loginBox = new VBox(10);
        loginBox.setPadding(new Insets(20));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.getChildren().addAll(usernameTextField, passwordTextField, loginButton, errorLabel, PanButton, FragkButton);
        
        root.getChildren().addAll(welcomeTextBox,loginBox);

        // Set up the login button action
        loginButton.setOnAction(e -> {
            String userId = usernameTextField.getText();
            String password = passwordTextField.getText();
            System.out.println(userId + " " + password);
            // Establish a connection with the database using the credentials
            try {
                Page.connection = DatabaseConnector.connect(userId, password);
                showMainMenu(primaryStage);
                
            } catch (Exception e1) {
                // Incorrect credentials, show an error message
                // Otherwise show stackTrace
                passwordTextField.clear();
                System.out.println(e1.getLocalizedMessage().substring(0,13));
                if(e1.getLocalizedMessage().substring(0,13).equals("Access denied")){
                    errorLabel.setFont(new Font(20));
                    errorLabel.setText("Invalid username or password. Please try again.");
                }                
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
            try {
                Page.connection = DatabaseConnector.connect(userId, password);
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
            try{
                Page.connection = DatabaseConnector.connect(userId, password);
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
}

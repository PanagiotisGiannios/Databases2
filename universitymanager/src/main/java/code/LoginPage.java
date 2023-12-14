package code;

import java.sql.Connection;

import javax.print.DocFlavor.STRING;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPage extends Page {

    // TODO: In the final version delete those + PanButton + FragkButton
    private final static String USERNAME = "root";
    private final static String PANPASS = "1234";
    private final static String FRAGKPASS = "!Sql12345Sql!";
    private final static String BACKGROUND_COL_LIGHT = "151,198,154";
    private final static String BACKGROUND_COL_DARK = "120,157,122";
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadLogo();
        loadBackground("uniPage.png");
        loginPageSetup();
        createScene();
    }

    private static void createBackground(javafx.scene.Node node, String color, double transparency){
        node.setStyle("-fx-background-color: rgba("+color+","+ transparency+");");
    }

    private void loginPageSetup() {
        //TODO:
        StackPane usernameContainer = new StackPane();
        
        Text welcomeText = new Text("Welcome to University Management\n\n");
        
        welcomeText.setStyle("-fx-font-family: 'Irish Grover'; -fx-font-size: 40;");

        // Set up the username text field
        TextField usernameTextField = new TextField();
        //usernameTextField.setPromptText("Username");
        //usernameTextField.setMaxWidth(400);
        //usernameTextField.setPrefWidth(150);
        usernameTextField.setMaxWidth(150);
        usernameTextField.setPrefHeight(45);
        Label usernamePromptLabel = new Label("Username");
        usernamePromptLabel.setStyle("-fx-text-fill: black;");
        usernamePromptLabel.setTranslateX(0);
        usernamePromptLabel.setTranslateY(0);
        usernameContainer.getChildren().addAll(usernamePromptLabel,usernameTextField);
        usernamePromptLabel.setTranslateX(usernameTextField.getWidth());
        usernamePromptLabel.setTranslateY(usernameTextField.getHeight());
        usernamePromptLabel.toFront();
        // Set up the password text field
        PasswordField passwordTextField = new PasswordField();
        passwordTextField.setPromptText("Password");
        passwordTextField.setMaxWidth(400);



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
        //createBackground(errorLabel, BACKGROUND_COL_LIGHT, 0.5);
        errorLabel.setStyle("-fx-text-fill: red;"+
                            "-fx-background-color: rgba(" + BACKGROUND_COL_LIGHT +"," + "0.8);");

        
        //Create two vertical boxes to hold the login components
        
        VBox loginBox = new VBox(10);
        VBox welcomeTextBox = new VBox();
        welcomeTextBox.setPadding(new Insets(70, 20, 0, 20));
        welcomeTextBox.setAlignment(Pos.TOP_CENTER);
        welcomeTextBox.getChildren().addAll(welcomeText);
        loginBox.setPadding(new Insets(20));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.getChildren().addAll(usernameContainer, passwordTextField, loginButton, errorLabel, PanButton, FragkButton);
        root.getChildren().addAll(welcomeTextBox,loginBox);
        
        usernameTextField.setOnMouseClicked(event ->{
            if(event.getClickCount() > 0){
                usernamePromptLabel.setTranslateX(-usernameTextField.getWidth()/2 + 30);
                usernamePromptLabel.setTranslateY(-usernameTextField.getHeight()/2 + 8);
                usernamePromptLabel.toFront();
            }
        });
        usernameTextField.focusedProperty().addListener((obs,oldValue,newValue)->{
            if(!newValue){
                usernamePromptLabel.setTranslateX(0);
                usernamePromptLabel.setTranslateY(0);
                usernamePromptLabel.toFront();
            }
        });

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

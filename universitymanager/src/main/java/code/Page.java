package code;

import java.io.File;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.BigIntegerStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.sql.Connection;

public class Page extends Application {
    private static String path = System.getProperty("user.dir") + "\\universitymanager\\images\\";
    private static String pagePath = path + "pages\\";
    private static String logo = "university.png";
    private static String background = "uniPage.png";
    private static String title = "University Management";

    final int WIDTH = 1000;
    final int HEIGHT = 900;

    public static Connection connection;
    public static Stage primaryStage;
    public static Pane root;

    public static String getPagePath() {
        return pagePath;
    }

    public static String getPath() {
        return path;
    }
    
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        initializePage();
    }
    
    public void initializePage() {
        loadBackground();
        loadLogo();   
    }

    public void loadLogo() {
        File imageFile = new File(path + logo);
        Image logo = new Image(imageFile.toURI().toString());
        primaryStage.getIcons().add(logo);
    }

    public void loadLogo(String image) {
        logo = image;
        loadLogo();
    }
    
    public void loadBackground() {
        pagePath = pagePath.replace('\\', '/');
        // Load the background image
        Image backgroundImage = new Image("file:" + pagePath + background);
    
        // Create an ImageView for the background
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitHeight(960);
        backgroundView.setFitWidth(960);
        //backgroundView.setFitHeight(600);
        //backgroundView.setFitWidth(800);
        backgroundView.setPreserveRatio(true); // Allow stretching
    
        // Create a StackPane as the root with the ImageView as a child
        Pane root = new StackPane(backgroundView);
    
        // Set the background color using a hex value
        String backgroundColor = "#789D7A";
        root.setStyle("-fx-background-color: " + backgroundColor + ";" +
                      "-fx-background-size: cover;"); // 'cover' ensures the image covers the entire StackPane
    
        // Bind the size of the StackPane to the size of the Scene
        root.prefWidthProperty().bind(primaryStage.widthProperty());
        root.prefHeightProperty().bind(primaryStage.heightProperty());
        Page.root = root;
    }

    // Set up the scene and stage
    public void createScene(){
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.show();
    }
    
    public void loadBackground(String image) {
        background = image;
        loadBackground();
    }

    public static Button createBackButton() {
        // Load the image
        Image image = new Image("file:" + System.getProperty("user.dir") + "\\universitymanager\\images\\university.png");

        // Create an ImageView with the image
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        imageView.setPreserveRatio(true);


        Button backButton = new Button();
        backButton.setGraphic(imageView);
        backButton.setPadding(Insets.EMPTY);
        addButtonTransition(backButton, 30, 30);
        backButton.setStyle("-fx-background-color: rgba(255,255,0,0);"+"-fx-background-radius: 1;");

        // Set up the action for when the button is released
        backButton.setOnMouseReleased(e -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.start(Page.primaryStage);
        });
        

        return backButton;
    }

    // Make the a button have a Transition.
    public static void addButtonTransition(Button button) {
        addButtonTransition(button, 100, 50);
    }


    public static void addButtonTransition(Button button, int w, int h) {
        ScaleTransition hoverTransition = new ScaleTransition(Duration.millis(100),button);
        ScaleTransition clickTransition = new ScaleTransition(Duration.millis(50),button);

        button.setMinWidth(w);
        button.setMinHeight(h);
        button.setPrefWidth(w);
        button.setPrefHeight(h);
        button.setFont(Font.font("System",FontWeight.BOLD, 18));
        button.setStyle("-fx-background-radius: 15;");
        button.setCursor(Cursor.HAND);

        hoverTransition.setFromX(1);
        hoverTransition.setFromY(1);
        hoverTransition.setToX(1.1);
        hoverTransition.setToY(1.1);
        button.setOnMouseEntered(e -> {
            hoverTransition.setRate(1);
            hoverTransition.play();
        });

        button.setOnMouseExited(e -> {
            hoverTransition.setRate(-1);
            hoverTransition.play();
        });


        clickTransition.setFromX(1);
        clickTransition.setFromY(1);
        clickTransition.setToX(0.9);
        clickTransition.setToY(0.9);

        button.setOnMousePressed(e ->{
            clickTransition.setRate(1);
            clickTransition.play();
            
        });
        button.setOnMouseReleased(e->{
            hoverTransition.setRate(1);
            hoverTransition.play();
        });

        button.setFocusTraversable(false);
    }

    public static void addStackPaneTransition(StackPane stackPane){
        ScaleTransition hoverTransition = new ScaleTransition(Duration.millis(100),stackPane);
        ScaleTransition clickTransition = new ScaleTransition(Duration.millis(50),stackPane);

        hoverTransition.setFromX(1);
        hoverTransition.setFromY(1);
        hoverTransition.setToX(1.3);
        hoverTransition.setToY(1.3);

        stackPane.setOnMouseEntered(e ->{
            hoverTransition.setRate(1);
            hoverTransition.play();
        });
        stackPane.setOnMouseExited(e ->{
            hoverTransition.setRate(-1);
            hoverTransition.play();
        });

        clickTransition.setFromX(1);
        clickTransition.setFromY(1);
        clickTransition.setToX(0.9);
        clickTransition.setToY(0.9);

        stackPane.setOnMousePressed(e ->{
            clickTransition.setRate(1);
            clickTransition.play();
        });
    }

    
    public static TextField createNumericTextField(int maxLength) {
        TextField textField = new TextField();

        // Create a converter for integer values
        BigIntegerStringConverter converter = new BigIntegerStringConverter();

        // Create a TextFormatter to limit the length of the text
        textField.setTextFormatter(new TextFormatter<>(converter,
                null,
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("[0-9]*") && newText.length() <= maxLength) {
                        return change;
                    }
                    return null;
                }));

        return textField;
    }

    public static TextField makeTextField(int maxLength) {
        TextField textField = new TextField();

        // Create a TextFormatter to limit the length of the text
        TextFormatter<String> textFormatter = new TextFormatter<>(
                change -> change.getControlNewText().length() <= maxLength ? change : null
        );
        textField.setTextFormatter(textFormatter);

        return textField;
    }

    // It shows an Alert.
    public void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static TextField createGradeTextField() {
        // Create a TextField
        TextField textField = new TextField();

        // Set up a TextFormatter to restrict input to double values in the range -1 to 10
        TextFormatter<Double> textFormatter = new TextFormatter<>(
                new DoubleStringConverter(),
                null,
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("[0-9]*\\.?[0-9]*?") || newText.isEmpty()) {
                        try {
                            if(newText.isEmpty()){
                                return change;
                            }
                            double value = Double.parseDouble(newText);
                            if (value >= 0 && value <= 10) {
                                System.out.println("accepted!");
                                return change; // Accept the change
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("rejected");
                            // Ignore and reject the change if parsing fails
                        }
                    }
                    System.out.println("rejected");
                    return null; // Reject the change
                });

        // Apply the TextFormatter to the TextField
        textField.setTextFormatter(textFormatter);

        return textField;
    }


    public static void main(String[] args) {
        launch(args);
    }
}

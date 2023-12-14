package code;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainMenu extends Application {

    private String path = System.getProperty("user.dir") + "\\universitymanager\\images\\";
    private String pagePath = path + "\\pages\\";
    // Create a GridPane to arrange the buttons
    private GridPane gridPane = new GridPane();


    @Override
    public void start(Stage primaryStage) {
        File imageFile = new File(path + "university.png");
        Image logo = new Image(imageFile.toURI().toString());
        primaryStage.getIcons().add(logo);

        // Load the blankPage image as the background
        File backgroundFile = new File(pagePath + "emptyPage.png");
        Image backgroundImage  = new Image(backgroundFile.toURI().toString());

        // Create a background image
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );

        
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(140);
        gridPane.setVgap(80);

        // Create and add buttons to the GridPane
        addButton("professor.png", 0, 0, primaryStage);
        //addButton("university.png", 1, 0, primaryStage);
        addButton("auxiliary_staff.png", 2, 0, primaryStage);
        addButton("student.png", 0, 1, primaryStage);
        addButton("course.png", 1, 1, primaryStage);
        addButton("book.png", 2, 1, primaryStage);
        addButton("advance_search.png", 1, 2, primaryStage);

        // Set up the scene and stage
        StackPane root = new StackPane(gridPane);
        root.setBackground(new Background(background));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addButton(String imageName, int col, int row, Stage primaryStage) {
        // Load the button image
        Image buttonImage = new Image("file:" + path + imageName);
        ImageView buttonView = new ImageView(buttonImage);
        buttonView.setFitWidth(120);
        buttonView.setPreserveRatio(true);

        // Create a StackPane for the button
        StackPane buttonPane = new StackPane(buttonView);
        buttonPane.setOnMouseClicked(event -> {
            try {
                handleCase(col, row, primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Add the button to the GridPane
        gridPane.add(buttonPane, col, row);
    }

    private void handleCase(int col, int row, Stage primaryStage) throws Exception {
        int caseNum = col  + row * 3;
        switch (caseNum + 1) {
            // Professor Pressed
            case 1:
                Professor professor = new Professor();
                professor.start(primaryStage);
                break;
            
            // Secretariat Pressed
            case 2:
                System.out.println("Secretariat Pressed");
                break;
            
            // Auxiliary Staff Pressed
            case 3:
                System.out.println("Auxiliary Staff Pressed");
                break;
            
            // Student Pressed
            case 4:
                System.out.println("Student Pressed");
                break;

            // Book Pressed
            case 5:
                System.out.println("Book Pressed");
                break;

            // Course Pressed
            case 6:
                System.out.println("Course Pressed");
                break;
        

            // Advance Search Pressed
            default:
                System.out.println("Advanced Search Pressed");
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package code;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainMenu extends Page {
    // Create a GridPane to arrange the buttons
    private GridPane gridPane = null;


    @Override
    public void start(Stage primaryStage) {
        Page.primaryStage = primaryStage;
        loadLogo();
        loadBackground("emptyPage.png");
        createMainMenuPage();
        createScene();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Make sure you have updated to the latest database with everything using everything.sql !!");
        alert.showAndWait();
    }

    private void createMainMenuPage() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(70);
        gridPane.setVgap(70);

        // Create and add buttons to the GridPane
        addButton("professor.png", 0, 0, primaryStage);
        addButton("auxiliary_staff.png", 1, 0, primaryStage);
        addButton("student.png", 0, 1, primaryStage);
        addButton("course.png", 1, 1, primaryStage);

        root.getChildren().addAll(gridPane);
    }

    private void addButton(String imageName, int col, int row, Stage primaryStage) {
        // Load the button image
        Image buttonImage = new Image("file:" + getPath() + imageName);
        ImageView buttonView = new ImageView(buttonImage);
        buttonView.setFitWidth(200);
        buttonView.setPreserveRatio(true);
        //Creates a label with the text generated from the imageName by making the first letter upercase, removing the .png part,
        //replacing all underscores with spaces and making it plural
        Label buttonTextLabel = new Label(imageName.substring(0, 1).toUpperCase() + imageName.substring(1, imageName.length() - 4).replace("_", " ").concat("s").replace("fs", "f"));
        buttonTextLabel.setStyle("-fx-background-color: rgba(151,198,154,0.65);");
        buttonTextLabel.setAlignment(Pos.BOTTOM_CENTER);
        buttonTextLabel.setPrefWidth(200);
        buttonTextLabel.setPrefHeight(40);
        buttonTextLabel.setFont(Font.font("System", FontWeight.BOLD, 27));
        buttonTextLabel.setTextFill(Color.BLACK);
        StackPane orderStackPane = new StackPane(buttonTextLabel);
        orderStackPane.setAlignment(Pos.BOTTOM_CENTER);
        
        // Create a StackPane for the button
        StackPane buttonPane = new StackPane(buttonView,orderStackPane);
        buttonPane.setCursor(Cursor.HAND);
        buttonPane.setStyle("-fx-background-color: rgb(120,157,122); " +
                            "-fx-background-radius: 10;");      
        Page.addStackPaneTransition(buttonPane);                
        buttonPane.setOnMouseReleased(event -> {
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
        int caseNum = col  + row * 2;
        switch (caseNum + 1) {
            // Professor Pressed
            case 1:
                ProfessorMenu professorMainMenu = new ProfessorMenu();
                professorMainMenu.start(primaryStage);
                break;
            
        
            // Auxiliary Staff Pressed
            case 2:
                AuxiliaryMenu auxiliaryMenu = new AuxiliaryMenu();
                auxiliaryMenu.start(primaryStage);
                System.out.println("Auxiliary Staff Pressed");
                //AddPage auxiliaryAddPage = new AddPage("auxiliary");
                //auxiliaryAddPage.start(primaryStage);
                break;
            
            // Student Pressed
            case 3:
                System.out.println("Student Pressed");
                StudentMenu studentMenu = new StudentMenu();
                studentMenu.start(primaryStage);
                break;

            // Course Pressed
            case 4:
                System.out.println("Course Pressed");
                CourseMenu courseMenu = new CourseMenu();
                courseMenu.start(primaryStage);
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

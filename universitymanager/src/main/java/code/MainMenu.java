package code;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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
    }

    private void createMainMenuPage() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(250);
        gridPane.setVgap(100);

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
        buttonView.setFitWidth(180);
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
        int caseNum = col  + row * 2;
        switch (caseNum + 1) {
            // Professor Pressed
            case 1:
                ProfessorMenu professorMainMenu = new ProfessorMenu();
                professorMainMenu.start(primaryStage);
                break;
            
            // Auxiliary Staff Pressed
            case 2:
                System.out.println("Auxiliary Staff Pressed");
                AddPage auxiliaryAddPage = new AddPage("auxiliary");
                auxiliaryAddPage.start(primaryStage);
                break;
            
            // Student Pressed
            case 3:
                System.out.println("Student Pressed");
                AddPage studentAddPage = new AddPage("student");
                studentAddPage.start(primaryStage);

                break;

            // Course Pressed
            case 4:
                System.out.println("Course Pressed");
                AddPage courseAddPage = new AddPage("course");
                courseAddPage.start(primaryStage);
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

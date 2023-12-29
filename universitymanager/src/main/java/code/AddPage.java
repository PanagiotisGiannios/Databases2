package code;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPage extends Page {
    private String[] components;
    private String[] special;
    private String background = "uniPage.png";

    public AddPage(String type) {
        switch (type) {
            case "professor":
                profInitialize();                
                break;
            case "auxilery":
                break;
            case "":
                break;
        
            default:
                break;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Page.primaryStage = primaryStage;
        loadLogo();
        loadBackground("professorPage.png");
        createAddPage();
        createScene();
    }

    private void createAddPage() {
        // Set up the title at the top
        Label titleLabel = new Label("Professor");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));

        // Set up the text fields for features in the first two columns
        TextField feature1TextField = new TextField();
        TextField feature2TextField = new TextField();

        // Set up the project section in the third column
        Label projectLabel = new Label("Project Section");
        // Add your project-related UI components here

        // Set up the columns in the center
        GridPane centerGrid = new GridPane();
        centerGrid.setHgap(20);
        centerGrid.setVgap(10);

        // Add the text fields to the first two columns
        centerGrid.add(new Label("Feature 1:"), 0, 0);
        centerGrid.add(feature1TextField, 1, 0);

        centerGrid.add(new Label("Feature 2:"), 0, 1);
        centerGrid.add(feature2TextField, 1, 1);

        // Add the project section to the third column
        centerGrid.add(projectLabel, 2, 0, 1, 2);

        // Set up the button at the bottom
        Button addButton = new Button("Add Entry");
        addButton.setOnAction(e -> {
            // Add logic to handle adding the new entry
            System.out.println("Add Entry Button Clicked");
        });
        HBox buttonBox = new HBox(addButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));

        // Set up the main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(titleBox);
        mainLayout.setCenter(centerGrid);
        mainLayout.setBottom(buttonBox);
        
        root.getChildren().addAll(mainLayout);
    }

    private void profInitialize() {
        String[] compArray = {"Professor", "First Name", "Last Name", "Social Security Number", "Sex", "Email", "Phone Number", "Address", "Salary", "Birthday", "Date to Start Working"};
        String[] specialProf = {"Projects","Name", "Field", "Type", "Information"};
        this.components = compArray;
        this.special = specialProf;
        background = "professorPage.png";
    }

    public static void main(String[] args) {
        AddPage prof = new AddPage("professor");
        prof.launch(args);
    }
}

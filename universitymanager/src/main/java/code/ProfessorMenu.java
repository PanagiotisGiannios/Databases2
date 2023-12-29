package code;

import java.sql.*;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.*;

public class ProfessorMenu extends Page {

    private static final String[] FILTER_BUTTON_TEXTS = {"Is Rector", "Salary", "Sex", "Age", "SSN", "E-mail", "Project"};
    
    private List<String> projectNames = new ArrayList<String>();
    private List<String> projectTypes = new ArrayList<String>();

    private MenuButton filterButton;

    private CustomMenuItem rectorButton;

    private CustomMenuItem salaryButton;
    private CustomMenuItem salaryRangeTextFieldsMenuItem;
    private TextField startTextField;
    private TextField endTextField;

    private CustomMenuItem sexFilterButton;
    private CustomMenuItem radioButtons;
    private RadioButton maleButton = new RadioButton("Male");
    private RadioButton femaleButton = new RadioButton("Female");
    private ToggleGroup toggleGroup = new ToggleGroup();

    private CustomMenuItem ageButton;
    private CustomMenuItem ageRangeTextFieldsMenuItem;
    private TextField ageStartTextField;
    private TextField ageEndTextField;

    private CustomMenuItem ssnButton;
    private CustomMenuItem ssnTextFieldMenuItem;
    private TextField ssnTextField;

    private CustomMenuItem emailButton;
    private CustomMenuItem emailTextFieldMenuItem;
    private TextField emailTextField;

    private CustomMenuItem projectButton;
    private CustomMenuItem projectScrollPaneMenuItem;


    private TextField firstNameField;
    private TextField lastNameField;

    @Override
    public void start(Stage primaryStage) {
        //Page.scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        if(Page.connection == null){
            try {
                Page.connection = DatabaseConnector.connect("root", "!Sql12345Sql!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String getProjectNames = "SELECT Name FROM Project ORDER BY cast(substring(Name, 8) AS SIGNED)";
        String getProjectTypes = "SELECT DISTINCT type FROM Project";
        try (ResultSet result = Page.connection.createStatement().executeQuery(getProjectNames)) {
            while (result.next()) {
                projectNames.add(result.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (ResultSet result = Page.connection.createStatement().executeQuery(getProjectTypes)){
            while (result.next()) {
                projectTypes.add(result.getString("type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Page.primaryStage = primaryStage;
        loadLogo();
        loadBackground("professorPage.png");
        professorMenuSetup();
        createScene();
    }

    /*
     * Handles what each button does based on the text of the button.
     */
    private void handleButtonPress(Button button){
        String text = button.getText();
        switch (text) {
            case "Add":
                System.out.println("Added!");
                AddPage prof = new AddPage("professor");
                prof.start(Page.primaryStage);
                break;
            case "Delete":
                System.out.println("Deleted!");
                break;
            case "Edit":
                System.out.println("Edited!");
                break;
            case "Teaches":
                System.out.println("Teaches!");
                break;
            case "Rector":
                System.out.println("Rector!");
                break;
            default:
                System.out.println("Undefined!");
                break;
        }

    }

    /*
     * Sets the prefered width and height of a button, also makes the font bold and size 18
     * and creates two animations, one for hovering that makes the button bigger when hovered
     * and one for clicking that shrinks the button when clicked.
     */
    private void setButtonProperties(Button button){
        ScaleTransition hoverTransition = new ScaleTransition(Duration.millis(100),button);
        ScaleTransition clickTransition = new ScaleTransition(Duration.millis(50),button);

        button.setMinWidth(100);
        button.setMinHeight(50);
        button.setPrefWidth(100);
        button.setPrefHeight(50);
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
            handleButtonPress(button);
        });

        button.setFocusTraversable(false);

    }
    
    private void professorMenuSetup(){
        VBox base = new VBox();
        HBox titleBox = new HBox();
        HBox mainBox = new HBox();
    
        
        Text titleText = new Text("Professor");
        titleText.setFont(Font.font(19));
        titleBox.getChildren().add(titleText);
        titleBox.setAlignment(Pos.TOP_CENTER);
        titleText.setScaleX(2);
        titleText.setScaleY(1.85);


        /*
         * Create the stackpanes that will contain all 
         * of the necessary components for each side
         */
        System.out.println("test");
        StackPane rightSide = new StackPane();
        rightSide.setStyle("-fx-background-color: rgb(255,255,0);");
        StackPane leftSide  = new StackPane();
        leftSide.setStyle("-fx-background-color: rgb(255,0,255);");
        VBox leftBox  = new VBox(35);
        VBox rightBox = new VBox(35);

        String[] buttonTexts = {"Add","Delete","Edit","Teaches","Rector"};
        Button[] rightSideButtons = new Button[buttonTexts.length];
        for(int i = 0; i < rightSideButtons.length;i++){
            rightSideButtons[i] = new Button(buttonTexts[i]);
        }

        for (Button button : rightSideButtons) {
            setButtonProperties(button);
        }

        rightBox.getChildren().addAll(rightSideButtons);
        rightBox.setAlignment(Pos.TOP_RIGHT);
        rightBox.setPadding(new Insets(20, 25, 40, 0));
        rightSide.setAlignment(Pos.CENTER);
        mainBox.setAlignment(Pos.CENTER);
        rightSide.setMinWidth(200);

        HBox queryOptionsBox = new HBox(15);

        filterButton = new MenuButton("Filters");
        filterButton.setFocusTraversable(false);
        rectorButton = toCustomMenu(FILTER_BUTTON_TEXTS[0]);
        salaryButton = toCustomMenu(FILTER_BUTTON_TEXTS[1]);
        sexFilterButton = toCustomMenu(FILTER_BUTTON_TEXTS[2]);
        ageButton = toCustomMenu(FILTER_BUTTON_TEXTS[3]);
        ssnButton = toCustomMenu(FILTER_BUTTON_TEXTS[4]);
        emailButton = toCustomMenu(FILTER_BUTTON_TEXTS[5]);
        projectButton = toCustomMenu(FILTER_BUTTON_TEXTS[6]);

        startTextField = new TextField();
        endTextField = new TextField();
        startTextField.setPromptText("Min.");
        endTextField.setPromptText("Max.");

        maleButton.setToggleGroup(toggleGroup);
        femaleButton.setToggleGroup(toggleGroup);
        radioButtons = new CustomMenuItem(new VBox(10,maleButton,femaleButton));
        radioButtons.setHideOnClick(false);

        ageStartTextField = new TextField();
        ageEndTextField = new TextField();
        ageStartTextField.setPromptText("Min.");
        ageEndTextField.setPromptText("Max.");

        ssnTextField = new TextField();
        ssnTextField.setPromptText("SSN:");

        emailTextField = new TextField();
        emailTextField.setPromptText("E-mail:");

        filterButton.getItems().addAll(rectorButton,salaryButton,sexFilterButton,ageButton,ssnButton,emailButton,projectButton);
        
        queryOptionsBox.getChildren().addAll(filterButton);
        leftBox.getChildren().addAll(queryOptionsBox);

        leftSide.setMinWidth(200);
        leftSide.getChildren().addAll(leftBox);
        rightSide.getChildren().addAll(rightBox);
        mainBox.getChildren().addAll(leftSide,rightSide);

        /*Make each side of the middle box fill the entire available area*/
        HBox.setHgrow(leftSide, javafx.scene.layout.Priority.ALWAYS);
        //HBox.setHgrow(rightSide, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(mainBox, javafx.scene.layout.Priority.ALWAYS);
        base.getChildren().addAll(titleBox,mainBox);

        root.getChildren().addAll(base);
        
    }

    private void updateMenu(MenuButton menu,CheckBox checkBox){
        if(FILTER_BUTTON_TEXTS[0].equals(checkBox.getText())) {
            System.out.println("Rector");
        } 
        else if(FILTER_BUTTON_TEXTS[1].equals(checkBox.getText())) {
            menu.getItems().remove(salaryRangeTextFieldsMenuItem);
            if(checkBox.isSelected()) {
                startTextField.setPrefWidth(80);
                endTextField.setPrefWidth(80);
                salaryRangeTextFieldsMenuItem = new CustomMenuItem(new HBox(startTextField, endTextField));
                menu.getItems().add(menu.getItems().indexOf(salaryButton) + 1, salaryRangeTextFieldsMenuItem);
            }
        } 
        else if(FILTER_BUTTON_TEXTS[2].equals(checkBox.getText())){
            menu.getItems().remove(radioButtons);
            if(checkBox.isSelected()){
                radioButtons = new CustomMenuItem(new VBox(maleButton,femaleButton));
                radioButtons.setHideOnClick(false);
                menu.getItems().add(menu.getItems().indexOf(sexFilterButton)+1,radioButtons);
            }
        }
        else if(FILTER_BUTTON_TEXTS[3].equals(checkBox.getText())){
            menu.getItems().remove(ageRangeTextFieldsMenuItem);
            if(checkBox.isSelected()){
                ageStartTextField.setPrefWidth(80);
                ageEndTextField.setPrefWidth(80);
                ageRangeTextFieldsMenuItem = new CustomMenuItem(new HBox(ageStartTextField,ageEndTextField));
                menu.getItems().add(menu.getItems().indexOf(ageButton)+1,ageRangeTextFieldsMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[4].equals(checkBox.getText())){
            menu.getItems().remove(ssnTextFieldMenuItem);
            if(checkBox.isSelected()){
                ssnTextField.setPrefWidth(160);
                ssnTextFieldMenuItem = new CustomMenuItem(new HBox(ssnTextField));
                menu.getItems().add(menu.getItems().indexOf(ssnButton)+1,ssnTextFieldMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[5].equals(checkBox.getText())){
            menu.getItems().remove(emailTextFieldMenuItem);
            if(checkBox.isSelected()){
                emailTextField.setPrefWidth(160);
                emailTextFieldMenuItem = new CustomMenuItem(new HBox(emailTextField));
                menu.getItems().add(menu.getItems().indexOf(emailButton)+1,emailTextFieldMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[6].equals(checkBox.getText())){
            menu.getItems().remove(projectScrollPaneMenuItem);
            if(checkBox.isSelected()){
                VBox checkBoxContainer = new VBox();
                for(String projectName : projectNames){
                    CheckBox projectBox = new CheckBox(projectName);
                    projectBox.setFocusTraversable(false);
                    projectBox.setPrefHeight(25);
                    projectBox.setPrefWidth(140);
                    checkBoxContainer.getChildren().add(projectBox);
                }
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(checkBoxContainer);
                scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
                scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
                scrollPane.setFocusTraversable(false);
                scrollPane.setPrefHeight(100);
            
                projectScrollPaneMenuItem = new CustomMenuItem(scrollPane);
                projectScrollPaneMenuItem.setHideOnClick(false);
                menu.getItems().add(menu.getItems().indexOf(projectButton)+1,projectScrollPaneMenuItem);
            }
        }
        else {
            System.out.println("Other!");
        }
    }

    private CustomMenuItem toCustomMenu(String text){
        CheckBox checkBox = new CheckBox(text);
        checkBox.setFocusTraversable(false);
        checkBox.selectedProperty().addListener((observable,oldvalue,newvalue )-> {
            updateMenu(filterButton,checkBox);
        });
        CustomMenuItem customMenuItem = new CustomMenuItem(checkBox);
        customMenuItem.setHideOnClick(false);
        return customMenuItem;
        
    }

    public static void main(String[] args){
        launch(args);
    }
}

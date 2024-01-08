package code;

import java.sql.*;
import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import java.util.*;

public class CourseMenu extends Page {

    //TODO: 
    private String FPass = "!Sql12345Sql!";
    private String PPass = "1234";



    private static final String[] FILTER_BUTTON_TEXTS = {"Average Grade", "Teacher Amount", "Student Amount", "Semester", "CourseID"};
    private static final String[] SELECT_FILTER_BUTTON_TEXTS = {"Average Grade", "Teacher Amount", "Student Amount"};
    private List<String> courseSemesters = new ArrayList<String>();

    private MenuButton filterButton;

    private CustomMenuItem averageGradeButton;
    private CustomMenuItem averageGradeRangeTextFieldsMenuItem;
    private TextField averageGradeStartTextField;
    private TextField averageGradeEndTextField;

    private CustomMenuItem teacherAmountButton;
    private CustomMenuItem teacherAmountRangeTextFieldsMenuItem;
    private TextField teacherAmountStartTextField;
    private TextField teacherAmountEndTextField;

    private CustomMenuItem courseIDButton;
    private CustomMenuItem courseIDTextFieldMenuItem;
    private TextField courseIDTextField;
    
    private CustomMenuItem semesterButton;
    private CustomMenuItem semesterScrollCustomMenuItem;

    private CustomMenuItem studentAmountButton;
    private CustomMenuItem studentAmuntRangeTexFieldsMenuItem;
    private TextField studentAmountStartTextField;
    private TextField studentAmountEndTextField;

    private MenuButton selectFilterButton;

    private CustomMenuItem selectFiltersMenuItem;
    private VBox selectFiltersContainer;

    private TextField nameField;

    private Button searchButton;

    private String selectString = "SELECT CourseID, Name, Semester, ";
    private String joinString   = "FROM Course";
    private String whereString  = "";
    private String groupString  = "";
    private PreparedStatement previousQuery = null;

    private List<Object> whereParametersList = new ArrayList<Object>();
    private List<Object> groupParametersList = new ArrayList<Object>();

    private ResultSet  resultSet;
    private ScrollPane resultScrollPane;
    private TableView<ObservableList<String>>  resultTableView;

    //private String ssnSelected;

    @Override
    public void start(Stage primaryStage) {
        Page.primaryStage = primaryStage;
        loadLogo();
        loadBackground("professorPage.png");
        professorMenuSetup();
        createScene();

        if(Page.connection == null){
            
            try {
                Page.connection = DatabaseConnector.connect("root", "1234");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        retrieveSemesters();

        //Simulate a press on the search button to populate the viewTable at the start.
        handleButtonPress(new Button("Search"));
        System.out.println("\n\nDONE!\n\n");
    }

    private void retrieveSemesters(){
        String getSemesters = "SELECT DISTINCT semester FROM Course";
        
        try (ResultSet result = Page.connection.createStatement().executeQuery(getSemesters)){
            courseSemesters.clear();
            while (result.next()) {
                courseSemesters.add(result.getString("semester"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Handles what each button does based on the text of the button.
     */
    private void handleButtonPress(Button button){
        String text = button.getText();
        switch (text) {
            case "Add":
                System.out.println("Added!");
                AddPage prof = new AddPage("course");
                prof.start(Page.primaryStage);
                break;
            case "Delete":
                System.out.println("Deleted! " + TableManager.selectedId);
                if(TableManager.selectedRowIdList.isEmpty()){
                    Alert alert = new Alert(AlertType.ERROR); 
                    alert.setTitle("Error");
                    alert.setHeaderText("No course selected");
                    alert.setContentText("Select a course and try again!");
                    alert.showAndWait();
                    break;
                }
                Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Deletion!");
                if(TableManager.selectedRowIdList.size() == 1){
                    confirmAlert.setHeaderText("Are you sure you want to delete the course with Id: '" + TableManager.selectedRowIdList.get(0) + "' ?");
                }
                else{
                    String selections = "";
                    for (String str : TableManager.selectedRowIdList) {
                        selections += "'" + str +"', ";
                    }
                    selections = selections.substring(0,selections.length()-2);
                    int index = selections.lastIndexOf(", ");
                    if(index != -1){
                        selections = selections.substring(0, index) + " and " + selections.substring(index+2);
                    }
                    confirmAlert.setHeaderText("Are you sure you want to delete the courses with Ids: " + selections + " ?");
                }
                ButtonType yesButtonType = new ButtonType("Yes");
                ButtonType noButtonType = new ButtonType("No");
                confirmAlert.getButtonTypes().setAll(yesButtonType,noButtonType);
                confirmAlert.showAndWait().ifPresent(buttonType ->{
                    if(buttonType == yesButtonType){
                        try {
                            for (String id : TableManager.selectedRowIdList) {
                                Page.connection.createStatement().executeUpdate("DELETE FROM Attends WHERE CourseID = " + id);
                                Page.connection.createStatement().executeUpdate("DELETE FROM Teaches WHERE CourseID = " + id);
                                Page.connection.createStatement().executeUpdate("DELETE FROM Course  WHERE CourseID = " + id);
                            }
                            resultTableView.getItems().remove(resultTableView.getSelectionModel().getSelectedIndices());
                            resultTableView.getSelectionModel().clearSelection();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        //After deleting everything there is to do with the course, we execute the previous query
                        //(Refresh the table without the deleted entry) and then we retrieve the semesters again
                        //in case it was the last course in that semester, after that we update the menu with
                        //the new semesters.
                        refreshTable();
                        retrieveSemesters();
                        updateMenu(filterButton, (CheckBox)semesterButton.getContent());
                    }
                    else{
                        System.out.println("NO pressed!");
                    }
                });
                resultTableView.getSelectionModel().clearSelection();
                TableManager.selectedId = null;

                break;
            case "Edit":
                for (String str : TableManager.selectedRowIdList) {
                    System.out.println("Edit person with ssn: "+ str);
                }
                
                break;
            case "Teaches":
                System.out.println("Teaches!");
                break;
            
            case "Search":
            System.out.println("Searching!\n");
                selectString = "SELECT DISTINCT c.CourseID AS 'CourseID', c.Name AS 'Course Name', c.Semester AS 'Semester', ";
                joinString   = "FROM course c\n" +
                                "LEFT JOIN (\n" +
                                "    SELECT CourseID, AVG(CASE WHEN Grade != -1 THEN Grade END) AS AverageGrade\n" +
                                "    FROM Attends\n" +
                                "    GROUP BY CourseID\n" +
                                ") AS ca ON c.CourseID = ca.CourseID\n" +
                                "LEFT JOIN (\n" +
                                "    SELECT CourseID, COUNT(*) AS Amount\n" +
                                "    FROM teaches\n" +
                                "    GROUP BY CourseID\n" +
                                ") AS t ON c.CourseID = t.CourseID\n" +
                                "LEFT JOIN (\n" +
                                "    SELECT CourseID, COUNT(*) AS Amount\n" +
                                "    FROM Attends\n" +
                                "    GROUP BY CourseID\n" +
                                ") AS s ON c.CourseID = s.CourseID";
                whereString  = "WHERE";
                groupString = "";

                String invalidRangeFilters ="";
                Boolean isCorrectOrder = true;
                whereParametersList.clear();
                groupParametersList.clear();
                Boolean showMissingAlert = false;

                CheckBox[] checkBoxArray = selectFiltersContainer.getChildren().toArray(new CheckBox[0]);
                if(checkBoxArray[0].isSelected()){
                    selectString += "ca.AverageGrade AS 'Average Grade', ";
                }
                if(checkBoxArray[1].isSelected()){
                    selectString += "t.Amount AS 'Teacher Amount', ";
                }
                if(checkBoxArray[2].isSelected()){
                    selectString += "s.Amount AS 'Student Amount', ";
                }
                
                selectString = selectString.substring(0,selectString.length() -2);
                if(((CheckBox)courseIDButton.getContent()).isSelected()){
                    if(!courseIDTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND c.CourseID = ?");
                        whereParametersList.add(Integer.parseInt(courseIDTextField.getText()));
                    }
                    else{
                        showMissingAlert = true;
                    }
                }
                if(((CheckBox)semesterButton.getContent()).isSelected()){
                    whereString = whereString.concat( " AND (");
                    ScrollPane scroll = (ScrollPane)semesterScrollCustomMenuItem.getContent();
                    VBox containerBox = (VBox)scroll.getContent();
                    Boolean isFirst = true;
                    Boolean hasSelected = false;

                    for (Node node : containerBox.getChildren()) {
                        if (node instanceof CheckBox) {
                            if(((CheckBox)node).isSelected()){
                                hasSelected = true;
                                if(!isFirst){
                                    whereString = whereString.concat(" OR ");
                                }
                                else{
                                    isFirst = false;
                                    
                                }
                                whereString = whereString.concat("c.semester = ?");
                                whereParametersList.add(Integer.parseInt(((CheckBox)node).getText()));
                            }
                        }
                    }
                    if(!hasSelected){
                        whereString = whereString.concat(("1=1"));
                        showMissingAlert = true;
                    }
                    whereString = whereString.concat(")");
                }
                if(((CheckBox)averageGradeButton.getContent()).isSelected()){
                    int start= Integer.MIN_VALUE;
                    int end = Integer.MAX_VALUE;
                    if(!averageGradeStartTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND ca.AverageGrade >= ?");
                        whereParametersList.add(Integer.parseInt(averageGradeStartTextField.getText()));
                        start = Integer.parseInt(averageGradeStartTextField.getText());
                    }
                    if(!averageGradeEndTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND ca.AverageGrade <= ?");
                        whereParametersList.add(Integer.parseInt(averageGradeEndTextField.getText()));
                        end = Integer.parseInt(averageGradeEndTextField.getText());
                    }
                    if(averageGradeStartTextField.getText().isEmpty() && averageGradeEndTextField.getText().isEmpty()){
                        showMissingAlert = true;
                    }
                    if(start > end){
                        invalidRangeFilters += "'Average Grade', ";
                        isCorrectOrder = false;
                    }
                }
                if(((CheckBox)teacherAmountButton.getContent()).isSelected()){
                    int start = Integer.MIN_VALUE;
                    int end = Integer.MAX_VALUE;
                    if(!teacherAmountStartTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND t.Amount >= ?");
                        whereParametersList.add(Integer.parseInt(teacherAmountStartTextField.getText()));
                        start = Integer.parseInt(teacherAmountStartTextField.getText());
                    }
                    if(!teacherAmountEndTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND t.Amount <= ?");
                        whereParametersList.add(Integer.parseInt(teacherAmountEndTextField.getText()));
                        end = Integer.parseInt(teacherAmountEndTextField.getText());
                    }
                    if(teacherAmountStartTextField.getText().isEmpty() && teacherAmountEndTextField.getText().isEmpty()){
                        showMissingAlert = true;
                    }
                    if(start > end){
                        invalidRangeFilters += "'Teacher Amount', ";
                        isCorrectOrder = false;
                    }
                }
                if(((CheckBox)studentAmountButton.getContent()).isSelected()){
                    int start = Integer.MIN_VALUE;
                    int end = Integer.MAX_VALUE;
                    if(!studentAmountStartTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND s.Amount >= ?");
                        whereParametersList.add(Integer.parseInt(studentAmountStartTextField.getText()));
                        start = Integer.parseInt(studentAmountStartTextField.getText());
                    }
                    if(!studentAmountEndTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND s.Amount <= ?");
                        whereParametersList.add(Integer.parseInt(studentAmountEndTextField.getText()));
                        end = Integer.parseInt(studentAmountEndTextField.getText());
                    }
                    if(studentAmountStartTextField.getText().isEmpty() && studentAmountEndTextField.getText().isEmpty()){
                        showMissingAlert = true;
                    }
                    if(start > end){
                        invalidRangeFilters += "'Student Amount', ";
                        isCorrectOrder = false;
                    }
                }
                if(!nameField.getText().isEmpty()){
                    whereString = whereString.concat(" AND c.Name LIKE ?");
                    whereParametersList.add("%"+ nameField.getText() +"%");
                }
                if(whereString.indexOf(" AND") != -1){
                    whereString = whereString.substring(0,whereString.indexOf(" AND")) + whereString.substring(whereString.indexOf(" AND") + 4);
                }
                else{
                    whereString = "";
                }
                String query = selectString + "\n" + joinString + "\n" + whereString + "\n" + groupString;
                System.out.println("\n\n"+ query + "\n\n");
                if(showMissingAlert){
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Incomplete Filters");
                    alert.setHeaderText(null);
                    alert.setContentText("One or more filters have been selected but left empty.\nThese filters will be ignored during the search\nPlease review the selected filters to make sure all necessary fields are filled");
                    alert.showAndWait();
                }
                if(!isCorrectOrder){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Incorrect Order");
                    alert.setHeaderText("Incorrect order in range(s)");
                    //Remove the last: ", " 
                    invalidRangeFilters = invalidRangeFilters.substring(0,invalidRangeFilters.length()-2);
                    //Find the last: ", " and replace it with "and"
                    int index = invalidRangeFilters.lastIndexOf(", ");
                    if(index != -1){
                        invalidRangeFilters = invalidRangeFilters.substring(0,index) + " and " + invalidRangeFilters.substring(index+2);
                    }
                    alert.setContentText("Incorrect order in filter(s): "+ invalidRangeFilters);
                    alert.showAndWait();
                    break;
                }
                try{
                    PreparedStatement preparedStatement = Page.connection.prepareStatement(query);
                    int parameterIndex = 1;
                    for (Object parameter : whereParametersList) {
                        preparedStatement.setObject(parameterIndex, parameter);
                        parameterIndex++;
                    }
                    for (Object parameter : groupParametersList){
                        preparedStatement.setObject(parameterIndex, parameter);
                        parameterIndex++;
                    }
                    
                    previousQuery = preparedStatement;
                    resultSet = preparedStatement.executeQuery();
                    
                    resultTableView = TableManager.CreateTableView(resultSet, "course");
                    resultTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                    TableManager.setUpMouseReleased(resultTableView);
                    TableManager.setAllowMultipleRowSelection(true);
                    resultTableView.setFixedCellSize(Region.USE_COMPUTED_SIZE);
                    resultScrollPane.setContent(resultTableView);
                    resultScrollPane.setFitToWidth(true);
                    resultScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Undefined!");
                break;
        }

    }

    /**
     * Sets the prefered width and height of a button, also makes the font bold and size 18
     * and creates two animations, one for hovering that makes the button bigger when hovered
     * and one for clicking that shrinks the button when clicked.
     */
    private void setButtonProperties(Button button){
        ScaleTransition hoverTransition = new ScaleTransition(Duration.millis(100),button);
        ScaleTransition clickTransition = new ScaleTransition(Duration.millis(50),button);
        System.out.println("\nsetting up button: " + button.getText() + "\n");
        button.setMinWidth(100);
        button.setMinHeight(50);
        button.setPrefWidth(100);
        button.setPrefHeight(50);
        button.setFont(Font.font("System",FontWeight.BOLD, 18));
        button.setStyle("-fx-background-radius: 15;");
        button.setCursor(Cursor.HAND);
        if(button.getText().equals("Taught By")){
            button.setPrefWidth(115);
        }
        else if(button.getText().equals("Attended By")){
            button.setPrefWidth(130);
        }

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
        VBox base = new VBox(10);
        HBox titleBox = new HBox();
        HBox mainBox = new HBox();
        HBox backBox = new HBox();
        
        Text titleText = new Text("Course");
        titleText.setFont(Font.font("System",FontWeight.BOLD,29));
        titleBox.getChildren().add(titleText);
        titleBox.setAlignment(Pos.TOP_CENTER);
        


        /*
         * Create the stackpanes that will contain all 
         * of the necessary components for each side
         */
        StackPane rightSide = new StackPane();
        //rightSide.setStyle("-fx-background-color: rgb(255,255,0);");
        StackPane leftSide  = new StackPane();
        //leftSide.setStyle("-fx-background-color: rgb(255,0,255);");
        VBox leftBox  = new VBox(35);
        VBox rightBox = new VBox(35);

        String[] buttonTexts = {"Add","Delete","Edit","Taught By","Attended By"};
        Button[] rightSideButtons = new Button[buttonTexts.length];
        for(int i = 0; i < rightSideButtons.length;i++){
            rightSideButtons[i] = new Button(buttonTexts[i]);
        }

        for (Button button : rightSideButtons) {
            setButtonProperties(button);
        }

        rightBox.getChildren().addAll(rightSideButtons);
        rightBox.setAlignment(Pos.TOP_CENTER);
        rightBox.setPadding(new Insets(20, 25, 40, 0));
        rightSide.setAlignment(Pos.CENTER);
        mainBox.setAlignment(Pos.CENTER);
        rightSide.setMinWidth(200);

        HBox queryOptionsBox = new HBox(15);

        filterButton = new MenuButton("Filters");
        filterButton.setFocusTraversable(false);

        averageGradeButton = toCustomMenu(FILTER_BUTTON_TEXTS[0]);
        teacherAmountButton = toCustomMenu(FILTER_BUTTON_TEXTS[1]);
        studentAmountButton = toCustomMenu(FILTER_BUTTON_TEXTS[2]);
        semesterButton = toCustomMenu(FILTER_BUTTON_TEXTS[3]);
        courseIDButton = toCustomMenu(FILTER_BUTTON_TEXTS[4]);
        System.out.println("Done customMenus!\n");

        averageGradeStartTextField = Page.createNumericTextField(3);
        averageGradeEndTextField   = Page.createNumericTextField(3);
        averageGradeStartTextField.setPromptText("Min.");
        averageGradeEndTextField.setPromptText("Max.");

        teacherAmountStartTextField = Page.createNumericTextField(3);
        teacherAmountEndTextField = Page.createNumericTextField(3);
        teacherAmountStartTextField.setPromptText("Min.");
        teacherAmountEndTextField.setPromptText("Max.");
        
        studentAmountStartTextField = Page.createNumericTextField(3);
        studentAmountEndTextField = Page.createNumericTextField(3);
        studentAmountStartTextField.setPromptText("Min.");
        studentAmountEndTextField.setPromptText("Max.");

        courseIDTextField = Page.createNumericTextField(5);
        courseIDTextField.setPromptText("SSN:");

        filterButton.getItems().addAll(courseIDButton,semesterButton,averageGradeButton,teacherAmountButton,studentAmountButton);
        filterButton.setCursor(Cursor.HAND);
        filterButton.setPrefWidth(90);
        filterButton.setPrefHeight(40);
        filterButton.setFont(Font.font("System",FontWeight.BOLD, 14));
        
        selectFiltersContainer = new VBox(5);
        for (String str : SELECT_FILTER_BUTTON_TEXTS) {
            CheckBox checkbox = new CheckBox(str);
            selectFiltersContainer.getChildren().add(checkbox);
        }
        selectFiltersMenuItem = new CustomMenuItem(selectFiltersContainer);
        selectFiltersMenuItem.setHideOnClick(false);
        selectFilterButton = new MenuButton("Show/Hide\nColumns");
        selectFilterButton.getItems().add(selectFiltersMenuItem);
        selectFilterButton.setCursor(Cursor.HAND);
        selectFilterButton.setPrefWidth(100);
        selectFilterButton.setPrefHeight(40);
        selectFilterButton.setFont(Font.font("System",FontWeight.BOLD, 12));

        nameField = new TextField();
        nameField.setPrefWidth(140);
        nameField.setPrefHeight(40);
        nameField.setPromptText("Name:");
        nameField.setFocusTraversable(false);
        nameField.setStyle("-fx-font-size: 12px;");

        searchButton = new Button("Search");
        Page.addButtonTransition(searchButton);
        searchButton.setMinHeight(30);
        searchButton.setMinWidth(30);
        searchButton.setPrefHeight(40);
        searchButton.setPrefWidth(70);
        searchButton.setFont(Font.font("System", FontWeight.BOLD, 15));
        searchButton.setOnMouseReleased(e -> {
            searchButton.setScaleX(1);
            searchButton.setScaleY(1);
            handleButtonPress(searchButton);
        });
        queryOptionsBox.getChildren().addAll(filterButton,selectFilterButton,nameField,searchButton);

        resultScrollPane = new ScrollPane();
        resultScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        resultScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        resultScrollPane.setContent(resultTableView);
        leftBox.getChildren().addAll(queryOptionsBox,resultScrollPane);
        leftBox.setPadding(new Insets(0, 0, 0, 25));
        leftSide.setMinWidth(200);
        leftSide.getChildren().addAll(leftBox);
        rightSide.getChildren().addAll(rightBox);
        mainBox.getChildren().addAll(leftSide,rightSide);

        /*Make each side of the middle box fill the entire available area*/
        HBox.setHgrow(leftSide, javafx.scene.layout.Priority.ALWAYS);
        //HBox.setHgrow(rightSide, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(mainBox, javafx.scene.layout.Priority.ALWAYS);

        Button backButton = Page.createBackButton();
        backBox.setAlignment(Pos.TOP_CENTER);
        backBox.getChildren().add(backButton);

        base.getChildren().addAll(titleBox,mainBox,backBox);

        root.getChildren().addAll(base);
        
    }

    private void updateMenu(MenuButton menu,CheckBox checkBox){
        if(FILTER_BUTTON_TEXTS[0].equals(checkBox.getText())) {
            menu.getItems().remove(averageGradeRangeTextFieldsMenuItem);
            if(checkBox.isSelected()) {
                averageGradeStartTextField.setPrefWidth(80);
                averageGradeEndTextField.setPrefWidth(80);
                averageGradeRangeTextFieldsMenuItem = new CustomMenuItem(new HBox(averageGradeStartTextField, averageGradeEndTextField));
                menu.getItems().add(menu.getItems().indexOf(averageGradeButton) + 1, averageGradeRangeTextFieldsMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[1].equals(checkBox.getText())){
            menu.getItems().remove(teacherAmountRangeTextFieldsMenuItem);
            if(checkBox.isSelected()){
                teacherAmountStartTextField.setPrefWidth(80);
                teacherAmountEndTextField.setPrefWidth(80);
                teacherAmountRangeTextFieldsMenuItem = new CustomMenuItem(new HBox(teacherAmountStartTextField,teacherAmountEndTextField));
                menu.getItems().add(menu.getItems().indexOf(teacherAmountButton)+1,teacherAmountRangeTextFieldsMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[2].equals(checkBox.getText())){
            menu.getItems().remove(studentAmuntRangeTexFieldsMenuItem);
            if(checkBox.isSelected()){
                studentAmountStartTextField.setPrefWidth(80);
                studentAmountEndTextField.setPrefWidth(80);
                studentAmuntRangeTexFieldsMenuItem = new CustomMenuItem(new HBox(studentAmountStartTextField,studentAmountEndTextField));
                menu.getItems().add(menu.getItems().indexOf(studentAmountButton)+1,studentAmuntRangeTexFieldsMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[3].equals(checkBox.getText())){
            menu.getItems().remove(semesterScrollCustomMenuItem);
            if(checkBox.isSelected()){
                refreshSemesters();
                semesterScrollCustomMenuItem.setHideOnClick(false);
                menu.getItems().add(menu.getItems().indexOf(semesterButton)+1,semesterScrollCustomMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[4].equals(checkBox.getText())){
            menu.getItems().remove(courseIDTextFieldMenuItem);
            if(checkBox.isSelected()){
                courseIDTextField.setPrefWidth(160);
                courseIDTextFieldMenuItem = new CustomMenuItem(new HBox(courseIDTextField));
                menu.getItems().add(menu.getItems().indexOf(courseIDButton)+1,courseIDTextFieldMenuItem);
            }
        }
        else {
            System.out.println("Other!");
        }
    }
    private void refreshSemesters(){
        VBox checkBoxContainer = new VBox();
        for (String field : courseSemesters) {
            CheckBox fieldBox = new CheckBox(field);
            fieldBox.setFocusTraversable(false);
            fieldBox.setPrefHeight(25);
            fieldBox.setPrefWidth(140);
            checkBoxContainer.getChildren().add(fieldBox);
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(checkBoxContainer);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setFocusTraversable(false);
        scrollPane.setPrefHeight(100);
        semesterScrollCustomMenuItem = new CustomMenuItem(scrollPane);
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

    private void refreshTable(){
        try {
            ResultSet resultSet =  previousQuery.executeQuery();
            resultTableView = TableManager.CreateTableView(resultSet, "course");
            TableManager.setUpMouseReleased(resultTableView);
            TableManager.setAllowMultipleRowSelection(true);
            resultScrollPane.setContent(resultTableView);
            resultTableView.setFixedCellSize(Region.USE_COMPUTED_SIZE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}

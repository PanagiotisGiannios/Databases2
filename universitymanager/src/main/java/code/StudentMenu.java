package code;

import java.sql.*;
import javafx.animation.ScaleTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import java.util.*;

import javafx.scene.layout.Background;

public class StudentMenu extends Page {
    private final static int LIMIT = 10;

    private static final String[] FILTER_BUTTON_TEXTS = {"Semester", "Sex", "Age", "Student ID", "E-mail", "Years Enrolled", "Phone", "Average Grade", "Course Amount", "Father Name"};
    private static final String[] SELECT_FILTER_BUTTON_TEXTS = {"Semester", "Average Grade", "Sex", "Address", "Phone Number", "E-mail", "Birthday","Entry Date", "Father Name"};
    private List<String> semestersList = new ArrayList<String>();

    private MenuButton filterButton;

    private CustomMenuItem averageGradeButton;
    private CustomMenuItem averageGradeRangeTextFieldsMenuItem;
    private TextField averageGradeStartTextField;
    private TextField averageGradeEndTextField;

    private CustomMenuItem sexFilterButton;
    private CustomMenuItem radioButtons;
    private RadioButton maleButton = new RadioButton("Male");
    private RadioButton femaleButton = new RadioButton("Female");
    private ToggleGroup toggleGroup = new ToggleGroup();

    private CustomMenuItem ageButton;
    private CustomMenuItem ageRangeTextFieldsMenuItem;
    private TextField ageStartTextField;
    private TextField ageEndTextField;

    private CustomMenuItem idButton;
    private CustomMenuItem idTextFieldMenuItem;
    private TextField idTextField;

    private CustomMenuItem emailButton;
    private CustomMenuItem emailTextFieldMenuItem;
    private TextField emailTextField;
    
    private CustomMenuItem semesterButton;
    private CustomMenuItem semesterScrollCustomMenuItem;

    private CustomMenuItem yearsEnrolledButton;
    private CustomMenuItem yearsRangeTextFieldsMenuItem;
    private TextField yearsStartTextField;
    private TextField yearsEndTextField;

    private CustomMenuItem phoneButton;
    private CustomMenuItem phoneTextFieldItem;
    private TextField phoneTextField;

    private CustomMenuItem courseAmountButton;
    private CustomMenuItem courseAmountRangeTextFieldsItem;
    private TextField courseAmountStartTextField;
    private TextField courseAmountEndTextField;

    private CustomMenuItem fatherNameButton;
    private CustomMenuItem fatherNameTextFieldItem;
    private TextField fatherNameTextField;

    private MenuButton selectFilterButton;

    private CustomMenuItem selectFiltersMenuItem;
    private VBox selectFiltersContainer;

    private TextField firstNameField;
    private TextField lastNameField;

    private Button searchButton;

    private ComboBox<String> viewComboBox  = new ComboBox<String>(FXCollections.observableArrayList());
    private TextField viewNameTextField = Page.makeTextField(20);

    private String selectString = "";
    private String joinString   = "";
    private String whereString  = "";
    private PreparedStatement previousQuery = null;

    private List<Object> whereParametersList = new ArrayList<Object>();

    private ResultSet  resultSet;
    private ScrollPane resultScrollPane;
    private TableView<ObservableList<String>>  resultTableView;

    //private String ssnSelected;

    @Override
    public void start(Stage primaryStage) {
        Page.primaryStage = primaryStage;
        loadLogo();
        loadBackground("studentPage.png");
        studentMenuSetup();
        createScene();
        retrieveFields();
        retrieveViews();
        handleButtonPress(new Button("Search"));
        System.out.println("\n\nDONE!\n\n");
    }
    
    private void retrieveViews() {
        try{
            viewComboBox.getItems().clear();
            ResultSet results = Page.connection.createStatement().executeQuery("Select table_name FROM information_schema.views WHERE table_schema = 'university'");
            while (results.next()) {
                if(!results.getString(1).substring(0,3).equals("ov_") && results.getString(1).substring(0,3).equals("pr_")){
                    viewComboBox.getItems().add(results.getString(1).substring(3));
                }
            }
            viewComboBox.getItems().add("Default");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void retrieveFields(){
        String getStudentSemester = "SELECT DISTINCT semester FROM student";
        
        try (ResultSet result = Page.connection.createStatement().executeQuery(getStudentSemester)){
            while (result.next()) {
                semestersList.add(result.getString("semester"));
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
                AddPage prof = new AddPage("student");
                prof.start(Page.primaryStage);
                break;
            case "Delete":
                System.out.println("Deleted! " + TableManager.selectedId);
                if(TableManager.selectedRowIdList.isEmpty()){
                    Alert alert = new Alert(AlertType.ERROR); 
                    alert.setTitle("Error");
                    alert.setHeaderText("No student selected");
                    alert.setContentText("Select a student and try again!");
                    alert.showAndWait();
                    break;
                }
                Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Deletion!");
                if(TableManager.selectedRowIdList.size() == 1){
                    confirmAlert.setHeaderText("Are you sure you want to delete the student with ID: '" + TableManager.selectedRowIdList.get(0) + "' ?");
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
                    confirmAlert.setHeaderText("Are you sure you want to delete the students with IDs: " + selections + " ?");
                }
                ButtonType yesButtonType = new ButtonType("Yes");
                ButtonType noButtonType = new ButtonType("No");
                confirmAlert.getButtonTypes().setAll(yesButtonType,noButtonType);
                confirmAlert.showAndWait().ifPresent(buttonType ->{
                    if(buttonType == yesButtonType){
                        try {
                            for (String id : TableManager.selectedRowIdList) {
                                Page.connection.createStatement().executeUpdate("DELETE FROM student WHERE StudentID = " + id);
                                Page.connection.createStatement().executeUpdate("DELETE FROM attends WHERE StudentID = " + id);
                            }
                            
                            resultTableView.getItems().remove(resultTableView.getSelectionModel().getSelectedIndex());
                            resultTableView.getSelectionModel().clearSelection();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        refreshTable();
                        retrieveFields();
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
                if(TableManager.selectedRowIdList.size() > 1){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Too many selections");
                    alert.setHeaderText("Too many students selected, please select only one");
                    alert.showAndWait();
                }
                else if(TableManager.selectedRowIdList.size() < 1){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("No selection");
                    alert.setHeaderText("You have not selected a student");
                    alert.setContentText("Select a student and try again!");
                    alert.showAndWait();
                }
                else{
                    EditPage editStudent = new EditPage("student", TableManager.selectedRowIdList.get(0));
                    editStudent.start(primaryStage);
                }
                break;
            case "Attends":
                if(TableManager.selectedRowIdList == null || TableManager.selectedRowIdList.isEmpty()){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("No selection");
                    alert.setHeaderText("No student selected, select a student and try again!");
                    alert.showAndWait();
                }
                else{
                    AttendsPage attends = new AttendsPage("student", TableManager.selectedRowIdList);
                    attends.start(primaryStage);
                }
                break;
            case "Search":
                selectString = "SELECT DISTINCT StudentID AS 'Student ID', FirstName AS 'First Name', LastName AS 'Last Name', ";
                String selectedViewString = viewComboBox.getSelectionModel().getSelectedItem();
                if(selectedViewString == null || selectedViewString.equals("Default")){
                    selectedViewString = "ov_students";
                }
                else{
                    selectedViewString = "st_" + selectedViewString;
                }
                joinString   = "FROM `" + selectedViewString + "`";
                whereString  = "WHERE";

                String invalidRangeFilters ="";
                Boolean isCorrectOrder = true;
                whereParametersList.clear();
                Boolean showMissingAlert = false;

                CheckBox[] checkBoxArray = selectFiltersContainer.getChildren().toArray(new CheckBox[0]);
                if(checkBoxArray[0].isSelected()){
                    selectString += "Semester AS 'Semester', ";
                }
                if(checkBoxArray[1].isSelected()){
                    selectString += "AverageGrade AS 'Average Grade', ";
                }
                if(checkBoxArray[2].isSelected()){
                    selectString += "Sex, ";
                }
                if(checkBoxArray[3].isSelected()){
                    selectString += "Address, ";
                }
                if(checkBoxArray[4].isSelected()){
                    selectString += "Phone AS 'Phone Number', ";
                }
                if(checkBoxArray[5].isSelected()){
                    selectString += "Email AS 'E-mail', ";
                }
                if(checkBoxArray[6].isSelected()){
                    selectString += "Birthday, ";
                }
                if(checkBoxArray[7].isSelected()){
                    selectString += "EntryDate AS 'Entry Date', ";
                }
                if(checkBoxArray[8].isSelected()){
                    selectString += "FatherName AS 'Father Name', ";
                }
                
                
                selectString = selectString.substring(0,selectString.length() -2);
                if(((CheckBox)idButton.getContent()).isSelected()){
                    if(!idTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND StudentID = ?");
                        whereParametersList.add(Integer.parseInt(idTextField.getText()));
                    }
                    else{
                        showMissingAlert = true;
                    }
                }
                if(((CheckBox)emailButton.getContent()).isSelected()){
                    if(!emailTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND Email = ?");
                        whereParametersList.add(emailTextField.getText());
                    }
                    else{
                        showMissingAlert = true;
                    }
                }
                if(((CheckBox)phoneButton.getContent()).isSelected()){
                    if(!phoneTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND Phone = ?");
                        whereParametersList.add(Integer.parseInt(phoneTextField.getText()));
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
                                whereString = whereString.concat("semester = ?");
                                whereParametersList.add(((CheckBox)node).getText());
                            }
                        }
                    }
                    if(!hasSelected){
                        whereString = whereString.concat(("1=1"));
                        showMissingAlert = true;
                    }
                    whereString = whereString.concat(")");
                }     
                if(((CheckBox)sexFilterButton.getContent()).isSelected()){
                    if(toggleGroup.getSelectedToggle() != null) {
                        whereString = whereString.concat(" AND sex = ?");
                        whereParametersList.add(((RadioButton)toggleGroup.getSelectedToggle()).getText());
                    }
                    else{
                        showMissingAlert = true;
                    }
                }
                if(((CheckBox)ageButton.getContent()).isSelected()){
                    int start= Integer.MIN_VALUE;
                    int end = Integer.MAX_VALUE;
                    if(!ageStartTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND datediff(curdate(),birthday)/365.25 >= ?");
                        whereParametersList.add(Integer.parseInt(ageStartTextField.getText()));
                        start = Integer.parseInt(ageStartTextField.getText());
                    }
                    if(!ageEndTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND datediff(curdate(),birthday)/365.25 <= ?");
                        whereParametersList.add(Integer.parseInt(ageEndTextField.getText()));
                        end = Integer.parseInt(ageEndTextField.getText());
                    }
                    if(ageStartTextField.getText().isEmpty() && ageEndTextField.getText().isEmpty()){
                        showMissingAlert = true;
                    }
                    if(start > end){
                        invalidRangeFilters += "'Age', ";
                        isCorrectOrder = false;
                    }
                }
                if(((CheckBox)averageGradeButton.getContent()).isSelected()){
                    int start = Integer.MIN_VALUE;
                    int end = Integer.MAX_VALUE;
                    if(!averageGradeStartTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND AverageGrade >= ?");
                        whereParametersList.add(Integer.parseInt(averageGradeStartTextField.getText()));
                        start = Integer.parseInt(averageGradeStartTextField.getText());
                    }
                    if(!averageGradeEndTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND AverageGrade <= ?");
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
                if(((CheckBox)yearsEnrolledButton.getContent()).isSelected()){
                    int start = Integer.MIN_VALUE;
                    int end = Integer.MAX_VALUE;
                    if(!yearsStartTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND datediff(curdate(),entrydate)/365.25 >= ?");
                        whereParametersList.add(Integer.parseInt(yearsStartTextField.getText()));
                        start = Integer.parseInt(yearsStartTextField.getText());
                    }
                    if(!yearsEndTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND datediff(curdate(),entrydate)/365.25 <= ?");
                        whereParametersList.add(Integer.parseInt(yearsEndTextField.getText()));
                        end = Integer.parseInt(yearsEndTextField.getText());
                    }
                    if(yearsStartTextField.getText().isEmpty() && yearsEndTextField.getText().isEmpty()){
                        showMissingAlert = true;
                    }
                    if(start > end){
                        invalidRangeFilters += "'Years Worked', ";
                        isCorrectOrder = false;
                    }
                }
                if(((CheckBox)courseAmountButton.getContent()).isSelected()){
                    int start = Integer.MIN_VALUE;
                    int end = Integer.MAX_VALUE;
                    if(!courseAmountStartTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND CourseAmount >= ?");
                        whereParametersList.add(Integer.parseInt(courseAmountStartTextField.getText()));
                        start = Integer.parseInt(courseAmountStartTextField.getText());
                    }
                    if(!courseAmountEndTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND CourseAmount <= ?");
                        whereParametersList.add(Integer.parseInt(courseAmountEndTextField.getText()));
                        end = Integer.parseInt(courseAmountEndTextField.getText());
                    }
                    if(courseAmountEndTextField.getText().isEmpty() && courseAmountEndTextField.getText().isEmpty()){
                        showMissingAlert = true;
                    }
                    if(start > end){
                        invalidRangeFilters += "'Course Amount', ";
                        isCorrectOrder = false;
                    }
                }
                if(((CheckBox)fatherNameButton.getContent()).isSelected()){
                    if(!fatherNameTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND FatherName LIKE ?");
                        whereParametersList.add("%" + fatherNameTextField.getText() + "%");
                    }
                }
                if(!firstNameField.getText().isEmpty()){
                    whereString = whereString.concat(" AND FirstName LIKE ?");
                    whereParametersList.add("%"+ firstNameField.getText() +"%");
                }
                if (!lastNameField.getText().isEmpty()) {
                    whereString = whereString.concat(" AND LastName LIKE ?");
                    whereParametersList.add("%" + lastNameField.getText() + "%");
                }
                if(whereString.indexOf(" AND") != -1){
                    whereString = whereString.substring(0,whereString.indexOf(" AND")) + whereString.substring(whereString.indexOf(" AND") + 4);
                }
                else{
                    whereString = "";
                }
                String query = selectString + "\n" + joinString + "\n" + whereString;
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
                    
                    previousQuery = preparedStatement;
                    resultSet = preparedStatement.executeQuery();
                    
                    resultTableView = TableManager.CreateTableView(resultSet, "student");
                    TableManager.setUpMouseReleased(resultTableView);
                    TableManager.setAllowMultipleRowSelection(true);
                    resultTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                    resultTableView.setFixedCellSize(Region.USE_COMPUTED_SIZE);
                    resultScrollPane.setContent(resultTableView);
                    resultScrollPane.setFitToWidth(true);
                    resultScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
                break;
            case "Create View":
                if(viewNameTextField.getText().trim().isEmpty()){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("No name input");
                    alert.setHeaderText("View Name Text Field is empty\nInput a name and try again!");
                    alert.setContentText(null);
                    alert.showAndWait();
                }
                else if(viewNameTextField.getText().trim().toLowerCase().equals("default")){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Not allowed name");
                    alert.setHeaderText("The name of the created view cannot be \"Default\"!\nChange it and try again!");
                    alert.setContentText(null);
                    alert.showAndWait();
                }
                else{
                    try {
                        String name = "`pr_" + viewNameTextField.getText().replace(" ", "_") + "`";
                        for(Object parameter: whereParametersList){
                            whereString = whereString.replaceFirst("\\?", "'" + String.valueOf(parameter) + "'");
                        }
                        System.out.println("CREATE VIEW "+ name + " AS SELECT * " + joinString +" " + whereString);
                        Page.connection.createStatement().execute("CREATE VIEW "+ name + " AS SELECT * " + joinString +" " + whereString);
                        retrieveViews();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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
        DoubleProperty sizeProperty = new SimpleDoubleProperty();
        sizeProperty.bind(Page.primaryStage.widthProperty().add(Page.primaryStage.heightProperty()));
        button.setMinWidth(100);
        button.setMinHeight(50);
        button.setPrefWidth(100);
        button.setPrefHeight(50);
        
        button.setFont(Font.font("System",FontWeight.BOLD, 18));
        button.setStyle("-fx-background-radius: 15;");
        if(button.getText().equals("Create View")){
            button.setFont(Font.font("System",FontWeight.BOLD, 14));
        }
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
    


    private void studentMenuSetup(){
        VBox base = new VBox(10);
        HBox titleBox = new HBox();
        HBox mainBox = new HBox();
        HBox backBox = new HBox();
        
        Text titleText = new Text("Student");
        titleText.setFont(Font.font("System",FontWeight.BOLD,29));
        titleBox.getChildren().add(titleText);
        titleBox.setAlignment(Pos.TOP_CENTER);
        


        /*
         * Create the stackpanes that will contain all 
         * of the necessary components for each side
         */
        StackPane rightSide = new StackPane();
        StackPane leftSide  = new StackPane();
        VBox leftBox  = new VBox(35);
        VBox rightBox = new VBox(35);

        String[] buttonTexts = {"Add","Delete","Edit","Attends","Create View"};
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

        HBox queryOptionsBox = new HBox(5);

        filterButton = new MenuButton("Filters");
        filterButton.setFocusTraversable(false);
        semesterButton = toCustomMenu(FILTER_BUTTON_TEXTS[0]);
        sexFilterButton = toCustomMenu(FILTER_BUTTON_TEXTS[1]);
        ageButton = toCustomMenu(FILTER_BUTTON_TEXTS[2]);
        idButton = toCustomMenu(FILTER_BUTTON_TEXTS[3]);
        emailButton = toCustomMenu(FILTER_BUTTON_TEXTS[4]);
        yearsEnrolledButton = toCustomMenu(FILTER_BUTTON_TEXTS[5]);
        phoneButton = toCustomMenu(FILTER_BUTTON_TEXTS[6]);
        averageGradeButton = toCustomMenu(FILTER_BUTTON_TEXTS[7]);
        courseAmountButton = toCustomMenu(FILTER_BUTTON_TEXTS[8]);
        fatherNameButton = toCustomMenu(FILTER_BUTTON_TEXTS[9]);

        averageGradeStartTextField = Page.createNumericTextField(2);
        averageGradeEndTextField   = Page.createNumericTextField(2);
        averageGradeStartTextField.setPromptText("Min.");
        averageGradeEndTextField.setPromptText("Max.");

        maleButton.setToggleGroup(toggleGroup);
        femaleButton.setToggleGroup(toggleGroup);
        radioButtons = new CustomMenuItem(new VBox(10,maleButton,femaleButton));
        radioButtons.setHideOnClick(false);

        ageStartTextField = Page.createNumericTextField(3);
        ageEndTextField = Page.createNumericTextField(3);
        ageStartTextField.setPromptText("Min.");
        ageEndTextField.setPromptText("Max.");

        idTextField = Page.createNumericTextField(LIMIT);
        idTextField.setPromptText("Student ID:");

        emailTextField = new TextField();
        emailTextField.setPromptText("E-mail:");

        yearsStartTextField = Page.createNumericTextField(2);
        yearsEndTextField = Page.createNumericTextField(2);
        yearsStartTextField.setPromptText("Min.");
        yearsEndTextField.setPromptText("Max.");

        phoneTextField = Page.createNumericTextField(LIMIT);
        phoneTextField.setPromptText("Phone Number:");

        courseAmountStartTextField = Page.createNumericTextField(3);
        courseAmountEndTextField = Page.createNumericTextField(3);
        courseAmountStartTextField.setPromptText("Min.");
        courseAmountEndTextField.setPromptText("Max.");

        fatherNameTextField = Page.makeTextField(50);
        fatherNameTextField.setPromptText("Father Name:");

        filterButton.getItems().addAll(fatherNameButton,semesterButton,idButton,emailButton,phoneButton,sexFilterButton,ageButton,averageGradeButton,yearsEnrolledButton,courseAmountButton);
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

        firstNameField = new TextField();
        firstNameField.setPrefWidth(140);
        firstNameField.setPrefHeight(40);
        firstNameField.setPromptText("First Name:");
        firstNameField.setFocusTraversable(false);
        firstNameField.setStyle("-fx-font-size: 12px;");

        lastNameField = new TextField();
        lastNameField.setPrefWidth(140);
        lastNameField.setPrefHeight(40);
        lastNameField.setPromptText("Last Name:");
        lastNameField.setFocusTraversable(false);   
        lastNameField.setStyle("-fx-font-size: 12px;");

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

        viewComboBox.setPromptText("Select View");
        viewComboBox.setPrefHeight(40);
        viewComboBox.setPrefWidth(105);
        viewComboBox.setOnAction(event -> {
            String select = viewComboBox.getSelectionModel().getSelectedItem();
            System.out.println("Selected view: " + select);
        });
        viewNameTextField.setPrefWidth(105);
        viewNameTextField.setPrefHeight(40);
        viewNameTextField.setPromptText("New View Name");

        

        queryOptionsBox.getChildren().addAll(filterButton,selectFilterButton,firstNameField,lastNameField,searchButton,viewComboBox,viewNameTextField);

        resultScrollPane = new ScrollPane();
        resultScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        resultScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        resultScrollPane.setContent(resultTableView);
        resultScrollPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255, 0.5),CornerRadii.EMPTY,javafx.geometry.Insets.EMPTY)));
        leftBox.getChildren().addAll(queryOptionsBox,resultScrollPane);
        leftBox.setPadding(new Insets(0, 0, 0, 25));
        leftSide.setMinWidth(200);
        leftSide.getChildren().addAll(leftBox);
        rightSide.getChildren().addAll(rightBox);
        mainBox.getChildren().addAll(leftSide,rightSide);

        /*Make each side of the middle box fill the entire available area*/
        HBox.setHgrow(leftSide, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(mainBox, javafx.scene.layout.Priority.ALWAYS);

        Button backButton = Page.createBackButton();
        backBox.setAlignment(Pos.TOP_CENTER);
        backBox.getChildren().add(backButton);
        backBox.setPadding(new Insets(0, 0,5, 0));
        
        base.getChildren().addAll(titleBox,mainBox,backBox);

        VBox test = new VBox(base);
        test.setAlignment(Pos.CENTER);

        root.getChildren().addAll(test);
        
    }

    private void updateMenu(MenuButton menu,CheckBox checkBox){
        if(FILTER_BUTTON_TEXTS[0].equals(checkBox.getText())){
            menu.getItems().remove(semesterScrollCustomMenuItem);
            if(checkBox.isSelected()){
                VBox checkBoxContainer = new VBox();
                for (String field : semestersList) {
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
                semesterScrollCustomMenuItem.setHideOnClick(false);
                menu.getItems().add(menu.getItems().indexOf(semesterButton)+1,semesterScrollCustomMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[1].equals(checkBox.getText())){
            menu.getItems().remove(radioButtons);
            if(checkBox.isSelected()){
                radioButtons = new CustomMenuItem(new VBox(10,maleButton,femaleButton));
                radioButtons.setHideOnClick(false);
                menu.getItems().add(menu.getItems().indexOf(sexFilterButton)+1,radioButtons);
            }
        }
        else if(FILTER_BUTTON_TEXTS[2].equals(checkBox.getText())){
            menu.getItems().remove(ageRangeTextFieldsMenuItem);
            if(checkBox.isSelected()){
                ageStartTextField.setPrefWidth(80);
                ageEndTextField.setPrefWidth(80);
                ageRangeTextFieldsMenuItem = new CustomMenuItem(new HBox(ageStartTextField,ageEndTextField));
                menu.getItems().add(menu.getItems().indexOf(ageButton)+1,ageRangeTextFieldsMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[3].equals(checkBox.getText())){
            menu.getItems().remove(idTextFieldMenuItem);
            if(checkBox.isSelected()){
                idTextField.setPrefWidth(160);
                idTextFieldMenuItem = new CustomMenuItem(new HBox(idTextField));
                menu.getItems().add(menu.getItems().indexOf(idButton)+1,idTextFieldMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[4].equals(checkBox.getText())){
            menu.getItems().remove(emailTextFieldMenuItem);
            if(checkBox.isSelected()){
                emailTextField.setPrefWidth(160);
                emailTextFieldMenuItem = new CustomMenuItem(new HBox(emailTextField));
                menu.getItems().add(menu.getItems().indexOf(emailButton)+1,emailTextFieldMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[5].equals(checkBox.getText())){
            menu.getItems().remove(yearsRangeTextFieldsMenuItem);
            if(checkBox.isSelected()){
                yearsStartTextField.setPrefWidth(80);
                yearsEndTextField.setPrefWidth(80);
                yearsRangeTextFieldsMenuItem = new CustomMenuItem(new HBox(yearsStartTextField,yearsEndTextField));
                menu.getItems().add(menu.getItems().indexOf(yearsEnrolledButton) + 1, yearsRangeTextFieldsMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[6].equals(checkBox.getText())){
            menu.getItems().remove(phoneTextFieldItem);
            if(checkBox.isSelected()){
                phoneTextField.setPrefWidth(160);
                phoneTextFieldItem = new CustomMenuItem(new HBox(phoneTextField));
                menu.getItems().add(menu.getItems().indexOf(phoneButton) + 1, phoneTextFieldItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[7].equals(checkBox.getText())) {
            menu.getItems().remove(averageGradeRangeTextFieldsMenuItem);
            if(checkBox.isSelected()) {
                averageGradeStartTextField.setPrefWidth(80);
                averageGradeEndTextField.setPrefWidth(80);
                averageGradeRangeTextFieldsMenuItem = new CustomMenuItem(new HBox(averageGradeStartTextField, averageGradeEndTextField));
                menu.getItems().add(menu.getItems().indexOf(averageGradeButton) + 1, averageGradeRangeTextFieldsMenuItem);
            }
        } 
        else if(FILTER_BUTTON_TEXTS[8].equals(checkBox.getText())){
            menu.getItems().remove(courseAmountRangeTextFieldsItem);
            if(checkBox.isSelected()){
                courseAmountStartTextField.setPrefWidth(80);
                courseAmountEndTextField.setPrefWidth(80);
                courseAmountRangeTextFieldsItem = new CustomMenuItem(new HBox(courseAmountStartTextField,courseAmountEndTextField));
                menu.getItems().add(menu.getItems().indexOf(courseAmountButton)+1,courseAmountRangeTextFieldsItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[9].equals(checkBox.getText())){
            menu.getItems().remove(fatherNameTextFieldItem);
            if(checkBox.isSelected()){
                fatherNameTextField.setPrefWidth(160);
                fatherNameTextFieldItem = new CustomMenuItem(new VBox(fatherNameTextField));
                menu.getItems().add(menu.getItems().indexOf(fatherNameButton)+1,fatherNameTextFieldItem);
            }
        }
        else {
            System.out.println("We have big problem!");
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

    private void refreshTable(){
        try {
            ResultSet resultSet =  previousQuery.executeQuery();
            resultTableView = TableManager.CreateTableView(resultSet, "student");
            TableManager.setUpMouseReleased(resultTableView);
            TableManager.setAllowMultipleRowSelection(true);
            resultTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            resultScrollPane.setContent(resultTableView);
            resultTableView.setFixedCellSize(Region.USE_COMPUTED_SIZE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

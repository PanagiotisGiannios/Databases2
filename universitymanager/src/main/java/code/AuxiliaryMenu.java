package code;

import java.sql.*;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class AuxiliaryMenu extends Page {

    //TODO: 
    private String FPass = "!Sql12345Sql!";
    private String PPass = "1234";



    private static final String[] FILTER_BUTTON_TEXTS = {"Salary", "Sex", "Age", "SSN", "E-mail", "Profession", "Years Worked", "Phone"};
    private static final String[] SELECT_FILTER_BUTTON_TEXTS = {"Profession", "Salary", "Sex", "Address", "Phone Number", "E-mail", "Birthday","Job Starting Date"};

    private List<String> professionList = new ArrayList<String>();

    private MenuButton filterButton;

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
    
    private CustomMenuItem professionButton;
    private CustomMenuItem professionScrollCustomMenuItem;

    private CustomMenuItem yearsWorkedButton;
    private CustomMenuItem yearsRangeTexFieldsMenuItem;
    private TextField yearsStartTextField;
    private TextField yearsEndTextField;

    private CustomMenuItem phoneButton;
    private CustomMenuItem phoneTextFieldItem;
    private TextField phoneTextField;

    private MenuButton selectFilterButton;

    private CustomMenuItem selectFiltersMenuItem;
    private VBox selectFiltersContainer;

    private TextField firstNameField;
    private TextField lastNameField;

    private Button searchButton;

    private ComboBox<String> viewComboBox  = new ComboBox<String>(FXCollections.observableArrayList());
    private TextField viewNameTextField = Page.makeTextField(20);

    private String selectString = "SELECT ssn, FirstName, LastName, ";
    private String joinString   = "FROM employee,auxiliary_staff";
    private String whereString  = "WHERE ssn=EmployeeID";
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
        //Page.scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        if(Page.connection == null){
            
            try {
                Page.connection = DatabaseConnector.connect("root", "1234");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        retrieveProfessions();
        retrieveViews();
        Page.primaryStage = primaryStage;
        loadLogo();
        loadBackground("AuxiliaryStaffPage.png");
        auxiliaryMenuSetup();

        createScene();
        for (CheckBox checkBox : selectFiltersContainer.getChildren().toArray(new CheckBox[0])) {
            if(checkBox.getText().equals(SELECT_FILTER_BUTTON_TEXTS[0])){
                checkBox.setSelected(true);
            }
            if(checkBox.getText().equals(SELECT_FILTER_BUTTON_TEXTS[5])){
                checkBox.setSelected(true);
            }
        }
        //Simulate a press on the search button to populate the viewTable at the start.
        handleButtonPress(new Button("Search"));
    }

    private void retrieveViews() {
        try{
            viewComboBox.getItems().clear();
            ResultSet results = Page.connection.createStatement().executeQuery("Select table_name FROM information_schema.views WHERE table_schema = 'university'");
            while (results.next()) {
                if(!results.getString(1).substring(0,3).equals("ov_") && results.getString(1).substring(0,3).equals("au_")){
                    viewComboBox.getItems().add(results.getString(1).substring(3));
                }
            }
            viewComboBox.getItems().add("Default");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void retrieveProfessions(){
        String getProfessions = "SELECT DISTINCT profession FROM Auxiliary_staff";
        try (ResultSet result = Page.connection.createStatement().executeQuery(getProfessions)){
            while (result.next()) {
                professionList.add(result.getString("profession"));
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
                AddPage auxiliaryAddPage = new AddPage("auxiliary");
                auxiliaryAddPage.start(Page.primaryStage);
                break;
            case "Delete":
                System.out.println("Deleted! " + TableManager.selectedId);
                if(TableManager.selectedRowIdList.isEmpty()){
                    Alert alert = new Alert(AlertType.ERROR); 
                    alert.setTitle("Error");
                    alert.setHeaderText("No employee selected");
                    alert.setContentText("Select an employee and try again!");
                    alert.showAndWait();
                    break;
                }
                Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Deletion!");
                if(TableManager.selectedRowIdList.size() == 1){
                    confirmAlert.setHeaderText("Are you sure you want to delete the employee with SSN: '" + TableManager.selectedRowIdList.get(0) + "' ?");
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
                    confirmAlert.setHeaderText("Are you sure you want to delete the employees with SSNs: " + selections + " ?");
                }
                ButtonType yesButtonType = new ButtonType("Yes");
                ButtonType noButtonType = new ButtonType("No");
                confirmAlert.getButtonTypes().setAll(yesButtonType,noButtonType);
                confirmAlert.showAndWait().ifPresent(buttonType ->{
                    if(buttonType == yesButtonType){
                        try {
                            //TODO: check deletes
                            for (String id : TableManager.selectedRowIdList) {
                                Page.connection.createStatement().executeUpdate("DELETE FROM auxiliary_Staff WHERE EmployeeID = " + id);
                                Page.connection.createStatement().executeUpdate("DELETE FROM employee WHERE ssn = "              + id);
                            }
                            resultTableView.getItems().remove(resultTableView.getSelectionModel().getSelectedIndex());
                            resultTableView.getSelectionModel().clearSelection();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        refreshTable();
                        retrieveProfessions();
                        updateMenu(filterButton, (CheckBox)professionButton.getContent());
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
                    alert.setHeaderText("Too many employees selected, please select only one");
                    alert.showAndWait();
                }
                else if(TableManager.selectedRowIdList.size() < 1){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("No selection");
                    alert.setHeaderText("You have not selected an employee");
                    alert.setContentText("Select an employee and try again!");
                    alert.showAndWait();
                }
                else{
                    EditPage editAuxiliary = new EditPage("auxiliary", TableManager.selectedRowIdList.get(0));
                    editAuxiliary.start(primaryStage);
                }
                break;
            case "Search":
                selectString = "SELECT DISTINCT ssn AS 'SSN', FirstName AS 'First Name', LastName AS 'Last Name', ";
                String selectedViewString = viewComboBox.getSelectionModel().getSelectedItem();
                if(selectedViewString == null || selectedViewString.equals("Default")){
                    selectedViewString = "ov_auxiliarystaff";
                }
                else{
                    selectedViewString = "au_"+selectedViewString;
                }
                joinString   = "FROM `" + selectedViewString + "`";
                whereString  = "WHERE";
                groupString  = "";

                String invalidRangeFilters = "";
                Boolean isCorrectOrder = true;
                whereParametersList.clear();
                groupParametersList.clear();
                Boolean showMissingAlert = false;

                CheckBox[] checkBoxArray = selectFiltersContainer.getChildren().toArray(new CheckBox[0]);
                if(checkBoxArray[0].isSelected()){
                    selectString += "Profession AS 'Profession', ";
                }
                if(checkBoxArray[1].isSelected()){
                    selectString += "Salary, ";
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
                    selectString += "JobStartingDate AS 'Job Starting Date', ";
                }
                
                selectString = selectString.substring(0,selectString.length() -2);
                if(((CheckBox)ssnButton.getContent()).isSelected()){
                    if(!ssnTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND ssn = ?");
                        whereParametersList.add(ssnTextField.getText());
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
                        whereParametersList.add(phoneTextField.getText());
                    }
                    else{
                        showMissingAlert = true;
                    }
                }
                if(((CheckBox)professionButton.getContent()).isSelected()){
                    whereString = whereString.concat( " AND (");
                    ScrollPane scroll = (ScrollPane)professionScrollCustomMenuItem.getContent();
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
                                whereString = whereString.concat("profession = ?");
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
                if(((CheckBox)salaryButton.getContent()).isSelected()){
                    int start = Integer.MIN_VALUE;
                    int end = Integer.MAX_VALUE;
                    if(!startTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND salary >= ?");
                        whereParametersList.add(Integer.parseInt(startTextField.getText()));
                        start = Integer.parseInt(startTextField.getText());
                    }
                    if(!endTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND salary <= ?");
                        whereParametersList.add(Integer.parseInt(endTextField.getText()));
                        end = Integer.parseInt(endTextField.getText());
                    }
                    if(startTextField.getText().isEmpty() && endTextField.getText().isEmpty()){
                        showMissingAlert = true;
                    }
                    if(start > end){
                        invalidRangeFilters += "'Salary', ";
                        isCorrectOrder = false;
                    }
                }
                if(((CheckBox)yearsWorkedButton.getContent()).isSelected()){
                    int start = Integer.MIN_VALUE;
                    int end = Integer.MAX_VALUE;
                    if(!yearsStartTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND datediff(curdate(),jobstartingdate)/365.25 >= ?");
                        whereParametersList.add(Integer.parseInt(yearsStartTextField.getText()));
                        start = Integer.parseInt(yearsStartTextField.getText());
                    }
                    if(!yearsEndTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND datediff(curdate(),jobstartingdate)/365.25 <= ?");
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
                    
                    resultTableView = TableManager.CreateTableView(resultSet, "employee");
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
                        String name = "`au_" + viewNameTextField.getText().replace(" ", "_") + "`";
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

        button.setMinWidth(100);
        button.setMinHeight(50);
        button.setPrefWidth(100);
        button.setPrefHeight(50);
        button.setFont(Font.font("System",FontWeight.BOLD, 18));
        if(button.getText().equals("Create View")){
            button.setFont(Font.font("System",FontWeight.BOLD, 14));
        }
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

    private void auxiliaryMenuSetup(){
        VBox base = new VBox(10);
        HBox titleBox = new HBox();
        HBox mainBox = new HBox();
        HBox backBox = new HBox();
        
        Text titleText = new Text("Auxiliary Staff");
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

        String[] buttonTexts = {"Add","Delete","Edit","Create View"};
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
        salaryButton = toCustomMenu(FILTER_BUTTON_TEXTS[0]);
        sexFilterButton = toCustomMenu(FILTER_BUTTON_TEXTS[1]);
        ageButton = toCustomMenu(FILTER_BUTTON_TEXTS[2]);
        ssnButton = toCustomMenu(FILTER_BUTTON_TEXTS[3]);
        emailButton = toCustomMenu(FILTER_BUTTON_TEXTS[4]);
        professionButton = toCustomMenu(FILTER_BUTTON_TEXTS[5]);
        yearsWorkedButton = toCustomMenu(FILTER_BUTTON_TEXTS[6]);
        phoneButton = toCustomMenu(FILTER_BUTTON_TEXTS[7]);

        startTextField = Page.createNumericTextField(6);
        endTextField   = Page.createNumericTextField(6);
        startTextField.setPromptText("Min.");
        endTextField.setPromptText("Max.");

        maleButton.setToggleGroup(toggleGroup);
        femaleButton.setToggleGroup(toggleGroup);
        radioButtons = new CustomMenuItem(new VBox(10,maleButton,femaleButton));
        radioButtons.setHideOnClick(false);

        ageStartTextField = Page.createNumericTextField(3);
        ageEndTextField = Page.createNumericTextField(3);
        ageStartTextField.setPromptText("Min.");
        ageEndTextField.setPromptText("Max.");

        ssnTextField = Page.createNumericTextField(10);
        ssnTextField.setPromptText("SSN:");

        emailTextField = new TextField();
        emailTextField.setPromptText("E-mail:");


        yearsStartTextField = Page.createNumericTextField(3);
        yearsEndTextField = Page.createNumericTextField(3);
        yearsStartTextField.setPromptText("Min.");
        yearsEndTextField.setPromptText("Max.");

        phoneTextField = Page.createNumericTextField(10);
        phoneTextField.setPromptText("Phone Number:");

        filterButton.getItems().addAll(ssnButton,emailButton,phoneButton,professionButton,sexFilterButton,ageButton,salaryButton,yearsWorkedButton);
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
        searchButton.setFont(Font.font("System", FontWeight.BOLD, 13));
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
       // resultScrollPane.setPrefWidth(400);
        //resultScrollPane.setPrefHeight(300);
        resultScrollPane.setContent(resultTableView);
        //resultScrollPane.setStyle("-fx-background: rgba(255, 255, 255, 0.5);");
        resultScrollPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255, 0.5),CornerRadii.EMPTY,javafx.geometry.Insets.EMPTY)));
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
            menu.getItems().remove(salaryRangeTextFieldsMenuItem);
            if(checkBox.isSelected()) {
                startTextField.setPrefWidth(80);
                endTextField.setPrefWidth(80);
                salaryRangeTextFieldsMenuItem = new CustomMenuItem(new HBox(startTextField, endTextField));
                menu.getItems().add(menu.getItems().indexOf(salaryButton) + 1, salaryRangeTextFieldsMenuItem);
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
            menu.getItems().remove(ssnTextFieldMenuItem);
            if(checkBox.isSelected()){
                ssnTextField.setPrefWidth(160);
                ssnTextFieldMenuItem = new CustomMenuItem(new HBox(ssnTextField));
                menu.getItems().add(menu.getItems().indexOf(ssnButton)+1,ssnTextFieldMenuItem);
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
            menu.getItems().remove(professionScrollCustomMenuItem);
            if(checkBox.isSelected()){
                VBox checkBoxContainer = new VBox();
                for (String field : professionList) {
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

                professionScrollCustomMenuItem = new CustomMenuItem(scrollPane);
                professionScrollCustomMenuItem.setHideOnClick(false);
                menu.getItems().add(menu.getItems().indexOf(professionButton)+1,professionScrollCustomMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[6].equals(checkBox.getText())){
            menu.getItems().remove(yearsRangeTexFieldsMenuItem);
            if(checkBox.isSelected()){
                yearsStartTextField.setPrefWidth(80);
                yearsEndTextField.setPrefWidth(80);
                yearsRangeTexFieldsMenuItem = new CustomMenuItem(new HBox(yearsStartTextField,yearsEndTextField));
                menu.getItems().add(menu.getItems().indexOf(yearsWorkedButton) + 1, yearsRangeTexFieldsMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[7].equals(checkBox.getText())){
            menu.getItems().remove(phoneTextFieldItem);
            if(checkBox.isSelected()){
                phoneTextField.setPrefWidth(160);
                phoneTextFieldItem = new CustomMenuItem(new HBox(phoneTextField));
                menu.getItems().add(menu.getItems().indexOf(phoneButton) + 1, phoneTextFieldItem);
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

    private void refreshTable(){
        try {
            ResultSet resultSet =  previousQuery.executeQuery();
            resultTableView = TableManager.CreateTableView(resultSet, "employee");
            TableManager.setUpMouseReleased(resultTableView);
            TableManager.setAllowMultipleRowSelection(true);
            resultTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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

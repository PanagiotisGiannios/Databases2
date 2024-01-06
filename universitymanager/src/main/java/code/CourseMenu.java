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

public class CourseMenu extends Page {

    //TODO: 
    private String FPass = "!Sql12345Sql!";
    private String PPass = "1234";



    private static final String[] FILTER_BUTTON_TEXTS = {"Is Rector", "Salary", "Sex", "Age", "SSN", "E-mail", "Project", "Field", "Years Worked", "Phone"};
    private static final String[] SELECT_FILTER_BUTTON_TEXTS = {"Field", "Salary", "Sex", "Address", "Phone Number", "E-mail", "Birthday","Job Starting Date", "RectorID","Project Name",  "Project Information", "Project Type"};
    private List<String> projectNames = new ArrayList<String>();
    private List<String> professorFields = new ArrayList<String>();

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
    private CustomMenuItem projectRadioButtons;
    private TextField projectStartTextField;
    private TextField projectEndTextField;
    private RadioButton  projectsByNameButton = new RadioButton("By Name");
    private RadioButton projectsByAmountButton = new RadioButton("By Amount");
    private ToggleGroup projectsToggleGroup = new ToggleGroup();
    
    private CustomMenuItem fieldButton;
    private CustomMenuItem fieldScrollCustomMenuItem;

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

    private String selectString = "SELECT ssn, FirstName, LastName, ";
    private String joinString   = "FROM employee,professor";
    private String whereString  = "WHERE ssn=profID";
    private String groupString = "";
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
        //Page.scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        if(Page.connection == null){
            
            try {
                Page.connection = DatabaseConnector.connect("root", "1234");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String getProjectNames = "SELECT Name FROM Project ORDER BY cast(substring(Name, 8) AS SIGNED)";
        String getProfessorFields = "SELECT DISTINCT profession FROM Professor";
        try (ResultSet result = Page.connection.createStatement().executeQuery(getProjectNames)) {
            while (result.next()) {
                projectNames.add(result.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (ResultSet result = Page.connection.createStatement().executeQuery(getProfessorFields)){
            while (result.next()) {
                professorFields.add(result.getString("profession"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Simulate a press on the search button to populate the viewTable at the start.
        for (CheckBox checkBox : selectFiltersContainer.getChildren().toArray(new CheckBox[0])) {
            if(checkBox.getText().equals(SELECT_FILTER_BUTTON_TEXTS[0])){
                checkBox.setSelected(true);
            }
            if(checkBox.getText().equals(SELECT_FILTER_BUTTON_TEXTS[5])){
                checkBox.setSelected(true);
            }
        }
        handleButtonPress(new Button("Search"));
        System.out.println("\n\nDONE!\n\n");
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
                System.out.println("Deleted! " + TableManager.ssnSelected);
                if(TableManager.ssnSelected == null){
                    Alert alert = new Alert(AlertType.ERROR); 
                    alert.setTitle("Error");
                    alert.setHeaderText("No professor selected");
                    alert.setContentText("Select a professor and try again!");
                    alert.showAndWait();
                    break;
                }
                Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Deletion!");
                confirmAlert.setHeaderText("Are you sure you want to delete the professor with ssn: '" + TableManager.ssnSelected + "' ?");
                ButtonType yesButtonType = new ButtonType("Yes");
                ButtonType noButtonType = new ButtonType("No");
                confirmAlert.getButtonTypes().setAll(yesButtonType,noButtonType);
                confirmAlert.showAndWait().ifPresent(buttonType ->{
                    if(buttonType == yesButtonType){
                        try {
                            Page.connection.createStatement().executeUpdate("DELETE FROM project WHERE ProfessorID = " + TableManager.ssnSelected);
                            Page.connection.createStatement().executeUpdate("DELETE FROM professor WHERE ProfID = "    + TableManager.ssnSelected);
                            Page.connection.createStatement().executeUpdate("DELETE FROM employee WHERE ssn = "        + TableManager.ssnSelected);
                            resultTableView.getItems().remove(resultTableView.getSelectionModel().getSelectedIndex());
                            resultTableView.getSelectionModel().clearSelection();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        System.out.println("NO pressed!");
                    }
                });
                resultTableView.getSelectionModel().clearSelection();
                TableManager.ssnSelected = null;

                break;
            case "Edit":
                System.out.println("Edit person with ssn: "+ TableManager.ssnSelected);
                break;
            case "Teaches":
                System.out.println("Teaches!");
                break;
            case "Rector":
                System.out.println("make rector person with ssn: " + TableManager.ssnSelected);
                confirmAlert = new Alert(AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Setting Rector!");
                confirmAlert.setHeaderText("Are you sure you want to set the professor with ssn: '" + TableManager.ssnSelected + "' as Rector ?");
                yesButtonType = new ButtonType("Yes");
                noButtonType = new ButtonType("No");
                confirmAlert.getButtonTypes().setAll(yesButtonType,noButtonType);
                confirmAlert.showAndWait().ifPresent(buttonType ->{
                    if(buttonType == yesButtonType){
                        try {
                            if(TableManager.ssnSelected != null){
                                Page.connection.createStatement().executeUpdate("UPDATE professor SET ManagerID = " + TableManager.ssnSelected);
                                Page.connection.createStatement().executeUpdate("UPDATE professor SET ManagerID = NULL WHERE profId = " + TableManager.ssnSelected);
                                TableManager.ssnSelected = null;
                                try {
                                    ResultSet resultSet =  previousQuery.executeQuery();
                                    resultTableView = TableManager.CreateTableView(resultSet, "professor");
                                    TableManager.setUpMouseReleased(resultTableView);
                                    resultScrollPane.setContent(resultTableView);
                                    resultTableView.setFixedCellSize(Region.USE_COMPUTED_SIZE);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                Alert alert = new Alert(AlertType.ERROR); 
                                alert.setTitle("Error");
                                alert.setHeaderText("No professor selected");
                                alert.setContentText("Select a professor and try again!");
                                alert.showAndWait();
                            }
                            
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        System.out.println("No rector!");
                    }
                });
                
                break;
            case "Search":
                selectString = "SELECT DISTINCT ssn AS 'SSN', FirstName AS 'First Name', LastName AS 'Last Name', ";
                joinString   = "FROM employee JOIN professor ON ssn=profID";
                whereString  = "WHERE";
                groupString = "";

                String invalidRangeFilters ="";
                Boolean isCorrectOrder = true;
                whereParametersList.clear();
                groupParametersList.clear();
                Boolean showMissingAlert = false;
                Boolean addedProjectTable = false;

                CheckBox[] checkBoxArray = selectFiltersContainer.getChildren().toArray(new CheckBox[0]);
                if(checkBoxArray[0].isSelected()){
                    selectString += "Profession AS 'Field', ";
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
                if(checkBoxArray[8].isSelected()){
                    selectString += "ManagerID AS 'Rector ID', ";
                }
                if(checkBoxArray[9].isSelected()){
                    selectString += "Name AS 'Project Name', ";
                    if(!addedProjectTable){
                        joinString += " LEFT JOIN project ON profID=professorID";
                        addedProjectTable = true;
                    }
                }
                if(checkBoxArray[10].isSelected()){
                    selectString += "Information AS 'Project Information', ";
                    if(!addedProjectTable){
                        joinString += " LEFT JOIN project ON profID=professorID";
                        addedProjectTable = true;
                    }
                }
                if(checkBoxArray[11].isSelected()){
                    selectString += "Type, ";
                    if(!addedProjectTable){
                        joinString += " LEFT JOIN project ON profID=professorID";
                        addedProjectTable = true;
                    }
                }
                
                selectString = selectString.substring(0,selectString.length() -2);
                if(((CheckBox)rectorButton.getContent()).isSelected()){
                    whereString = whereString + " AND ManagerId IS NULL";
                }
                if(((CheckBox)ssnButton.getContent()).isSelected()){
                    if(!ssnTextField.getText().isEmpty()){
                        whereString = whereString.concat(" AND ssn = ?");
                        whereParametersList.add(Integer.parseInt(ssnTextField.getText()));
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
                if(((CheckBox)fieldButton.getContent()).isSelected()){
                    whereString = whereString.concat( " AND (");
                    ScrollPane scroll = (ScrollPane)fieldScrollCustomMenuItem.getContent();
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
                if(((CheckBox)projectButton.getContent()).isSelected()){
                    if(projectsToggleGroup.getToggles() != null){
                        int start = Integer.MIN_VALUE;
                        int end = Integer.MAX_VALUE;
                        if(((RadioButton)projectsToggleGroup.getSelectedToggle()).getText().equals("By Amount")){
                            if(!addedProjectTable){
                                joinString += " LEFT JOIN project ON profID = professorID";
                                addedProjectTable = true;
                            }
                            if(!projectStartTextField.getText().isEmpty() && !projectEndTextField.getText().isEmpty()){
                                whereString = whereString.concat(" AND ssn IN (SELECT ssn\n\tFROM employee\n\tJOIN professor ON ssn=profID\n\tLEFT JOIN project ON profID=professorID\n\tGROUP BY ssn\n\tHAVING COUNT(*) >= ? AND COUNT(*) <= ?)");
                                whereParametersList.add(Integer.parseInt(projectStartTextField.getText()));
                                whereParametersList.add(Integer.parseInt(projectEndTextField.getText()));
                                start = Integer.parseInt(projectStartTextField.getText());
                                end = Integer.parseInt(projectEndTextField.getText());
                            }
                            else if(!projectStartTextField.getText().isEmpty() && projectEndTextField.getText().isEmpty()){
                                whereString = whereString.concat(" AND ssn IN (SELECT ssn\n\tFROM employee\n\tJOIN professor ON ssn=profID\n\tLEFT JOIN project ON profID=professorID\n\tGROUP BY ssn\n\tHAVING COUNT(*) >= ?)");
                                whereParametersList.add(Integer.parseInt(projectStartTextField.getText()));
                            }
                            else if(projectStartTextField.getText().isEmpty() && !projectEndTextField.getText().isEmpty()){
                                whereString = whereString.concat(" AND ssn IN (SELECT ssn\n\tFROM employee\n\tJOIN professor ON ssn=profID\n\tLEFT JOIN project ON profID=professorID\n\tGROUP BY ssn\n\tHAVING COUNT(*) <= ?)");
                                whereParametersList.add(Integer.parseInt(projectEndTextField.getText()));
                            }
                            if(projectStartTextField.getText().isEmpty() && projectEndTextField.getText().isEmpty()){
                                showMissingAlert = true;
                            }
                            if(start > end){
                                invalidRangeFilters += "'Project by amount', ";
                                isCorrectOrder = false;
                            }

                        }
                        else{
                            if(!addedProjectTable){
                                joinString = joinString.concat(" LEFT JOIN project ON profID=professorID");
                                whereString = whereString.concat(" AND (");
                                addedProjectTable = true;
                            }
                            else{
                                whereString = whereString.concat(" AND (");
                            }
                            Boolean hasSelected = false;
                            Boolean isFirst = true;
                            VBox radioButtonVBox = (VBox)projectRadioButtons.getContent();
                            ScrollPane projectByNameScroll = (ScrollPane)radioButtonVBox.getChildren().get(1);
                            VBox containerBox = (VBox) projectByNameScroll.getContent();
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
                                        whereString = whereString.concat("name = ?");
                                        whereParametersList.add(((CheckBox)node).getText());
                                    }
                                }                                
                            } 
                            if(!hasSelected){
                                whereString = whereString.concat("1=1");
                                showMissingAlert = true;
                            }
                            whereString = whereString.concat(")");
                        }
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
                    
                    resultTableView = TableManager.CreateTableView(resultSet, "professor");
                    TableManager.setUpMouseReleased(resultTableView);
                    
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
    
    private void handleProjectRadioButtonPress(RadioButton button){
        filterButton.getItems().remove(projectRadioButtons);
        if(button.getText().equals("By Name")){
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
            scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
            scrollPane.setFocusTraversable(false);
            scrollPane.setPrefHeight(100);

            projectRadioButtons = new CustomMenuItem(new VBox(10,projectsByNameButton, scrollPane,projectsByAmountButton));
            projectRadioButtons.setHideOnClick(false);
            filterButton.getItems().add(filterButton.getItems().indexOf(projectButton)+1,projectRadioButtons);
        }
        else{
            HBox rangeContainer = new HBox();
            rangeContainer.getChildren().addAll(projectStartTextField,projectEndTextField);
            projectRadioButtons = new CustomMenuItem(new VBox(10,projectsByNameButton,projectsByAmountButton,rangeContainer));
            filterButton.getItems().add(filterButton.getItems().indexOf(projectButton)+1,projectRadioButtons);
        }
    }

    private void professorMenuSetup(){
        VBox base = new VBox(10);
        HBox titleBox = new HBox();
        HBox mainBox = new HBox();
        HBox backBox = new HBox();
        
        Text titleText = new Text("Professor");
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
        fieldButton = toCustomMenu(FILTER_BUTTON_TEXTS[7]);
        yearsWorkedButton = toCustomMenu(FILTER_BUTTON_TEXTS[8]);
        phoneButton = toCustomMenu(FILTER_BUTTON_TEXTS[9]);

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

        projectsByNameButton.setToggleGroup(projectsToggleGroup);
        projectsByNameButton.setOnAction(e -> handleProjectRadioButtonPress(projectsByNameButton));
        projectsByAmountButton.setToggleGroup(projectsToggleGroup);
        projectsByAmountButton.setOnAction(e -> handleProjectRadioButtonPress(projectsByAmountButton));
        projectRadioButtons = new CustomMenuItem(new VBox(10,projectsByNameButton,projectsByAmountButton));
        projectStartTextField = Page.createNumericTextField(3);
        projectStartTextField.setPrefWidth(80);
        projectEndTextField = Page.createNumericTextField(3);
        projectEndTextField.setPrefWidth(80);
        projectStartTextField.setPromptText("Min.");
        projectEndTextField.setPromptText("Max.");

        yearsStartTextField = Page.createNumericTextField(3);
        yearsEndTextField = Page.createNumericTextField(3);
        yearsStartTextField.setPromptText("Min.");
        yearsEndTextField.setPromptText("Max.");

        phoneTextField = Page.createNumericTextField(10);
        phoneTextField.setPromptText("Phone Number:");

        filterButton.getItems().addAll(rectorButton,ssnButton,emailButton,phoneButton,fieldButton,projectButton,sexFilterButton,ageButton,salaryButton,yearsWorkedButton);
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
        queryOptionsBox.getChildren().addAll(filterButton,selectFilterButton,firstNameField,lastNameField,searchButton);

        resultScrollPane = new ScrollPane();
        resultScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        resultScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
       // resultScrollPane.setPrefWidth(400);
        //resultScrollPane.setPrefHeight(300);
        resultScrollPane.setContent(resultTableView);
        //resultScrollPane.setStyle("-fx-background: rgba(255, 255, 255, 0.5);");
        resultScrollPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255, 0.5),CornerRadii.EMPTY,javafx.geometry.Insets.EMPTY)));
        leftBox.getChildren().addAll(queryOptionsBox,resultScrollPane);

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
                radioButtons = new CustomMenuItem(new VBox(10,maleButton,femaleButton));
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
            menu.getItems().remove(projectRadioButtons);
            if(checkBox.isSelected()){
                projectRadioButtons = new CustomMenuItem(new VBox(projectsByNameButton,projectsByAmountButton));
                projectRadioButtons.setHideOnClick(false);
                menu.getItems().add(menu.getItems().indexOf(projectButton)+1,projectRadioButtons);
                
                RadioButton selectedRadioButton = (RadioButton) projectsToggleGroup.getSelectedToggle();
                String selectedText = new String();
                if(selectedRadioButton != null){
                    menu.getItems().remove(projectRadioButtons);
                   selectedText = selectedRadioButton.getText();
                }
                if(selectedRadioButton != null && selectedText.equals("By Name")){
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

                    projectRadioButtons = new CustomMenuItem(new VBox(10,projectsByNameButton, scrollPane,projectsByAmountButton));
                    projectRadioButtons.setHideOnClick(false);
                    menu.getItems().add(menu.getItems().indexOf(projectButton)+1,projectRadioButtons);
                }
                else if(selectedRadioButton != null && selectedText.equals("By Amount")){
                    HBox rangeContainer = new HBox();
                    rangeContainer.getChildren().addAll(projectStartTextField,projectEndTextField);
                    projectRadioButtons = new CustomMenuItem(new VBox(10,projectsByNameButton,projectsByAmountButton,rangeContainer));
                    menu.getItems().add(menu.getItems().indexOf(projectButton)+1,projectRadioButtons);
                }
            }
        }
        else if(FILTER_BUTTON_TEXTS[7].equals(checkBox.getText())){
            menu.getItems().remove(fieldScrollCustomMenuItem);
            if(checkBox.isSelected()){
                VBox checkBoxContainer = new VBox();
                for (String field : professorFields) {
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

                fieldScrollCustomMenuItem = new CustomMenuItem(scrollPane);
                fieldScrollCustomMenuItem.setHideOnClick(false);
                menu.getItems().add(menu.getItems().indexOf(fieldButton)+1,fieldScrollCustomMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[8].equals(checkBox.getText())){
            menu.getItems().remove(yearsRangeTexFieldsMenuItem);
            if(checkBox.isSelected()){
                yearsStartTextField.setPrefWidth(80);
                yearsEndTextField.setPrefWidth(80);
                yearsRangeTexFieldsMenuItem = new CustomMenuItem(new HBox(yearsStartTextField,yearsEndTextField));
                menu.getItems().add(menu.getItems().indexOf(yearsWorkedButton) + 1, yearsRangeTexFieldsMenuItem);
            }
        }
        else if(FILTER_BUTTON_TEXTS[9].equals(checkBox.getText())){
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

    public static void main(String[] args){
        launch(args);
    }
}

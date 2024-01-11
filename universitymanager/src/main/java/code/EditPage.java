package code;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class EditPage extends Page {
    private String type;
    private String key;
    private String background = "uniPage.png";

    // Components
    List<TextField> textComponents = null;
    List<VBox> radioComponents = null;
    List<DatePicker> dateComponents = null;

    private Map<String, String> entry = null;
    private Map<String, String> updatedEntry = null;
    private String selectQuery = "";
    private List<String> secondEntry = null;
    
    private TableView<ObservableList<String>> table = null;
    private ScrollPane pane = null;

    // SQL Tables
    private final String[] EMPLOYEE = {"SSN", "FirstName", "LastName", "Sex", "Phone", "Email", "JobStartingDate", "Birthday", "Address", "Salary"};
    private final String[] PROFESSOR = {"ProfId", "ManagerId", "Profession"};
    private final String[] PROJECT = {"ProfessorId", "Name", "Field", "Type", "Information"};
    private final String[] AUXILIARY_STAFF = {"EmployeeID", "profession"};
    private final String[] STUDENT = {"FirstName", "LastName", "FatherName", "Sex", "Semester", "Email", "Phone", "Birthday", "EntryDate", "Address"};
    private final String[] COURSE = {"CourseId", "Name", "Semester"};


    public EditPage(String type, String key) {
        this.type = type;
        this.key = key;

        if (type == "professor") {
            background = "professorPage.png";
        }
        else if (type == "auxiliary") {
            background = "auxiliaryStaffPage.png";
        }
        else if (type == "student") {
            background = "studentPage.png";
        }
        else if (type == "course") {
            background = "coursePage.png";
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Page.primaryStage = primaryStage;
        loadLogo();
        loadBackground(background);
        createEditPage();
        createScene();
    }

    private void createEditPage() {
        if (type == "professor") {
            background = "professorPage.png";
            selectQuery = "SELECT * FROM EMPLOYEE e JOIN PROFESSOR p ON e.SSN = p.ProfId WHERE p.ProfId = ?";
            createProfessor();
        }
        else if (type == "auxiliary") {
            background = "auxiliaryStaffPage.png";
            selectQuery = "SELECT * FROM EMPLOYEE e JOIN AUXILIARY_STAFF a ON e.SSN = a.EmployeeId WHERE a.EmployeeId = ?";
            createAuxiliary();
        }
        else if (type == "student") {
            background = "studentPage.png";
            selectQuery = "SELECT * FROM STUDENT WHERE StudentId = ?";
            createStudent();
        }
        else if (type == "course") {
            background = "coursePage.png";
            selectQuery = "SELECT * FROM COURSE WHERE CourceId = ?";
            createCourse();
        }
    }

    private void createProfessor() {
        // Fetch the Professor from the database.
        fetchEntry();

        // Setup the boxes.
        VBox base = new VBox();
        HBox mainBox = new HBox();
        VBox bottomBox = new VBox();

        // Setup the components List
        textComponents = new ArrayList<>();
        radioComponents = new ArrayList<>();
        dateComponents = new ArrayList<>();

        // Set up the title
        Label professorLabel = new Label("Professor");
        professorLabel.setFont(Font.font(30));
        professorLabel.setStyle("-fx-font-weight: bold;");

        // Set up the features
        // First Name
        TextField fname = makeTextField(50);
        fname.setPromptText("First Name");
        fname.setText(entry.get("FirstName"));

        // Last Name
        TextField lname = makeTextField(50);
        lname.setPromptText("Last Name");
        lname.setText(entry.get("LastName"));

        // SSN
        TextField ssn = createNumericTextField(10);
        ssn.setPromptText("Social Security Number");
        ssn.setText(entry.get("SSN"));
        textComponents.add(ssn);
        textComponents.add(fname);
        textComponents.add(lname);
        
        // Phone
        TextField phone = createNumericTextField(10);
        phone.setPromptText("Phone Number");
        phone.setText(entry.get("Phone"));
        textComponents.add(phone);

        // Sex Radio Button
        Label gender = new Label("Sex");
        gender.setFont(Font.font(15));
        gender.setStyle("-fx-font-weight: bold;");
        RadioButton maleRadioButton = new RadioButton("Male");
        RadioButton femaleRadioButton = new RadioButton("Female");

        ToggleGroup toggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(toggleGroup);
        femaleRadioButton.setToggleGroup(toggleGroup);

        VBox sex = new VBox(gender, maleRadioButton, femaleRadioButton);
        sex.setPadding(new Insets(5));
        
        if (entry.get("Sex") != null) {
            if (entry.get("Sex").equals("Male")) {
                maleRadioButton.setSelected(true);
            } else if (entry.get("Sex").equals("Female")) {
                femaleRadioButton.setSelected(true);
            }
        }
        radioComponents.add(sex);

        // Address
        TextField address = makeTextField(150);
        address.setPromptText("Address");
        address.setText(entry.get("Address"));
        textComponents.add(address);

        // Salary
        TextField salary = createNumericTextField(10);
        salary.setPromptText("Salary");
        salary.setText(entry.get("Salary"));
        textComponents.add(salary);

        // Email
        TextField email = makeTextField(150);
        email.setPromptText("Email Address");
        email.setText(entry.get("Email"));
        textComponents.add(email);
        
        // Birthday
        DatePicker birthday = new DatePicker();
        birthday.setPromptText("Birthday");
        if (entry.get("Birthday") != null) {
            birthday.setValue(LocalDate.parse(entry.get("Birthday")));
        }
        dateComponents.add(birthday);

        // Job Starting Date
        DatePicker jobStartingDate = new DatePicker();
        jobStartingDate.setPromptText("Job Starting Date");
        if (entry.get("JobStartingDate") != null) {
            jobStartingDate.setValue(LocalDate.parse(entry.get("JobStartingDate")));
        }
        dateComponents.add(jobStartingDate);

        // Profession Radio Buttons setup
        Label profLabel = new Label("Profession");
        profLabel.setFont(Font.font(15));
        profLabel.setStyle("-fx-font-weight: bold;");
        RadioButton radio1 = new RadioButton("Computer Hardware and Architecture");
        RadioButton radio2 = new RadioButton("Signals and Communications");
        RadioButton radio3 = new RadioButton("Applications and Foundations of Computer Science");
        RadioButton radio4 = new RadioButton("Energy");
        RadioButton radio5 = new RadioButton("Software and Information System Engineering");

        ToggleGroup professionGroup = new ToggleGroup();
        radio1.setToggleGroup(professionGroup);
        radio2.setToggleGroup(professionGroup);
        radio3.setToggleGroup(professionGroup);
        radio4.setToggleGroup(professionGroup);
        radio5.setToggleGroup(professionGroup);

        VBox profession = new VBox(profLabel, radio1, radio2, radio3, radio4, radio5);
        profession.setPadding(new Insets(5));

        if (entry.get("Profession") != null) {
            switch (entry.get("Profession")) {
                case "Computer Hardware and Architecture":
                    radio1.setSelected(true);
                    break;
                case "Signals and Communications":
                    radio2.setSelected(true);
                    break;
                case "Applications and Foundations of Computer Science":
                    radio3.setSelected(true);
                    break;
                case "Energy":
                    radio4.setSelected(true);
                    break;
                case "Software and Information System Engineering":
                    radio5.setSelected(true);
                    break;
            }
        }
        radioComponents.add(profession);

        HBox titleBox = new HBox(professorLabel);
        VBox professorSide = new VBox(titleBox, textComponents.get(1), textComponents.get(2), textComponents.get(0), textComponents.get(3), radioComponents.get(0), textComponents.get(4), textComponents.get(5), textComponents.get(6), dateComponents.get(0), dateComponents.get(1), radioComponents.get(1));
        professorSide.setAlignment(Pos.CENTER_LEFT);
        professorSide.setSpacing(5);
        professorSide.setPadding(new Insets(10, 430, 0, 0));

        // Project Side Setup
        VBox projectSide = makeProjectSide();

        // Setup of Save Entry Button
        Button saveButton = new Button("Save");
        addButtonTransition(saveButton, 100, 50);
        saveButton.setOnAction(event -> {
            updateProfessorEntry();
            ProfessorMenu menu = new ProfessorMenu();
            menu.start(primaryStage);            
        });

        // Setup the back to main menu button
        Button backButton = Page.createBackButton();
        bottomBox.getChildren().addAll(saveButton, backButton);
        bottomBox.setSpacing(5);
        bottomBox.setAlignment(Pos.CENTER);

        // Setup the boxes
        mainBox.getChildren().addAll(professorSide, projectSide);
        mainBox.setAlignment(Pos.CENTER);

        base.getChildren().addAll(mainBox, bottomBox);
        base.setAlignment(Pos.CENTER);
        root.getChildren().addAll(base);
    }

    // Makes a VBox that contains the project add components.
    private VBox makeProjectSide() {
        // Make the label
        Label projectLabel = new Label("Projects");
        projectLabel.setFont(Font.font(30));
        projectLabel.setStyle("-fx-font-weight: bold;");

        // Name of Project
        TextField name = makeTextField(50);
        name.setPromptText("Name of  the Project");
        textComponents.add(name);

        // Field Radio Buttons setup
        Label fieldLabel = new Label("Field");
        fieldLabel.setFont(Font.font(15));
        fieldLabel.setStyle("-fx-font-weight: bold;");
        RadioButton radio1 = new RadioButton("Computer Hardware and Architecture");
        RadioButton radio2 = new RadioButton("Signals and Communications");
        RadioButton radio3 = new RadioButton("Applications and Foundations of Computer Science");
        RadioButton radio4 = new RadioButton("Energy");
        RadioButton radio5 = new RadioButton("Software and Information System Engineering");

        ToggleGroup fieldGroup = new ToggleGroup();
        radio1.setToggleGroup(fieldGroup);
        radio2.setToggleGroup(fieldGroup);
        radio3.setToggleGroup(fieldGroup);
        radio4.setToggleGroup(fieldGroup);
        radio5.setToggleGroup(fieldGroup);

        VBox field = new VBox(fieldLabel, radio1, radio2, radio3, radio4, radio5);
        field.setPadding(new Insets(5));
        radioComponents.add(field);      

        // Type Radio Buttons setup
        Label typeLabel = new Label("Type");
        typeLabel.setFont(Font.font(15));
        typeLabel.setStyle("-fx-font-weight: bold;");
        RadioButton r1 = new RadioButton("Research");
        RadioButton r2 = new RadioButton("Development");
        RadioButton r3 = new RadioButton("Education");

        ToggleGroup typeGroup = new ToggleGroup();
        r1.setToggleGroup(typeGroup);
        r2.setToggleGroup(typeGroup);
        r3.setToggleGroup(typeGroup);

        VBox type = new VBox(typeLabel, r1, r2, r3);
        type.setPadding(new Insets(5));
        radioComponents.add(type);

        // Information TextField
        TextField info = makeTextField(500);
        info.setPromptText("Information");
        textComponents.add(info);    

        // Make the add project button        
        Image image = new Image("file:" + getPath() + "add_project.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        Button addProjectButton = new Button();
        addProjectButton.setGraphic(imageView);
        addButtonTransition(addProjectButton, 30, 30);
        addProjectButton.setStyle("-fx-background-color: rgba(255,255,255,0);");
        addProjectButton.setOnAction(event -> {

            // Save the data into a list
            secondEntry = new ArrayList<>();
            secondEntry.add(name.getText());

            // Save field to the list
            try {
                RadioButton selectedField = (RadioButton) fieldGroup.getSelectedToggle();
                secondEntry.add(selectedField.getText());
            }
            catch (NullPointerException e) {
                secondEntry.add(null);
            }

            // Save type to the list
            try {
                RadioButton selectedType = (RadioButton) typeGroup.getSelectedToggle();
                secondEntry.add(selectedType.getText());
            }
            catch (NullPointerException e) {
                secondEntry.add(null);
            }

            // Save info
            secondEntry.add(info.getText());

            handleButtonPress("addProject");
        });

        // Make the edit project button        
        image = new Image("file:" + getPath() + "edit_project.png");
        imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        Button editProjectButton = new Button();
        editProjectButton.setGraphic(imageView);
        addButtonTransition(editProjectButton, 30, 30);
        editProjectButton.setStyle("-fx-background-color: rgba(255,255,255,0);");
        editProjectButton.setOnAction(event -> {
            String projectName = TableManager.selectedId;
            if (projectName == null) {
                return;
            }
            
            String query = "SELECT Name, Field, Type, Information FROM PROJECT WHERE ProfessorId = ? AND Name = ?";
    
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, entry.get(EMPLOYEE[0]));
                preparedStatement.setString(2, projectName);
        
                // Execute the query and get the result set
                try {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    textComponents.get(textComponents.size() - 2).setText(resultSet.getString(PROJECT[1]));
                    textComponents.get(textComponents.size() - 1).setText(resultSet.getString(PROJECT[4]));

                    // Field Radio Button
                    String fieldString = resultSet.getString(PROJECT[2]);
                    VBox vbox = radioComponents.get(radioComponents.size() - 2);
                    for (Node childNode : vbox.getChildren()) {
                        if (childNode instanceof RadioButton) {
                            RadioButton radioButton = (RadioButton) childNode;
                            if (radioButton.getText().equals(fieldString)) {
                                radioButton.setSelected(true);
                            }
                        }                    
                    }

                    // Type Radio Button
                    String typeString = resultSet.getString(PROJECT[3]);
                    vbox = radioComponents.get(radioComponents.size() - 1);
                    for (Node childNode : vbox.getChildren()) {
                        if (childNode instanceof RadioButton) {
                            RadioButton radioButton = (RadioButton) childNode;
                            if (radioButton.getText().equals(typeString)) {
                                radioButton.setSelected(true);
                            }
                        }                    
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }

            // Refreash Table.
            table = createProjectTable();
            table.setPrefHeight(150);
            table.setPrefWidth(300);
            pane.setContent(table);
            pane.setFitToWidth(true);
            pane.setVbarPolicy(ScrollBarPolicy.NEVER);
        });

        // Create the project board
        VBox projectBox;
        if (entry != null) {
            table = createProjectTable();
            pane = new ScrollPane(table);
        }
        else {
            table = null;
            pane = new ScrollPane();
        }

        // Make the Delete project button        
        Image deleteImage = new Image("file:" + getPath() + "delete_project.png");
        ImageView deleteImageView = new ImageView(deleteImage);
        deleteImageView.setFitWidth(30);
        deleteImageView.setFitHeight(30);
        Button deleteProjectButton = new Button();
        deleteProjectButton.setGraphic(deleteImageView);
        addButtonTransition(deleteProjectButton, 30, 30);
        deleteProjectButton.setStyle("-fx-background-color: rgba(255,255,255,0);");

        deleteProjectButton.setOnAction(event -> {
            String projectName = TableManager.selectedId;

            if (projectName == null) {
                return;
            }

            String deleteQuery = "DELETE FROM PROJECT WHERE ProfessorId = ? AND Name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, entry.get("ProfId"));
                preparedStatement.setString(2, projectName);

                if (preparedStatement.executeUpdate() > 0) {
                    showAlert(AlertType.INFORMATION, "Success", "Operation Successful", "We have successfully deleted the Project " + projectName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Refreash Table.
            table = createProjectTable();
            table.setPrefHeight(150);
            table.setPrefWidth(300);
            pane.setContent(table);
            pane.setFitToWidth(true);
            pane.setVbarPolicy(ScrollBarPolicy.NEVER);
        });
        
        HBox topBox = new HBox(projectLabel, addProjectButton, editProjectButton, deleteProjectButton);
        topBox.setSpacing(30);
        
        projectBox = new VBox(topBox, name, field, type, info, pane);
        projectBox.setSpacing(5);
        projectBox.setPadding(new Insets(20, 10, 0, -300));

        return projectBox;
    }

    private void handleButtonPress(String button) {
        if (button == "addProject") {
            if (checkForMissingValues(secondEntry)) {
                addProject();
            }
            else {
                showAlert(AlertType.ERROR, "Missing Values", "Some values are missing", "Please fill all the cells");
            }
            return;
        }
        else if (button == "editProject") {

        }
    }

    // Insert a new entry in the Project Table.
    private void addProject() {
        // Insert to Project table
        String insertProfessorQuery = "INSERT INTO PROJECT (ProfessorId, Name, Field, Type, Information) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProfessorQuery)) {
            preparedStatement.setString(1, entry.get(EMPLOYEE[0]));
            preparedStatement.setString(2, secondEntry.get(0));
            preparedStatement.setString(3, secondEntry.get(1));
            preparedStatement.setString(4, secondEntry.get(2));
            preparedStatement.setString(5, secondEntry.get(3));

            if (preparedStatement.executeUpdate() > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Operation Successful", "Project " + secondEntry.get(0) + " added to professor " + entry.get(EMPLOYEE[1]) + " " + entry.get(EMPLOYEE[2])+ ".");

                // Refreash Table.
                table = createProjectTable();
                table.setPrefHeight(150);
                table.setPrefWidth(300);
                pane.setContent(table);
                pane.setFitToWidth(true);
                pane.setVbarPolicy(ScrollBarPolicy.NEVER);

                // Clear entry and fields.
                secondEntry = null;
                clearFields("project");
            }
            else {
                showAlert(AlertType.ERROR, "Problem", "Failed to insert Project.", "An error occurred. Please check your input.");
            }
        } catch (SQLException ex) {
            showAlert(AlertType.ERROR, "Duplicate Project Name", secondEntry.get(0) + " already exists", "You have added a project with the same name!");
            clearFields("project");
        }
    }

    private TableView<ObservableList<String>> createProjectTable() {
        String query = "SELECT Name, Field, Type, Information FROM PROJECT WHERE ProfessorId = ?";
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entry.get(EMPLOYEE[0]));
    
            // Execute the query and get the result set
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                // Create the TableView using the result set
                table = TableManager.CreateTableView(resultSet, "project");
    
                // Set up any additional configurations for the table
                TableManager.setUpMouseReleased(table);
                table.setFixedCellSize(Region.USE_COMPUTED_SIZE);

                table.setPrefHeight(150);
                table.setPrefWidth(300);
                resultSet.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return table;
    }

    private void createAuxiliary() {
        // Fetch the Auxiliary Staff from the database.
        fetchEntry();

        // Setup the boxes.
        VBox base = new VBox();
        VBox mainBox = new VBox();
        VBox bottomBox = new VBox();

        // Setup the components List
        textComponents = new ArrayList<>();
        radioComponents = new ArrayList<>();
        dateComponents = new ArrayList<>();

        // Set up the title
        Label titleLabel = new Label("Auxiliary Staff");
        titleLabel.setFont(Font.font(30));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // Set up the features
        // First Name
        TextField fname = makeTextField(50);
        fname.setPromptText("First Name");
        fname.setText(entry.get("FirstName"));

        // Last Name
        TextField lname = makeTextField(50);
        lname.setPromptText("Last Name");
        lname.setText(entry.get("LastName"));

        // SSN
        TextField ssn = createNumericTextField(10);
        ssn.setPromptText("Social Security Number");
        ssn.setText(entry.get("SSN"));
        textComponents.add(ssn);
        textComponents.add(fname);
        textComponents.add(lname);
        
        // Phone
        TextField phone = createNumericTextField(10);
        phone.setPromptText("Phone Number");
        phone.setText(entry.get("Phone"));
        textComponents.add(phone);

        // Sex Radio Button
        Label gender = new Label("Sex");
        gender.setFont(Font.font(15));
        gender.setStyle("-fx-font-weight: bold;");
        RadioButton maleRadioButton = new RadioButton("Male");
        RadioButton femaleRadioButton = new RadioButton("Female");

        ToggleGroup toggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(toggleGroup);
        femaleRadioButton.setToggleGroup(toggleGroup);

        VBox sex = new VBox(gender, maleRadioButton, femaleRadioButton);
        sex.setPadding(new Insets(5));
        
        if (entry.get("Sex") != null) {
            if (entry.get("Sex").equals("Male")) {
                maleRadioButton.setSelected(true);
            } else if (entry.get("Sex").equals("Female")) {
                femaleRadioButton.setSelected(true);
            }
        }
        radioComponents.add(sex);

        // Address
        TextField address = makeTextField(150);
        address.setPromptText("Address");
        address.setText(entry.get("Address"));
        textComponents.add(address);

        // Salary
        TextField salary = createNumericTextField(10);
        salary.setPromptText("Salary");
        salary.setText(entry.get("Salary"));
        textComponents.add(salary);

        // Email
        TextField email = makeTextField(150);
        email.setPromptText("Email Address");
        email.setText(entry.get("Email"));
        textComponents.add(email);
        
        // Birthday
        DatePicker birthday = new DatePicker();
        birthday.setPromptText("Birthday");
        if (entry.get("Birthday") != null) {
            birthday.setValue(LocalDate.parse(entry.get("Birthday")));
        }
        dateComponents.add(birthday);

        // Job Starting Date
        DatePicker jobStartingDate = new DatePicker();
        jobStartingDate.setPromptText("Job Starting Date");
        if (entry.get("JobStartingDate") != null) {
            jobStartingDate.setValue(LocalDate.parse(entry.get("JobStartingDate")));
        }
        dateComponents.add(jobStartingDate);

        // Profession Radio Buttons setup
        Label profLabel = new Label("Profession");
        profLabel.setFont(Font.font(15));
        profLabel.setStyle("-fx-font-weight: bold;");
        RadioButton radio1 = new RadioButton("Secretariat");
        RadioButton radio2 = new RadioButton("Security Guard");
        RadioButton radio3 = new RadioButton("Maintenance");
        RadioButton radio4 = new RadioButton("Cleaning Personnel");
        RadioButton radio5 = new RadioButton("Accountant");

        ToggleGroup professionGroup = new ToggleGroup();
        radio1.setToggleGroup(professionGroup);
        radio2.setToggleGroup(professionGroup);
        radio3.setToggleGroup(professionGroup);
        radio4.setToggleGroup(professionGroup);
        radio5.setToggleGroup(professionGroup);

        VBox profession = new VBox(profLabel, radio1, radio2, radio3, radio4, radio5);
        profession.setPadding(new Insets(5));

        if (entry.get("Profession") != null) {
            switch (entry.get("Profession")) {
                case "Secretariat":
                    radio1.setSelected(true);
                    break;
                case "Security Guard":
                    radio2.setSelected(true);
                    break;
                case "Maintenance":
                    radio3.setSelected(true);
                    break;
                case "Cleaning Personnel":
                    radio4.setSelected(true);
                    break;
                case "Accountant":
                    radio5.setSelected(true);
                    break;
            }
        }
        radioComponents.add(profession);

        // Setup of Save Entry Button
        Button saveButton = new Button("Save");
        addButtonTransition(saveButton, 100, 50);
        saveButton.setOnAction(event -> {
            updateAuxiliaryEntry();
            AuxiliaryMenu menu = new AuxiliaryMenu();
            menu.start(primaryStage);            
        });

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER_LEFT);        
        titleBox.setPadding(new Insets(20, 0, 0, 30));

        // Setup the back to main menu button
        Button backButton = Page.createBackButton();
        bottomBox.getChildren().addAll(saveButton, backButton);
        bottomBox.setSpacing(5);
        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        

        // Limit the TextField size to 100 pixels
        for (TextField field: textComponents) {
            field.setMaxWidth(250);
        }

        mainBox.getChildren().addAll(textComponents.get(0), textComponents.get(1), textComponents.get(2), textComponents.get(3), radioComponents.get(0), textComponents.get(4), textComponents.get(5), textComponents.get(6), dateComponents.get(0), dateComponents.get(1), radioComponents.get(1));

        mainBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.setPadding(new Insets(0, 0, 0, 30));
        mainBox.setSpacing(5);

        base.getChildren().addAll(titleBox, mainBox, bottomBox);
        root.getChildren().addAll(base);
    }

    private void createStudent() {
    }

    private void createCourse() {
    }

    private void updateProfessorEntry() {
        if (updateEmployee()){
            if (updateProfessor()) {
                showAlert(AlertType.INFORMATION, "Success", "The Professor has changed", "You have successfully update " + entry.get(EMPLOYEE[1]) + " " + entry.get(EMPLOYEE[2]));
                return;
            }            
        }
        showAlert(AlertType.ERROR, "", "", "");        
    }

    private void updateAuxiliaryEntry() {
        if (updateEmployee()){
            if (updateAuxiliary()) {
                showAlert(AlertType.INFORMATION, "Success", "The Auxiliary Staff has changed", "You have successfully update " + entry.get(EMPLOYEE[1]) + " " + entry.get(EMPLOYEE[2]));
                return;
            }            
        }
        showAlert(AlertType.ERROR, "", "", "");        
    }

    private boolean updateEmployee() {
        updatedEntry = new HashMap<>();
        updatedEntry.put(EMPLOYEE[0], textComponents.get(0).getText());
        updatedEntry.put(EMPLOYEE[1], textComponents.get(1).getText());
        updatedEntry.put(EMPLOYEE[2], textComponents.get(2).getText());
        updatedEntry.put(EMPLOYEE[4], textComponents.get(3).getText());
        updatedEntry.put(EMPLOYEE[5], textComponents.get(4).getText());
        updatedEntry.put(EMPLOYEE[8], textComponents.get(5).getText());
        updatedEntry.put(EMPLOYEE[9], textComponents.get(6).getText());

        // Update Sex
        VBox vbox = radioComponents.get(0); // Sex Radio Button
        for (Node childNode : vbox.getChildren()) {
            if (childNode instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) childNode;
                if (radioButton.isSelected()) {
                    updatedEntry.put(EMPLOYEE[3], radioButton.getText());
                }
            }
        }

        // Update Dates
        try {
            LocalDate selectedDate1 = dateComponents.get(0).getValue();
            LocalDate selectedDate2 = dateComponents.get(1).getValue();
        
            // Store the selected dates in updatedEntry
            updatedEntry.put(EMPLOYEE[6], selectedDate1.toString());
            updatedEntry.put(EMPLOYEE[7], selectedDate2.toString());
            
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Date Pickers are incorrect", "Dates are not fill");
        }

        // Make the query and update the EMPLOYEE Table
        String updateQuery = makeUpdateQuery(EMPLOYEE);
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            for (int i = 0; i < EMPLOYEE.length; i++) {
                preparedStatement.setString(i+1, updatedEntry.get(EMPLOYEE[i]));
            }
            preparedStatement.setString(11, entry.get(EMPLOYEE[0]));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean updateProfessor() {
        // Find who is the manager.
        String manager = "";
        String whoIsManager = "SELECT ProfId FROM PROFESSOR WHERE ManagerId IS NULL";        
        try (ResultSet result = connection.createStatement().executeQuery(whoIsManager)){
            result.next();
            manager = result.getString("ProfId");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update the updateEntry.
        updatedEntry.put(PROFESSOR[0], textComponents.get(0).getText());
        updatedEntry.put(PROFESSOR[1], manager);

        // Update Profession
        VBox vbox = radioComponents.get(1); // Profession Radio Button
        for (Node childNode : vbox.getChildren()) {
            if (childNode instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) childNode;
                if (radioButton.isSelected()) {
                    updatedEntry.put(PROFESSOR[2], radioButton.getText());
                }
            }
        }

        // Make the query and update the PROFESSOR Table
        String updateQuery = makeUpdateQuery(PROFESSOR);
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            for (int i = 0; i < PROFESSOR.length; i++) {
                preparedStatement.setString(i+1, updatedEntry.get(PROFESSOR[i]));
            }
            preparedStatement.setString(4, updatedEntry.get(EMPLOYEE[0]));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean updateAuxiliary() {
        // Update the updateEntry.
        updatedEntry.put(AUXILIARY_STAFF[0], textComponents.get(0).getText());

        // Update Profession
        VBox vbox = radioComponents.get(1); // Profession Radio Button
        for (Node childNode : vbox.getChildren()) {
            if (childNode instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) childNode;
                if (radioButton.isSelected()) {
                    updatedEntry.put(AUXILIARY_STAFF[1], radioButton.getText());
                }
            }
        }

        // Make the query and update the AUXILIARY_STAFF Table
        String updateQuery = makeUpdateQuery(AUXILIARY_STAFF);
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, updatedEntry.get(AUXILIARY_STAFF[1]));            
            preparedStatement.setString(2, updatedEntry.get(EMPLOYEE[0]));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private void fetchEntry() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, key);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Process the result set if needed
                while (resultSet.next()) {
                    // Retrieve data from the result set
                    entry = new HashMap<>();
                    
                    // Get the column names from ResultSetMetaData
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        String value = resultSet.getString(i);
                        entry.put(columnName, value);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String makeUpdateQuery(String[] table) {
        StringBuilder updateQuery = new StringBuilder("UPDATE ");
        if (table == EMPLOYEE) {
            updateQuery.append("EMPLOYEE SET ");
        }
        else if (table == PROFESSOR) {
            updateQuery.append("PROFESSOR SET ");
        }
        else if (table == AUXILIARY_STAFF) {
            updateQuery.append("AUXILIARY_STAFF SET ");
        }
        else if (table == STUDENT) {
            updateQuery.append("STUDENT SET ");
        }
        else if (table == COURSE) {
            updateQuery.append("COURSE SET ");
        }

        for (String feature : table) {
            updateQuery.append(feature);
            updateQuery.append("=?, ");
        }
        updateQuery.deleteCharAt(updateQuery.length() - 2);
        updateQuery.append("WHERE ");
        updateQuery.append(table[0]);
        updateQuery.append(" =?");

        System.out.println(updateQuery);
        return updateQuery.toString();
    }

    // Checks and controls the input that the user enters.
    private boolean check() {
        if (!checkForMissingValues(entry)) {
            showAlert(AlertType.ERROR, "Missing Values", "Some values are missing", "Please fill all the cells");
            return false;
        }
        else if (!checkForDuplicate()) {
            showAlert(AlertType.ERROR, "Duplicate SSN", "SSN already exists", "An employee with the same SSN already exists.");
            return false;
        }
        return true;
    }

    // Check for missing values in a Map.
    private boolean checkForMissingValues(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String e = entry.getValue();
            if (e == null || e == "" || onlySpace(e)) {
                return false;
            }
        }
        return true;
    }

    // Check for missing values in a List.
    private boolean checkForMissingValues(List<String> list) {
        for (String e : list) {
            if (e == null || e == "" || onlySpace(e)) {
                return false;
            }
        }
        return true;
    }

    // Check for duplicates.
    private boolean checkForDuplicate() {
        String checkDuplicateQuery = "";
        String[] table = EMPLOYEE;
        if (type == "professor" || type == "axiliary") {
            checkDuplicateQuery = "SELECT COUNT(*) FROM EMPLOYEE WHERE SSN = ?";
            table = EMPLOYEE;
        }
        else if (type == "student") {
            checkDuplicateQuery = "SELECT COUNT(*) FROM STUDENT WHERE StudentId = ?";
            table = STUDENT;
        }
        else if (type == "course") {
            checkDuplicateQuery = "SELECT COUNT(*) FROM COURSE WHERE CourseId = ?";
            table = COURSE;
        }

        try (PreparedStatement checkDuplicateStatement = connection.prepareStatement(checkDuplicateQuery)) {
            checkDuplicateStatement.setString(1, entry.get(table[0]));

            try (ResultSet resultSet = checkDuplicateStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);

                // SSN already exists
                if (count > 0) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    // Clear all the fields.
    private void clearFields(String selection) {
        if (selection == "project" || selection == "all") {
            int size = textComponents.size();
            for (int i = size - 2; i < size; i++) {
                TextField textField = textComponents.get(i);
                textField.clear();
            }

            // Clear the radio Buttons from the Project
            size = radioComponents.size();
            for (int i = size - 2; i < size; i++) {
                VBox vbox = radioComponents.get(i);
                for (Node childNode : vbox.getChildren()) {
                    if (childNode instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) childNode;
                        radioButton.setSelected(false);
                    }
                }
            }
        }

        if (selection == "professor" || selection == "all") {

            for (TextField text : textComponents) {
                text.clear();
            }
            
            for (DatePicker date : dateComponents) {
                date.setValue(null);
            }

            for (VBox vbox : radioComponents) {
                for (Node childNode : vbox.getChildren()) {
                    if (childNode instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) childNode;
                        radioButton.setSelected(false);
                    }
                }
            }
        }

        if (selection == "course") {
            for (TextField text : textComponents) {
                text.clear();
            }
        }      
    }
    
    // Checks if the input is only spaces or blank.
    private boolean onlySpace(String e) {
        for (int i = 0; i < e.length(); i++) {
            if (e.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }
}
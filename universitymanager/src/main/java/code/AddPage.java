package code;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AddPage extends Page {
    private String type;
    private String background;

    // Components
    List<TextField> textComponents = null;
    List<VBox> radioComponents = null;
    List<DatePicker> dateComponents = null;

    private List<String> entry = null;
    private List<String> secondEntry = null;
    private List<String> projectHistoryList = null;
    private Boolean professorStored = false;

    private TableView<ObservableList<String>> table = null;
    private ScrollPane pane = null;
    
    public AddPage(String type) {
        this.type = type;
        switch (type) {
            case "professor":
                background = "professorPage.png";                
                break;
            case "auxiliary":
                background = "auxiliaryStaffPage.png";
                break;
            case "student":
                background = "studentPage.png";
                break;
            case "book":
                background = "bookPage.png";
                break;
            case "course":
                background = "coursePage.png";      
            default:
                break;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Page.primaryStage = primaryStage;
        loadLogo();
        loadBackground(background);
        createAddPage();
        createScene();
    }

    // Creates the Add Page accordingly to the type.
    private void createAddPage() {
         switch (type) {
            case "professor":
                createAddPageProfessor();
                break;
            case "auxiliary":
                createAddPageAuxiliary();
                break;
            case "student":
                createAddPageStudent();
                break;
            case "course":
                createAddPageCourse();                     
            default:
                break;
         }
    }

    // Creates a Professor Add Page.
    private void createAddPageProfessor() {
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
        TextField fname = makeTextField(50);
        fname.setPromptText("First Name");
        textComponents.add(fname);

        TextField lname = makeTextField(50);
        lname.setPromptText("Last Name");
        textComponents.add(lname);

        TextField ssn = createNumericTextField(10);
        ssn.setPromptText("Social Security Number");
        textComponents.add(ssn);
        
        TextField phone = createNumericTextField(10);
        phone.setPromptText("Phone Number");
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
        radioComponents.add(sex);

        TextField address = makeTextField(150);
        address.setPromptText("Address");
        textComponents.add(address);

        TextField salary = createNumericTextField(10);
        salary.setPromptText("Salary");
        textComponents.add(salary);

        TextField email = makeTextField(150);
        email.setPromptText("Email Address");
        textComponents.add(email);
        
        DatePicker birthday = new DatePicker();
        birthday.setPromptText("Birthday");
        dateComponents.add(birthday);

        DatePicker jobStartingDate = new DatePicker();
        jobStartingDate.setPromptText("Job Starting Date");
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
        radioComponents.add(profession);

        // Setup the add professor button
        Image image = new Image("file:" + getPath() + "add_user.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        Button addButton = new Button();
        addButton.setGraphic(imageView);

        addButtonTransition(addButton, 30, 30);

        addButton.setOnAction(event -> {
            entry = new ArrayList<>();
            
            entry.add(ssn.getText());
            entry.add(fname.getText());
            entry.add(lname.getText());

            try {
                RadioButton selectedGender = (RadioButton) toggleGroup.getSelectedToggle();
                entry.add(selectedGender.getText());
            }
            catch (NullPointerException e) {
                entry.add(null);
            }

            entry.add(phone.getText());
            entry.add(email.getText());

            LocalDate jobStartingDateValue  = jobStartingDate.getValue();
            entry.add(jobStartingDateValue  == null ? "" : jobStartingDateValue .toString());

            LocalDate birthdayDate = birthday.getValue();
            entry.add(birthdayDate == null ? "" : birthdayDate.toString());

            entry.add(address.getText());
            entry.add(salary.getText());

            try {
                RadioButton selectedProfession = (RadioButton) professionGroup.getSelectedToggle();
                entry.add(selectedProfession.getText());
            }
            catch (NullPointerException e) {
                entry.add(null);
            }
            handleButtonPress("professor");
        });

        HBox titleBox = new HBox(professorLabel, addButton);
        titleBox.setSpacing(30);

        VBox professorSide = new VBox(titleBox, textComponents.get(0), textComponents.get(1), textComponents.get(2), textComponents.get(3), radioComponents.get(0), textComponents.get(4), textComponents.get(5), textComponents.get(6), dateComponents.get(0), dateComponents.get(1), radioComponents.get(1));
        professorSide.setAlignment(Pos.CENTER_LEFT);
        professorSide.setSpacing(5);
        professorSide.setPadding(new Insets(10, 430, 0, 0));

        // Project Side Setup
        VBox projectSide = makeProjectSide();

        // Setup of New Entry Button
        Button newButton = new Button("New");
        addButtonTransition(newButton, 100, 50);

        newButton.setOnAction(event -> {
            clearFields("all");
            entry = null;
            secondEntry = null;
            professorStored = false;
            projectHistoryList = null;
            lock(false);
        });

        // Setup the back to main menu button
        Button backButton = Page.createBackButton();
        bottomBox.getChildren().addAll(newButton, backButton);
        bottomBox.setSpacing(5);
        bottomBox.setAlignment(Pos.CENTER);

        // Setup the boxes
        mainBox.getChildren().addAll(professorSide, projectSide);
        mainBox.setAlignment(Pos.CENTER);

        base.getChildren().addAll(mainBox, bottomBox);
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

        addProjectButton.setOnAction(event -> {
            if (projectHistoryList == null) {
                projectHistoryList = new ArrayList<>();
            }

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

            handleButtonPress("project");
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

        deleteProjectButton.setOnAction(event -> {
            String projectName = TableManager.ssnSelected;

            if (projectName == null) {
                return;
            }

            String deleteQuery = "DELETE FROM PROJECT WHERE ProfessorId = ? AND Name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, entry.get(0));
                preparedStatement.setString(2, projectName);

                if (preparedStatement.executeUpdate() > 0) {
                    showAlert(AlertType.INFORMATION, "Success", "Operation Successful", "We have successfully deleted the Project " + projectName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Refreash Table.
            table = createProjectTable();
            table.setPrefHeight(120);
            pane.setContent(table);
            pane.setFitToWidth(true);
            pane.setVbarPolicy(ScrollBarPolicy.NEVER);
        });
        
        HBox topBox = new HBox(projectLabel, addProjectButton, deleteProjectButton);
        topBox.setSpacing(30);
        
        projectBox = new VBox(topBox, name, field, type, info, pane);
        projectBox.setSpacing(5);
        projectBox.setPadding(new Insets(20, 10, 0, -300));

        return projectBox;
    }

    private TableView<ObservableList<String>> createProjectTable() {
        String query = "SELECT Name, Field, Type, Information FROM PROJECT WHERE ProfessorId = ?";
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, entry.get(0));
    
            // Execute the query and get the result set
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                // Create the TableView using the result set
                table = TableManager.CreateTableView(resultSet, "project");
    
                // Set up any additional configurations for the table
                TableManager.setUpMouseReleased(table);
                table.setFixedCellSize(Region.USE_COMPUTED_SIZE);

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

    // Creates a Auxiliary Staff Add Page.
    private void createAddPageAuxiliary() {
        VBox base = new VBox();
        VBox mainBox = new VBox();
        VBox bottomBox = new VBox();

        // Setup the components List
        textComponents = new ArrayList<>();
        radioComponents = new ArrayList<>();
        dateComponents = new ArrayList<>();

        // Set up the title
        Label professorLabel = new Label("Auxiliary");
        professorLabel.setFont(Font.font(30));
        professorLabel.setStyle("-fx-font-weight: bold;");

        // Set up the features
        TextField fname = makeTextField(50);
        fname.setPromptText("First Name");
        textComponents.add(fname);

        TextField lname = makeTextField(50);
        lname.setPromptText("Last Name");
        textComponents.add(lname);

        TextField ssn = createNumericTextField(10);
        ssn.setPromptText("Social Security Number");
        textComponents.add(ssn);
        
        TextField phone = createNumericTextField(10);
        phone.setPromptText("Phone Number");
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
        radioComponents.add(sex);

        TextField address = makeTextField(150);
        address.setPromptText("Address");
        textComponents.add(address);

        TextField salary = createNumericTextField(10);
        salary.setPromptText("Salary");
        textComponents.add(salary);

        TextField email = makeTextField(150);
        email.setPromptText("Email Address");
        textComponents.add(email);
        
        DatePicker birthday = new DatePicker();
        birthday.setPromptText("Birthday");
        dateComponents.add(birthday);

        DatePicker jobStartingDate = new DatePicker();
        jobStartingDate.setPromptText("Job Starting Date");
        dateComponents.add(jobStartingDate);

        // Profession Radio Buttons setup
        //TODO: Since later we might want to add new professions
        // Maybe we need to do a quick query to populate the radiobuttons so that not everything is hardcoded
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
        radioComponents.add(profession);

        // Setup the add professor button
        Button addButton = new Button("ADD");

        addButtonTransition(addButton, 100, 50);

        addButton.setOnAction(event -> {
            entry = new ArrayList<>();
            
            entry.add(ssn.getText());
            entry.add(fname.getText());
            entry.add(lname.getText());

            try {
                RadioButton selectedGender = (RadioButton) toggleGroup.getSelectedToggle();
                entry.add(selectedGender.getText());
            }
            catch (NullPointerException e) {
                entry.add(null);
            }

            entry.add(phone.getText());
            entry.add(email.getText());

            LocalDate jobStartingDateValue  = jobStartingDate.getValue();
            entry.add(jobStartingDateValue  == null ? "" : jobStartingDateValue .toString());

            LocalDate birthdayDate = birthday.getValue();
            entry.add(birthdayDate == null ? "" : birthdayDate.toString());

            entry.add(address.getText());
            entry.add(salary.getText());

            try {
                RadioButton selectedProfession = (RadioButton) professionGroup.getSelectedToggle();
                entry.add(selectedProfession.getText());
            }
            catch (NullPointerException e) {
                entry.add(null);
            }
            handleButtonPress("auxiliary");
        });

        HBox titleBox = new HBox(professorLabel);
        titleBox.setAlignment(Pos.CENTER_LEFT);        
        titleBox.setPadding(new Insets(20, 0, 0, 30));

        // Setup the back to main menu button
        Button backButton = Page.createBackButton();
        bottomBox.getChildren().addAll(addButton, backButton);
        bottomBox.setSpacing(5);
        bottomBox.setAlignment(Pos.CENTER);
        

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

    // Creates a Student Add Page. 
    private void createAddPageStudent() {
        VBox base = new VBox();
        VBox mainBox = new VBox();
        VBox bottomBox = new VBox();

        // Setup the components List
        textComponents = new ArrayList<>();
        radioComponents = new ArrayList<>();
        dateComponents = new ArrayList<>();

        // Set up the title
        Label studentLabel = new Label("Student");
        studentLabel.setFont(Font.font(30));
        studentLabel.setStyle("-fx-font-weight: bold;");

        // Set up the features
        // Student ID
        TextField studentId = createNumericTextField(10);
        studentId.setPromptText("Student ID");
        textComponents.add(studentId);

        // First Name
        TextField fname = makeTextField(50);
        fname.setPromptText("First Name");
        textComponents.add(fname);

        // Laste Name
        TextField lname = makeTextField(50);
        lname.setPromptText("Last Name");
        textComponents.add(lname);

        // Father Name
        TextField fatherName = makeTextField(50);
        fatherName.setPromptText("Father's Name");
        textComponents.add(fatherName);

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
        radioComponents.add(sex);

        // Semester
        TextField semester = createNumericTextField(2);
        semester.setPromptText("Semester");
        textComponents.add(semester);

        // Email
        TextField email = makeTextField(150);
        email.setPromptText("Email Address");
        textComponents.add(email);

        // Phone
        TextField phone = createNumericTextField(10);
        phone.setPromptText("Phone Number");
        textComponents.add(phone);
        
        // Birthday
        DatePicker birthday = new DatePicker();
        birthday.setPromptText("Birthday");
        dateComponents.add(birthday);

        DatePicker entryDate = new DatePicker();
        entryDate.setPromptText("Entry Date");
        dateComponents.add(entryDate);

        // Address
        TextField address = makeTextField(150);
        address.setPromptText("Address");
        textComponents.add(address);

        // Setup the add professor button
        Button addButton = new Button("ADD");

        addButtonTransition(addButton, 100, 50);

        addButton.setOnAction(event -> {
            entry = new ArrayList<>();
            
            entry.add(studentId.getText());
            entry.add(fname.getText());
            entry.add(lname.getText());
            entry.add(fatherName.getText());

            try {
                RadioButton selectedGender = (RadioButton) toggleGroup.getSelectedToggle();
                entry.add(selectedGender.getText());
            }
            catch (NullPointerException e) {
                entry.add(null);
            }

            entry.add(semester.getText());
            entry.add(email.getText());
            entry.add(phone.getText());

            LocalDate birthdayDate = birthday.getValue();
            entry.add(birthdayDate == null ? "" : birthdayDate.toString());

            LocalDate entryDateValue  = entryDate.getValue();
            entry.add(entryDateValue  == null ? "" : entryDateValue .toString());

            entry.add(address.getText());

            handleButtonPress("student");
        });

        HBox titleBox = new HBox(studentLabel);
        titleBox.setAlignment(Pos.CENTER_LEFT);        
        titleBox.setPadding(new Insets(20, 0, 0, 30));

        // Setup the back to main menu button
        Button backButton = Page.createBackButton();
        bottomBox.getChildren().addAll(addButton, backButton);
        bottomBox.setSpacing(5);
        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        

        // Limit the TextField size to 100 pixels
        for (TextField field: textComponents) {
            field.setMaxWidth(250);
        }

        int i = 0;
        for (TextField field : textComponents) {
            if (i == 3) {
                mainBox.getChildren().add(radioComponents.get(0));
            }
            mainBox.getChildren().add(field);
            i++;
        }
        mainBox.getChildren().addAll(dateComponents.get(0), dateComponents.get(1));
        mainBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.setPadding(new Insets(0, 0, 0, 30));
        mainBox.setSpacing(5);

        // Set VBox.setVgrow to Priority.ALWAYS for both bottomBox and mainBox
        VBox.setVgrow(bottomBox, Priority.ALWAYS);
        VBox.setVgrow(mainBox, Priority.ALWAYS);

        base.getChildren().addAll(titleBox, mainBox, bottomBox);
        root.getChildren().addAll(base);
        
    }

    // Creates a Course Add Page.
    private void createAddPageCourse() {
        VBox base = new VBox();
        VBox mainBox = new VBox();
        VBox bottomBox = new VBox();

        // Setup the components List
        textComponents = new ArrayList<>();

        // Set up the title
        Label courseLabel = new Label("Course");
        courseLabel.setFont(Font.font(30));
        courseLabel.setStyle("-fx-font-weight: bold;");

        // Set up the features
        // Student ID
        TextField courseId = createNumericTextField(10);
        courseId.setPromptText("Course ID");
        textComponents.add(courseId);

        // Course Name
        TextField name = makeTextField(50);
        name.setPromptText("Course Name");
        textComponents.add(name);

        // Semester
        TextField semester = createNumericTextField(2);
        semester.setPromptText("Semester");
        textComponents.add(semester);

        // Setup the add button
        Button addButton = new Button("ADD");

        addButtonTransition(addButton, 100, 50);

        addButton.setOnAction(event -> {
            entry = new ArrayList<>();
            
            entry.add(courseId.getText());
            entry.add(name.getText());
            entry.add(semester.getText());

            handleButtonPress("course");
        });

        HBox titleBox = new HBox(courseLabel);
        titleBox.setAlignment(Pos.CENTER_LEFT);        
        titleBox.setPadding(new Insets(20, 0, 0, 30));

        // Setup the back to main menu button
        Button backButton = Page.createBackButton();
        bottomBox.getChildren().addAll(addButton, backButton);
        bottomBox.setSpacing(5);
        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        

        // Limit the TextField size to 100 pixels
        for (TextField field: textComponents) {
            field.setMaxWidth(250);
        }

        for (TextField field : textComponents) {
            mainBox.getChildren().add(field);
        }
        mainBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.setPadding(new Insets(0, 0, 0, 30));
        mainBox.setSpacing(5);

        // Set VBox.setVgrow to Priority.ALWAYS for both bottomBox and mainBox
        VBox.setVgrow(bottomBox, Priority.ALWAYS);
        VBox.setVgrow(mainBox, Priority.ALWAYS);

        base.getChildren().addAll(titleBox, mainBox, bottomBox);
        root.getChildren().addAll(base);
        
    }

    // Handles the ADD button for each type.
    private void handleButtonPress(String button) {
        button = button.toLowerCase();
        if (button == "project") {
            if(professorStored == false) {
                showAlert(AlertType.ERROR, "Missing Professor", "Professor Entry Missing", "Please add a Professor first to this Project");
                clearFields("project");
                return;
            }
            if (checkForMissingValues(secondEntry)) {
                addProject();
            }
            else {
                showAlert(AlertType.ERROR, "Missing Values", "Some values are missing", "Please fill all the cells");
            }
            return;            
        }

        if (!check()) {
            return;
        }
        if (button == "professor" || button == "auxiliary") {
            addEmployee();
            if (button == "professor") {
                addProfessor();
            }
            else if (button == "auxiliary") {
                addAuxilery();
            }
        }
        else if (button == "student") {
            addStudent();
        }
        else if (button == "course") {
            addCourse();

        }    
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

    // Check for missing values.
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
        if (type == "professor" || type == "axiliary") {
            checkDuplicateQuery = "SELECT COUNT(*) FROM EMPLOYEE WHERE SSN = ?";
        }
        else if (type == "student") {
            checkDuplicateQuery = "SELECT COUNT(*) FROM STUDENT WHERE StudentId = ?";
        }
        else if (type == "course") {
            checkDuplicateQuery = "SELECT COUNT(*) FROM COURSE WHERE CourseId = ?";
        }

        try (PreparedStatement checkDuplicateStatement = connection.prepareStatement(checkDuplicateQuery)) {
            checkDuplicateStatement.setString(1, entry.get(0));

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

    // Insert a new entry in the Employee Table.
    private void addEmployee() {        
        String insertEmployeeQuery = "INSERT INTO EMPLOYEE (SSN, FirstName, LastName, Sex, Phone, Email, JobStartingDate, Birthday, Address, Salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertEmployeeQuery)) {
            for (int i = 0; i < 10; i++) {
                preparedStatement.setString(i+1, entry.get(i));
            }

            if (preparedStatement.executeUpdate() <= 0) {
                showAlert(AlertType.ERROR, "Problem", "Failed to insert employee.", "An error occurred. Please check your input.");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert a new entry in the Professor Table.
    private void addProfessor() {
        // Find who is the manager
        String whoIsManager = "SELECT ProfId FROM PROFESSOR WHERE ManagerId IS NULL";
        String manager = "";
        try (ResultSet result = Page.connection.createStatement().executeQuery(whoIsManager)){
            result.next();
            manager = result.getString("ProfId");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Insert to Professor table
        String insertProfessorQuery = "INSERT INTO PROFESSOR (ProfId, ManagerId, Profession) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProfessorQuery)) {
            preparedStatement.setString(1, entry.get(0));
            preparedStatement.setString(2, manager);
            preparedStatement.setString(3, entry.get(entry.size()-1));

            if (preparedStatement.executeUpdate() > 0) {
                showAlert(AlertType.INFORMATION, "Success", "We have successfully added a new professor", "You can now assign projects to " + entry.get(1) + " " + entry.get(2) + ".");
                professorStored = true;
                lock(true);
            }
            else {
                showAlert(AlertType.ERROR, "Problem", "Failed to insert project.", "An error occurred. Please check your input.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();            
        }
    }

    // Insert a new entry in the Project Table.
    private void addProject() {
        // Insert to Project table
        String insertProfessorQuery = "INSERT INTO PROJECT (ProfessorId, Name, Field, Type, Information) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProfessorQuery)) {
            preparedStatement.setString(1, entry.get(0));
            preparedStatement.setString(2, secondEntry.get(0));
            preparedStatement.setString(3, secondEntry.get(1));
            preparedStatement.setString(4, secondEntry.get(2));
            preparedStatement.setString(5, secondEntry.get(3));

            if (preparedStatement.executeUpdate() > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Operation Successful", "Project " + secondEntry.get(0) + " added to professor " + entry.get(1) + " " + entry.get(2)+ ".");
                projectHistoryList.add(secondEntry.get(0));

                // Refreash Table.
                table = createProjectTable();
                table.setPrefHeight(120);
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
            ex.printStackTrace();
        }
    }

    // Insert a new entry in the Auxiliary Staff Table.
    private void addAuxilery() {
        // Insert to Professor table
        String insertProfessorQuery = "INSERT INTO AUXILIARY_STAFF (EmployeeId, Profession) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProfessorQuery)) {
            preparedStatement.setString(1, entry.get(0));
            preparedStatement.setString(2, entry.get(entry.size()-1));

            if (preparedStatement.executeUpdate() > 0) {
                showAlert(AlertType.INFORMATION, "Success", "We have successfully added a new Auxiliary Staff", "The operation was a success");
                clearFields("professor");
                entry = null;
            }
            else {
                showAlert(AlertType.ERROR, "Problem", "Failed to insert auxiliary staff", "An error occurred. Please check your input.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();            
        }
    }

    // Insert a new entry in the Student Table.
    private void addStudent() {
        String insertStudentQuery = "INSERT INTO STUDENT (StudentId, FirstName, LastName, FatherName, Sex, Semester, Email, Phone, Birthday, EntryDate, Address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStudentQuery)) {
            for (int i = 0; i < 11; i++) {
                preparedStatement.setString(i+1, entry.get(i));
            }

            if (preparedStatement.executeUpdate() > 0) {
                showAlert(AlertType.INFORMATION, "Success", "We have successfully added a new Student", "The operation was a success");
                clearFields("professor");
                entry = null;
            }
        }
        catch (SQLException e) {
            showAlert(AlertType.ERROR, "Problem", "Failed to insert Student.", "An error occurred. Please check your input.");
            e.printStackTrace();
        }
    }

    // Insert a new entry in the Course Table.
    private void addCourse() {
        String insertStudentQuery = "INSERT INTO COURSE (CourseId, FirstName, Semester) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStudentQuery)) {
            for (int i = 0; i < 3; i++) {
                preparedStatement.setString(i+1, entry.get(i));
            }

            if (preparedStatement.executeUpdate() > 0) {
                showAlert(AlertType.INFORMATION, "Success", "We have successfully added a new Course", "The operation was a success");
                clearFields("course");
                entry = null;
            }
        }
        catch (SQLException e) {
            showAlert(AlertType.ERROR, "Problem", "Failed to insert Course.", "An error occurred. Please check your input.");
            e.printStackTrace();
        }
    }
    
    // It shows an Alert.
    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
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

    // Locks the TextFields, DatePickers and RadioButtons for the Professor Add Page.
    private void lock(boolean value) {
        for (DatePicker date : dateComponents) {
                date.setDisable(value);
            }

        int size = textComponents.size();
        for (int i = 0; i < size - 2 ; i++) {
            TextField textField = textComponents.get(i);
            textField.setDisable(value);
        }

        // Clear the radio Buttons from the Project
        size = radioComponents.size();
        for (int i = 0; i < size - 2; i++) {
            VBox vbox = radioComponents.get(i);
            for (Node childNode : vbox.getChildren()) {
                if (childNode instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) childNode;
                    radioButton.setDisable(value);
                }
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

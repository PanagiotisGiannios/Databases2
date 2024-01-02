package code;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
    private Boolean professorStored = false;


    public AddPage(String type) {
        this.type = type;
        switch (type) {
            case "professor":
                background = "professorPage.png";                
                break;
            case "auxilery":
                background = "auxiliaryStaffPage.png";
                break;
            case "Student":
                background = "studentPage.png";
                break;
            case "Book":
                background = "bookPage.png";
                break;
            case "Course":
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

    // Creates the Add Pages along the type.
    private void createAddPage() {
         switch (type) {
            case "professor":
                createAddPageProfessor();                
                break;
            case "auxilery":
                
                break;
            case "Student":
                
                break;
            case "Book":
                
                break;
            case "Course":
                     
            default:
                break;
         }
    }

    // Creates a Professor Add Page.
    private void createAddPageProfessor() {
        VBox base = new VBox();
        HBox titleBox = new HBox();
        HBox mainBox = new HBox();
        VBox bottomBox = new VBox();

        // Set up the title
        Label titleLabel = new Label("Professor");
        titleLabel.setFont(Font.font(30));
        titleBox.setStyle("-fx-font-weight: bold;");
        titleBox.getChildren().add(titleLabel);
        titleBox.setAlignment(Pos.TOP_CENTER);

        // Setup the components List
        textComponents = new ArrayList<>();
        radioComponents = new ArrayList<>();
        dateComponents = new ArrayList<>();

        // Set up the features
        TextField fname = new TextField();
        fname.setPromptText("First Name");
        textComponents.add(fname);

        TextField lname = new TextField();
        lname.setPromptText("Last Name");
        textComponents.add(lname);

        TextField ssn = createNumericTextField();
        ssn.setPromptText("Social Security Number");
        textComponents.add(ssn);
        
        TextField phone = createNumericTextField();
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

        TextField address = new TextField();
        address.setPromptText("Address");
        textComponents.add(address);

        TextField salary = createNumericTextField();
        salary.setPromptText("Salary");
        textComponents.add(salary);

        TextField email = new TextField();
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

        VBox featureSide = new VBox(textComponents.get(0), textComponents.get(1), textComponents.get(2), textComponents.get(3), radioComponents.get(0), textComponents.get(4), textComponents.get(5), textComponents.get(6), dateComponents.get(0), dateComponents.get(1), radioComponents.get(1));
        featureSide.setAlignment(Pos.CENTER_LEFT);
        featureSide.setSpacing(5);
        featureSide.setPadding(new Insets(10, 430, 0, 0));
        

        // Setup the ADD button
        Button addButton = new Button("ADD");
        addButtonTransition(addButton, 100, 50);
        Button backButton = Page.createBackButton();
        bottomBox.getChildren().addAll(addButton, backButton);
        bottomBox.setSpacing(5);
        bottomBox.setAlignment(Pos.CENTER);

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

            LocalDate jobStartingDateValue  = birthday.getValue();
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
            handleButtonPress(addButton);
        });

        // Project Side Setup
        VBox projectSide = makeProjectSide();

        mainBox.getChildren().addAll(featureSide, projectSide);
        mainBox.setAlignment(Pos.CENTER);

        base.getChildren().addAll(titleBox, mainBox, bottomBox);
        root.getChildren().addAll(base);
    }

    private VBox makeProjectSide() {
        // Make the label
        Label projectLabel = new Label("Projects");
        projectLabel.setFont(Font.font(30));
        projectLabel.setStyle("-fx-font-weight: bold;");

        // Name of Project
        TextField name = new TextField();
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
        TextField info = new TextField();
        info.setPromptText("Information");
        textComponents.add(info);
        
        int maxLength = 150;

        // Create a TextFormatter to limit the length of the text
        TextFormatter<String> textFormatter = new TextFormatter<>(
                change -> change.getControlNewText().length() <= maxLength ? change : null
        );
        info.setTextFormatter(textFormatter);

        // Create the project board


        // Make the add button
        
        Image image = new Image("file:" + System.getProperty("user.dir") + "\\universitymanager\\images\\add.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        Button addProjectButton = new Button();
        addProjectButton.setGraphic(imageView);
        addButtonTransition(addProjectButton, 30, 30);
        addProjectButton.setOnAction(event -> {

            // Save the data into a list
            secondEntry = new ArrayList<>();
            secondEntry.add(name.getText());
            secondEntry.add(info.getText());

            // Save field to the list
            try {
                RadioButton selectedField = (RadioButton) fieldGroup.getSelectedToggle();
                entry.add(selectedField.getText());
            }
            catch (NullPointerException e) {
                entry.add(null);
            }

            //Save type to the list
            try {
                RadioButton selectedType = (RadioButton) typeGroup.getSelectedToggle();
                entry.add(selectedType.getText());
            }
            catch (NullPointerException e) {
                entry.add(null);
            }
            handleButtonPress(addProjectButton);
        });
        
        HBox topBox = new HBox(projectLabel, addProjectButton);
        topBox.setSpacing(30);

        VBox projectBox = new VBox(topBox, name, field, type, info);
        projectBox.setSpacing(5);
        projectBox.setPadding(new Insets(20, 10, 0, -300));

        return projectBox;
    }

    // 
    private void handleButtonPress(Button button) {
        if (button.getText() == "+") {
            addProject();
            return;
        }
        if (type == "professor" || type == "auxilery") {
            if (check()) {
                addEmployee();
                if (type == "professor") {
                    addProfessor();
                }
                else {
                    // TODO:  addAuxilery();
                }
                clearFields("professor");                
            }
            else {
                // TODO: fill this
            }
        }
        // TODO: if for the rest
        else if (true) {

        }       
    }

    private void addProject() {
        if (professorStored == false) {
            showAlert(AlertType.ERROR, "Missing Professor", "Professor Entry Missing", "Please add a Professor first to this Project");
            return;
        }

        // Insert to Project table
        String insertProfessorQuery = "INSERT INTO PROJECT (ProfessorId, Name, Field, Type, Information) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProfessorQuery)) {
            preparedStatement.setString(1, entry.get(0));
            preparedStatement.setString(2, secondEntry.get(0));
            preparedStatement.setString(3, secondEntry.get(1));
            preparedStatement.setString(4, secondEntry.get(2));
            preparedStatement.setString(5, secondEntry.get(3));

            if (preparedStatement.executeUpdate() > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Operation Successful", "The operation was completed successfully.");
                clearFields("project");
                // TODO: fillBoard(secondEntry);
                secondEntry = null;
            }
            else {
                showAlert(AlertType.ERROR, "Problem", "Failed to insert Project.", "An error occurred. Please check your input.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();            
        }

        

    }

    private boolean check() {
        if (!checkForMissingValues()) {
            showAlert(AlertType.ERROR, "Missing Values", "Some values are missing", "Please fill all the cells");
            return false;
        }
        else if (!checkForDuplicate()) {
            showAlert(AlertType.ERROR, "Duplicate SSN", "SSN already exists", "An employee with the same SSN already exists.");
            return false;
        }
        return true;
    }

    private boolean checkForMissingValues() {
        for (String e : entry) {
            if (e == null || e == "") {
                System.out.println(e);
                return false;
            }
        }
        return true;
    }

    private boolean checkForDuplicate() {
        // Check for duplicates
        String checkDuplicateQuery = "SELECT COUNT(*) FROM EMPLOYEE WHERE SSN = ?";
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


    private void addEmployee() {
        // Insert in the Employee Table
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
                showAlert(AlertType.INFORMATION, "Success", "Operation Successful", "The operation was completed successfully.");
            }
            else {
                showAlert(AlertType.ERROR, "Problem", "Failed to insert professor.", "An error occurred. Please check your input.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();            
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
    }
}

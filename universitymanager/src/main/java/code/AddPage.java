package code;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.Cursor;
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
import javafx.stage.Stage;
import javafx.util.Duration;

public class AddPage extends Page {
    private String type;
    private String background;

    // Components
    List<TextField> textComponents = null;
    List<VBox> radioComponents = null;
    List<DatePicker> dateComponents = null;

    private List<String> entry;


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
        HBox bottomBox = new HBox();

        // Set up the title
        Label titleLabel = new Label("Professor");
        titleLabel.setFont(Font.font(25));
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
        featureSide.setSpacing(8);
        featureSide.setPadding(new Insets(10, 450, 0, 0));
        VBox.setVgrow(featureSide, Priority.ALWAYS);
        

        // Setup the ADD button
        Button addButton = new Button("ADD");
        setButtonProperties(addButton);
        bottomBox.getChildren().addAll(addButton);
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
        });



        VBox projectSide = new VBox();
        // TODO: Staff

        

        mainBox.getChildren().addAll(featureSide, projectSide);
        mainBox.setAlignment(Pos.CENTER);

        base.getChildren().addAll(titleBox, mainBox, bottomBox);
        root.getChildren().addAll(base);
    }

    // Make the a button have a Transition.
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

    // 
    private void handleButtonPress(Button button) {
        if (type == "professor" || type == "auxilery") {
            if (check()) {
                addEmployee();
                if (type == "professor") {
                    addProfessor();
                }
                else {
                    // TODO:  addAuxilery();
                }
                clearFields();                
            }
            else {
                // TODO: fill this
            }
        }
        // TODO: if for the rest
        else if (true) {

        }       
    }

    private boolean check() {
        if (!checkForMissingValues()) {
            showErrorAlert("Missing Values", "Some values are missing", "Please fill all the cells");
            return false;
        }
        else if (!checkForDuplicate()) {
            showErrorAlert("Duplicate SSN", "SSN already exists", "An employee with the same SSN already exists.");
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
                showErrorAlert("Problem", "Failed to insert employee.", "An error occurred. Please check your input.");
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
                showSuccessAlert("Success", "Operation Successful", "The operation was completed successfully.");
            }
            else {
                showErrorAlert("Problem", "Failed to insert professor.", "An error occurred. Please check your input.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();            
        }

    }

    private void showSuccessAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Helper method to show error alert
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private TextField createNumericTextField() {
        TextField textField = new TextField();

        // Create a UnaryOperator that filters out non-numeric characters
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (Pattern.matches("[0-9,.]*", newText)) {
                return change; // Accept the change
            }
            return null; // Reject the change
        };

        // Apply the filter to the TextFormatter
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);

        return textField;
    }

    // Clear all the fields.
    private void clearFields() {
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

    public static void main(String[] args) {
        AddPage prof = new AddPage("professor");
        prof.start(Page.primaryStage);
    }
}

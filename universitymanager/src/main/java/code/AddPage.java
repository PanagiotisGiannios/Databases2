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

    // Employee Features
    private String ssn;
    private String fname;
    private String lname;
    private String sex;
    private String phone;
    private String email;
    private String jobStartingDate;
    private String birthday;
    private String address;
    private String salary;

    // Professor feature
    private String profession;

    private String[] entry = null;

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

        // Set up the features
        TextField fname = new TextField();
        fname.setPromptText("First Name");

        TextField lname = new TextField();
        lname.setPromptText("Last Name");

        TextField ssn = new TextField();
        ssn.setPromptText("Social Security Number");
        
        TextField phone = createNumericTextField();
        phone.setPromptText("Phone Number");

        // Sex Radio Button
        Label gender = new Label("Gender");
        gender.setFont(Font.font(15));
        gender.setStyle("-fx-font-weight: bold;");
        RadioButton maleRadioButton = new RadioButton("Male");
        RadioButton femaleRadioButton = new RadioButton("Female");

        ToggleGroup toggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(toggleGroup);
        femaleRadioButton.setToggleGroup(toggleGroup);

        VBox sex = new VBox(gender, maleRadioButton, femaleRadioButton);
        sex.setPadding(new Insets(5));

        TextField address = new TextField();
        address.setPromptText("Address");

        TextField salary = createNumericTextField();
        salary.setPromptText("Salary");

        TextField email = new TextField();
        email.setPromptText("Email Address");
        
        DatePicker birthday = new DatePicker();
        birthday.setPromptText("Birthday");

        DatePicker jobStartingDate = new DatePicker();
        jobStartingDate.setPromptText("Job Starting Date");

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

        VBox featureSide = new VBox(fname, lname, ssn, phone, sex, address, salary, email, birthday, jobStartingDate, profession);
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
            String[] newEntry = new String[10];
            this.ssn = newEntry[0] = ssn.getText();

            this.fname = newEntry[1] = fname.getText();

            this.lname = newEntry[2] = lname.getText();

            RadioButton selectedGender = (RadioButton) toggleGroup.getSelectedToggle();
            this.sex = newEntry[3] = selectedGender.getText();

            this.phone = newEntry[4] = phone.getText();

            this.email = newEntry[5] = email.getText();

            LocalDate jobStartingDateValue  = birthday.getValue();
            this.jobStartingDate = newEntry[6] = jobStartingDateValue  == null ? "" : jobStartingDateValue .toString();

            LocalDate birthdayDate = birthday.getValue();
            this.birthday = newEntry[7] = birthdayDate == null ? "" : birthdayDate.toString();

            this.address = newEntry[8] = address.getText();

            this.salary = newEntry[9] = salary.getText();

            RadioButton selectedProfession = (RadioButton) professionGroup.getSelectedToggle();
            this.profession = selectedProfession.getText();

            this.entry = newEntry;
            handleButtonPress(addButton);
        });



        VBox projectSide = new VBox();
        // TODO: Staff

        

        mainBox.getChildren().addAll(featureSide, projectSide);
        mainBox.setAlignment(Pos.CENTER);

        base.getChildren().addAll(titleBox, mainBox, bottomBox);
        root.getChildren().addAll(base);
    }

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

    private void handleButtonPress(Button button) {
        // Insert in the Employee Table
        boolean error = false;
        String insertEmployeeQuery = "INSERT INTO EMPLOYEE (SSN, FirstName, LastName, Sex, Phone, Email, JobStartingDate, Birthday, Address, Salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertEmployeeQuery)) {
            preparedStatement.setString(1, this.ssn);
            preparedStatement.setString(2, this.fname);
            preparedStatement.setString(3, this.lname);
            preparedStatement.setString(4, this.sex);
            preparedStatement.setString(5, this.phone);
            preparedStatement.setString(6, this.email); 
            preparedStatement.setString(7, this.jobStartingDate);
            preparedStatement.setString(8, this.birthday);
            preparedStatement.setString(9, this.address);
            preparedStatement.setString(10, this.salary);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                error = false;                
            } else {
                error = true;
                showErrorAlert("Problem", "Failed to insert professor.", "An error occurred. Please check your input.");
            }
        }
        catch (SQLException e) {
        e.printStackTrace();
        }

        // Find who is the manager
        String whoIsManager = "SELECT DISTINCT * FROM PROFESSOR WHERE ManagerId IS NULL";
        String manager = "";
        try (ResultSet result = Page.connection.createStatement().executeQuery(whoIsManager)){
            manager = result.getString("ManagerId");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert to Professors table
        String insertProfessorQuery = "INSERT INTO PROFESSOR (ProfId, ManagerId, Profession) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProfessorQuery)) {
            preparedStatement.setString(1, this.ssn);
            preparedStatement.setString(2, manager);
            preparedStatement.setString(3, this.profession);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0 && error == false) {
                showSuccessAlert("Success", "Operation Successful", "The operation was completed successfully.");
            }
            else if (error == false) {
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

    public static void main(String[] args) {
        AddPage prof = new AddPage("professor");
        prof.start(Page.primaryStage);
    }
}

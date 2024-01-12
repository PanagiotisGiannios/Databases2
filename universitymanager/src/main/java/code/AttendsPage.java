package code;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttendsPage extends Page {
    
    private String source;
    private List<String> idList = new ArrayList<String>();
    private ToggleGroup leftToggleGroup = new ToggleGroup();
    private String selectedStudent = "";
    private VBox rightVBox;
    private ScrollPane rightScroll;
    public AttendsPage(String source,List<String> idList){
        this.source = source;
        this.idList = idList;
    }

    @Override
    public void start(Stage primaryStage){
        loadLogo();
        loadBackground("coursePage.png");
        if(source.equals("student")){
            System.out.println("prof!");
            professorSetup();
        }
        else if(source.equals("course")){
            System.out.println("course!");
            professorSetup();
        }
        createScene();
    }

    private void professorSetup(){
        VBox container = new VBox();
        VBox base = new VBox(10);
        HBox titleBox = new HBox(200);
        HBox mainBox = new HBox(10);
        HBox backBox = new HBox();

        Text studentText = new Text("Student");
        studentText.setFont(Font.font("System", FontWeight.BOLD, 29));
        Text courseText = new Text("Course");
        courseText.setFont(Font.font("System", FontWeight.BOLD, 29));
        if(source.equals("student")){
            titleBox.getChildren().addAll(studentText,courseText);
        }
        else{
            titleBox.getChildren().addAll(courseText,studentText);
        }
        titleBox.setAlignment(Pos.TOP_CENTER);

        StackPane leftSide = new StackPane();
        StackPane rightSide = new StackPane();

        VBox leftVBox = new VBox(30);
        rightVBox = new VBox(30);
        ScrollPane leftScroll = new ScrollPane(leftVBox);
        rightScroll = new ScrollPane(rightVBox);

        if(source.equals("student")){
            leftVBox = createLeftVBox("student");
            rightVBox = createRightVBox("course", null);
        }
        else{
            leftVBox = createLeftVBox("course");
            rightVBox = createRightVBox("student", null);
        }
        leftScroll.setContent(leftVBox);
        leftSide.getChildren().addAll(leftScroll);
        leftScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        leftScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        
        leftScroll.setPrefHeight(200);
        leftSide.setPadding(new Insets(0, 0, 0, 200));
        rightScroll.setContent(rightVBox);
        rightScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        rightScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        rightScroll.setPrefHeight(200);
        rightSide.getChildren().add(rightScroll);
        rightSide.setAlignment(Pos.CENTER_RIGHT);
        rightSide.setPadding(new Insets(0, 0, 0, 70));
        mainBox.getChildren().addAll(leftSide,rightSide);
        
        Button backButton = Page.createBackButton();
        backButton.setOnMouseReleased(e-> {
            if(selectedStudent != null && !selectedStudent.isEmpty() && source.equals("student")){
                saveEntry();
            }
            MainMenu mainMenu = new MainMenu();
            mainMenu.start(Page.primaryStage);
        });
        backBox.setAlignment(Pos.TOP_CENTER);
        backBox.getChildren().add(backButton);
        backBox.setPadding(new Insets(0,0,5,0));

        VBox.setVgrow(mainBox,Priority.ALWAYS);
        base.getChildren().addAll(titleBox,mainBox,backBox);
        
        container.getChildren().addAll(base);
        container.setAlignment(Pos.CENTER);
        root.getChildren().addAll(container);
    }

    private VBox createLeftVBox(String type){
        VBox vbox = new VBox(5);
        if(type.equals("student")){
            ResultSet res = getStudentsFromIdList();
            try {
                List<Double> columnWidths = new ArrayList<>();
                
                HBox titles = new HBox(15);
                titles.setPadding(new Insets(0, 0, 0, 30));
                Text studentId = new Text("SSN");
                Text fName = new Text("First Name");
                Text lName = new Text("Last Name");
                titles.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
                titles.getChildren().addAll(studentId,fName,lName);
                columnWidths.add(studentId.prefWidth(-1));
                columnWidths.add(fName.prefWidth(-1));
                columnWidths.add(lName.prefWidth(-1));
                vbox.getChildren().add(titles);

                while (res.next()) {
                    HBox hbox = new HBox(15);
                    RadioButton  radioButton = new RadioButton();
                    radioButton.setToggleGroup(leftToggleGroup);
                    Text studentIDText   = new Text(res.getString(1));
                    Text fNameText = new Text(res.getString(2));
                    Text lNameText = new Text(res.getString(3));
                    hbox.getChildren().addAll(radioButton,studentIDText,fNameText,lNameText);
                    
                    columnWidths.add(studentIDText.prefWidth(-1));
                    columnWidths.add(fNameText.prefWidth(-1));
                    columnWidths.add(lNameText.prefWidth(-1));
                    Border border = new Border(new BorderStroke(Color.BLACK,
                                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
                    hbox.setBorder(border);

                    radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            if(selectedStudent != null && !selectedStudent.isEmpty()){
                                saveEntry();
                            }
                            String selectedStudentId = studentIDText.getText();
                            selectedStudent = selectedStudentId;
                            System.out.println("Selected ID: " + selectedStudentId);
                            rightVBox = createRightVBox("course", selectedStudentId);
                            rightScroll.setContent(null);
                            rightScroll.setContent(rightVBox);
                            // Perform further actions with the selected SSN
                        }
                    });

                    vbox.getChildren().add(hbox);
                }

                double maxWidth = Collections.max(columnWidths);

                for (Node node : vbox.getChildren()) {
                    if (node instanceof HBox) {
                        HBox hbox = (HBox) node;
                        for (Node cell : hbox.getChildren()) {
                            if (cell instanceof Text) {
                                ((Text) cell).setWrappingWidth(maxWidth);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("course")){
            ResultSet res = getCoursesFromIdList();
            try {
                List<Double> columnWidths = new ArrayList<>();
                
                HBox titles = new HBox(15);
                titles.setPadding(new Insets(0, 0, 0, 30)); // Add padding to the top
                Text id = new Text("ID");
                Text fName = new Text("Course Name");
                
                titles.getChildren().addAll(
                    id,
                    fName
                );
                columnWidths.add(id.prefWidth(-1));
                columnWidths.add(fName.prefWidth(-1));
                vbox.getChildren().add(titles);
                // Add labels to the labelsHBox

                while (res.next()) {
                    HBox hbox = new HBox(15);
                    RadioButton  radioButton = new RadioButton();
                    radioButton.setToggleGroup(leftToggleGroup);
                    Text idText   = new Text(res.getString(1));
                    Text nameText = new Text(res.getString(2));
                    hbox.getChildren().addAll(radioButton,idText,nameText);
                    
                    columnWidths.add(idText.prefWidth(-1));
                    columnWidths.add(nameText.prefWidth(-1));
                    Border border = new Border(new BorderStroke(Color.BLACK,
                                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
                    hbox.setBorder(border);

                    radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            String selectedID = idText.getText();
                            System.out.println("Selected id: " + selectedID);
                            rightVBox = createRightVBox("student", selectedID);
                            rightScroll.setContent(null);
                            rightScroll.setContent(rightVBox);
                            // Perform further actions with the selected SSN
                        }
                    });

                    vbox.getChildren().add(hbox);
                }

                double maxWidth = Collections.max(columnWidths);

                for (Node node : vbox.getChildren()) {
                    if (node instanceof HBox) {
                        HBox hbox = (HBox) node;
                        for (Node cell : hbox.getChildren()) {
                            if (cell instanceof Text) {
                                ((Text) cell).setWrappingWidth(maxWidth);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return vbox;
    }

   
    private VBox createRightVBox(String type,String callerID){
        VBox vbox = new VBox(5);
        if(type.equals("course")){
            ResultSet res;
            try {
                List<String> attendedCoursesList = getAttendedCourses(callerID);
                res = Page.connection.createStatement().executeQuery("Select CourseID,Name From ov_courseinfo");
                List<Double> columnWidths = new ArrayList<>();
                
                HBox titles = new HBox(15);
                titles.setPadding(new Insets(0, 0, 0, 30)); // Add padding to the top
                Text id = new Text("ID");
                Text name = new Text("Course Name");
                titles.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
                titles.getChildren().addAll(id,name);
                columnWidths.add(id.prefWidth(-1));
                columnWidths.add(name.prefWidth(-1));
                
                vbox.getChildren().add(titles);
                
                while (res.next()) {
                    HBox hbox = new HBox(15);
                    CheckBox checkBox = new CheckBox();
                    TextField gradeTextField = Page.createGradeTextField();
                    if(callerID == null){
                        checkBox.setDisable(true);
                    }
                    for (String attendedCoursesId : attendedCoursesList) {
                        if(attendedCoursesId.equals(res.getString(1))){
                            ResultSet grade = Page.connection.createStatement().executeQuery("select grade from attends where studentId = "+ callerID +" AND courseID = " + attendedCoursesId );
                            while (grade.next()) {
                                System.out.println(grade.getString(1));
                                gradeTextField.setText(grade.getString(1));
                            }
                            checkBox.setSelected(true);
                        }
                    }
                    Text idText   = new Text(res.getString(1));
                    Text nameText = new Text(res.getString(2));
                    //TextField gradeTextField = Page.createGradeTextField();
                    gradeTextField.setPromptText("Grade:");
                    hbox.getChildren().addAll(checkBox,idText,nameText,gradeTextField);
                    columnWidths.add(idText.prefWidth(-1));
                    columnWidths.add(nameText.prefWidth(-1));
                    columnWidths.add(gradeTextField.prefWidth(-1));
                    Border border = new Border(new BorderStroke(Color.BLACK,
                                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
                    hbox.setBorder(border);

                    checkBox.selectedProperty().addListener((obs,oldValue,newValue) ->{
                        try{
                            if(newValue == true){
                                if(gradeTextField.getText().isEmpty()){
                                    Page.connection.createStatement().executeUpdate("Insert into attends (studentId,courseId,grade) Values ("+callerID +","+idText.getText()+",-1);");
                                }
                                else{
                                    Page.connection.createStatement().executeUpdate("Insert into attends (studentId,courseId,grade) Values ("+callerID +","+idText.getText()+"," + gradeTextField.getText()+");");
                                
                                }
                            }
                            else{
                                Page.connection.createStatement().executeUpdate("Delete from attends where studentId =" + callerID +" AND courseID = " + idText.getText());
                            }
                        } catch (SQLException e){
                            e.printStackTrace();
                        }
                        
                    });

                    vbox.getChildren().add(hbox);
                }
                double maxWidth = Collections.max(columnWidths);

                for (Node node : vbox.getChildren()) {
                    if (node instanceof HBox) {
                        HBox hbox = (HBox) node;
                        for (Node cell : hbox.getChildren()) {
                            if (cell instanceof Text) {
                                ((Text) cell).setWrappingWidth(maxWidth);
                            }
                        }
                    }
                }
                return vbox;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("student")){
            ResultSet res;
            try {
                List<String> attendingStudentsList = getAttendingStudents(callerID);
                res = Page.connection.createStatement().executeQuery("Select StudentID,FirstName,LastName From ov_students");
                List<Double> columnWidths = new ArrayList<>();

                HBox titles = new HBox(15);
                titles.setPadding(new Insets(0,0,0,30));
                Text studentId = new Text("ID");
                Text fName = new Text("First Name");
                Text lName = new Text("Last Name");
                titles.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
                titles.getChildren().addAll(studentId,fName,lName);
                columnWidths.add(studentId.prefWidth(-1));
                columnWidths.add(fName.prefWidth(-1));
                columnWidths.add(lName.prefWidth(-1));

                vbox.getChildren().add(titles);


                while (res.next()) {
                    HBox hbox = new HBox(15);
                    CheckBox checkBox = new CheckBox();
                    if(callerID == null){
                        checkBox.setDisable(true);
                    }
                    for (String attendingStudentId : attendingStudentsList) {
                        if(attendingStudentId.equals(res.getString(1))){
                            checkBox.setSelected(true);
                        }
                    }
                    Text studentIdText   = new Text(res.getString(1));
                    Text fnameText = new Text(res.getString(2));
                    Text lnameText = new Text(res.getString(3));
                    hbox.getChildren().addAll(checkBox,studentIdText,fnameText,lnameText);
                    columnWidths.add(studentIdText.prefWidth(-1));
                    columnWidths.add(fnameText.prefWidth(-1));
                    columnWidths.add(lnameText.prefWidth(-1));

                    Border border = new Border(new BorderStroke(Color.BLACK,
                                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
                    hbox.setBorder(border);

                    checkBox.selectedProperty().addListener((obs,oldValue,newValue) ->{
                        try{
                            if(newValue == true){
                                Page.connection.createStatement().executeUpdate("Insert into attends (courseID,studentId,grade) Values ("+callerID +","+studentIdText.getText()+",-1);");
                            }
                            else{
                                Page.connection.createStatement().executeUpdate("Delete from attends where courseID =" + callerID +" AND studentId = " + studentIdText.getText());
                            }
                        } catch (SQLException e){
                            e.printStackTrace();
                        }
                        
                    });

                    vbox.getChildren().add(hbox);
                }
                double maxWidth = Collections.max(columnWidths);

                for (Node node : vbox.getChildren()) {
                    if (node instanceof HBox) {
                        HBox hbox = (HBox) node;
                        for (Node cell : hbox.getChildren()) {
                            if (cell instanceof Text) {
                                ((Text) cell).setWrappingWidth(maxWidth);
                            }
                        }
                    }
                }
                return vbox;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vbox;
    }

    private List<String> getAttendedCourses(String callerID){
        try{
            List<String> courses = new ArrayList<>();
            if(callerID == null || callerID.isEmpty()){
                return courses;
            }

            ResultSet coursesResultSet = Page.connection
                                        .createStatement()
                                        .executeQuery
                                        ("Select distinct courseID from course where courseID IN (Select courseID from attends where studentID = " + callerID + ")");
            while (coursesResultSet.next()) {
                courses.add(coursesResultSet.getString(1));
            }
            return courses;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private List<String> getAttendingStudents(String callerID){
        try{
            List<String> professors = new ArrayList<>();
            if(callerID == null || callerID.isEmpty()){
                return professors;
            }
            ResultSet professorsResultSet = Page.connection
                                            .createStatement()
                                            .executeQuery("Select distinct studentID from student where studentID IN (Select studentID from attends where courseID = " + callerID + ")");
            while(professorsResultSet.next()){
                professors.add(professorsResultSet.getString(1));
            }
            return professors;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private ResultSet getStudentsFromIdList() {
        ResultSet res = null;
        try {
            String whereString = "WHERE";
            for (String id : idList) {
                whereString = whereString + " OR StudentID = " + id;
            }
            whereString = whereString.replaceFirst("OR", "");
            System.out.println(whereString);
            res = Page.connection.createStatement().executeQuery("SELECT studentId, FirstName, LastName FROM OV_students " + whereString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    private ResultSet getCoursesFromIdList(){
        ResultSet res = null;
        try{
            String whereString = "WHERE";
            for (String id : idList) {
                whereString = whereString + " OR courseID = " + id;
            }
            whereString = whereString.replaceFirst("OR", "");
            System.out.println(whereString);
            res = Page.connection.createStatement()
                    .executeQuery("SELECT courseID, Name FROM OV_courseinfo " + whereString);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    private void saveEntry(){
        if(source.equals("student")){

            ObservableList<Node> hboxesList = rightVBox.getChildren();
            for (Node node : hboxesList) {
                if((((HBox)node).getChildren()).size() <= 2){
                    continue;
                }
                //ObservableList<Node> children  = ((HBox)node).getChildren();
                Node first = ((HBox)node).getChildren().get(0);
                boolean checked = false;
                String courseId = null;
                Double grade = null;
                if(first instanceof CheckBox){
                    if(((CheckBox)first).isSelected()){
                        checked = true;
                    }
                    else{
                        checked = false;
                    }
                }
                Node second = ((HBox)node).getChildren().get(1);
                if(second instanceof Text){
                    courseId = ((Text)second).getText();
                }
                Node fourth = ((HBox)node).getChildren().get(3);
                if(fourth instanceof TextField){
                    if(checked){
                        if(((TextField)fourth).getText().isEmpty()){
                            grade = -1.0;
                        }
                        else{
                            grade =  Double.parseDouble(((TextField)fourth).getText());
                        }
                    }
                }

                if(checked){
                    try {
                        Page.connection.createStatement().executeUpdate("Update attends SET grade = " + grade + " WHERE courseID = " + courseId + " AND studentID = "+ selectedStudent);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

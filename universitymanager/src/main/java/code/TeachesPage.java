package code;

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



public class TeachesPage extends Page {
    
    private String source;
    private List<String> idList = new ArrayList<String>();
    private ToggleGroup leftToggleGroup = new ToggleGroup();

    private VBox rightVBox;
    private ScrollPane rightScroll;
    public TeachesPage(String source,List<String> idList){
        this.source = source;
        this.idList = idList;
    }

    @Override
    public void start(Stage primaryStage){
        loadLogo();
        loadBackground("teachesPage.png");
        if(source.equals("professor")){
            professorSetup();
        }
        else if(source.equals("course")){
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

        Text profText = new Text("Professor");
        profText.setFont(Font.font("System", FontWeight.BOLD, 29));
        Text courseText = new Text("Course");
        courseText.setFont(Font.font("System", FontWeight.BOLD, 29));
        if(source.equals("professor")){
            titleBox.getChildren().addAll(profText,courseText);
        }
        else{
            titleBox.getChildren().addAll(courseText,profText);
        }
        titleBox.setAlignment(Pos.TOP_CENTER);

        StackPane leftSide = new StackPane();
        StackPane rightSide = new StackPane();

        VBox leftVBox = new VBox(30);
        rightVBox = new VBox(30);
        ScrollPane leftScroll = new ScrollPane(leftVBox);
        rightScroll = new ScrollPane(rightVBox);

        if(source.equals("professor")){
            leftVBox = createLeftVBox("professor");
            rightVBox = createRightVBox("course", null);
        }
        else{
            leftVBox = createLeftVBox("course");
            rightVBox = createRightVBox("professor", null);
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
        if(type.equals("professor")){
            ResultSet res = getProfessorsFromIdList();
            try {
                List<Double> columnWidths = new ArrayList<>();
                
                HBox titles = new HBox(15);
                titles.setPadding(new Insets(0, 0, 0, 30));
                Text ssn = new Text("SSN");
                Text fName = new Text("First Name");
                Text lName = new Text("Last Name");
                titles.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
                titles.getChildren().addAll(
                    ssn,
                    fName,
                    lName
                );
                columnWidths.add(ssn.prefWidth(-1));
                columnWidths.add(fName.prefWidth(-1));
                columnWidths.add(lName.prefWidth(-1));
                vbox.getChildren().add(titles);

                while (res.next()) {
                    HBox hbox = new HBox(15);
                    RadioButton  radioButton = new RadioButton();
                    radioButton.setToggleGroup(leftToggleGroup);
                    Text ssnText   = new Text(res.getString(1));
                    Text fNameText = new Text(res.getString(2));
                    Text lNameText = new Text(res.getString(3));
                    hbox.getChildren().addAll(radioButton,ssnText,fNameText,lNameText);
                    
                    columnWidths.add(ssnText.prefWidth(-1));
                    columnWidths.add(fNameText.prefWidth(-1));
                    columnWidths.add(lNameText.prefWidth(-1));
                    Border border = new Border(new BorderStroke(Color.BLACK,
                                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
                    hbox.setBorder(border);

                    radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            String selectedSSN = ssnText.getText();
                            rightVBox = createRightVBox("course", selectedSSN);
                            rightScroll.setContent(null);
                            rightScroll.setContent(rightVBox);
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
                titles.setPadding(new Insets(0, 0, 0, 30));
                Text id = new Text("ID");
                Text fName = new Text("Course Name");
                
                titles.getChildren().addAll(
                    id,
                    fName
                );
                columnWidths.add(id.prefWidth(-1));
                columnWidths.add(fName.prefWidth(-1));
                vbox.getChildren().add(titles);

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
                            rightVBox = createRightVBox("professor", selectedID);
                            rightScroll.setContent(null);
                            rightScroll.setContent(rightVBox);
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
                List<String> taughtCoursesList = getTaughtCourses(callerID);
                res = Page.connection.createStatement().executeQuery("Select CourseID,Name From ov_courseinfo");
                List<Double> columnWidths = new ArrayList<>();
                
                HBox titles = new HBox(15);
                titles.setPadding(new Insets(0, 0, 0, 30));
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
                    if(callerID == null){
                        checkBox.setDisable(true);
                    }
                    for (String taughtCourseId : taughtCoursesList) {
                        if(taughtCourseId.equals(res.getString(1))){
                            checkBox.setSelected(true);
                        }
                    }
                    Text idText   = new Text(res.getString(1));
                    Text nameText = new Text(res.getString(2));
                    hbox.getChildren().addAll(checkBox,idText,nameText);
                    columnWidths.add(idText.prefWidth(-1));
                    columnWidths.add(nameText.prefWidth(-1));

                    Border border = new Border(new BorderStroke(Color.BLACK,
                                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
                    hbox.setBorder(border);

                    checkBox.selectedProperty().addListener((obs,oldValue,newValue) ->{
                        try{
                            if(newValue == true){
                                Page.connection.createStatement().executeUpdate("Insert into teaches (profId,courseId) Values ("+callerID +","+idText.getText()+");");
                            }
                            else{
                                Page.connection.createStatement().executeUpdate("Delete from teaches where profId =" + callerID +" AND courseID = " + idText.getText());
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
        else if(type.equals("professor")){
            ResultSet res;
            try {
                List<String> teachingProfsList = getTeachingProfessors(callerID);
                res = Page.connection.createStatement().executeQuery("Select ProfID,FirstName,LastName From ov_professors");
                List<Double> columnWidths = new ArrayList<>();

                HBox titles = new HBox(15);
                titles.setPadding(new Insets(0,0,0,30));
                Text ssn = new Text("SSN");
                Text fName = new Text("First Name");
                Text lName = new Text("Last Name");
                titles.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
                titles.getChildren().addAll(ssn,fName,lName);
                columnWidths.add(ssn.prefWidth(-1));
                columnWidths.add(fName.prefWidth(-1));
                columnWidths.add(lName.prefWidth(-1));

                vbox.getChildren().add(titles);


                while (res.next()) {
                    HBox hbox = new HBox(15);
                    CheckBox checkBox = new CheckBox();
                    if(callerID == null){
                        checkBox.setDisable(true);
                    }
                    for (String teachingProfId : teachingProfsList) {
                        if(teachingProfId.equals(res.getString(1))){
                            checkBox.setSelected(true);
                        }
                    }
                    Text ssnText   = new Text(res.getString(1));
                    Text fnameText = new Text(res.getString(2));
                    Text lnameText = new Text(res.getString(3));
                    hbox.getChildren().addAll(checkBox,ssnText,fnameText,lnameText);
                    columnWidths.add(ssnText.prefWidth(-1));
                    columnWidths.add(fnameText.prefWidth(-1));
                    columnWidths.add(lnameText.prefWidth(-1));

                    Border border = new Border(new BorderStroke(Color.BLACK,
                                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
                    hbox.setBorder(border);

                    checkBox.selectedProperty().addListener((obs,oldValue,newValue) ->{
                        try{
                            if(newValue == true){
                                Page.connection.createStatement().executeUpdate("Insert into teaches (courseID,profID) Values ("+callerID +","+ssnText.getText()+");");
                            }
                            else{
                                Page.connection.createStatement().executeUpdate("Delete from teaches where courseID =" + callerID +" AND profID = " + ssnText.getText());
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

    private List<String> getTaughtCourses(String callerID){
        try{
            List<String> courses = new ArrayList<>();
            if(callerID == null || callerID.isEmpty()){
                return courses;
            }

            ResultSet coursesResultSet = Page.connection
                                        .createStatement()
                                        .executeQuery
                                        ("Select distinct courseID from course where courseID IN (Select courseID from teaches where profID = " + callerID + ")");
            while (coursesResultSet.next()) {
                courses.add(coursesResultSet.getString(1));
            }
            return courses;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private List<String> getTeachingProfessors(String callerID){
        try{
            List<String> professors = new ArrayList<>();
            if(callerID == null || callerID.isEmpty()){
                return professors;
            }
            ResultSet professorsResultSet = Page.connection
                                            .createStatement()
                                            .executeQuery("Select distinct profID from professor where profID IN (Select profID from teaches where courseID = " + callerID + ")");
            while(professorsResultSet.next()){
                professors.add(professorsResultSet.getString(1));
            }
            return professors;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private ResultSet getProfessorsFromIdList() {
        ResultSet res = null;
        try {
            String whereString = "WHERE";
            for (String id : idList) {
                whereString = whereString + " OR SSN = " + id;
            }
            whereString = whereString.replaceFirst("OR", "");
            res = Page.connection.createStatement().executeQuery("SELECT ssn, FirstName, LastName FROM OV_professors " + whereString);
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
            res = Page.connection.createStatement()
                    .executeQuery("SELECT courseID, Name FROM OV_courseinfo " + whereString);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }
}

package code;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class ProfessorMenu extends Page {

    @Override
    public void start(Stage primaryStage) {
        Page.primaryStage = primaryStage;
        loadLogo();
        loadBackground("professorPage.png");
        professorMenuSetup();
        createScene();
    }
    
    private void professorMenuSetup(){
        VBox base = new VBox();
        HBox titleBox = new HBox();
        HBox mainBox = new HBox();
        HBox bottomBox = new HBox();

        Text bottomText = new Text("BottomText\n\n");

        bottomBox.getChildren().addAll(bottomText);
        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomBox.setStyle("-fx-background-color: rgba(255,0,255,0.5)");
        Text titleText = new Text("Professor");
        titleBox.setStyle("-fx-background-color: rgba(255,255,255,0.5)");
        titleBox.getChildren().add(titleText);
        titleBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setAlignment(Pos.CENTER);
        StackPane rightSide = new StackPane();
        rightSide.setMinWidth(200);
        StackPane leftSide  = new StackPane();
        leftSide.setMinWidth(200);
        Text titleText1 = new Text("Left");
        Text titleText2 = new Text("Right");
        leftSide.getChildren().addAll(titleText1);
        rightSide.getChildren().addAll(titleText2);
        mainBox.getChildren().addAll(leftSide,rightSide);
        rightSide.setStyle("-fx-background-color: rgba(255,255,0,0.5)");
        leftSide.setStyle("-fx-background-color: rgba(0,255,255,0.5)");
        HBox.setHgrow(leftSide, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(rightSide, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(mainBox, javafx.scene.layout.Priority.ALWAYS);
        base.getChildren().addAll(titleBox,mainBox,bottomBox);
        root.getChildren().addAll(base);
    }


    public static void main(String[] args){
        launch(args);
    }
}

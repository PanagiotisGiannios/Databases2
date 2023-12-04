package code;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Page extends Application {
    private String path = System.getProperty("user.dir") + "\\universitymanager\\images\\";
    private String pagePath = path + "\\pages\\";
    private String logo = "university.png";
    private String background = "uniPage.png";
    private String title = "University Management";
    public Stage primaryStage;
    public StackPane root;

    // Change logo image
    public void setLogo(String logo) {
        this.logo = logo;
    }

    // Change background image
    public void setBackground(String background) {
        this.background = background;
    }

    // Change the pagePath
    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    // Change the path
    public void setPath(String path) {
        this.path = path;
    }
    
    // Change the primaryStage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Change the title
    public void setTitle(String title) {
        this.title = title;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        initializePage();
    }

    public void initializePage() {
        loadBackground();
        loadLogo();   
    }

    public void loadLogo() {
        File imageFile = new File(path + logo);
        Image logo = new Image(imageFile.toURI().toString());
        primaryStage.getIcons().add(logo);
    }

    public void loadLogo(String image) {
        setLogo(image);
        loadLogo();
    }

    public void loadBackground() {
        // Load the background image
        Image backgroundImage = new Image("file:" + pagePath + background);
    
        // Create an ImageView for the background
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false); // Allow stretching
    
        // Create a StackPane as the root with the ImageView as a child
        StackPane root = new StackPane(backgroundView);
    
        // Set the background color using a hex value
        String backgroundColor = "#789D7A";
        root.setStyle("-fx-background-color: " + backgroundColor + ";" +
                      "-fx-background-image: url('file:" + pagePath + "uniPage.png');" +
                      "-fx-background-size: cover;"); // 'cover' ensures the image covers the entire StackPane
    
        // Bind the size of the StackPane to the size of the Scene
        root.prefWidthProperty().bind(primaryStage.widthProperty());
        root.prefHeightProperty().bind(primaryStage.heightProperty());
        this.root = root;

        // Set up the scene and stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void loadBackground(String image) {
        setBackground(image);
        loadBackground();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

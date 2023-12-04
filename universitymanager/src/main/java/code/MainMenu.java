package code;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainMenu extends VBox {
    public MainMenu() {
        initializeComponents();
    }

    private void initializeComponents() {
        // Add components to the MainMenu
        Button button1 = new Button("Option 1");
        Button button2 = new Button("Option 2");
        Button button3 = new Button("Option 3");

        // Add event handlers for the buttons if needed

        // Customize the appearance or layout if necessary

        // Set spacing, alignment, and padding
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));

        // Add components to the MainMenu
        getChildren().addAll(button1, button2, button3);
    }
}

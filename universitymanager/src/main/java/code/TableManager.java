package code;

import java.sql.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;

public class TableManager {
    public static String ssnSelected;

    public static TableView<ObservableList<String>> CreateTableView(ResultSet resultSet, String type) throws SQLException{
        
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        //Creates List of Strings for each row of our resultSet and puts it in data.
        //This way we have all columns and rows in our resultSet as Strings inside a List of List of Strings.
        while (resultSet.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= columnCount; i++) {
                row.add(resultSet.getString(i));
            }

            data.add(row);
        }
        TableView<ObservableList<String>> tableView = new TableView<>(data);
        //Creates the columns necessary for displaying the tableView correctly.
        for (int i = 1; i <= columnCount; i++) {
            int index = i-1;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(metaData.getColumnLabel(i));
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(index)));
            tableView.getColumns().add(column);
        }
        Label placeHolderLabel = new Label("No "+ type +" matches current filters!");
        placeHolderLabel.setFont(Font.font(25));
        tableView.setPlaceholder(placeHolderLabel);
        return tableView;

    }

    /**
     * Set up event handler for when an entry is selected, when an entry is selected
     * set [ssnSelected] to the ssn of the selected row in the [tableView] if that
     * row was already selected it now becomes deselected.
     * @param tableView
     */
    public static void setUpMouseReleased(TableView<ObservableList<String>> tableView){
        tableView.setOnMouseReleased(e -> handleMouseReleased(tableView));
    }

    private static void handleMouseReleased(TableView<ObservableList<String>> tableView){
        ObservableList<String> selectedRow = tableView.getSelectionModel().getSelectedItem();
        if(selectedRow != null && !selectedRow.isEmpty()){
            System.out.println(selectedRow.get(0));
            if(selectedRow.get(0).equals(ssnSelected)){
                tableView.getSelectionModel().clearSelection();
                ssnSelected = null;
            }
            else{
                ssnSelected = selectedRow.get(0);
            }
        }
    }
}

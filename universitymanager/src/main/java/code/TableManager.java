package code;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;

public class TableManager {
    public static String selectedId;
    public static List<String> selectedRowIdList = new ArrayList<String>();
    private static Boolean allowMultipleRowSelection = false;

    public static TableView<ObservableList<String>> CreateTableView(ResultSet resultSet, String type) throws SQLException{
        
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        //Creates List of Strings for each row of our resultSet and puts it in data.
        //This way we have all columns and rows in our resultSet as Strings inside a List of List of Strings.
        while (resultSet.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= columnCount; i++) {
                if(resultSet.getString(i) != null){
                    row.add(resultSet.getString(i));
                }
                else if(metaData.getColumnLabel(i).equals("Rector ID") && resultSet.getString(i) == null){
                    row.add("Rector");
                }
                else{
                    row.add("-");
                }
            }

            data.add(row);
        }
        TableView<ObservableList<String>> tableView = new TableView<>(data);
        setAllowMultipleRowSelection(false);
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
     * Set wether the user can select multiple rows at once 
     * when  this is automatically set to false
     * to avoid situations where it is never needed to select multiple
     * rows after we change scenes but forget to set this to false
     * @param allow True if they can select multiple rows False if not
     */
    public static void setAllowMultipleRowSelection(Boolean allow){
        allowMultipleRowSelection = allow;
    }

    /**
     * Set up event handler for when an entry is selected, when an entry is selected
     * if we allow multiple selected rows we 
     * @param tableView
     */
    public static void setUpMouseReleased(TableView<ObservableList<String>> tableView){
        tableView.setOnMouseReleased(e -> handleMouseReleased(tableView));
    }

    private static void handleMouseReleased(TableView<ObservableList<String>> tableView){
        if(allowMultipleRowSelection){
            handleMultipleSelection(tableView);
        }
        else{
            handleSingleSelection(tableView);
        }
    }

    private static void handleSingleSelection(TableView<ObservableList<String>> tableView){
        ObservableList<String> selectedRow = tableView.getSelectionModel().getSelectedItem();
        if(selectedRow != null && selectedRow.isEmpty()){
            selectedId = null;
            return;
        }
        if(selectedRow != null){
            if(selectedRow.get(0).equals(selectedId)){
                tableView.getSelectionModel().clearSelection();
                selectedId = null;
            }
            else{
                selectedId = selectedRow.get(0);
            }
        }
    }

    private static void handleMultipleSelection(TableView<ObservableList<String>> tableView){
        ObservableList<ObservableList<String>> selectedRows = tableView.getSelectionModel().getSelectedItems();
        if(selectedRows.isEmpty()){
            selectedRowIdList.clear();
            return;
        }
        if(selectedRows != null){
            List<String> tempList = new ArrayList<String>();
            for (ObservableList<String> row : selectedRows) {
                tempList.add(row.get(0));
            }
            selectedRowIdList = tempList;
        }
        System.out.println(tableView.getSelectionModel().getSelectedIndices());
    }
}

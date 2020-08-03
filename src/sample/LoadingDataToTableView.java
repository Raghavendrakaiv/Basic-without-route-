package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;


public class LoadingDataToTableView {



    static boolean Welcome(String query, Pane viewTablePane) throws Exception {
        ObservableList<ObservableList> data;

        TableView tableView;
        tableView = new TableView();
        tableView.setMaxHeight(200);
        Connection c=null;
        data = FXCollections.observableArrayList();
        try {
            c = DBConnect.connect();

            ResultSet rs = c.createStatement().executeQuery(query);
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tableView.getColumns().addAll(col);
            }
            int k = 0;
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();

                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if(rs.getString(i)==null)
                    {
                        row.add(" ");
                    }
                    else {
                        row.add(rs.getString(i));
                    }

                }
                data.add(row);
                k++;
                if (k < 1) {
                }
                tableView.setItems(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);
        }finally {
            try {
                c.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return viewTablePane.getChildren().setAll(tableView);

    }

    static boolean Welcome(String query, Pane viewTablePane,double height) throws Exception {
        ObservableList<ObservableList> data;

        TableView tableView;
        tableView = new TableView();
        tableView.setPrefHeight(450);
        tableView.setPrefWidth(850);
        Connection c=null;
        data = FXCollections.observableArrayList();
        try {
            c = DBConnect.connect();

            ResultSet rs = c.createStatement().executeQuery(query);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));

                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);
            }
            int k = 0;
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();

                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if(rs.getString(i)==null)
                    {
                        row.add(" ");
                    }
                    else {
                        row.add(rs.getString(i));
                    }

                }
                data.add(row);
                k++;
                if (k < 1) {
                }
                tableView.setItems(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);
        }finally {
            try {
                c.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return viewTablePane.getChildren().setAll(tableView);
    }

    static boolean Welcome(String query, Pane viewTablePane,double height,double width) throws Exception {
        ObservableList<ObservableList> data;

        TableView tableView;
        tableView = new TableView();
        tableView.setMaxSize(width,height);
        tableView.setMinSize(width,height);

        Connection c=null;
        data = FXCollections.observableArrayList();
        try {
            c = DBConnect.connect();

            ResultSet rs = c.createStatement().executeQuery(query);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);

            }
            int k = 0;
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();

                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if(rs.getString(i)==null)
                    {
                        row.add(" ");
                    }
                    else {
                        row.add(rs.getString(i));
                    }
                }
                data.add(row);
                k++;
                if (k < 1) {
                }
                tableView.setItems(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);

        }finally {
            try {
                c.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return viewTablePane.getChildren().setAll(tableView);
    }
    public static TableView tableView;
    static boolean Welcome(String query, Pane viewTablePane, double height, double  width, double font) throws Exception {
        ObservableList<ObservableList> data;
        tableView = new TableView();
        tableView.setEditable(true);
        tableView.setMaxSize(width,height);
        tableView.setMinSize(width,height);
        tableView.setStyle("-fx-font-size: "+font+"%;");
        Connection c=null;
        data = FXCollections.observableArrayList();
        try {
            c = DBConnect.connect();

            ResultSet rs = c.createStatement().executeQuery(query);
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;


                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);
            }
            int k = 0;
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();

                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if(rs.getString(i)==null)
                    {
                        row.add(" ");
                    }
                    else {
                        row.add(rs.getString(i));
                    }

                }
                data.add(row);
                k++;
                if (k < 1) {

                }
                tableView.setItems(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);

        }finally {
            try {
                c.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return viewTablePane.getChildren().setAll(tableView);
    }


    public static ObservableList selectItem() {
        ObservableList selectedItems = tableView.getSelectionModel().getSelectedItems();
        return selectedItems;
    }

}
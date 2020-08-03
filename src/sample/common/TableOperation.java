package sample.common;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import sample.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;


public class TableOperation {

    public static boolean load(String query, TableView tableView) {
        ObservableList<ObservableList> data;
        tableView.getItems().removeAll(tableView.getItems());
        tableView.getColumns().removeAll(tableView.getColumns());
        tableView.setMaxSize(tableView.getPrefWidth(), tableView.getPrefHeight());
        tableView.setMinSize(tableView.getPrefWidth(), tableView.getPrefHeight());
        Connection c = null;
        data = FXCollections.observableArrayList();
        try {
            c = DBConnect.connect();

            ResultSet rs = c.createStatement().executeQuery(query);
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;

                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
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
                    row.add(rs.getString(i));
                }
                data.add(row);
                k++;
                if (k < 1) {

                }
                tableView.setItems(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);

        } finally {
            try {
                if(!c.isClosed())c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static ObservableList selectItem( TableView tableView ) {
        ObservableList selectedItems = tableView.getSelectionModel().getSelectedItems();
        return selectedItems;
    }

    public static String selectItemId(TableView tableView) {
        String data = "NODATA";
        sample.P.p("tableView : "+tableView);
        ObservableList oa = tableView.getSelectionModel().getSelectedItems();
        P.p("oa : "+oa);
        ArrayList aa = new ArrayList();
        aa.add(oa.get(0));
        String old = String.valueOf(aa.get(0));
        ArrayList<String> bb = new ArrayList<String>();

        old = old.replace("[", "");
        old = old.replace("]", "");

        String log1[] = old.split(",");
        try {
            bb.add(log1[0].trim());
            bb.add(log1[1].trim());
            data = bb.get(0);
        } catch (Exception e) {
            data = "NODATA";
        }
        return  data;
    }

    public static String selectItemData(int i, TableView tableView) {
        String data = "NODATA";
        ObservableList oa = TableOperation.selectItem(tableView);
        ArrayList aa = new ArrayList();
        aa.add(oa.get(0));
        String old = String.valueOf(aa.get(0));
        ArrayList<String> bb = new ArrayList<String>();

        old = old.replace("[", "");
        old = old.replace("]", "");

        String log1[] = old.split(",");
        try {
            bb.add(log1[0].trim());
            bb.add(log1[1].trim());
            data = log1[i-1].trim();
        } catch (Exception e) {
            data = "NODATA";
        }
        return  data;
    }




}

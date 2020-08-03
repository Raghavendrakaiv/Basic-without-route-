package sample;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;


public class LoadingTableView {


    public static TableView tableView, tableView2,tableView3;


    public static boolean load(String query, Pane viewTablePane, double height, double width) {
        ObservableList<ObservableList> data;
        tableView = new TableView();
        tableView.setEditable(true);
        tableView.setMaxSize(width, height);
        tableView.setMinSize(width, height);
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
            try {
                P.loggerLoader(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } finally {
            try {
                if(!c.isClosed())c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return viewTablePane.getChildren().setAll(tableView);
    }

    public static ObservableList selectItem() {
        ObservableList selectedItems = tableView.getSelectionModel().getSelectedItems();
        return selectedItems;
    }

    public static String selectItemId() {
        String data = "NODATA";
        ObservableList oa = LoadingTableView.selectItem();
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

    public static String selectItemData(int i) {
        String data = "NODATA";
        ObservableList oa = LoadingTableView.selectItem();
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


    public static boolean load2(String query, Pane viewTablePane, double height, double width) throws Exception {
        ObservableList<ObservableList> data;
        tableView2 = new TableView();
        tableView2.setEditable(true);
        tableView2.setMaxSize(width, height);
        tableView2.setMinSize(width, height);
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

                tableView2.getColumns().addAll(col);
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
                tableView2.setItems(data);
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
        return viewTablePane.getChildren().setAll(tableView2);
    }

    public static ObservableList selectItem2() {
        ObservableList selectedItems = tableView2.getSelectionModel().getSelectedItems();
        return selectedItems;
    }

    public static String selectItemId2() {
        String data = "NODATA";
        ObservableList oa = LoadingTableView.selectItem2();
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

    public static String selectItemData2(int i) {
        String data = "NODATA";
        ObservableList oa = LoadingTableView.selectItem2();
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

    public static boolean load3(String query, Pane viewTablePane, double height, double width) {
        ObservableList<ObservableList> data;
        tableView3 = new TableView();
        tableView3.setEditable(true);
        tableView3.setMaxSize(width, height);
        tableView3.setMinSize(width, height);
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

                tableView3.getColumns().addAll(col);
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
                tableView3.setItems(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                P.loggerLoader(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } finally {
            try {
                if(!c.isClosed())c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return viewTablePane.getChildren().setAll(tableView3);
    }

    public static ObservableList selectItem3() {
        ObservableList selectedItems = tableView3.getSelectionModel().getSelectedItems();
        return selectedItems;
    }

    public static String selectItemId3() {
        String data = "NODATA";
        ObservableList oa = LoadingTableView.selectItem3();
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

    public static String selectItemData3(int i) {
        String data = "NODATA";
        ObservableList oa = LoadingTableView.selectItem3();
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

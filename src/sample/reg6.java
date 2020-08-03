package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.DAYS;

public class reg6 implements Initializable {

    public TextField customer_Id;
    public TextField customer_name;
    public TextField phone_no;
    public TextField state;
    public TextField gstin_no;
    public TextField address;
    public Pane edit_pane;
    public Button edit;
    public Button delete;
    public DatePicker date1;
    public DatePicker date2;
    public Pane payment_view_pane;


    Connection connection = null;

    @FXML
    private Pane view_reg_pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            view_info();
            edit.setVisible(true);
            delete.setVisible(true);
            payment_view_pane.setVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gen_reg_rep(ActionEvent event) throws Exception {

        view_info();
    }

    @FXML
    void gen_reg_rep1(ActionEvent event) throws Exception {
        String query = "select * from sp_table";
        LoadingDataToTableView.Welcome(query, view_reg_pane, 250, 850);
        edit.setVisible(false);
        delete.setVisible(false);
        payment_view_pane.setVisible(true);
    }

    void view_info() throws Exception {

        edit.setVisible(true);
        delete.setVisible(true);
        payment_view_pane.setVisible(false);
        String query = "select * from customer";
        LoadingTableViewDataSelectedRowName.Welcome(query, view_reg_pane, 250, 850);
        edit_pane.setVisible(false);
    }

    @FXML
    void creditReportPDF(ActionEvent event) throws Exception {

        payment_view_pane.setVisible(true);
        File dir = new File(P.drive_name() + FilePath.FOLDER_PATH);
        dir.mkdir();
        File dir2 = new File(P.drive_name() + FilePath.FOLDER_PATH + "CREDIT_CUSTOMER_REPORT");
        dir2.mkdir();
        String path = P.drive_name() + FilePath.FOLDER_PATH + "/CREDIT_CUSTOMER_REPORT/credit_customer_report.csv";
        String query = "";

        if (date1.getValue() == null && date2.getValue() == null) {
            query = "select * from sp_table";
            P.p("query1---"+query);

        } else if (date1.getValue() != null && date2.getValue() == null) {
            query = "select * from sp_table where sp_pay_date='" + date1.getValue() + "'";
            P.p("query2---"+query);

        } else if (date1.getValue() == null && date2.getValue() != null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("SELECT STARTING DATE");
            alert.showAndWait();
        } else if (date1.getValue() != null && date2.getValue() != null) {
            long daysBetween = DAYS.between(date1.getValue(), date2.getValue());
            if(daysBetween<0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("SELECT CORRECT DATE");
                alert.showAndWait();
            }
            else
            {
                query = "select * from sp_table where sp_pay_date>='" + date1.getValue() + "' and sp_pay_date<='"+date2.getValue()+"'";
                P.p("query3---"+query);
            }
        }
        Controller.export_excel(query, path);
    }

    public void edit_customer(ActionEvent event) throws Exception {
        ObservableList oa = LoadingTableViewDataSelectedRowName.selectItem();

        ArrayList aa = new ArrayList();
        aa.add(oa.get(0));

        ArrayList newArray = new ArrayList();
        newArray = aa;
        String old = String.valueOf(newArray.get(0));
        ArrayList<String> bb = new ArrayList<String>();
        int check = 0;

        old = old.replace("[", "");
        old = old.replace("]", "");

        String log[] = old.split(",");
        String log1[] = old.split(",");

        bb.add(log1[0].trim());
        try {
            bb.add(log1[1].trim());
            edit_pane.setVisible(true);
        } catch (Exception e) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("PLEASE SELECT CUSTOMER");
            a.showAndWait();
            check++;
        }
        if (check == 0) {
            try {
                connection = DBConnect.getConnection();

                int c_id = Integer.parseInt(bb.get(0).trim());

                String query = "Select * from customer where customerid='" + c_id + "'";
                ResultSet rs = connection.createStatement().executeQuery(query);

                while (rs.next()) {
                    customer_Id.setText(rs.getString(1));
                    customer_name.setText(rs.getString(2));
                    phone_no.setText(rs.getString(3));
                    state.setText(rs.getString(4));
                    gstin_no.setText(rs.getString(5));
                    address.setText(rs.getString(6));
                }
            } catch (Exception e) {
                e.printStackTrace();
                P.loggerLoader(e);
            }
        }
    }



    public void delete_customer(ActionEvent event) throws Exception {

        ObservableList oa = LoadingTableViewDataSelectedRowName.selectItem();

        ArrayList aa = new ArrayList();
        aa.add(oa.get(0));

        ArrayList newArray = new ArrayList();
        newArray = aa;
        String old = String.valueOf(newArray.get(0));
        ArrayList<String> bb = new ArrayList<String>();
        int check = 0;

        old = old.replace("[", "");
        old = old.replace("]", "");

        String log[] = old.split(",");
        String log1[] = old.split(",");

        bb.add(log1[0].trim());
        try {
            bb.add(log1[1].trim());
        } catch (Exception e) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("PLEASE SELECT THE ITEM FROM TABLE");
            a.showAndWait();
            check++;
        }
        if (check == 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("ARE YOU SURE YOU WANT TO DELETE CUSTOMER RECORD");
            Optional<ButtonType> result = alert.showAndWait();

            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                try {
                    connection = DBConnect.getConnection();
                    int c_id = Integer.parseInt(bb.get(0).trim());

                    String query = "delete from customer where customerid='" + c_id + "'";
                    PreparedStatement ps = connection.prepareStatement(query);
                    int i = ps.executeUpdate();

                    if (i > 0) {
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setContentText("CUSTOMER INFORMATION DELETED SUCCESSFULLY");
                        a.showAndWait();
                        view_info();
                    } else {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("ERROR IN DELETING DEALER INFORMATION");
                        a.showAndWait();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsString = sw.toString();
                    Logger.log(exceptionAsString);
                }
            }
        }
    }

    public void save_customer(ActionEvent event) throws Exception {

        if (customer_name.getText().trim().isEmpty()) {

            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER CUSTOMER NAME");
            alert1.showAndWait();
        } else if (phone_no.getText().trim().isEmpty() || phone_no.getText().trim().length() != 10) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER CONTACT NUMBER");
            alert1.showAndWait();
        } else {
            try {

                connection = DBConnect.getConnection();

                PreparedStatement ps = connection.prepareStatement
                        ("update customer set " +
                                "customername='" + customer_name.getText().trim() + "', " +
                                "phoneno='" + phone_no.getText().trim() + "' ,  " +
                                " state_statecode='" + state.getText().trim() + "' ,    " +
                                "gstinno='" + gstin_no.getText().trim() + "' ,  " +
                                "customeraddress='" + address.getText().trim() + "'" +
                                " where customerid='" + customer_Id.getText().trim() + "'");

                int i = ps.executeUpdate();

                if (i > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("CUSTOMER INFORMATION UPDATED SUCCESSFULLY");
                    alert.showAndWait();

                    refresh_customer();
                    view_info();
                    edit_pane.setVisible(false);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN SAVING CUSTOMER INFORMATION");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);

            } finally {
                if (connection != null)
                    connection.close();
            }
        }
    }

    void refresh_customer() throws Exception {
        customer_name.clear();
        customer_Id.clear();
        phone_no.clear();
        address.clear();
        state.clear();
        gstin_no.clear();

    }

    public void view_payment_details(ActionEvent event) throws Exception {

        String query = "";
        if (date1.getValue() == null && date2.getValue() == null) {
            query = "select * from sp_table";
            P.p("query1---"+query);
            LoadingDataToTableView.Welcome(query, view_reg_pane, 250, 950);

        } else if (date1.getValue() != null && date2.getValue() == null) {
            query = "select * from sp_table where sp_pay_date='" + date1.getValue() + "'";
            P.p("query2---"+query);
            LoadingDataToTableView.Welcome(query, view_reg_pane, 250, 950);

        } else if (date1.getValue() == null && date2.getValue() != null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("SELECT STARTING DATE");
            alert.showAndWait();
        } else if (date1.getValue() != null && date2.getValue() != null) {
            long daysBetween = DAYS.between(date1.getValue(), date2.getValue());
            if(daysBetween<0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("SELECT CORRECT DATE");
                alert.showAndWait();
            }
            else
            {
                query = "select * from sp_table where sp_pay_date>='" + date1.getValue() + "' and sp_pay_date<='"+date2.getValue()+"'";
                P.p("query3---"+query);
                LoadingDataToTableView.Welcome(query, view_reg_pane, 250, 950);
            }
        }
    }
}
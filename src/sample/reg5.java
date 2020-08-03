package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class reg5 implements Initializable {

    public Pane edit_pane;
    public TextField dealer_Id;
    public TextField dealer_name;
    public TextField dealer_gstin;
    public TextField c_number;
    public TextField pan_number;
    public TextField a_details;
    public TextField address;

    Connection connection=null;

    @FXML
    private Pane view_reg_pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            view_info();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gen_reg_rep(ActionEvent event) throws Exception  {
        view_info();
    }

    void view_info() throws Exception {

        String query="select dealer_id, dealer_name, dealer_gstn_number, dealer_contact_number, dealer_pan," +
                "dealer_account_details, dealer_address from dealer";
        LoadingTableViewDataSelectedRowName.Welcome(query, view_reg_pane,250,950);
        edit_pane.setVisible(false);
    }

    @FXML
    void dealerPrintRepT(ActionEvent event) throws Exception {

        String query = "SELECT * FROM dealer";

        File dir = new File(P.drive_name()+FilePath.FOLDER_PATH);
        dir.mkdir();
        File dir2=new File(P.drive_name()+FilePath.FOLDER_PATH+"DEALER_REPORT");
        dir2.mkdir();
        String path=P.drive_name()+FilePath.FOLDER_PATH+"/DEALER_REPORT/Dealer_report.csv";

        Controller.export_excel(query,path);

    }

    public void edit_dealer(ActionEvent event) throws Exception {

        ObservableList oa = LoadingTableViewDataSelectedRowName.selectItem();

        ArrayList aa = new ArrayList();
        aa.add(oa.get(0));

        ArrayList newArray = new ArrayList();
        newArray = aa;
        String old = String.valueOf(newArray.get(0));
        ArrayList<String> bb = new ArrayList<String>();

        old = old.replace("[", "");
        old = old.replace("]", "");

        String log[] = old.split(",");
        String log1[] = old.split(",");

        bb.add(log1[0]);
        try {
            bb.add(log1[1]);
            edit_pane.setVisible(true);
        } catch (Exception e) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("PLEASE SELECT DEALER");
            a.showAndWait();
        }
        try
        {
            connection=DBConnect.getConnection();
            int d_id = Integer.parseInt(bb.get(0));
            String query="Select * from dealer where dealer_id='"+d_id+"'";
            ResultSet rs=connection.createStatement().executeQuery(query);
            while (rs.next())
            {
                dealer_Id.setText(rs.getString(1));
                dealer_name.setText(rs.getString(2));
                dealer_gstin.setText(rs.getString(3));
                c_number.setText(rs.getString(4));
                pan_number.setText(rs.getString(5));
                a_details.setText(rs.getString(6));
                address.setText(rs.getString(7));

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);
        }
    }


    public void delete_dealer(ActionEvent event) throws Exception {

        ObservableList oa = LoadingTableViewDataSelectedRowName.selectItem();

        ArrayList aa = new ArrayList();
        aa.add(oa.get(0));

        ArrayList newArray = new ArrayList();
        newArray = aa;
        String old = String.valueOf(newArray.get(0));
        ArrayList<String> bb = new ArrayList<String>();

        old = old.replace("[", "");
        old = old.replace("]", "");

        String log[] = old.split(",");
        String log1[] = old.split(",");

        bb.add(log1[0]);
        try {
            bb.add(log1[1]);
        } catch (Exception e) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("PLEASE SELECT THE ITEM FROM TABLE");
            a.showAndWait();
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("ARE YOU SURE YOU WANT TO DELETE DEALER RECORD");
        Optional<ButtonType> result = alert.showAndWait();

        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            try {
                connection = DBConnect.getConnection();
                int d_id = Integer.parseInt(bb.get(0));

                String query = "delete from dealer where dealer_id='" + d_id + "'";
                PreparedStatement ps = connection.prepareStatement(query);
                int i = ps.executeUpdate();

                if (i > 0) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("DEALER INFORMATION DELETED SUCCESSFULLY");
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


    public void save_dealer(ActionEvent event) throws Exception {

        if (dealer_name.getText().isEmpty()) {

            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER DEALER NAME");
            alert1.showAndWait();
        }
        else if(c_number.getText().isEmpty())
        {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER CONTACT NUMBER");
            alert1.showAndWait();
        }
        else {
            try {

                connection = DBConnect.getConnection();

                PreparedStatement ps = connection.prepareStatement("update dealer set dealer_name='"+dealer_name.getText().trim()+"'," +
                        "dealer_gstn_number='"+dealer_gstin.getText().trim()+"' , dealer_contact_number='"+c_number.getText().trim()+"' , " +
                        "dealer_pan='"+pan_number.getText().trim()+"' , dealer_account_details='"+a_details.getText().trim()+"' , " +
                        "dealer_address='"+address.getText().trim()+"' where dealer_id='"+dealer_Id.getText().trim()+"'");

                int i = ps.executeUpdate();

                if(i>0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("DEALER INFORMATION UPDATED SUCCESSFULLY");
                    alert.showAndWait();

                    refresh_dealer();
                    view_info();
                    edit_pane.setVisible(false);
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN SAVING DEALER INFORMATION");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);

            } finally
            {
                if(connection!=null)
                    connection.close();
            }
        }
    }

    void refresh_dealer()
    {
        dealer_name.clear();
        dealer_Id.clear();
        dealer_gstin.clear();
        address.clear();
        c_number.clear();
        pan_number.clear();
        a_details.clear();
    }
}

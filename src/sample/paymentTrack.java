
package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Mitra on 9/28/2017.
 */
public class paymentTrack implements Initializable {

    public TextField invoivenumber;
    public TextField customerName;
    public DatePicker datestart1;
    public DatePicker dateend1;
    public TextField customerNameB;
    public TextField customerIdB;
  //  public ComboBox routeName;


    @FXML
    private Pane paymentdpane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String q1="select * from sp_table limit 100";
        try {
            LoadingTableViewDataSelectedRowName.Welcome(q1,paymentdpane,360,950);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Connection connection=null;
      /*  try{
            connection = DBConnect.getConnection();
            routeName.getItems().removeAll(routeName.getItems());
            ResultSet set   =   connection.createStatement().executeQuery("select distinct(route_Name) from customerroutes");
            routeName.getItems().add("ALL ROUTE");
            while (set.next()){
                routeName.getItems().add(set.getString(1));
            }
            routeName.setValue("ALL ROUTE");
        }catch (Exception e){
            e.printStackTrace();
            try {
                P.loggerLoader(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
            if(!connection.isClosed()){ connection.close();     } }catch (SQLException e) {e.printStackTrace();    }
        }*/

    }

    public void loadPaneByInvoiceNumber(ActionEvent actionEvent) throws Exception {
        String q="select * from sp_table where billAmount>0 ";
        if(((invoivenumber.getText().trim().isEmpty())  &&   (customerName.getText().trim().isEmpty())  &&
                (datestart1.getValue()==null)  &&  (dateend1.getValue()==null) )) {
            Alert alerte=new Alert(Alert.AlertType.ERROR);
            alerte.setContentText("TO SEARCH THE PAYMENT DATA PLEASE ENTER ATLEAST ENTER \"INVOICE NO\"  OR \"CUSTOMER NAME\"  OR  \"PAYEMENT DATE FROM AND TO\"");
            alerte.showAndWait();
        }else{
            if (!invoivenumber.getText().trim().isEmpty()) {
                q = q + " and invoice_no='" + invoivenumber.getText().trim() + "' ";
            }
            if (!customerName.getText().trim().isEmpty()) {
                q = q + " and upper(customer_name)='" + customerName.getText().trim().toUpperCase() + "' ";
            }
            if (!(datestart1.getValue()==null)) {
                q = q + " and sp_pay_date>='" + datestart1.getValue() + "' ";
            }
            if (!(dateend1.getValue()==null)) {
                q = q + " and sp_pay_date<='" + dateend1.getValue() + "' ";
            }

            P.p("qqqqqqqqqqqqqqqq   :   "+q);
            LoadingTableViewDataSelectedRowName.Welcome(q, paymentdpane, 360, 950);
        }
    }

    public void loadExcelByInvoiceNumber(ActionEvent event) throws Exception {
        String q="select * from sp_table where billAmount>0 ";
        if(((invoivenumber.getText().trim().isEmpty())  &&   (customerName.getText().trim().isEmpty())  &&
                (datestart1.getValue()==null)  &&  (dateend1.getValue()==null) )) {
            Alert alerte=new Alert(Alert.AlertType.ERROR);
            alerte.setContentText("To search the payment details,  atleast enter \"INVOICE NO\"  or \"CUSTOMER NAME\"  or  \"PAYEMENT DATE FROM and TO\"");
            alerte.showAndWait();
        }else{
            String aa="";
            if (!invoivenumber.getText().trim().isEmpty()) {
                q = q + " and invoice_no='" + invoivenumber.getText().trim() + "' ";
                aa=aa+("_("+invoivenumber.getText().trim()+")");
            }
            if (!customerName.getText().trim().isEmpty()) {
                q = q + " and upper(customer_name)='" + customerName.getText().trim().toUpperCase() + "' ";
                aa=aa+("_("+customerName.getText().trim()+")");
            }
            if (!(datestart1.getValue()==null)) {
                q = q + " and sp_pay_date>='" + datestart1.getValue() + "' ";
                aa=aa+("_("+datestart1.getValue()+")");
            }
            if (!(dateend1.getValue()==null)) {
                q = q + " and sp_pay_date<='" + dateend1.getValue() + "' ";
                aa=aa+("_("+dateend1.getValue()+")");
            }

            P.p("qq   :   "+q);
            P.p("P.drive_name() : "+ P.drive_name());
            P.p("P.path : "+ P.drive_name());
            new File(P.drive_name()+ FilePath.FOLDER_PATH).mkdir();
            new File(P.drive_name()+ FilePath.PAYEMNT_DETAILS_PATH).mkdir();
            String path= P.drive_name()+ FilePath.PAYEMNT_DETAILS_PATH+"Payment_details"+aa+".xlsx";
            Controller.export_excel(q,path);
        }
    }

    public void loadPaneByCustomerBalanceWise(ActionEvent event) throws Exception {
      /*  String condition    =   " ";
       *//* if(!routeName.getValue().equals("ALL ROUTE")){
            condition   = condition+" and route_Name = '"+routeName.getValue().toString()+"' ";
        }*//*
        String q="SELECT customername, phoneno, state_statecode, customeraddress, balance from customer where balance>0 "+condition+" order by route_Name ";
        P.p("String q  : "+q);
        LoadingTableViewDataSelectedRowName.Welcome(q, paymentdpane, 360, 950);
  */  }

    public void loadExcelByCustomerBalanceWise(ActionEvent event) throws Exception {
        /*        String condition    =   " ";
         *//* if(!routeName.getValue().equals("ALL ROUTE")){
            condition   = condition+" and route_Name = '"+routeName.getValue().toString()+"' ";
        }*//*
        String q="SELECT customername, phoneno, state_statecode, customeraddress, balance  from customer where balance>0 "+condition+" order by route_Name ";
        P.p("String q  : "+q);
        new File(P.drive_name()+ FilePath.FOLDER_PATH).mkdir();
        new File(P.drive_name()+ FilePath.PAYEMNT_DETAILS_PATH).mkdir();
        String path= P.drive_name()+ FilePath.PAYEMNT_DETAILS_PATH+"CUSTOMER_CREDIT_BALANCE_REPORT.xlsx";
        Controller.export_excel(q,path);
    }*/
    }
    public void loadPaneByInvoiceBalanceWise(ActionEvent event) throws Exception {
        String condition    =   " ";
        if(!customerIdB.getText().trim().isEmpty()){
            condition   = condition+" and customer_id = '"+customerIdB.getText().trim()+"' ";
        }
        if(!customerNameB.getText().trim().isEmpty()){
            condition   = condition+" and customer_name = '"+customerNameB.getText().trim()+"' ";
        }
        String q="SELECT distinct(invoice_no),  bill_date,  customer_id, customer_name,  customer_phno, customer_address,  " +
                "customer_state,  grand_total,   payedAmount , (grand_total-payedAmount) as 'balance', bill_status   " +
                "from billing2 " +
                "where " +
                "(grand_total-payedAmount)>0 and bill_status='credit' "+condition+" order by customer_name desc";
        P.p(" qqq : q " + q);
        LoadingTableViewDataSelectedRowName.Welcome(q, paymentdpane, 360, 950);
    }

    public void loadExcelByInvoiceBalanceWise(ActionEvent event) throws Exception {
        String condition    =   " ";
        if(!customerIdB.getText().trim().isEmpty()){
            condition   = condition+" and customer_id = '"+customerIdB.getText().trim()+"' ";
        }
        if(!customerNameB.getText().trim().isEmpty()){
            condition   = condition+" and customer_name = '"+customerNameB.getText().trim()+"' ";
        }
        String q="SELECT distinct(invoice_no),  bill_date,  customer_id, customer_name,  customer_phno, customer_address,  " +
                "customer_state,  grand_total,   payedAmount , (grand_total-payedAmount) as 'balance', bill_status   " +
                "from billing2 " +
                "where " +
                "(grand_total-payedAmount)>0 and bill_status='credit' "+condition+" order by customer_name desc";
        P.p(" qqq : q " + q);
        new File(P.drive_name()+ FilePath.FOLDER_PATH).mkdir();
        new File(P.drive_name()+ FilePath.PAYEMNT_DETAILS_PATH).mkdir();
        String path= P.drive_name()+ FilePath.PAYEMNT_DETAILS_PATH+"INVOICE_CREDIT_BALANCE_REPORT.xlsx";
        Controller.export_excel(q,path);
    }
}

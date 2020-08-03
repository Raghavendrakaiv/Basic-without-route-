package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Created by Mitra on 06/25/18.
 */
public class CancellBilling implements Initializable{
    public DatePicker billDate;
    public TextField invoiceNumber;
    public TextField customerId;
    public TextField customerNameEditText;
    public TextField billAMount;
    public TextField deductionAmount;
    public TextField returnAmount;
    public DatePicker cancelDate;
    public Pane billListPane;
    public TextField cancelReason;
    public TextField alreadyCancelReason;

    public void loadInvoiceDetailsByInvoiceNumber(KeyEvent event) {
        if (!invoiceNumber.getText().trim().isEmpty())
        {
            loadDetailsByInvoiceNumber2(invoiceNumber.getText().trim());
            String query = "select product_id, bill_id, bill_date, customer_id, invoice_no, product_name, " +
                "product_code, quantity, packing, mrp, trade_price, discount_in_per, " +
                "discount_amount, taxable_value, cgst, cgst_amount, sgst, " +
                "sgst_amount, net_total  FROM billing WHERE invoice_no = '"+invoiceNumber.getText().trim()+ "'";
            LoadingTableView.load(query, billListPane, 230,900);
        }
    }

    public void loadDetailsByInvoiceNumber2(String d_invoice_no){
        Connection connection = null;
        try {
            connection = DBConnect.connect();

            String query3 = "select grand_total, customer_id , customer_name, bill_date from billing2 where invoice_no='" + d_invoice_no + "'";
            ResultSet set2 = connection.createStatement().executeQuery(query3);

            while (set2.next()) {
                customerNameEditText.setText(set2.getString("customer_name"));
                billAMount.setText(P.df00(set2.getString("grand_total")));
                customerId.setText(set2.getString("customer_id"));
                billDate.setValue(LocalDate.parse(set2.getString("bill_date")));
                deductionAmount.clear();
            }
            loadReturnAmont();

            ResultSet set3 = connection.createStatement().executeQuery("select * from billing2 where invoice_no='"+invoiceNumber.getText().trim()+"' and bill_status='cancelled'");
            if(set3.next()){
                alreadyCancelReason.setText(set3.getString("other_details"));
            }else{
                alreadyCancelReason.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                P.loggerLoader(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                if(!connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadReturnAmont() {
        double billAmmt = 0, deductioAmmt = 0, finalReturnAmmt = 0;

        try{ billAmmt = Double.parseDouble(billAMount.getText().trim());}catch (Exception e){}
        try{ deductioAmmt= Double.parseDouble(deductionAmount.getText().trim());}catch (Exception e){}
        finalReturnAmmt = billAmmt - deductioAmmt;
        returnAmount.setText(P.df00(finalReturnAmmt));
    }

    public void cancelBill(ActionEvent event) {
        Connection connection = null;
        try{
            connection = DBConnect.connect();
            int invoiceNo = 0;
            try{    invoiceNo = Integer.parseInt(invoiceNumber.getText().trim());   }catch (Exception e) {  }

            int count1 = 0, count2 = 0;
            String otherMsg = "",pid="",old_quantity="",bill_status="";
            double billquantity=0.0;
            boolean cancelled = false;
            ResultSet set1 = connection.createStatement().executeQuery("select * from billing where invoice_no='"+invoiceNo+"'");
            ResultSet set2 = connection.createStatement().executeQuery("select * from billing2 where invoice_no='"+invoiceNo+"'");

            ResultSet set3 = connection.createStatement().executeQuery("select * from billing2 where invoice_no='"+invoiceNo+"' and bill_status='cancelled'");


            while(set1.next()){ count1++; }
            if(set2.next()){
                otherMsg = set2.getString("other_details");
                count2++;
            }

            while(set3.next()){
                cancelled = true;
            }


            if(count1<=0 || count2<=0){ invoiceNo = 0; }
            if(invoiceNo==0){
                AlertMessage.error("PLEASE ENTER VALID INVOICE NUMBER");
            }else if(cancelled){
                AlertMessage.error("BILL IS ALREADY CANCELLED");
            }else {

                otherMsg = otherMsg + "\nCancelled Date : " + cancelDate.getValue() + "\nCancelled Reason : " + cancelReason.getText();

                String query1 = "UPDATE billing set bill_status='cancelled' where invoice_no = '" + invoiceNumber.getText().trim() + "'";
                String query2 = "UPDATE billing2 set bill_status='cancelled', other_details = '" + otherMsg + "'  where invoice_no = '" + invoiceNumber.getText().trim() + "'";
                ResultSet set4 = connection.createStatement().executeQuery("select sum(quantity) as quantity,product_id from billing where invoice_no='"+invoiceNo+"'");
                while(set4.next()){
                    billquantity = set4.getDouble("quantity");
                    pid = set4.getString("product_id");
                  //  bill_status = set4.getString("bill_status");
                }
                ResultSet r = connection.createStatement().executeQuery("Select * from stock where id='" + pid+ "'");
                while (r.next()) {
                    old_quantity = r.getString("stockquantity");

                }
                int j = 0, i = 0;
                double total_q = 0.0;
                total_q = billquantity + Double.parseDouble(old_quantity);

                i = connection.createStatement().executeUpdate("UPDATE stock SET stockquantity='" + total_q + "' where id='" + pid + "'");
                boolean updated = true;
                if (connection.createStatement().execute(query1)) {
                    updated = false;
                }
                if (connection.createStatement().execute(query2)) {
                    updated = false;
                }

                if (updated) {
                    AlertMessage.inform("Bill Has Been Cancelled successfully");

                    invoiceNumber.clear();
                    customerId.clear();
                    customerNameEditText.clear();
                    billDate.getEditor().clear();
                    cancelDate.setValue(LocalDate.now());
                    billAMount.clear();
                    returnAmount.clear();
                    returnAmount.clear();
                    cancelReason.clear();
                } else {
                    AlertMessage.error("Error while cancelling Bill");
                }


            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            try {   if(!connection.isClosed()){ connection.close();}   } catch (SQLException e) {  e.printStackTrace();    }
        }
    }

    public void calculateReturnAmount(KeyEvent event) {
        loadReturnAmont();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelDate.setValue(LocalDate.now());
    }
}

package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static sample.Billing.connection;

/**
 * Created by kings on 29-11-17.
 */
public class credit_payment_controler implements Initializable {

    public DatePicker c_date;
    public TextField bal_amount;
    public TextField pay_amount;
    public TextField c_phno;
    public ComboBox p_type;
    public TextField p_details;
    public Pane invoiceNoList;
    public Pane sp_table_pane;
    public TextField c_id;
    public TextField c_name;
    public TextField c_balance;


    Connection con=null;

    ZoneId zoneId = ZoneId.of("Indian/Maldives");
    LocalDate today = LocalDate.now(zoneId);

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        c_date.setValue(today);

        p_type.getItems().removeAll(p_type.getItems());
        p_type.getItems().addAll("Select Payment Type", "Cash", "Cheque", "Credit Card", "Online Banking");
        p_type.getSelectionModel().select("Select Payment Type");
    }


    public void search_bill_by_c_id(KeyEvent event) throws Exception {
        if(!c_id.getText().trim().isEmpty())
        {
            try {
                con = DBConnect.getConnection();
                double balance = 0, extraAmount=0;
                String query1 = "select distinct(invoice_no), grand_total, payedAmount , (grand_total-payedAmount) as 'balance', customer_id, customer_name from billing2 " +
                        "where payedAmount < grand_total and customer_id='"+c_id.getText().trim()+"' and bill_status='credit' order by id";
                LoadingTableViewDataSelectedRowName.Welcome(query1, invoiceNoList, 230, 590, 80);
                if(connection.isClosed()){  connection=DBConnect.getConnection();  }
                ResultSet set=connection.createStatement().executeQuery(
                        "select sum(grand_total-payedAmount) as 'balance', customer_name from billing2 " +
                        "where payedAmount < grand_total and customer_id='"+c_id.getText().trim()+"' and bill_status='credit'");
                ResultSet set1=connection.createStatement().executeQuery(
                        "select extra from customer " +
                                "where customerid='"+c_id.getText().trim()+"'");

                if(set.next()){
                    balance=set.getDouble(1);
                    c_name.setText(set.getString(2));
                }
                if(set1.next()){
                    extraAmount=set1.getDouble(1);
                }
                c_balance.setText(P.df00(balance-extraAmount));

                try{
                    String string1="select * from sp_table where customer_id='"+c_id.getText().trim()+"' order by sp_id desc limit 200";
                    LoadingDataToTableView.Welcome(string1, sp_table_pane, 230, 930, 90);
                }catch (Exception e){ e.printStackTrace(); P.loggerLoader(e);  }

            }
             catch (Exception e) {
                P.loggerLoader(e);
                e.printStackTrace();
            }finally {  if(!connection.isClosed()) connection.close(); }
        }
    }

    public void search_bill_by_c_name(KeyEvent event) throws Exception {
        String cNmae="";
        try{
            cNmae=c_name.getText().trim();
        }catch (Exception e){}
        if(!cNmae.isEmpty())
        {
            try {
                con = DBConnect.getConnection();
                double balance = 0, extraAmount=0;
                String query1 = "select distinct(invoice_no), grand_total, payedAmount, (grand_total-payedAmount) as 'balance', customer_id, customer_name from billing2 " +
                        "where payedAmount < grand_total and customer_name='"+c_name.getText().trim()+"' and bill_status='credit' order by id";
                LoadingTableViewDataSelectedRowName.Welcome(query1, invoiceNoList, 230, 590, 80);
                if(connection.isClosed()){  connection=DBConnect.getConnection();  }
                ResultSet set=connection.createStatement().executeQuery(
                        "select sum(grand_total-payedAmount) as 'balance', customer_id from billing2 " +
                                "where payedAmount < grand_total and customer_name='"+c_name.getText().trim()+"' and bill_status='credit'");
                ResultSet set1=connection.createStatement().executeQuery(
                        "select extra from customer " +
                                "where customername='"+c_name.getText().trim()+"'");
                if(set.next()){
                    balance=set.getDouble(1);
                    c_id.setText(set.getString(2));
                }
                if(set1.next()){
                    extraAmount=set1.getDouble(1);
                }
                c_balance.setText(P.df00(balance-extraAmount));
                try{
                    String string1="select * from sp_table where customer_id='"+c_id.getText().trim()+"' order by sp_id desc limit 200";
                    LoadingDataToTableView.Welcome(string1, sp_table_pane, 230, 930, 90);
                }catch (Exception e){ e.printStackTrace(); P.loggerLoader(e); }
            }
            catch (Exception e) {
                P.loggerLoader(e);
                e.printStackTrace();
            }finally {  if(!connection.isClosed()) connection.close(); }
        }
    }


    public void amount_payment(ActionEvent event) throws Exception {
        Connection connection=null;
        try{
            connection=DBConnect.getConnection();

            String cum_id=c_id.getText().trim();
            boolean datafoundB=false, dataFoundPA=true;
            double cum_balance=0, payingAMT = 0, extraAA=0;
            if(!cum_id.isEmpty()){
                String query4CB="select sum(grand_total-payedAmount) from billing2 where " +
                        " payedAmount < grand_total and customer_id = '"+cum_id+"' and bill_status = 'credit'";
                ResultSet set4CB    =   connection.createStatement().executeQuery(query4CB);
                while   (set4CB.next()){
                    cum_balance =   set4CB.getDouble(1);
                    if(cum_balance>0) {
                        datafoundB = true;
                    }
                }
            }
            try{
                ResultSet set1=connection.createStatement().executeQuery(
                        "select extra from customer " +
                                "where customername='"+c_name.getText().trim()+"'");
                if(set1.next()){
                    extraAA=set1.getDouble(1);
                }
            }catch (Exception e){}

            try{
                payingAMT   =   Double.parseDouble(pay_amount.getText().trim());
                if(payingAMT<0){
                    dataFoundPA=false;
                }
            }catch (Exception e){
                dataFoundPA=false;
            }

            if(!datafoundB){
                AlertMessage.error("CUSTOMER BALANCE IS ZERO \"0\"");
            }else if(!dataFoundPA)
            {
                AlertMessage.error("ENTER VALID PAYING AMOUNT");
            }
            else
            {
                int rowCount=0;
                double remaingBA=cum_balance, remaingPA=payingAMT+extraAA;

                String query4CB="select invoice_no from billing2 where " +
                        " payedAmount < grand_total and customer_id = '"+cum_id+"' and bill_status = 'credit'";
                ResultSet set4CB    =   connection.createStatement().executeQuery(query4CB);
                ArrayList<String> invoiceList   =   new ArrayList<>();
                while   (set4CB.next()){
                    invoiceList.add(set4CB.getString(1));
                    rowCount++;
                }
                for (int r=0;r<rowCount;r++){
                    String invoice_no = invoiceList.get(r);
                    P.p("invoice_no : "+(r+1)+" : "+invoice_no);
                    String invoice_date="", customer_id="", customer_no="",customer_name="";
                    double billAmount=0, payedAmount=0, paying_amount=0, billBalnceAmount=0;

                    invoice_no  =   invoiceList.get(r);
                    if(remaingBA<=0 || remaingPA<=0) break;
                    String query2="select * from billing2 where invoice_no='"+invoice_no+"'";
                    ResultSet  set2=connection.createStatement().executeQuery(query2);
                    while ( set2.next() )
                    {
                        invoice_date        =   set2.getString("bill_date");
                        customer_id         =   set2.getString("customer_id");
                        customer_name       =   set2.getString("customer_name");
                        customer_no         =   set2.getString("customer_phno");
                        billAmount          =   set2.getDouble("grand_total");
                        payedAmount         =   set2.getDouble("payedAmount");
                        billBalnceAmount    =   billAmount-payedAmount;
                    }

                    double currentPayingA=0;

                    P.p("condition condition : "+(r+1));
                    P.p("currentPayingA  :" +currentPayingA  );
                    P.p("remaingBA  :" +remaingBA);
                    P.p("remaingPA  :" +remaingPA);

                    if(remaingPA>billBalnceAmount){
                        P.p("condtion 1");
                        currentPayingA  =   billBalnceAmount;
                        remaingPA       =   remaingPA-billBalnceAmount;
                        remaingBA       =   remaingBA-billBalnceAmount;
                    }else if(remaingPA==billBalnceAmount){
                        P.p("condtion 2");
                        currentPayingA  =   billBalnceAmount;
                        remaingPA       =   0;
                        remaingBA       =   remaingBA-billBalnceAmount;
                    }else if(remaingPA<billBalnceAmount){
                        P.p("condtion 3");
                        currentPayingA  =   remaingPA;
                        remaingBA       =   remaingBA-remaingPA;
                        remaingPA       =   0;
                    }
                    P.p("after condition : "+(r+1));
                    P.p("currentPayingA  :" +currentPayingA  );
                    P.p("remaingBA  :" +remaingBA);
                    P.p("remaingPA  :" +remaingPA);

                    String query4 = "Insert into sp_table(" +
                            "sp_bal_amount, sp_pay_type, sp_pay_details, sp_pay_amount,  " +
                            "sp_pay_date, customer_id, customer_name , invoice_no, invoice_date, " +
                            "billAmount, customer_no) values " +
                            "(?,?,?,?,?,   ?,?,?,?,?,  ?)";

                    PreparedStatement preparedStatement_1 = con.prepareStatement(query4);

                    preparedStatement_1.setString(1, P.df00(billBalnceAmount));
                    preparedStatement_1.setString(2, p_type.getValue().toString());
                    preparedStatement_1.setString(3, p_details.getText());
                    preparedStatement_1.setString(4, P.df00(currentPayingA));
                    preparedStatement_1.setString(5, c_date.getValue().toString());
                    preparedStatement_1.setString(6, customer_id);
                    preparedStatement_1.setString(7, customer_name);
                    preparedStatement_1.setString(8, invoice_no);
                    preparedStatement_1.setString(9, invoice_date);
                    preparedStatement_1.setString(10, P.df00(billAmount));
                    preparedStatement_1.setString(11, customer_no);
                    int j=0,k=0;
                    j = preparedStatement_1.executeUpdate();

                    double new_payedamount=payedAmount+currentPayingA;

                    String query5 = "update billing2 set " +
                            "payedAmount='" + P.df00(new_payedamount)+ "' where " + "invoice_no='" + invoice_no+ "'";
                    k=connection.createStatement().executeUpdate(query5);
                    P.p("insert to sp_table j : "+j + "update billing2 k : "+k);
                    if(j<0 || k<0)
                    {
                        AlertMessage.error("ERROR WHILE UPADTING PAYMENT DETAILS");
                        break;
                    }
                    if(remaingBA<=0 || remaingPA<=0) break;
                }
                int i=0;
                String c_balance="0.00", extraAmount="0.00";
                if(remaingBA>0){
                    c_balance=P.df00(remaingBA);
                }else if(remaingPA>0){
                    extraAmount=P.df00(remaingPA);
                }
                String q = "update customer set balance='" + c_balance + "', extra='"+extraAmount+"' where customerid='" + cum_id+ "'";
                System.out.println("q---" + q);

                i = connection.createStatement().executeUpdate(q);

                P.p("i   : "+i);
                if (i > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("CREDIT PAYMENT DONE SUCCESSFULLY");
                    alert.showAndWait();

                    String string1="select * from sp_table where customer_id='"+cum_id+"' order by sp_id desc limit 200 ";
                    LoadingDataToTableView.Welcome(string1, sp_table_pane, 230, 930, 90);
                    c_phno.clear();
                    p_type.setValue("Select Payment Type");
                    p_details.clear();
                    pay_amount.clear();

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN SAVING DATA PLEASE ENTER VALID VALUES");
                    alert.showAndWait();
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            if(connection!=null)  {  connection.close();  }
        }
    }

    public void viewPaymentTrackerfxml(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("paymentTrack.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("PAYMENT");
        stage.setScene(new Scene(root1, 1020, 670));
        stage.show();
    }
}
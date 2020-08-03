
package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class Billing implements Initializable {

    public AnchorPane billingMianAnchiorePane;
    public TextField Old_balanceField;
    public TextField NeedTo_payfield;
    public Label label1;
    public Label label2;
    public TextField dis_in_per;

    public static DecimalFormat df3 = new DecimalFormat("#.#####");
    public static Connection connection = null;

    public Pane showstock;
    public Label user_name;
    public TextField customerId;
    public TextField customerNameEditText;

    public DatePicker dateTaking;
    public TextArea dispatchto;
    public TextField dispatchedthrough;
    public ComboBox paymentmode;
    public TextArea otherDetails;
    public TextField dis_in_amount;
    public TextField d_rAmount;
    public TextField finalAmount;
    public TextField payedAmt;
    public TextField remainAmt;
    public TextField returnStockAmount;
    public CheckBox a4half;
    public Label billtype;

    @FXML
    private TextField invoiceNumber;

    @FXML
    private TextField productName;

    @FXML
    private TextField productQuantity;

    @FXML
    private Pane ReturnAdded;

    @FXML
    private TextField productcodebill;

    @FXML
    void AddBill(ActionEvent event) throws Exception {
        AddBill();
    }

    void AddBill() throws Exception {
        try{    addCustomer();  }catch (Exception e){e.printStackTrace();}
        int totalQuantity = 0;
        double p_price = 0;
        String p_name = "", p_code = "", hsn = "", pd_name = "", packing="";
        double mrp = 0, rate = 0, gst = 0, oldQty = 0;
        Connection connection1 = null;
        try {
            connection1 = DBConnect.getConnection();

            if (ReturnAdded.getChildren().isEmpty()) {
                String queryc = "Select Max(invoice_no) as invoice from billing";
                ResultSet rs1c = connection1.createStatement().executeQuery(queryc);
                int invoiceNumberInteger = 0;
                while (rs1c.next()) {
                    String invoiceNumberString = rs1c.getString("invoice");
                    if (invoiceNumberString == null) {
                        invoiceNumberInteger = 1;
                    } else {
                        invoiceNumberInteger = Integer.parseInt(rs1c.getString("invoice"));
                        invoiceNumberInteger = invoiceNumberInteger + 1;
                    }
                }
                invoiceNumber.setText(String.valueOf(invoiceNumberInteger));
            }

            String product_id = "";
            ObservableList oa = LoadingDataToTableView.selectItem();

            ArrayList aa = new ArrayList();
            aa.add(oa.get(0));

            ArrayList newArray = new ArrayList();
            newArray = aa;
            String old = String.valueOf(newArray.get(0));
            ArrayList<String> bb = new ArrayList<String>();

            old = old.replace("[", "");
            old = old.replace("]", "");

            String log1[] = old.split(",");
            boolean datafound=true;
            try {
                bb.add(log1[0].trim());
                bb.add(log1[1].trim());
                product_id = bb.get(0);
            } catch (Exception e) {
                datafound=false;
            }

            String selectQueryForGst = "Select * from stock where id='" + product_id + "'";
            ResultSet rs = connection1.createStatement().executeQuery(selectQueryForGst);
            while (rs.next()) {
                p_name = rs.getString("productname");
                p_code = rs.getString("productcode");
                hsn = rs.getString("hsn");
                pd_name = rs.getString("dealer_name");
                mrp = rs.getDouble("mrp");
                p_price = rs.getDouble("purchaseprice");
                rate = rs.getDouble("selling_price");
                gst = rs.getDouble("cgst") * 2;
                oldQty = rs.getDouble("stockquantity");
                packing = rs.getString("packing");
            }

            try {  totalQuantity = (int) Double.parseDouble(productQuantity.getText().trim());  } catch ( Exception e){}

            try{
                double rate1=Double.parseDouble(tradeprice.getText().trim());
                if(rate1>0) {rate =rate1; }
            }catch (Exception e){  }

            double num = oldQty - Double.parseDouble(productQuantity.getText());
            if (!datafound) {
                AlertMessage.error("PLEASE SELECT THE ITEM FROM SELECTION TABLE");
            } else if (totalQuantity<=0) {
                AlertMessage.error("ENTER VALID QUANTITY");
            } else if (num < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Available stock  is = " + oldQty);
                alert.showAndWait();
            } else {
                double disc = 0, disc_amount = 0, value = 0, gst_amount = 0, net_amount = 0;
                try{    disc = Double.parseDouble(dis_in_per.getText());  }catch ( Exception e){ disc = 0; }
                double sp_per_qty=rate;
                try{
                    disc_amount = Double.parseDouble(dis_in_amount.getText().trim());
                    if (disc_amount<=0){
                        disc_amount = sp_per_qty*disc/100;
                    }else{
                        disc = 0;
                    }
                }

                catch (Exception e){ disc_amount = sp_per_qty*disc/100; }
                double spAmt_discAmount = sp_per_qty - disc_amount;
                value = ((spAmt_discAmount*100)/(100+gst)) * totalQuantity;
                gst_amount = value * gst / 100;
                net_amount = value + gst_amount;
                P.p("net_amount net_amount  : "+net_amount );

                String c_id = "0", c_name = "No", c_phno = "No", c_gstin= "No";
                try{  c_name = customerNameEditText.getText().trim().toUpperCase();}catch (Exception e){}
                if (customerId.getText().trim().isEmpty()) {
                    c_id = "0";
                    c_name = "No";
                    c_phno = "No";
                    c_gstin = "No";
                } else {
                    String query = "select * from customer where customerid='" + customerId.getText().trim() + "'";
                    ResultSet rs1 = connection1.createStatement().executeQuery(query);
                    while (rs1.next()) {
                        c_id = customerId.getText().trim();
                        c_phno = rs1.getString("phoneno");
                        c_gstin = rs1.getString("gstinno");
                    }
                }
                double newtotal=0.0;
                String query1 = "Insert into billing ( " +
                        "invoice_no, customer_id, customer_name, customer_phno, product_name," +
                        "product_code, quantity, hsn, mrp, purchase_price," +
                        "trade_price, discount_in_per, discount_amount, taxable_value, cgst, " +
                        "cgst_amount, sgst, sgst_amount, net_total, bill_status," +
                        " bill_date, billed_by, old_stock, dealername, product_id, " +
                        " packing,deduction_amount,final_total,gstin_no) " +
                        "values (?,?,?,?,?,   ?,?,?,?,?,   ?,?,?,?,?,   ?,?,?,?,?,  ?,?,?,?,?  ,?,?,?,?)";

                PreparedStatement preparedStatement1 = connection1.prepareStatement(query1);

                preparedStatement1.setString(1, invoiceNumber.getText());
                preparedStatement1.setString(2, c_id);
                preparedStatement1.setString(3, c_name);
                preparedStatement1.setString(4, c_phno);
                preparedStatement1.setString(5, p_name);
                preparedStatement1.setString(6, p_code);
                preparedStatement1.setString(7, productQuantity.getText().trim());
                preparedStatement1.setString(8, hsn);
                preparedStatement1.setString(9, P.df00(mrp));
                preparedStatement1.setString(10, P.df00(p_price));
                preparedStatement1.setString(11, P.df00(rate));
                preparedStatement1.setString(12, P.df00(disc));
                preparedStatement1.setString(13, P.df00(disc_amount*totalQuantity));
                preparedStatement1.setString(14, String.valueOf(value));

                double cgst = gst / 2;
                double cgst_amount = gst_amount / 2;
                double dramt = 0.0;
                d_rAmount.setText("0");
                dramt= Double.parseDouble(d_rAmount.getText().trim());

                preparedStatement1.setString(15, P.df00(cgst));
                preparedStatement1.setString(16, String.valueOf(cgst_amount));
                preparedStatement1.setString(17, P.df00(cgst));
                preparedStatement1.setString(18, String.valueOf(cgst_amount));
                preparedStatement1.setString(19, P.df00(net_amount));
                preparedStatement1.setString(20, "No");
                preparedStatement1.setString(21, dateTaking.getValue() + "");
                preparedStatement1.setString(22, user_new);
                preparedStatement1.setString(23, oldQty + "");
                preparedStatement1.setString(24, pd_name);
                preparedStatement1.setString(25, product_id);
                preparedStatement1.setString(26, packing);
                preparedStatement1.setString(27, String.valueOf(dramt));
                preparedStatement1.setString(28, P.df00(newtotal));
                preparedStatement1.setString(29, c_gstin);

                int i = preparedStatement1.executeUpdate();

                selectQueryForGst = "Select stockquantity from stock where id='" + product_id + "'";
                rs = connection1.createStatement().executeQuery(selectQueryForGst);
                while (rs.next()) {
                    oldQty = rs.getDouble("stockquantity");
                }
                double remainingqnty = oldQty - totalQuantity;
                int j=0;
                try {
                    String stockUpdateQuery = "UPDATE stock SET stockquantity='" + remainingqnty + "' WHERE id ='" + product_id + "'";
                    j = connection1.createStatement().executeUpdate(stockUpdateQuery);
                }catch (Exception e){
                    e.printStackTrace();
                    P.loggerLoader(e);
                }

                if (i > 0 && j > 0) {
                    productName.clear();
                    productName.requestFocus();
                    productcodebill.clear();
                    dis_in_per.clear();
                    dis_in_amount.clear();
                    productQuantity.clear();
                    tradeprice.clear();
                    showstock.getChildren().clear();

                    String queryIs = "SELECT " +
                            "product_id, bill_id, bill_date, customer_id, invoice_no, product_name, " +
                            "product_code, quantity, packing, mrp, trade_price, discount_in_per, " +
                            "discount_amount, taxable_value, cgst, cgst_amount, sgst, " +
                            "sgst_amount, net_total FROM billing WHERE invoice_no = '" + invoiceNumber.getText().trim() + "'";
                    LoadingTableViewDataSelectedRowName.Welcome(queryIs, ReturnAdded, 200, 880);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        } finally {
            if (connection1 != null)
                connection1.close();
        }
        calculateTatal(invoiceNumber.getText().trim());
        loadNeeToPayAndOldBalnce();

    }


    private void calculateTatal(String invoiceNu) throws Exception {
        Connection connection1 = null;
        try {

            connection1 = DBConnect.getConnection();
            ResultSet rsTakingTotal = connection1.createStatement().executeQuery("select sum(net_total) from billing WHERE invoice_no='" + invoiceNu + "'");
            if(rsTakingTotal.next()) {
                grantBilling.setText(P.df00(Math.round(rsTakingTotal.getDouble(1))));
            }else{
                grantBilling.setText("0.00");
            }
            calculateFinalAmount();
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        } finally {
            if (connection1 != null)
                connection1.close();
        }
    }

    public void calculateFinalAmount() throws Exception {
        connection=DBConnect.getConnection();
        double drAmount = 0, finalAmmmt = 0 , grandAmmt = 0, returnSTockAmmmt = 0;
        int j=0;
        try{ drAmount = Double.parseDouble(d_rAmount.getText().trim()); }catch (Exception e){}
        try{ grandAmmt = Double.parseDouble(grantBilling.getText().trim()); }catch (Exception e){}
        try{ returnSTockAmmmt = Double.parseDouble(returnStockAmount.getText().trim()); }catch (Exception e){}
        finalAmmmt = grandAmmt - drAmount - returnSTockAmmmt;
        finalAmount.setText(P.df00(finalAmmmt));
        try{
            String query = "Update billing set final_total='"+finalAmmmt+"'where invoice_no ='"+invoiceNumber.getText()+"' ";

            j = connection.createStatement().executeUpdate(query); }

        catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);}

        double payedAAmmt  = 0, remAAmmt = 0;
        // try{ payedAAmmt = Double.parseDouble(payedAmt.getText().trim());  }catch ( Exception e) { }
        remAAmmt = finalAmmmt-payedAAmmt;
        remainAmt.setText(P.df00(remAAmmt));

        try {
            loadNeeToPayAndOldBalnce();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null)
                connection.close();
        }
    }

    public void calculateFinalAmount1(KeyEvent keyEvent) throws Exception {
        calculateFinalAmount();
    }

    public void calculateFinalAmount2(KeyEvent keyEvent) throws Exception { calculateFinalAmount(); }

    public void getAllTheQuantity(KeyEvent event) throws Exception
    {
        String query = "select id, productcode , productname, mrp, selling_price,  stockquantity as 'avb qty' ,packing from stock where productcode like '%" + productcodebill.getText().trim() + "%' and stockquantity > 0";
        LoadingDataToTableView.Welcome(query, showstock, 250, 550, 75);
        productQuantity.setText("1");
        dis_in_per.setText("0");
    }

    public void ProductLiveSearch(KeyEvent event) throws Exception {
        String query = "select id, productcode , productname, mrp, selling_price,  stockquantity  as 'avb qty', packing  from stock where productname like '" + "%" + productName.getText().trim() + "%" + "' and stockquantity > 0";
        LoadingDataToTableView.Welcome(query, showstock, 250, 550, 75);
        productQuantity.setText("1");
        dis_in_per.setText("0");
    }

    public void refreshingbill(ActionEvent event) throws Exception {
        String queryIs ="SELECT " +
                "product_id, bill_id, bill_date, customer_id, invoice_no, product_name, " +
                "product_code, quantity, packing, mrp, trade_price, discount_in_per, " +
                "discount_amount, taxable_value, cgst, cgst_amount, sgst, " +
                "sgst_amount, net_total FROM billing WHERE invoice_no = '" + invoiceNumber.getText().trim() + "'";
        LoadingTableViewDataSelectedRowName.Welcome(queryIs, ReturnAdded,200,880);
        calculateTatal(invoiceNumber.getText());
        loadNeeToPayAndOldBalnce();
    }

    String user_new = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ZoneId zoneId = ZoneId.of("Indian/Maldives");
        LocalDate today = LocalDate.now(zoneId);
        dateTaking.setValue(today);


        paymentmode.getItems().removeAll(paymentmode.getItems());
        paymentmode.getItems().addAll("Select Payment Mode", "Cash","Online","Cheque","Credit Card", "Online Banking");
        paymentmode.getSelectionModel().select("Select Payment Mode");

//      productcodebill.setFocusTraversable(false);
        TextInputDialog username = new TextInputDialog();
        username.setTitle("Verification");
        username.setHeaderText("User Name verification");
        username.setContentText("ENTER USERNAME");
        Optional<String> user = username.showAndWait();

        user_new = user.get().trim();
        user_name.setText(user.get().trim().toUpperCase());

        try {
            connection = DBConnect.getConnection();

            int u = 0;
            connection = DBConnect.getConnection();
            String check_query = "select * from login where Username='" + user.get() + "'";
            ResultSet r = connection.createStatement().executeQuery(check_query);
            while (r.next()) {
                u++;
            }

            int i = 0;

            //for drafting invoice number
            if (u > 0) {
                boolean billed = true;
                String d_invoice_no = null;

                String draft_query = "Select invoice_no from billing where bill_status='No' and billed_by='" + user_new + "'";
                P.p("mitra----****---" + draft_query);
                ResultSet drs = connection.createStatement().executeQuery(draft_query);

                while (drs.next()) {
                    d_invoice_no = drs.getString("invoice_no");
                    billed = false;
                    break;
                }

                if (billed == true) {
                    //no action
                } else if (billed == false) {
                    invoiceNumber.setText(d_invoice_no);
                    loadDetailsByInvoiceNumber2(d_invoice_no);
                }
            } else {
                AlertMessage.error("INVALID USER");
                Platform.exit();
            }
//end of drafting
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadDetailsByInvoiceNumber2(String d_invoice_no){
        Connection connection = null;
        try {
            connection = DBConnect.connect();
            loadselecctedtablelist(d_invoice_no);

            String query3 = "select sum(net_total), invoice_no, customer_name, customer_phno, customer_id,bill_status  from billing where invoice_no='" + d_invoice_no + "'";
            ResultSet set2 = connection.createStatement().executeQuery(query3);

            while (set2.next()) {
                customerNameEditText.setText(set2.getString("customer_name"));
                grantBilling.setText(P.df00(set2.getString(1)));
                customerId.setText(set2.getString("customer_id"));
                billtype.setText(set2.getString("bill_status"));

                // calculateFinalAmount();
            }
            // loadNeeToPayAndOldBalnce();
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

    void loadselecctedtablelist(String invoiceNumber) throws Exception {

        String queryIs = "SELECT " +
                "product_id, bill_id, bill_date, customer_id, invoice_no, product_name, " +
                "product_code, quantity, packing,  mrp, trade_price, " +
                "discount_in_per, discount_amount, taxable_value, cgst, cgst_amount," +
                "sgst, sgst_amount, net_total  FROM billing WHERE invoice_no = '" + invoiceNumber + "'";
        LoadingTableViewDataSelectedRowName.Welcome(queryIs, ReturnAdded, 200, 880);
        // calculateTatal(this.invoiceNumber.getText());
        // loadNeeToPayAndOldBalnce();
    }

    @FXML
    void return_file(ActionEvent event) throws Exception {

        ObservableList oa = LoadingTableViewDataSelectedRowName.selectItem();

        ArrayList aa = new ArrayList();
        aa.add(oa.get(0));

        ArrayList newArray = new ArrayList();
        newArray = aa;
        String old = String.valueOf(newArray.get(0));
        ArrayList<String> bb = new ArrayList<String>();

        old = old.replace("[", "");
        old = old.replace("]", "");

        String log1[] = old.split(",");

        boolean datafound = true;

        bb.add(log1[0].trim());
        try {
            bb.add(log1[1].trim());
        } catch (Exception e) {
            datafound = false;
        }
        if(!datafound){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("PLEASE SELECT ITEM FROM TABLE");
            a.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to return Product");
            Optional<ButtonType> result = alert.showAndWait();

            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                try {
                    connection = DBConnect.getConnection();
                    int b_id = Integer.parseInt(bb.get(1));

                    String query = "Select * from billing where bill_id='" + b_id + "'";
                    P.p("query-------->" + query);
                    ResultSet rs = connection.createStatement().executeQuery(query);
                    String p_name = "", p_code = "", invoice = "", p_id = "";
                    double qty = 0;
                    while (rs.next()) {
                        invoice = rs.getString("invoice_no");
                        qty = rs.getDouble("quantity");
                        p_id = rs.getString("product_id");

                    }
                    String query2 = "select stockquantity from stock where id='" + p_id + "'";
                    ResultSet rs2 = connection.createStatement().executeQuery(query2);
                    double p_qty = 0;
                    while (rs2.next()) {
                        p_qty = rs2.getDouble("stockquantity");
                    }

                    double t_qty = p_qty + qty;

                    P.p("stock quantity------>" + qty);
                    P.p("p quantity------>" + p_qty);
                    P.p(" total quantity------>" + t_qty);

                    PreparedStatement ps = connection.prepareStatement("update stock set stockquantity='" + t_qty + "' " +
                            "where id='" + p_id + "'");

                    int i = ps.executeUpdate();

                    String query3 = "delete from billing where bill_id='" + b_id + "'";
                    PreparedStatement ps3 = connection.prepareStatement(query3);
                    int j = ps3.executeUpdate();

                    loadselecctedtablelist(invoice);
                    loadNeeToPayAndOldBalnce();
                } catch (Exception e) {
                    e.printStackTrace();
                    P.loggerLoader(e);
                }
            }
        }
    }

    public static void RupeesinWords(PdfPTable table, String amount) {
        Connection connection = null;
        try {
            connection = DBConnect.getConnection();
            String bill_amount = amount;
            P.p("aaa   :" + bill_amount);
            NumToWordss numToWordss = new NumToWordss();
            String s1 = "00";
            String s2 = bill_amount.substring(bill_amount.indexOf('.') + 1, bill_amount.length());
            P.p("aaaa s1  :" + s1);
            P.p("aaaa s2  :" + s2);
            String NumberINW1 = numToWordss.convert(Integer.parseInt(s1));
            P.p("NumberINW1  : " + NumberINW1);
            String NumberINW2 = numToWordss.convert(Integer.parseInt(s2));
            P.p("NumberINW2  : " + NumberINW2);

            Chunk dd = new Chunk("Rupees in words   :    \n", new Font(Font.FontFamily.UNDEFINED, 10));
            Chunk dd1 = new Chunk(NumberINW2.toUpperCase() + " RUPEES AND " + NumberINW1.toUpperCase() + " PAISA   ONLY", new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD));
            Paragraph p = new Paragraph();
            p.add(dd);
            p.add(dd1);
            PdfPCell cell = new PdfPCell(p);
            table.addCell(cell);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int sheetone = 0;

    String BillType="unBilled";

    public boolean updatingB1B2() throws Exception {
        int mm=0, k=0;
        boolean updatingB1B2Status=false;
        Connection connection = null;
        String c_id = "0", c_name = customerNameEditText.getText().trim().toUpperCase()
                , c_phno = "No", c_address="No", c_state="No", c_gst_number="No";
        try {
            connection = DBConnect.getConnection();
            ResultSet resultSet1 = connection.createStatement().executeQuery("select * from billing where invoice_no='" + invoiceNumber.getText() + "'");

            double trade_sum = 0;
            while (resultSet1.next()) {
                trade_sum = trade_sum + resultSet1.getDouble("quantity") * resultSet1.getDouble("trade_price");
            }

            if(!customerId.getText().trim().isEmpty())  {   c_id = customerId.getText().trim(); }
            ResultSet settt = connection.createStatement().executeQuery("select * from customer where customerid='" + c_id + "'");
            while (settt.next()) {
                c_id = settt.getString("customerid");
                c_phno = settt.getString("phoneno");
                c_address = settt.getString("customeraddress");
                c_state = settt.getString("state_statecode");
                c_gst_number = settt.getString("gstinno");
//                routeName = settt.getString("route_Name");
            }

            String quer2 = "UPDATE billing SET " +
                    " bill_status='"+BillType+"', " +
                    " customer_id='" + c_id + "', " +
                    " customer_name='" + c_name + "', " +
                    " customer_phno='" + c_phno + "',  " +
                    "bill_date='" + dateTaking.getValue() + "' " +
                    " WHERE " +
                    "invoice_no ='" + invoiceNumber.getText().trim() + "'";
            mm = connection.createStatement().executeUpdate(quer2);

            if(mm<=0){
                AlertMessage.error("ERROR WHILE UPDATING B1");
                P.loggerLoader("ERROR WHILE UPDATING B1 while billing");
                updatingB1B2Status=false;
            }

            String query6 = "select sum(discount_amount), sum(taxable_value), sum(cgst_amount), sum(sgst_amount), sum(net_total) " +
                    "from billing where invoice_no='" + invoiceNumber.getText().trim() + "'";

            ResultSet rs1 = connection.createStatement().executeQuery(query6);
            double d_amount = 0, t_amount = 0, c_amount = 0, s_amount = 0, n_amount = 0, returnStockAmmmt = 0;

            try{
                returnStockAmmmt = Double.parseDouble(returnStockAmount.getText().trim());
            }catch (Exception e){ }

            while (rs1.next()) {
                d_amount = rs1.getDouble(1);
                t_amount = rs1.getDouble(2);
                c_amount = rs1.getDouble(3);
                s_amount = rs1.getDouble(4);
                n_amount = rs1.getDouble(5);
            }

            String payedAMT =   "0.00";
            if( BillType.equalsIgnoreCase("billed") ){
            //    try { payedAMT = P.df00(Math.round(Float.parseFloat(payedAmt.getText().trim()))); }catch (Exception e){ payedAMT = "0.00"; }
            }else if( BillType.equalsIgnoreCase("credit") ){
            //    try { payedAMT = P.df00(Math.round(Float.parseFloat(payedAmt.getText().trim()))); }catch (Exception e){ payedAMT = "0.00"; }
            }

            try {
                if(connection.isClosed())  {    connection = DBConnect.connect();   }
                connection.createStatement().execute("delete from billing2 where invoice_no = '"+invoiceNumber.getText().trim()+"'");

                String query1 = "Insert into billing2 " +
                        "(bill_date, invoice_no, customer_id, customer_name, customer_phno, " +
                        "total_trade_amount, total_dis_amount, total_taxable_amount, total_cgst_amount, total_sgst_amount, " +
                        "total_net_amount, grand_total, bill_status, billed_by, payedAmount, " +
                        "customer_gst_number, customer_address, customer_state, dispatch_to_address, " +
                        "dispatch_through, payment_type, other_details, remainingAmount, returnStockAmount) " +
                        "values " +
                        "(  ?,?,?,?,?,   ?,?,?,?,?,   ?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?)";

                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);

                preparedStatement1.setString(1, dateTaking.getValue().toString());
                preparedStatement1.setString(2, invoiceNumber.getText().trim());

                preparedStatement1.setString(3, c_id);
                preparedStatement1.setString(4, c_name);
                preparedStatement1.setString(5, c_phno);

                preparedStatement1.setString(6, P.df00(trade_sum));
                preparedStatement1.setString(7, P.df00(d_amount));
                preparedStatement1.setString(8, P.df00(t_amount));
                preparedStatement1.setString(9, P.df00(c_amount));
                preparedStatement1.setString(10, P.df00(s_amount));
                preparedStatement1.setString(11, P.df00(n_amount));
                preparedStatement1.setDouble(12, Math.round(Double.parseDouble(finalAmount.getText().trim())));
                preparedStatement1.setString(13, BillType);
                preparedStatement1.setString(14, user_new);
                preparedStatement1.setString(15, payedAMT);
                preparedStatement1.setString(16, c_gst_number);
                preparedStatement1.setString(17, c_address);
                preparedStatement1.setString(18, c_state);
              //  preparedStatement1.setString(19, routeName);
                preparedStatement1.setString(19, dispatchto.getText().trim());
                preparedStatement1.setString(20, dispatchedthrough.getText().trim());
                preparedStatement1.setString(21, (paymentmode.getValue().toString().trim().equalsIgnoreCase("Select Payment Mode")?"CASH":paymentmode.getValue().toString()));
                preparedStatement1.setString(22, otherDetails.getText().trim());
                preparedStatement1.setString(23, remainAmt.getText().trim());
                preparedStatement1.setString(24, P.df00(returnStockAmmmt));

                k=preparedStatement1.executeUpdate();
                if(k<=0){
                    AlertMessage.error("ERROR WHILE UPDATING B2");
                    P.loggerLoader("ERROR WHILE UPDATING B2 while billing");
                    updatingB1B2Status = false;
                }
                if(k>0 && mm >0){
                    updatingB1B2Status = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                P.loggerLoader(e);
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            if(!connection.isClosed()) connection.close();
            return updatingB1B2Status;
        }
    }

    double balanceAmounttt = 0;

    public void rec_gen(ActionEvent event) throws Exception {
        try{    updateCustomerBalanceByNameOrId(); }catch (Exception e){e.printStackTrace();}
        BillType="billed";
        if(invoiceNumber.getText().trim().isEmpty() || ReturnAdded.getChildren().isEmpty()){
            AlertMessage.error("PLEASE ADD ITEMS BEFORE GENERATING THE BILL");
        }else {

            int noofitems=0;
            try {
                if(updatingB1B2()) {
                    connection = DBConnect.getConnection();
                    ResultSet resultSet1 = connection.createStatement().executeQuery("select count(bill_id) from billing where invoice_no='" + invoiceNumber.getText() + "'");
                    if (resultSet1.next()) {
                        noofitems = resultSet1.getInt(1);
                    }

                    new File(P.drive_name() + FilePath.FOLDER_PATH).mkdir();
                    new File(P.drive_name() + FilePath.FOLDER_PATH + "RECIPT").mkdir();
                    String path = P.drive_name() + FilePath.FOLDER_PATH + "/RECIPT/";
                    String FILE = path + "/GST_INVOICE_" + invoiceNumber.getText().trim() + ".pdf";

                    if (a4half.isSelected()) {

                        Document doc = new Document();
                        PdfWriter.getInstance(doc, new FileOutputStream(FILE));
                        Rectangle pageSize = new Rectangle(PageSize.A4.getWidth(), PageSize.A4.getHeight()/2);
                        doc.setPageSize(PageSize.A4);
                        doc.setMargins(10, 10, 10, 5);
                        doc.open();

                        while (true) {
                            if (noofitems > 0) {
                                pdf_sheet_creatorA5_3(1, noofitems, doc);
                            }
                            break;
                        }
                        doc.close();
                        Desktop.getDesktop().open(new File(FILE));
                    }


                    else {
                        Document doc = new Document();
                        PdfWriter.getInstance(doc, new FileOutputStream(FILE));
                        Rectangle pageSize = new Rectangle(PageSize.A6.getWidth(), PageSize.A6.getHeight());
                        doc.setPageSize(PageSize.A6);
                        doc.setMargins(10, 10, 10, 5);
                        doc.open();

                        while (true) {
                            if (noofitems > 0) {
                                pdf_sheet_creatorA5_2(1, noofitems, doc);
                            }
                            break;
                        }
                        doc.close();
                        Desktop.getDesktop().open(new File(FILE));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                P.loggerLoader(e);
            } finally { if (connection != null) connection.close();     }
        }

    }

    public void rec_gen_credit(ActionEvent event) throws Exception {
        try{ updateCustomerBalanceByNameOrId();}catch (Exception e){e.printStackTrace();}
        Connection connection = null;
        boolean isNotCreditCustomer=true;
        try {
            connection  =   DBConnect.getConnection();
            ResultSet set   =   connection.createStatement().executeQuery("select * from customer where customerid='"+customerId.getText().trim()+"'");
            if(set.next()){
                isNotCreditCustomer=false;
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }
        BillType="credit";
        if(invoiceNumber.getText().trim().isEmpty() || ReturnAdded.getChildren().isEmpty()){
            AlertMessage.error("PLEASE ADD ITEMS BEFORE GENERATING THE BILL");
        }else if(isNotCreditCustomer) {
            AlertMessage.error("PLEASE ENTER CREDIT CUSTOMER ID");
        }else
        {
            int noofitems=0;
            try
            {
                if(updatingB1B2()) {
                    if(connection.isClosed())   { connection = DBConnect.getConnection();   }
                    ResultSet resultSet1 = connection.createStatement().executeQuery("select count(bill_id) from billing where invoice_no='" + invoiceNumber.getText() + "'");
                    if (resultSet1.next()) {
                        noofitems = resultSet1.getInt(1);
                    }
                    try {
                        String stockUpdateQuery = "UPDATE customer SET balance='" + NeedTo_payfield.getText().trim() + "' WHERE customerid ='" + customerId.getText() + "'";
                        connection.createStatement().executeUpdate(stockUpdateQuery);
                    } catch (Exception e) {
                        e.printStackTrace();
                        P.loggerLoader(e);
                    }

                    new File(P.drive_name() + FilePath.FOLDER_PATH).mkdir();
                    new File(P.drive_name() + FilePath.FOLDER_PATH + "RECIPT").mkdir();
                    String path = P.drive_name() + FilePath.FOLDER_PATH + "/RECIPT/";
                    String FILE = path + "/GST_INVOICE_" + invoiceNumber.getText().trim() + ".pdf";

                    if (a4half.isSelected()) {
                        Document doc = new Document();
                        PdfWriter.getInstance(doc, new FileOutputStream(FILE));
                        Rectangle pageSize = new Rectangle(PageSize.A4.getWidth(),PageSize.A4.getHeight()/2);
                        doc.setPageSize(PageSize.A4);
                        doc.setMargins(10, 10, 10, 5);
                        doc.open();

                        while (true) {
                            if (noofitems > 0) {
                                pdf_sheet_creatorA5_3(1, noofitems, doc);
                            }
                            break;
                        }
                        doc.close();
                        Desktop.getDesktop().open(new File(FILE));
                    }else{
                        Document doc = new Document();
                        PdfWriter.getInstance(doc, new FileOutputStream(FILE));
                        Rectangle pageSize = new Rectangle(PageSize.A4.getWidth(),PageSize.A4.getHeight());
                        doc.setPageSize(PageSize.A4);
                        doc.setMargins(10, 10, 10, 5);
                        doc.open();

                        while (true) {
                            if (noofitems > 0) {
                                pdf_sheet_creatorA5_2(1, noofitems, doc);
                            }
                            break;
                        }
                        doc.close();
                        Desktop.getDesktop().open(new File(FILE));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                P.loggerLoader(e);
            } finally {     if (connection != null) connection.close();     }

        }
    }


    public static   PdfPCell createCell1(String string, Font font, int alignment, int border, DottedCell dotSide)
    {
        Paragraph paragraph = new Paragraph(new Phrase(string, font));
        paragraph.setAlignment(alignment);
        PdfPCell Cel1 = new PdfPCell(paragraph);
        Cel1.setBorder(border);
        Cel1.setHorizontalAlignment(alignment);
        Cel1.setCellEvent(dotSide);
        return Cel1;
    }

    public static PdfPCell createCell2(String string, Font font, int alignment, int border)
    {
        Paragraph paragraph = new Paragraph(new Phrase(string, font));
        paragraph.setAlignment(alignment);
        PdfPCell Cel1 = new PdfPCell(paragraph);
        Cel1.setBorder(border);
        Cel1.setHorizontalAlignment(alignment);
        return Cel1;
    }

    public void pdf_sheet_creatorA6_2(int startfrom, int endwith, Document doc) throws SQLException, DocumentException, FileNotFoundException, ClassNotFoundException {
        Connection connection = null;
        try {
            connection = DBConnect.getConnection();
            {
                String name = "", address = "", phno = "", logoPath="", mailId="", gst_number="";

                PdfPTable tableM1_1=new PdfPTable(3);
                tableM1_1.setWidthPercentage(100);
                tableM1_1.setWidths(new int[]{15,1,15});

                PdfPTable tableM1_2=new PdfPTable(1);
                tableM1_2.setWidthPercentage(100);
                PdfPCell mainCell2;

                ResultSet set2 = connection.createStatement().executeQuery("select * from profile ");
                while (set2.next()) {
                    name = set2.getString("name");
                    address = set2.getString("mail_addres");
                    mailId  =   set2.getString("comemailid");
                    phno = set2.getString("contact");
                    logoPath=set2.getString("logo");
                    gst_number  =   set2.getString("gstin");
                }

                String c_id="", d_to="", d_through="", other_details="";
                double BA=0, TA=0, taxableAMOUNT=0;
                ResultSet setc_id_billAMount = connection.createStatement().executeQuery("select * from billing2 where invoice_no ='"+invoiceNumber.getText().trim()+"'");
                while(setc_id_billAMount.next()){
                    c_id=setc_id_billAMount.getString("customer_id");
                    BA=setc_id_billAMount.getDouble("grand_total");
                    TA=setc_id_billAMount.getDouble("total_sgst_amount")+setc_id_billAMount.getDouble("total_cgst_amount");
                    taxableAMOUNT=setc_id_billAMount.getDouble("total_taxable_amount");
                    d_to    =   setc_id_billAMount.getString("dispatch_to_address").trim();
                    d_through    =   setc_id_billAMount.getString("dispatch_through").trim();
                    other_details    =   setc_id_billAMount.getString("other_details").trim();
                }

                String C_name="", C_phno="", C_gst="", C_address="", C_state="";
                ResultSet setcustomer = connection.createStatement().executeQuery("select * from customer where customerid='"+c_id+"'");
                while (setcustomer.next()) {
                    C_name = setcustomer.getString("customername");
                    C_address = setcustomer.getString("customeraddress");
                    C_phno = setcustomer.getString("phoneno");
                    C_state = setcustomer.getString("state_statecode");
                    C_gst = setcustomer.getString("gstinno");
                }

                PdfPTable table1_2=new PdfPTable(2);
                table1_2.setWidthPercentage(95);
                table1_2.setWidths(new int[ ]{2,10});
                Image logo=null;
                try {
                    logo = Image.getInstance(logoPath);
                    logo.setBorder(Rectangle.NO_BORDER);
                }catch (Exception e){ e.printStackTrace(); P.loggerLoader(e);  }


                try{
                    table1_2.addCell(logo);
                }catch (Exception e){
                    table1_2.addCell(" ");
                    e.printStackTrace();
                    P.loggerLoader(e);
                }

                Paragraph paragraph6 = new Paragraph();
                paragraph6.setAlignment(Element.ALIGN_CENTER);
                paragraph6.add(new Chunk("GST INVOICE\n", new Font(Font.FontFamily.UNDEFINED, 7, Font.BOLD)));
                paragraph6.add(new Chunk(name, new Font(Font.FontFamily.UNDEFINED, 13, Font.BOLD)));
                paragraph6.add(new Chunk("\n"+address, new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                String demo="";
                if((!phno.isEmpty())){  demo =   phno;  }
                else{ demo  =   phno;   }
                if (!demo.isEmpty()) {  paragraph6.add(new Chunk("\nPH : " + demo, new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));  }
                if (!gst_number.isEmpty()) {  paragraph6.add(new Chunk("GSTIN : " + gst_number, new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));  }
                paragraph6.add(new Chunk("\nINVOICE NO:" + invoiceNumber.getText(), new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                String d = dateTaking.getEditor().getText();
                String d1[] = d.split("/");
                paragraph6.add(new Phrase("                                          DATE :" + d1[1] + "-" + d1[0] + "-" + d1[2], new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));

                PdfPCell cell1_3=new PdfPCell(paragraph6);
                cell1_3.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1_2.addCell(cell1_3);

                mainCell2   =   new PdfPCell(table1_2);
                tableM1_2.addCell(mainCell2);

                Paragraph paragraph7 = new Paragraph();
                Font font3 = new Font(Font.FontFamily.UNDEFINED, 9, Font.BOLD);
                Font font33 = new Font(Font.FontFamily.UNDEFINED, 7);
                Font font333 = new Font(Font.FontFamily.UNDEFINED, 7, Font.BOLD);
                if(d_through.isEmpty() && d_to.isEmpty() && other_details.isEmpty()){
                    paragraph7.add(new Chunk(  "", font3));
                }else {
                    if(!d_to.isEmpty()) {
                        paragraph7.add(new Chunk("DISPATCH TO : ", font333));
                        paragraph7.add(new Chunk(d_to+"\n", font33));
                    }
                    if(!d_through.isEmpty()) {
                        paragraph7.add(new Chunk("DISPATCH THROUGH : ", font333));
                        paragraph7.add(new Chunk(d_through+"\n", font33));
                    }
                    if(!other_details.isEmpty()) {
                        paragraph7.add(new Chunk("OTHER DETAILS : ", font333));
                        paragraph7.add(new Chunk(other_details+"\n", font33));
                    }
                }


                Paragraph paragraph8 = new Paragraph();
                paragraph8.add(new Chunk("CUSTOMER NAME : " , font3));
                paragraph8.add(new Chunk(""+ customerNameEditText.getText(), font33));
                paragraph8.add(new Chunk("\n" + C_phno, font33));
                paragraph8.add(new Chunk("\n" + C_address, font33));
                paragraph8.add(new Chunk("\n" + C_state, font33));
                paragraph8.add(new Chunk("\n" + C_gst, font33));
                PdfPTable table2 = new PdfPTable(2);
                table2.setWidthPercentage(95);
                table2.setWidths(new int[]{1, 1});
                PdfPCell cell2 = new PdfPCell(paragraph8);
                cell2.setBorder(Rectangle.TOP);
                PdfPCell cell1 = new PdfPCell(paragraph7);
                cell1.setBorder(Rectangle.TOP);
                table2.addCell(cell2);
                table2.addCell(cell1);

                mainCell2   =   new PdfPCell(table2);
                tableM1_2.addCell(mainCell2);

                PdfPTable table1 = new PdfPTable(7);

                table1.setWidths(new int[]{2, 15, 5, 3, 5,  4, 6});
                Font font1 = new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD);
                Font font2 = new Font(Font.FontFamily.UNDEFINED, 7);
                Font font22 = new Font(Font.FontFamily.UNDEFINED, 6);

                table1.addCell(new Phrase(new Chunk("Sl", font1)));
                table1.addCell(new Phrase(new Chunk("Descrption", font1)));
                table1.addCell(new Phrase(new Chunk("HSN code", font1)));
                table1.addCell(new Phrase(new Chunk("Qty", font1)));
//                table1.addCell(new Phrase(new Chunk("PACK", font1)));
                table1.addCell(new Phrase(new Chunk("Rate", font1)));
//                table1.addCell(new Phrase(new Chunk("Disc%", font1)));
//                table1.addCell(new Phrase(new Chunk("Taxable Amount", font1)));
                table1.addCell(new Phrase(new Chunk("GST%", font1)));
//                table1.addCell(new Phrase(new Chunk("GST Amount", font1)));
                table1.addCell(new Phrase(new Chunk("Net Amount", font1)));

                int startSINO = startfrom;
                int endtSINO = startfrom + 10;
                int sino = 1, num = 0;

                DottedCell dcell=new DottedCell(2);

                table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                String query = "select * from billing where invoice_no='" + invoiceNumber.getText() + "'";
                ResultSet set1 = connection.createStatement().executeQuery(query);
                int countLiines = 0;
                while (set1.next()) {
                    if ((sino >= startSINO) && (sino < endtSINO)) {
                        countLiines++;

                        table1.addCell(createCell1(String.valueOf(sino), font2, Element.ALIGN_CENTER, Rectangle.NO_BORDER, dcell));
                        table1.addCell(createCell1(set1.getString("product_name"), font2, Element.ALIGN_LEFT, Rectangle.NO_BORDER, dcell));
                        table1.addCell(createCell1(set1.getString("hsn"), font22, Element.ALIGN_LEFT, Rectangle.NO_BORDER, dcell));
                        table1.addCell(createCell1(set1.getString("quantity"), font2, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, dcell));
//                        table1.addCell(createCell1(set1.getString("packing"), font22, Element.ALIGN_LEFT, Rectangle.NO_BORDER, dcell));
                        table1.addCell(createCell1(P.df00(set1.getString("trade_price")), font2, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, dcell));
//                        table1.addCell(createCell1(P.df00(set1.getString("discount_in_per")), font2, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, dcell));
//                        table1.addCell(createCell1(P.df00(set1.getString("taxable_value")), font2, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, dcell));
                        table1.addCell(createCell1(P.df00(set1.getDouble("cgst") * 2), font2, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, dcell));
//                        table1.addCell(createCell1(P.df00(set1.getDouble("cgst_amount") * 2), font2, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, dcell));
                        table1.addCell(createCell1(P.df00(set1.getString("net_total")), font2, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, dcell));
                    }
                    sino++;
                    if (sino >= endtSINO) {

                        break;
                    }
                }
                if (countLiines < 10) {
                    int j = 10 - countLiines;

                    Paragraph par40 = new Paragraph(new Chunk(" ", font2));
                    PdfPCell Cel40 = new PdfPCell(par40);
                    Cel40.setBorder(Rectangle.NO_BORDER);
                    Cel40.setCellEvent(dcell);

                    for (int k = 0; k < (7 * j); k++) {
                        table1.addCell(Cel40);
                    }
                }
                table1.getDefaultCell().setBorder(Rectangle.TOP);

                table1.addCell(new Phrase(new Chunk("", font2)));
                table1.addCell(createCell2("TOTAL", new Font(Font.FontFamily.UNDEFINED, 9, Font.BOLD) , Element.ALIGN_CENTER, Rectangle.TOP));

                table1.addCell(new Phrase(new Chunk("", font2)));
                table1.addCell(new Phrase(new Chunk("", font2)));
                table1.addCell(new Phrase(new Chunk("", font2)));
//                table1.addCell(new Phrase(new Chunk("", font2)));
//                table1.addCell(new Phrase(new Chunk("", font2)));
//                table1.addCell(createCell2(P.df00(taxableAMOUNT), font2 , Element.ALIGN_CENTER, Rectangle.TOP));
                table1.addCell(new Phrase(new Chunk("", font2)));

//                table1.addCell(createCell2(P.df00(TA), font2 , Element.ALIGN_CENTER, Rectangle.TOP));
                table1.addCell(createCell2(P.df00(BA), font2 , Element.ALIGN_CENTER, Rectangle.TOP));

                PdfPTable table4 = new PdfPTable(1);
                table4.setWidthPercentage(95);
                table4.addCell(new PdfPCell(table1));

                mainCell2   =   new PdfPCell(table4);
                tableM1_2.addCell(mainCell2);

                ResultSet sett = connection.createStatement().executeQuery("select * from billing2 where invoice_no='" + invoiceNumber.getText().trim() + "'");
                double billamount = 0.0;
                while (sett.next()) {
                    billamount = sett.getDouble("grand_total");
                }

                ResultSet sett1 = connection.createStatement().executeQuery("select * from profile");
                String terms = " ", busness_name = " ", accDetails=" ";
                while (sett1.next()) {
                    terms = sett1.getString("terms") + "\n" + sett1.getString("declaration");
                    busness_name = sett1.getString("name");
                    accDetails= "ACC NAME : "+sett1.getString("acc_name")+
                            "\nACC NO : "+sett1.getString("acc_num")+
                            "\nACC TYPE : "+sett1.getString("acc_type")+
                            "\nIFSC COE : "+sett1.getString("ifsc");
                    break;
                }

                PdfPTable table = new PdfPTable(2);
                table.setWidths(new int[]{7, 4});
                table.setWidthPercentage(100);

                NumToWordss numToWordss = new NumToWordss();
                String amountInString = numToWordss.convert((long) billamount)+" Rupees only";
                Paragraph amo=new Paragraph();
                amo.add(new Chunk("Rupees in words   :    \n", new Font(Font.FontFamily.UNDEFINED, 9)));
                amo.add(new Chunk(amountInString.toUpperCase() , new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));

//                table.addCell(new PdfPCell(amo));

                {
                    PdfPTable tableTotal=new PdfPTable(1);
                    tableTotal.setWidthPercentage(100);

                    PdfPTable tableGstSummary=new PdfPTable(5);
                    tableGstSummary.setWidths(new int[]{2,5,4,4,5});
                    tableGstSummary.setWidthPercentage(100);
                    ResultSet setgstSummary=connection.createStatement().executeQuery(
                            "select cgst+sgst, sum(taxable_value), sum(cgst_amount), sum(sgst_amount), sum(cgst_amount+sgst_amount) " +
                                    "from billing where invoice_no = '"+invoiceNumber.getText().trim()+"' and (cgst+sgst) > "+0+"  group by  cgst+sgst ORDER BY cgst+sgst");

                    Font fontg1=new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD );
                    Font fontg2=new Font(Font.FontFamily.HELVETICA, 6 );
                    tableGstSummary.addCell(createCell2("@", fontg1, 1, 15));
                    tableGstSummary.addCell(createCell2("TXBL AMOUNT", fontg1, 1, 15));
                    tableGstSummary.addCell(createCell2("CGST", fontg1, 1, 15));
                    tableGstSummary.addCell(createCell2("SGST", fontg1, 1, 15));
                    tableGstSummary.addCell(createCell2("GST AMOUNT", fontg1, 1, 15));

                    boolean dataFound=false;
                    while(setgstSummary.next()){
                        dataFound=true;
                        tableGstSummary.addCell(createCell1( setgstSummary.getString(1), fontg2, 1, 15, dcell));
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(2), fontg2, 1, 15,dcell));
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(3), fontg2, 1, 15,dcell));
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(4), fontg2, 1, 15,dcell));
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(5), fontg2, 1, 15,dcell));
                    }
                    if(dataFound){
                        tableTotal.addCell(new PdfPCell(tableGstSummary));
                    }

                    Paragraph paragraph33 = new Paragraph();
                    paragraph33.add(new Chunk("Amount Receivable  : " + P.df00(BA), new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                    PdfPCell cell = new PdfPCell(paragraph33);
                    tableTotal.addCell(cell);
                    table.addCell(new PdfPCell( tableTotal));
                }

                Paragraph paragraph36 = new Paragraph();
                paragraph36.add(new Chunk("For  " + busness_name, new Font(Font.FontFamily.UNDEFINED, 8)));
                paragraph36.add(new Chunk("\n\n\n\n\nAuthorised Signature", new Font(Font.FontFamily.UNDEFINED, 8)));
                paragraph36.setAlignment(Element.ALIGN_CENTER);
                PdfPCell cell13 = new PdfPCell(paragraph36);
                cell13.setRowspan(2);
                cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell13);


                PdfPTable table2_2=new PdfPTable(2);
                table2_2.setWidths(new int[]{5,4});

                Paragraph paragraph35 = new Paragraph();
                paragraph35.add(new Chunk("Terms and Condition  :   ", new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                paragraph35.add(new Chunk("\n" + terms, new Font(Font.FontFamily.UNDEFINED, 7)));
                PdfPCell cell12 = new PdfPCell(paragraph35);
                table2_2.addCell(cell12);


                table.addCell(paragraph35);


                mainCell2   =   new PdfPCell(table);
                tableM1_2.addCell(mainCell2);

                PdfPTable tablelast=new PdfPTable(2);
                PdfPCell cell=new PdfPCell(new Phrase(" "));
                cell.setBorder(Rectangle.NO_BORDER);


                Paragraph paragraph3 = new Paragraph(new Phrase("Computer Generated Bill", FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 7, BaseColor.GRAY)));
                paragraph3.setAlignment(Element.ALIGN_CENTER);
                cell=new PdfPCell(paragraph3);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablelast.addCell(cell);

                Paragraph paragraph4 = new Paragraph(new Phrase("Software by MITRA SOFTWARES-08182-298188        ", FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 6, BaseColor.GRAY)));
                paragraph4.setAlignment(Element.ALIGN_RIGHT);
                cell=new PdfPCell(paragraph4);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(Rectangle.NO_BORDER);
                tablelast.addCell(cell);

                mainCell2=new PdfPCell(tablelast);
                mainCell2.setBorder(Rectangle.NO_BORDER);
                tableM1_2.addCell(mainCell2);

                PdfPCell mainCell1  =   new PdfPCell();
                double cellHeight   =   (PageSize.A4.getHeight()-20);
                mainCell1.setFixedHeight((float) cellHeight);
                mainCell1.setBorder(Rectangle.NO_BORDER);
                mainCell1.addElement(tableM1_2);

                tableM1_1.addCell(mainCell1);
                tableM1_1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                tableM1_1.addCell("");
                tableM1_1.addCell(mainCell1);
                doc.add(tableM1_1);
                if (sino <= endwith) {
                    doc.newPage();
                    pdf_sheet_creatorA6_2(endtSINO, endwith, doc);
                }

            }
            // bill finishing

            invoiceNumber.clear();
            customerNameEditText.clear();
            customerId.clear();
            grantBilling.clear();
            ReturnAdded.getChildren().clear();

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static   PdfPCell createCellSpace1(String string, Font font, int alignment, int border, BaseColor color)
    {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Phrase(string, font));
        paragraph.setAlignment(alignment);
        PdfPCell Cel1 = new PdfPCell(paragraph);
        Cel1.setBackgroundColor(color);
        Cel1.setBorder(border);
        Cel1.setHorizontalAlignment(alignment);
        return Cel1;
    }
    BaseColor color=BaseColor.LIGHT_GRAY;
    public void pdf_sheet_creatorA5_2(int startfrom, int endwith, Document doc) throws SQLException, DocumentException, FileNotFoundException, ClassNotFoundException {
        Connection connection = null;
        try {
            connection = DBConnect.getConnection();
            {
                String name = "", address = "", phno = "", logoPath="", mailId="", gst_number="";

                PdfPTable tableM1_1=new PdfPTable(1);
                tableM1_1.setWidthPercentage(95);

                PdfPTable tableM1_2=new PdfPTable(1);
                tableM1_2.setWidthPercentage(100);
                PdfPCell mainCell2;

                ResultSet set2 = connection.createStatement().executeQuery("select * from profile ");
                while (set2.next()) {
                    name = set2.getString("name");
                    address = set2.getString("mail_addres");
                    mailId  =   set2.getString("comemailid");
                    phno = set2.getString("contact");
                    logoPath=set2.getString("logo");
                    gst_number  =   set2.getString("gstin");
                }

                String c_id="", d_to="", d_through="", other_details="", paytype = "", remAmmt = "", payedAmmt = "", returmStockAmmt= "";
                double BA=0, TA=0, taxableAMOUNT=0, billAmmmt =0 ;
                ResultSet setc_id_billAMount = connection.createStatement().executeQuery("select * from billing2 where invoice_no ='"+invoiceNumber.getText().trim()+"'");
                while(setc_id_billAMount.next()){
                    c_id=setc_id_billAMount.getString("customer_id");
                    BA=setc_id_billAMount.getDouble("grand_total");
                    billAmmmt=setc_id_billAMount.getDouble("total_net_amount");
                    TA=setc_id_billAMount.getDouble("total_sgst_amount")+setc_id_billAMount.getDouble("total_cgst_amount");
                    taxableAMOUNT=setc_id_billAMount.getDouble("total_taxable_amount");
                    d_to    =   setc_id_billAMount.getString("dispatch_to_address").trim();
                    d_through    =   setc_id_billAMount.getString("dispatch_through").trim();
                    other_details    =   setc_id_billAMount.getString("other_details").trim();
                    paytype    =   setc_id_billAMount.getString("payment_type").trim();
                    remAmmt =    setc_id_billAMount.getString("remainingAmount").trim();
                    payedAmmt =    setc_id_billAMount.getString("payedAmount").trim();
                    returmStockAmmt = setc_id_billAMount.getString("returnStockAmount").trim();
                }

                String C_name="", C_phno="", C_gst="", C_address="", C_state="",croute="";
                ResultSet setcustomer = connection.createStatement().executeQuery("select * from customer where customerid='"+c_id+"'");
                while (setcustomer.next()) {
                    C_name = setcustomer.getString("customername");
//                    C_address = setcustomer.getString("customeraddress");
                    C_phno = setcustomer.getString("phoneno");
//                    C_state = setcustomer.getString("state_statecode");
//                    C_gst = setcustomer.getString("gstinno");
//                    croute=setcustomer.getString("route_name");
                }

                PdfPTable table1_2=new PdfPTable(2);
                table1_2.setWidthPercentage(95);
                table1_2.setWidths(new int[ ]{2,10});
                Image logo=null;
                try {
                    logo = Image.getInstance(logoPath);
                    logo.setBorder(Rectangle.NO_BORDER);
                }catch (Exception e){ e.printStackTrace(); P.loggerLoader(e);  }


                try{
                    table1_2.addCell(logo);
                }catch (Exception e){
                    table1_2.addCell(" ");
                    e.printStackTrace();
                    P.loggerLoader(e);
                }

                Paragraph paragraph6 = new Paragraph();
                paragraph6.setAlignment(Element.ALIGN_CENTER);
                paragraph6.add(new Chunk("GST INVOICE\n", new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                paragraph6.add(new Chunk(name, new Font(Font.FontFamily.UNDEFINED, 18, Font.BOLD)));
                paragraph6.add(new Chunk("\n"+address, new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                String demo="";
                if((!phno.isEmpty())  && (!mailId.isEmpty())){  demo =   phno+"  ,   "+mailId;  }
                else{ demo  =   phno+mailId;   }
                if (!demo.isEmpty()) {  paragraph6.add(new Chunk("\n" + demo, new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));  }
                if (!gst_number.isEmpty()) {  paragraph6.add(new Chunk("\nGSTIN : " + gst_number, new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));  }
                //  paragraph6.add(new Chunk("\nINVOICE NO:" + invoiceNumber.getText(), new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                // String d = dateTaking.getEditor().getText();
                // String d1[] = d.split("/");
                // paragraph6.add(new Phrase("                                    DATE :" + d1[1] + "-" + d1[0] + "-" + d1[2], new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));

                PdfPCell cell1_3=new PdfPCell(paragraph6);
                cell1_3.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1_2.addCell(cell1_3);

                mainCell2   =   new PdfPCell(table1_2);
                tableM1_2.addCell(mainCell2);


                Font font3 = new Font(Font.FontFamily.UNDEFINED, 9, Font.BOLD);
                Font font33 = new Font(Font.FontFamily.UNDEFINED, 8);
                Font font333 = new Font(Font.FontFamily.UNDEFINED, 7, Font.BOLD);


                Paragraph paragraph8 = new Paragraph();
                paragraph8.add(new Chunk("CUSTOMER DETAILS " , font3));
                paragraph8.add(new Chunk("\nNAME        : " , font3));
                paragraph8.add(new Chunk(""+ customerNameEditText.getText(), font33));
                paragraph8.add(new Chunk("\nPHNO        : " , font3));
                paragraph8.add(new Chunk("" + C_phno, font33));
//                paragraph8.add(new Chunk("\nADDRESS :" , font3));
                paragraph8.add(new Chunk("" + C_address, font33));
//                paragraph8.add(new Chunk("\nSTATE       :" , font3));
                paragraph8.add(new Chunk("" + C_state, font33));
//                paragraph8.add(new Chunk("\nGSTNO      :", font3));
                paragraph8.add(new Chunk("" + C_gst, font33));
           //     paragraph8.add(new Chunk("\nRoute        :" + croute, font33));
                PdfPTable table2 = new PdfPTable(2);
                table2.setWidthPercentage(95);
                PdfPCell cell2 = new PdfPCell(paragraph8);
                cell2.setBorder(Rectangle.TOP);
                table2.addCell(cell2);

                paragraph8 = new Paragraph();
                paragraph8.add(new Chunk("                            INVOICE NO:" + invoiceNumber.getText(), new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                String d = dateTaking.getEditor().getText();
                String d1[] = d.split("/");
                paragraph8.add(new Phrase("\n\n                           DATE :" + d1[1] + "-" + d1[0] + "-" + d1[2], new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));

                //  paragraph8.add(new Chunk("\n"+mbDto.getDispatchDetails(), font33));
                cell2 = new PdfPCell(paragraph8);
                cell2.setBorder(Rectangle.TOP);
                table2.addCell(cell2);

                mainCell2   =   new PdfPCell(table2);
                tableM1_2.addCell(mainCell2);


                PdfPTable table1 = new PdfPTable(5);

                table1.setWidths(new int[]{2, 11, 3, 3, 5});
                Font font1 = new Font(Font.FontFamily.UNDEFINED, 9, Font.BOLD);
                Font font1_1 = new Font(Font.FontFamily.UNDEFINED, 10f, Font.BOLD);
                Font font21 = new Font(Font.FontFamily.UNDEFINED, 9);
                Font font2_1 = new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD);
                Font font22 = new Font(Font.FontFamily.UNDEFINED, 7);

                table1.addCell(createCellSpace1("SI", font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
                table1.addCell(createCellSpace1("Description", font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1("HSN", font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
                table1.addCell(createCellSpace1("Qty", font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1("Pack", font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1("Rate", font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1("Disc Amnt", font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1("Taxable Amnt", font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
                table1.addCell(createCellSpace1("GST %", font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1("GST Amnt", font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
                table1.addCell(createCellSpace1("Net Amnt", font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));

                int startSINO = startfrom;
                int endtSINO = startfrom + 25;
                int sino = 1, num = 0;

                DottedCell dcell=new DottedCell(2);

                table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                String query = "select * from billing where invoice_no='" + invoiceNumber.getText() + "'";
                ResultSet set1 = connection.createStatement().executeQuery(query);
                int countLiines = 0;
                while (set1.next()) {
                    if ((sino >= startSINO) && (sino < endtSINO)) {
                        countLiines++;

                        table1.addCell(createCell1(String.valueOf(sino), font21, Element.ALIGN_CENTER, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(set1.getString("product_name"), font21, Element.ALIGN_LEFT, Rectangle.RIGHT, dcell));
//                        table1.addCell(createCell1(set1.getString("hsn"), font21, Element.ALIGN_LEFT, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(set1.getString("quantity"), font21, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
//                        table1.addCell(createCell1(set1.getString("packing"), font21, Element.ALIGN_LEFT, Rectangle.RIGHT, dcell));
//                        table1.addCell(createCell1(P.df00(set1.getString("trade_price")), font21, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
//                        table1.addCell(createCell1(P.df00(set1.getString("discount_amount")), font21, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
//                        table1.addCell(createCell1(P.df00(set1.getString("taxable_value")), font21, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(P.df00(set1.getDouble("cgst") * 2), font21, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
//                        table1.addCell(createCell1(P.df00(set1.getDouble("cgst_amount") * 2), font21, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(P.df00(set1.getString("net_total")), font21, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                    }
                    sino++;
                    if (sino >= endtSINO) {
                        break;
                    }
                }
                if (countLiines < 9) {
                    int j = 9 - countLiines;

                     Paragraph par40 = new Paragraph(new Chunk(" ", font2_1));
                     PdfPCell Cel40 = new PdfPCell(par40);
                     Cel40.setBorder(Rectangle.RIGHT);
                     Cel40.setCellEvent(dcell);

                    for (int k = 0; k < (5 * j); k++) {
                        table1.addCell(Cel40);

                    }
                }
                table1.getDefaultCell().setBorder(Rectangle.BOX);

                table1.addCell(createCellSpace1("", font2_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
                table1.addCell(createCellSpace1("TOTAL", font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1("", font2_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1("", font2_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1("", font2_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1("", font2_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1("", font2_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1(P.df00(taxableAMOUNT), font1_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1("", font2_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
//                table1.addCell(createCellSpace1(P.df00(TA), font1_1 , Element.ALIGN_CENTER, Rectangle.BOX, color));
                table1.addCell(createCellSpace1("", font2_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
                table1.addCell(createCellSpace1("", font2_1, Element.ALIGN_CENTER, Rectangle.BOX, color));
                table1.addCell(createCellSpace1(P.df00(billAmmmt), font1_1 ,Element.ALIGN_CENTER, Rectangle.BOX, color));


                PdfPTable table4 = new PdfPTable(1);
                table4.setWidthPercentage(90);
                table4.addCell(new PdfPCell(table1));

                mainCell2   =   new PdfPCell(table4);
                tableM1_2.addCell(mainCell2);

                ResultSet sett = connection.createStatement().executeQuery("select * from billing2 where invoice_no='" + invoiceNumber.getText().trim() + "'");
                double billamount = 0.0;
                while (sett.next()) {
                    billamount = sett.getDouble("grand_total");
                }

                ResultSet sett1 = connection.createStatement().executeQuery("select * from profile");
                String terms = " ", busness_name = " ", accDetails=" ";
                while (sett1.next()) {
                    terms = sett1.getString("terms") + "\n" + sett1.getString("declaration");
                    busness_name = sett1.getString("name");
                    accDetails= "ACC NAME : "+sett1.getString("acc_name")+
                            "\nACC NO : "+sett1.getString("acc_num")+
                            "\nACC TYPE : "+sett1.getString("acc_type")+
                            "\nIFSC COE : "+sett1.getString("ifsc");
                    break;
                }

                PdfPTable table = new PdfPTable(2);
                table.setWidths(new int[]{6, 4});
                table.setWidthPercentage(95);

                NumToWordss numToWordss = new NumToWordss();
                double famount = 0;
                String amountInString = "", finalAMountText = "";

                double oldBBBlnce =  0, billAmmt = 0;
                try{ oldBBBlnce = Double.parseDouble(Old_balanceField.getText().trim());  }catch (Exception e){}
                try{ billAmmt = Double.parseDouble(NeedTo_payfield.getText().trim());  }catch (Exception e){}

                try{
                    famount = (long) billamount;
                    famount = (long) billAmmt;
                    if(famount<0){
                        famount = famount*(-1);
                        amountInString = numToWordss.convert((long) famount)+" Rupees only";
                        finalAMountText = "         Amount Payable  : " + P.df00(famount);
                    }else{
                        amountInString = numToWordss.convert((long) famount)+" Rupees only";
                        finalAMountText = "         Amount Receivable  : " + P.df00(famount);
                    }
                }catch (Exception e){}


                Paragraph amo=new Paragraph();
                amo.add(new Chunk("Rupees in words   :    \n", new Font(Font.FontFamily.UNDEFINED, 9)));
                amo.add(new Chunk(amountInString.toUpperCase() , new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                table.addCell(new PdfPCell(amo));

                {
                    PdfPTable tableTotal=new PdfPTable(1);
                    tableTotal.setWidthPercentage(100);

                    PdfPTable tableGstSummary=new PdfPTable(5);
                    tableGstSummary.setWidths(new int[]{2,5,4,4,5});
                    tableGstSummary.setWidthPercentage(100);
                    ResultSet setgstSummary=connection.createStatement().executeQuery(
                            "select cgst+sgst, sum(taxable_value), sum(cgst_amount), sum(sgst_amount), sum(cgst_amount+sgst_amount) " +
                                    "from billing where invoice_no = '"+invoiceNumber.getText().trim()+"' and (cgst+sgst) > "+0+"  group by  cgst+sgst ORDER BY cgst+sgst");

                    Font fontg1=new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD );
                    Font fontg2=new Font(Font.FontFamily.HELVETICA, 6 );
                    tableGstSummary.addCell(createCell2("@", fontg1, 1, 15));
                    tableGstSummary.addCell(createCell2("TXBL AMOUNT", fontg1, 1, 15));
                    tableGstSummary.addCell(createCell2("CGST", fontg1, 1, 15));
                    tableGstSummary.addCell(createCell2("SGST", fontg1, 1, 15));
                    tableGstSummary.addCell(createCell2("GST AMOUNT", fontg1, 1, 15));

                    boolean dataFound=false;
                    while(setgstSummary.next()){
                        dataFound=true;
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(1), fontg2, 1, 15, dcell));
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(2), fontg2, 1, 15,dcell));
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(3), fontg2, 1, 15,dcell));
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(4), fontg2, 1, 15,dcell));
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(5), fontg2, 1, 15,dcell));
                    }
                    if(dataFound){
                        tableTotal.addCell(new PdfPCell(tableGstSummary));
                    }
                    table.addCell(new PdfPCell( tableTotal));
                }

                int i=0;
                Paragraph paragraph33 = new Paragraph();
                paragraph33.add(new Chunk("Bill Amount  : " + P.df00(billAmmmt), new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                i++;
                double qqq=0; try{qqq = Double.parseDouble(d_rAmount.getText().trim());}catch (Exception e){}
                double rstockAmt=0; try{rstockAmt = Double.parseDouble(returmStockAmmt);}catch (Exception e){}
                P.p("qqq : "+qqq);
                P.p("rstockAmt : "+rstockAmt);
                if(qqq>0){
                    i++;
                    paragraph33.add(new Chunk("         Deduction Amount : " + P.df00(qqq), new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                }
                if(rstockAmt>0){
                    i++;
                    paragraph33.add(new Chunk("         Return stock Amount : " + P.df00(rstockAmt), new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                }
                if(i==3){
                    paragraph33.add(new Chunk("\n", new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                }
/*
                if(oldBBBlnce>0){
                    paragraph33.add(new Chunk("      Old Balance : " + P.df00(oldBBBlnce), new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                }
*/

                paragraph33.add(new Chunk(finalAMountText, new Font(Font.FontFamily.UNDEFINED, 12, Font.BOLD)));
                PdfPCell cell = new PdfPCell(paragraph33);
                cell.setColspan(2);
                table.addCell(cell);



                PdfPTable table2_2=new PdfPTable(2);
                table2_2.setWidths(new int[]{5,5});

                Paragraph paragraph35 = new Paragraph();
                paragraph35.add(new Chunk("Terms and Condition  :   ", new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                paragraph35.add(new Chunk("\n" + terms, new Font(Font.FontFamily.UNDEFINED, 7)));
                PdfPCell cell12 = new PdfPCell(paragraph35);
                table2_2.addCell(cell12);


                Paragraph paragraph7 = new Paragraph();
                if(remAmmt.isEmpty() && payedAmmt.isEmpty() && other_details.isEmpty() && paytype.trim().isEmpty()){
                    paragraph7.add(new Chunk(  "", font3));
                }else {
                    if(!paytype.isEmpty()) {
                        paragraph7.add(new Chunk("PAYMENT TYPE : ", font333));
                        paragraph7.add(new Chunk(paytype+"\n", font33));
                    }
                    if(!payedAmmt.isEmpty()) {
                        paragraph7.add(new Chunk("PAYED AMOUNT : ", font333));
                        paragraph7.add(new Chunk(payedAmmt+"\n", font33));
                    }
                    if(!remAmmt.isEmpty()) {
                        paragraph7.add(new Chunk("REMAINING AMOUNT : ", font333));
                        paragraph7.add(new Chunk(remAmmt+"\n", font33));
                    }
                    if(!other_details.isEmpty()) {
                        paragraph7.add(new Chunk("OTHER DETAILS : ", font333));
                        paragraph7.add(new Chunk(other_details, font33));
                    }
                }

                cell12 = new PdfPCell(paragraph7);
                table2_2.addCell(cell12);

                table.addCell(new PdfPCell(table2_2));

                Paragraph paragraph36 = new Paragraph();
                paragraph36.add(new Chunk("For  " + busness_name, new Font(Font.FontFamily.UNDEFINED, 10)));
                paragraph36.add(new Chunk("\n\n\nAuthorised Signature", new Font(Font.FontFamily.UNDEFINED, 10)));
                paragraph36.setAlignment(Element.ALIGN_CENTER);
                PdfPCell cell13 = new PdfPCell(paragraph36);
                cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell13);

                mainCell2   =   new PdfPCell(table);
                tableM1_2.addCell(mainCell2);

                PdfPTable tablelast=new PdfPTable(3);
                cell=new PdfPCell(new Phrase(" "));
                cell.setBorder(Rectangle.NO_BORDER);
                tablelast.addCell(cell);

                Paragraph paragraph3 = new Paragraph(new Phrase("Computer Generated Bill", FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 7, BaseColor.GRAY)));
                paragraph3.setAlignment(Element.ALIGN_CENTER);
                cell=new PdfPCell(paragraph3);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablelast.addCell(cell);

                Paragraph paragraph4 = new Paragraph(new Phrase("Software by MITRA SOFTWARES-08182-298188        ", FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 3, BaseColor.GRAY)));
                paragraph4.setAlignment(Element.ALIGN_RIGHT);
                cell=new PdfPCell(paragraph4);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(Rectangle.NO_BORDER);
                tablelast.addCell(cell);

                mainCell2=new PdfPCell(tablelast);
                mainCell2.setBorder(Rectangle.NO_BORDER);
                tableM1_2.addCell(mainCell2);

                PdfPCell mainCell1  =   new PdfPCell();
                double cellHeight   =   (PageSize.A6.getHeight()-20);
                mainCell1.setFixedHeight((float) cellHeight);
                mainCell1.setBorder(Rectangle.NO_BORDER);
                mainCell1.addElement(tableM1_2);

                // tableM1_1.addCell(mainCell1);
                tableM1_1.addCell(mainCell1);
                doc.add(tableM1_1);
                if (sino <= endwith) {
                    doc.newPage();
                    pdf_sheet_creatorA5_2(endtSINO, endwith, doc);
                }

            }
            // bill finishing

            invoiceNumber.clear();
            customerNameEditText.clear();
            customerId.clear();
            grantBilling.clear();
            d_rAmount.clear();
            finalAmount.clear();
            ReturnAdded.getChildren().clear();
            remainAmt.clear();
            returnStockAmount.clear();
//            payedAmt.clear();
            Old_balanceField.clear();
            NeedTo_payfield.clear();

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }



    public TextField grantBilling;

    public TextField tradeprice;



    public void return_all_item(ActionEvent actionEvent) throws Exception {

        Connection connection = null;
        try {
            connection = DBConnect.getConnection();
            if(ReturnAdded.getChildren().isEmpty()){

            } else if(invoiceNumber.getText().trim().isEmpty()) {

            }else
            {
                String query2 = "select * from billing where invoice_no='" + invoiceNumber.getText().trim() + "'";
                if(connection.isClosed()) { connection = DBConnect.getConnection(); }
                ResultSet set3 = connection.createStatement().executeQuery(query2);
                while (set3.next()) {
                    String p_id = set3.getString("product_id");
                    double mbID = set3.getDouble("bill_id");
                    double quantity = set3.getDouble("quantity");

                    String query6 = "select stockquantity from stock where id='" + p_id + "'";
                    ResultSet set6 = connection.createStatement().executeQuery(query6);
                    double oldStock = 0.0;
                    while (set6.next()) {
                        oldStock = set6.getDouble(1);
                        break;
                    }
                    double qntrtn = (oldStock + quantity);
                    String query4 = "update stock set stockquantity=" + qntrtn + " where id='" + p_id + "'";
                    connection.createStatement().executeUpdate(query4);
                    String query5 = "delete from billing where bill_id='" + mbID + "'";
                    connection.createStatement().executeUpdate(query5);
                    ReturnAdded.getChildren().clear();
                    customerNameEditText.clear();
                    customerId.clear();
                    grantBilling.clear();
                    d_rAmount.clear();
                    finalAmount.clear();
                    dis_in_amount.clear();

                }
                loadNeeToPayAndOldBalnce();
            }
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        } finally { if (!connection.isClosed()) connection.close();   }
    }

    public void openPaymentWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreditPayment.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("PAYMENT");
        stage.setScene(new Scene(root1, 900, 700));
        stage.show();
    }

    public void customerSearchByName(KeyEvent event) throws Exception {
        Connection connection2=null;
        try {
            connection2=DBConnect.getConnection();
            if (!customerNameEditText.getText().trim().isEmpty()) {
                String query    =   "select * from customer where customername like '%" + customerNameEditText.getText().trim().toUpperCase() + "%'";
                LoadingTableViewDataSelectedRowName.Welcome(query, showstock, 250, 550);
                if(connection2.isClosed())  { connection2= DBConnect.getConnection(); }
                ResultSet set2 = connection2.createStatement().executeQuery(query + " limit 1 ");
                if(set2.next()) {
                    customerId.setText(set2.getString("customerid"));
                }else {
                    customerId.setText("0");
                }
            }
            loadNeeToPayAndOldBalnce();
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            if(!connection2.isClosed())  { connection2.close(); }
        }
    }

    public void customerSearchById(KeyEvent event) throws Exception {
        Connection connection2=null;
        try {
            connection2=DBConnect.getConnection();
            if (!customerId.getText().trim().isEmpty()) {
                String query    =   "select * from customer where customerid like '%" + customerId.getText().trim().toUpperCase() + "%'";
                LoadingTableViewDataSelectedRowName.Welcome(query, showstock, 250, 550);
                if(connection2.isClosed())  { connection2= DBConnect.getConnection(); }
                ResultSet set2 = connection2.createStatement().executeQuery(query + " limit 1");
                if(set2.next()) {
                    customerNameEditText.setText(set2.getString("customername"));
                }else {
                    customerNameEditText.setText("");
                }
            }
            loadNeeToPayAndOldBalnce();
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            if(!connection2.isClosed())  { connection2.close(); }
        }
    }

    public void loadNeeToPayAndOldBalnce() throws Exception {
        Connection connection = null;
        try {
            connection = DBConnect.getConnection();
            double old_balance = 0, grandTotalAmount = 0;
            boolean customerExist = false;
            if (!customerId.getText().trim().isEmpty()) {
                ResultSet set2 = connection.createStatement().executeQuery("select * from  customer where customerid='" + customerId.getText().trim() + "'");
                if(set2.next()) {
                    customerExist = true;
                }
            }
            if (!customerExist) {
                Old_balanceField.setText(P.df00(0));
                NeedTo_payfield.setText(P.df00(0));
            } else {
                ResultSet set2 = connection.createStatement().executeQuery("select * from  customer where customerid='" + customerId.getText().trim() + "'");
                if(set2.next()) {
                    old_balance = set2.getDouble("balance");
                    grandTotalAmount=0;
                    Old_balanceField.setText(P.df00(old_balance));
                    try{
                        grandTotalAmount    =  Double.parseDouble(P.df00(finalAmount.getText()));
                    } catch (Exception e){   }
                    NeedTo_payfield.setText(P.df00(grandTotalAmount + old_balance));
                }
            }
            double blnceAmmt = 0, payingAmmt = 0;
          //  try { payingAmmt = Double.parseDouble(payedAmt.getText().trim()); }catch (Exception e) {e.printStackTrace();}
            blnceAmmt = ( old_balance) ;
            remainAmt.setText(P.df00(blnceAmmt));
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        } finally {
            if(!connection.isClosed()){  connection.close();  }
        }
    }

    public void loadInvoiceDetailsByInvoiceNumber(KeyEvent keyEvent) {
        loadDetailsByInvoiceNumber2(invoiceNumber.getText().trim());
    }

    public void updateCustomerBalanceByNameOrId() throws Exception {

        P.p("Update Balajee Customer Balance 1");
        Connection connection = null;
        try {
            int cIdForCustomerAdd = 0;
            connection = DBConnect.getConnection();
            boolean customerExist = false;
            if (!customerId.getText().trim().isEmpty()) {
                ResultSet set2 = connection.createStatement().executeQuery("select * from  customer where customerid='" + customerId.getText().trim() + "'");
                if(set2.next()) {
                    customerExist = true;
                    cIdForCustomerAdd = set2.getInt("customerid");
                }
            }

            if (!customerExist && !customerNameEditText.getText().trim().isEmpty()) {

                String query1 = "Insert into customer( customername, phoneno, state_statecode, gstinno, customeraddress, " +
                        " balance, extra ) " +
                        "values( ?,?,?,?,?,    ?,?)";

                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);

                preparedStatement1.setString(1, customerNameEditText.getText());
                preparedStatement1.setString(2, "");
                preparedStatement1.setString(3, "");
                preparedStatement1.setString(4, "");
                preparedStatement1.setString(5, "");
                preparedStatement1.setString(6, P.df00(remainAmt.getText().trim()));
               // preparedStatement1.setString(7, "");
                preparedStatement1.setString(7, "0");
                P.p(" preparedStatement1 : "+preparedStatement1);
                cIdForCustomerAdd = preparedStatement1.executeUpdate();

                P.p("Customer Primary Key");

            } else {

                String query1 = "update customer set balance = ? where customerid="+cIdForCustomerAdd;

                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement1.setString(1, P.df00(remainAmt.getText().trim()));

                if(preparedStatement1.execute()){
                    P.p("Customer Details updated");
                }else {
                    P.p("Customer Details not updated");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        } finally {
            if(!connection.isClosed()){  connection.close();  }
        }
    }

    public void addCustomer() throws Exception {

        P.p("Update Balajee Customer Balance 1");
        Connection connection = null;
        try {
            int cIdForCustomerAdd = 0;
            connection = DBConnect.getConnection();
            boolean customerExist = false;
            if (!customerId.getText().trim().isEmpty()) {
                ResultSet set2 = connection.createStatement().executeQuery("select * from  customer where customerid='" + customerId.getText().trim() + "'");
                if(set2.next()) {
                    customerExist = true;
                    cIdForCustomerAdd = set2.getInt("customerid");
                }
            }

            if (!customerExist && !customerNameEditText.getText().trim().isEmpty()) {

                String query1 = "Insert into customer( customername, phoneno, state_statecode, gstinno, customeraddress, " +
                        " balance, extra ) " +
                        "values( ?,?,?,?,?,    ?,?)";

                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);

                preparedStatement1.setString(1, customerNameEditText.getText());
                preparedStatement1.setString(2, "");
                preparedStatement1.setString(3, "");
                preparedStatement1.setString(4, "");
                preparedStatement1.setString(5, "");
                preparedStatement1.setString(6, "0");
                preparedStatement1.setString(7, "");
            //    preparedStatement1.setString(8, "0");
                P.p(" preparedStatement1 : "+preparedStatement1);
                cIdForCustomerAdd = preparedStatement1.executeUpdate();

                P.p("Customer Primary Key");

                try{
                    ResultSet set2 = connection.createStatement().executeQuery("select * from  customer where customername='" + customerNameEditText.getText().trim() + "'");
                    if(set2.next()) {
                        customerId.setText(set2.getString("customerid"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    P.loggerLoader(e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        } finally {
            if(!connection.isClosed()){  connection.close();  }
        }
    }


    public void pdf_sheet_creatorA5_3(int startfrom, int endwith, Document doc) throws SQLException, DocumentException, FileNotFoundException, ClassNotFoundException {
        Connection connection = null;
        try {
            connection = DBConnect.getConnection();
            {
                String name = "", address = "", phno = "", logoPath="", mailId="", gst_number="";

                PdfPTable tableM1_1=new PdfPTable(1);
                tableM1_1.setWidthPercentage(95);

                PdfPTable tableM1_2=new PdfPTable(1);
                tableM1_2.setWidthPercentage(100);
                PdfPCell mainCell2;

                ResultSet set2 = connection.createStatement().executeQuery("select * from profile ");
                while (set2.next()) {
                    name = set2.getString("name");
                    address = set2.getString("mail_addres");
                    mailId  =   set2.getString("comemailid");
                    phno = set2.getString("contact");
                    logoPath=set2.getString("logo");
                    gst_number  =   set2.getString("gstin");
                }

                String c_id="", d_to="", d_through="", other_details="", paytype = "", remAmmt = "", payedAmmt = "", returmStockAmmt= "";
                double BA=0, TA=0, taxableAMOUNT=0, billAmmmt =0 ;
                ResultSet setc_id_billAMount = connection.createStatement().executeQuery("select * from billing2 where invoice_no ='"+invoiceNumber.getText().trim()+"'");
                while(setc_id_billAMount.next()){
                    c_id=setc_id_billAMount.getString("customer_id");
                    BA=setc_id_billAMount.getDouble("grand_total");
                    billAmmmt=setc_id_billAMount.getDouble("total_net_amount");
                    TA=setc_id_billAMount.getDouble("total_sgst_amount")+setc_id_billAMount.getDouble("total_cgst_amount");
                    taxableAMOUNT=setc_id_billAMount.getDouble("total_taxable_amount");
                    d_to    =   setc_id_billAMount.getString("dispatch_to_address").trim();
                    d_through    =   setc_id_billAMount.getString("dispatch_through").trim();
                    other_details    =   setc_id_billAMount.getString("other_details").trim();
                    paytype    =   setc_id_billAMount.getString("payment_type").trim();
                    remAmmt =    setc_id_billAMount.getString("remainingAmount").trim();
                    payedAmmt =    setc_id_billAMount.getString("payedAmount").trim();
                    returmStockAmmt = setc_id_billAMount.getString("returnStockAmount").trim();
                }

                String C_name="", C_phno="", C_gst="", C_address="", C_state="",croute="";
                ResultSet setcustomer = connection.createStatement().executeQuery("select * from customer where customerid='"+c_id+"'");
                while (setcustomer.next()) {
                    C_name = setcustomer.getString("customername");
                    C_address = setcustomer.getString("customeraddress");
                    C_phno = setcustomer.getString("phoneno");
//                    C_state = setcustomer.getString("state_statecode");
//                    C_gst = setcustomer.getString("gstinno");
                //    croute=setcustomer.getString("route_name");
                }

                PdfPTable table1_2=new PdfPTable(2);
                table1_2.setWidthPercentage(95);
                table1_2.setWidths(new int[ ]{2,10});
                Image logo=null;
                try {
                    logo = Image.getInstance(logoPath);
                    logo.setBorder(Rectangle.NO_BORDER);
                }catch (Exception e){ e.printStackTrace(); P.loggerLoader(e);  }


                try{
                    table1_2.addCell(logo);
                }catch (Exception e){
                    table1_2.addCell(" ");
                    e.printStackTrace();
                    P.loggerLoader(e);
                }

                Paragraph paragraph6 = new Paragraph();
                paragraph6.setAlignment(Element.ALIGN_CENTER);
                paragraph6.add(new Chunk("GST INVOICE\n", new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                paragraph6.add(new Chunk(name, new Font(Font.FontFamily.UNDEFINED, 15, Font.BOLD)));
                paragraph6.add(new Chunk("\n"+address, new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                String demo="";
                if((!phno.isEmpty())  && (!mailId.isEmpty())){  demo =   phno+"  ,   "+mailId;  }
                else{ demo  =   phno+mailId;   }
                if (!demo.isEmpty()) {  paragraph6.add(new Chunk("\n" + demo, new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));  }
                if (!gst_number.isEmpty()) {  paragraph6.add(new Chunk("\nGSTIN : " + gst_number, new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));  }
                //paragraph6.add(new Chunk("\nINVOICE NO:" + invoiceNumber.getText(), new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                // String d = dateTaking.getEditor().getText();
                //String d1[] = d.split("/");
                //  paragraph6.add(new Phrase("                                    DATE :" + d1[1] + "-" + d1[0] + "-" + d1[2], new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));

                PdfPCell cell1_3=new PdfPCell(paragraph6);
                cell1_3.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1_2.addCell(cell1_3);

                mainCell2   =   new PdfPCell(table1_2);
                tableM1_2.addCell(mainCell2);


                Font font3 = new Font(Font.FontFamily.UNDEFINED, 9, Font.BOLD);
                Font font33 = new Font(Font.FontFamily.UNDEFINED, 7);
                Font font333 = new Font(Font.FontFamily.UNDEFINED, 7, Font.BOLD);


                Paragraph paragraph8 = new Paragraph();
                paragraph8.add(new Chunk("CUSTOMER DETAILS  : ", new Font(Font.FontFamily.UNDEFINED, 12, Font.BOLD)));
                paragraph8.add(new Chunk("\nNAME   : " + customerNameEditText.getText(), font3));
                paragraph8.add(new Chunk("\nPHNO        :" + C_phno, font33));
//                paragraph8.add(new Chunk("\nADDRESS :" + C_address, font33));
//                paragraph8.add(new Chunk("\nSTATE       :" + C_state, font33));
                paragraph8.add(new Chunk("\nGSTNO     :" + C_gst, font33));
            //    paragraph8.add(new Chunk("\nRoute        :" + croute, font33));
                PdfPTable table2 = new PdfPTable(2);
                table2.setWidthPercentage(95);
                PdfPCell cell2 = new PdfPCell(paragraph8);
                cell2.setBorder(Rectangle.TOP);
                table2.addCell(cell2);

                paragraph8 = new Paragraph();
                paragraph8.add(new Chunk("                                                                  INVOICE NO:" + invoiceNumber.getText(), new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                String d = dateTaking.getEditor().getText();
                String d1[] = d.split("/");
                paragraph8.add(new Phrase("\n                                                                 DATE :" + d1[1] + "-" + d1[0] + "-" + d1[2], new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));

                //  paragraph8.add(new Chunk("\n"+mbDto.getDispatchDetails(), font33));
                cell2 = new PdfPCell(paragraph8);
                cell2.setBorder(Rectangle.TOP);
                table2.addCell(cell2);

                mainCell2   =   new PdfPCell(table2);
                tableM1_2.addCell(mainCell2);

                PdfPTable table1 = new PdfPTable(11);

                table1.setWidths(new int[]{2, 15, 5, 3, 5, 4,4, 5, 4, 5, 6});
                Font font1 = new Font(Font.FontFamily.UNDEFINED, 9, Font.BOLD);
                Font font1_1 = new Font(Font.FontFamily.UNDEFINED, 9.5f, Font.BOLD);
                Font font2 = new Font(Font.FontFamily.UNDEFINED, 8);
                Font font2_1 = new Font(Font.FontFamily.UNDEFINED, 9, Font.BOLD);
                Font font22 = new Font(Font.FontFamily.UNDEFINED, 7);

                table1.addCell(new Phrase(new Chunk("Sl", font1_1)));
                table1.addCell(new Phrase(new Chunk("Description", font1_1)));
                table1.addCell(new Phrase(new Chunk("HSN ", font1_1)));
                table1.addCell(new Phrase(new Chunk("Qty", font1_1)));
                table1.addCell(new Phrase(new Chunk("PACK", font1_1)));
                table1.addCell(new Phrase(new Chunk("Rate", font1_1)));
                table1.addCell(new Phrase(new Chunk("Disc Amnt", font1_1)));
                table1.addCell(new Phrase(new Chunk("Taxable Amnt", font1_1)));
                table1.addCell(new Phrase(new Chunk("GST%", font1_1)));
                table1.addCell(new Phrase(new Chunk("GST Amnt", font1_1)));
                table1.addCell(new Phrase(new Chunk("Net Amnt", font1_1)));

                int startSINO = startfrom;
                int endtSINO = startfrom + 10;
                int sino = 1, num = 0;

                DottedCell dcell=new DottedCell(2);

                table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                String query = "select * from billing where invoice_no='" + invoiceNumber.getText() + "'";
                ResultSet set1 = connection.createStatement().executeQuery(query);
                int countLiines = 0;
                while (set1.next()) {
                    if ((sino >= startSINO) && (sino < endtSINO)) {
                        countLiines++;

                        table1.addCell(createCell1(String.valueOf(sino), font2_1, Element.ALIGN_CENTER, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(set1.getString("product_name"), font2_1, Element.ALIGN_LEFT, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(set1.getString("hsn"), font2_1, Element.ALIGN_LEFT, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(set1.getString("quantity"), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(set1.getString("packing"), font2_1, Element.ALIGN_LEFT, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(P.df00(set1.getString("trade_price")), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(P.df00(set1.getString("discount_amount")), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(P.df00(set1.getString("taxable_value")), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(P.df00(set1.getDouble("cgst") * 2), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(P.df00(set1.getDouble("cgst_amount") * 2), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(createCell1(P.df00(set1.getString("net_total")), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                    }
                    sino++;
                    if (sino >= endtSINO) {
                        break;
                    }
                }
                if (countLiines < 10) {
                    int j = 10 - countLiines;

                    Paragraph par40 = new Paragraph(new Chunk(" ", font2_1));
                    PdfPCell Cel40 = new PdfPCell(par40);
                    Cel40.setBorder(Rectangle.RIGHT);
                    Cel40.setCellEvent(dcell);

                    for (int k = 0; k < (11 * j); k++) {
                        table1.addCell(Cel40); }
                }
                table1.getDefaultCell().setBorder(Rectangle.BOX);

                table1.addCell(new Phrase(new Chunk("", font2_1)));
                table1.addCell(createCell2("TOTAL", font2_1 , Element.ALIGN_CENTER, Rectangle.BOX));
                table1.addCell(new Phrase(new Chunk("", font2_1)));
                table1.addCell(new Phrase(new Chunk("", font2_1)));
                table1.addCell(new Phrase(new Chunk("", font2_1)));
                table1.addCell(new Phrase(new Chunk("", font2_1)));
                table1.addCell(new Phrase(new Chunk("", font2_1)));
                table1.addCell(createCell2(P.df00(taxableAMOUNT), font1_1 , Element.ALIGN_CENTER, Rectangle.BOX));
                table1.addCell(new Phrase(new Chunk("", font2_1)));

                table1.addCell(createCell2(P.df00(TA), font1_1 , Element.ALIGN_CENTER, Rectangle.BOX));
                table1.addCell(createCell2(P.df00(billAmmmt), font1_1 , Element.ALIGN_CENTER, Rectangle.BOX));

                PdfPTable table4 = new PdfPTable(1);
                table4.setWidthPercentage(95);
                table4.addCell(new PdfPCell(table1));

                mainCell2   =   new PdfPCell(table4);
                tableM1_2.addCell(mainCell2);

                ResultSet sett = connection.createStatement().executeQuery("select * from billing2 where invoice_no='" + invoiceNumber.getText().trim() + "'");
                double billamount = 0.0;
                while (sett.next()) {
                    billamount = sett.getDouble("grand_total");
                }

                ResultSet sett1 = connection.createStatement().executeQuery("select * from profile");
                String terms = " ", busness_name = " ", accDetails=" ";
                while (sett1.next()) {
                    terms = sett1.getString("terms") + "\n" + sett1.getString("declaration");
                    busness_name = sett1.getString("name");
                    accDetails= "ACC NAME : "+sett1.getString("acc_name")+
                            "\nACC NO : "+sett1.getString("acc_num")+
                            "\nACC TYPE : "+sett1.getString("acc_type")+
                            "\nIFSC COE : "+sett1.getString("ifsc");
                    break;
                }

                PdfPTable table = new PdfPTable(2);
                table.setWidths(new int[]{6, 4});
                table.setWidthPercentage(95);

                NumToWordss numToWordss = new NumToWordss();
                double famount = 0;
                String amountInString = "", finalAMountText = "";

                double oldBBBlnce =  0, billAmmt = 0;
                try{ oldBBBlnce = Double.parseDouble(Old_balanceField.getText().trim());  }catch (Exception e){}
                try{ billAmmt = Double.parseDouble(NeedTo_payfield.getText().trim());  }catch (Exception e){}

                try{
                    famount = (long) billamount;
                    famount = (long) billAmmt;
                    if(famount<0){
                        famount = famount*(-1);
                        amountInString = numToWordss.convert((long) famount)+" Rupees only";
                        finalAMountText = "         Amount Payable  : " + P.df00(famount);
                    }else{
                        amountInString = numToWordss.convert((long) famount)+" Rupees only";
                        finalAMountText = "         Amount Receivable  : " + P.df00(famount);
                    }
                }catch (Exception e){}


                Paragraph amo=new Paragraph();
                amo.add(new Chunk("Rupees in words   :    \n", new Font(Font.FontFamily.UNDEFINED, 9)));
                amo.add(new Chunk(amountInString.toUpperCase() , new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                table.addCell(new PdfPCell(amo));

                {
                    PdfPTable tableTotal=new PdfPTable(1);
                    tableTotal.setWidthPercentage(100);

                    PdfPTable tableGstSummary=new PdfPTable(5);
                    tableGstSummary.setWidths(new int[]{2,5,4,4,5});
                    tableGstSummary.setWidthPercentage(100);
                    ResultSet setgstSummary=connection.createStatement().executeQuery(
                            "select cgst+sgst, sum(taxable_value), sum(cgst_amount), sum(sgst_amount), sum(cgst_amount+sgst_amount) " +
                                    "from billing where invoice_no = '"+invoiceNumber.getText().trim()+"' and (cgst+sgst) > "+0+"  group by  cgst+sgst ORDER BY cgst+sgst");

                    Font fontg1=new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD );
                    Font fontg2=new Font(Font.FontFamily.HELVETICA, 6 );
                    tableGstSummary.addCell(createCell2("@", fontg1, 1, 15));
                    tableGstSummary.addCell(createCell2("TXBL AMOUNT", fontg1, 1, 15));
                    tableGstSummary.addCell(createCell2("CGST", fontg1, 1, 15));
                    tableGstSummary.addCell(createCell2("SGST", fontg1, 1, 15));
                    tableGstSummary.addCell(createCell2("GST AMOUNT", fontg1, 1, 15));

                    boolean dataFound=false;
                    while(setgstSummary.next()){
                        dataFound=true;
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(1), fontg2, 1, 15, dcell));
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(2), fontg2, 1, 15,dcell));
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(3), fontg2, 1, 15,dcell));
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(4), fontg2, 1, 15,dcell));
                        tableGstSummary.addCell(createCell1(setgstSummary.getString(5), fontg2, 1, 15,dcell));
                    }
                    if(dataFound){
                        tableTotal.addCell(new PdfPCell(tableGstSummary));
                    }
                    table.addCell(new PdfPCell( tableTotal));
                }

                int i=0;
                Paragraph paragraph33 = new Paragraph();
                paragraph33.add(new Chunk("Bill Amount  : " + P.df00(billAmmmt), new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                i++;
                double qqq=0; try{qqq = Double.parseDouble(d_rAmount.getText().trim());}catch (Exception e){}
                double rstockAmt=0; try{rstockAmt = Double.parseDouble(returmStockAmmt);}catch (Exception e){}
                P.p("qqq : "+qqq);
                P.p("rstockAmt : "+rstockAmt);
                if(qqq>0){
                    i++;
                    paragraph33.add(new Chunk("         Diduction Amount : " + P.df00(qqq), new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                }
                if(rstockAmt>0){
                    i++;
                    paragraph33.add(new Chunk("         Return stock Amount : " + P.df00(rstockAmt), new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                }
                if(i==3){
                    paragraph33.add(new Chunk("\n", new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                }

                if(oldBBBlnce>0){
                    paragraph33.add(new Chunk("      Old Balance : " + P.df00(oldBBBlnce), new Font(Font.FontFamily.UNDEFINED, 10, Font.BOLD)));
                }


                paragraph33.add(new Chunk(finalAMountText, new Font(Font.FontFamily.UNDEFINED, 12, Font.BOLD)));
                PdfPCell cell = new PdfPCell(paragraph33);
                cell.setColspan(2);
                table.addCell(cell);



                PdfPTable table2_2=new PdfPTable(2);
                table2_2.setWidths(new int[]{5,5});

                Paragraph paragraph35 = new Paragraph();
                paragraph35.add(new Chunk("Terms and Condition  :   ", new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD)));
                paragraph35.add(new Chunk("\n" + terms, new Font(Font.FontFamily.UNDEFINED, 7)));
                PdfPCell cell12 = new PdfPCell(paragraph35);
                table2_2.addCell(cell12);


                Paragraph paragraph7 = new Paragraph();
                if(remAmmt.isEmpty() && payedAmmt.isEmpty() && other_details.isEmpty() && paytype.trim().isEmpty()){
                    paragraph7.add(new Chunk(  "", font3));
                }else {
                    if(!paytype.isEmpty()) {
                        paragraph7.add(new Chunk("PAYMENT TYPE : ", font333));
                        paragraph7.add(new Chunk(paytype+"\n", font33));
                    }
                    if(!payedAmmt.isEmpty()) {
                        paragraph7.add(new Chunk("PAYED AMOUNT : ", font333));
                        paragraph7.add(new Chunk(payedAmmt+"\n", font33));
                    }
                    if(!remAmmt.isEmpty()) {
                        paragraph7.add(new Chunk("REMAINING AMOUNT : ", font333));
                        paragraph7.add(new Chunk(remAmmt+"\n", font33));
                    }
                    if(!other_details.isEmpty()) {
                        paragraph7.add(new Chunk("OTHER DETAILS : ", font333));
                        paragraph7.add(new Chunk(other_details, font33));
                    }
                }

                cell12 = new PdfPCell(paragraph7);
                table2_2.addCell(cell12);

                table.addCell(new PdfPCell(table2_2));

                Paragraph paragraph36 = new Paragraph();
                paragraph36.add(new Chunk("For  " + busness_name, new Font(Font.FontFamily.UNDEFINED, 10)));
                paragraph36.add(new Chunk("\n\n\nAuthorised Signature", new Font(Font.FontFamily.UNDEFINED, 10)));
                paragraph36.setAlignment(Element.ALIGN_CENTER);
                PdfPCell cell13 = new PdfPCell(paragraph36);
                cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell13);

                mainCell2   =   new PdfPCell(table);
                tableM1_2.addCell(mainCell2);

                PdfPTable tablelast=new PdfPTable(3);
                cell=new PdfPCell(new Phrase(" "));
                cell.setBorder(Rectangle.NO_BORDER);
                tablelast.addCell(cell);

                Paragraph paragraph3 = new Paragraph(new Phrase("Computer Generated Bill", FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 20, BaseColor.GRAY)));
                paragraph3.setAlignment(Element.ALIGN_CENTER);
                cell=new PdfPCell(paragraph3);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablelast.addCell(cell);

                Paragraph paragraph4 = new Paragraph(new Phrase("Software by MITRA SOFTWARES-08182-298188        ", FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 3, BaseColor.GRAY)));
                paragraph4.setAlignment(Element.ALIGN_RIGHT);
                cell=new PdfPCell(paragraph4);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(Rectangle.NO_BORDER);
                tablelast.addCell(cell);

                mainCell2=new PdfPCell(tablelast);
                mainCell2.setBorder(Rectangle.NO_BORDER);
                tableM1_2.addCell(mainCell2);

                PdfPCell mainCell1  =   new PdfPCell();
                double cellHeight   =   (PageSize.A4.getHeight()-20)/2;
                mainCell1.setFixedHeight((float) cellHeight);
                mainCell1.setBorder(Rectangle.NO_BORDER);
                mainCell1.addElement(tableM1_2);

                // tableM1_1.addCell(mainCell1);
                tableM1_1.addCell(mainCell1);
                doc.add(tableM1_1);
                if (sino <= endwith) {
                    doc.newPage();
                    pdf_sheet_creatorA5_2(endtSINO, endwith, doc);
                }
            }
            // bill finishing

            invoiceNumber.clear();
            customerNameEditText.clear();
            customerId.clear();
            grantBilling.clear();
            d_rAmount.clear();
            finalAmount.clear();
            ReturnAdded.getChildren().clear();
            remainAmt.clear();
            returnStockAmount.clear();
          //  payedAmt.clear();
            Old_balanceField.clear();
            NeedTo_payfield.clear();

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
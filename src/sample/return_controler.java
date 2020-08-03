package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static sample.Billing.df3;

/**
 * Created by kings on 28-11-17.
 */
public class return_controler implements Initializable {

    public Pane return_view;
    @FXML
    private TextField invoice_no1;

    @FXML
    private DatePicker r_date;

    @FXML
    private TextField quantity;

    @FXML
    private Pane bill_view;

    public void view_bill_list(ActionEvent keyEvent) throws Exception {

        String invoice=invoice_no1.getText();

        String query="Select * from billing where invoice_no='"+invoice+"'";
        LoadingTableViewDataSelectedRowName.Welcome(query, bill_view,250,800);

        String query1="Select * from return_stock where invoice_no='"+invoice+"'";
        LoadingDataToTableView.Welcome(query1, return_view,250,800);

    }

    public void return_stock(ActionEvent event) throws Exception {
        Connection con = null;
        con=DBConnect.getConnection();
        if(invoice_no1.getText().trim().isEmpty())
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("PLEASE ENTER INVOICE NO");
            a.showAndWait();
        }else {

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

            bb.add(log1[0]);

            try {
                bb.add(log1[1]);
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);
                AlertMessage.error("PLEASE SELECT THE ITEM TO RETURN");
            }

            if (bb.get(0) == null) {
                AlertMessage.error("PLEASE SELECT THE ITEM TO RETURN");
            } else if (invoice_no1.getText().isEmpty()) {
                AlertMessage.error("ENTER INVOICE NUMBER");
            } else if (quantity.getText().isEmpty()) {
                AlertMessage.error("ENTER QUANTITY VALUE");
            }
            else {
                String m_bill_id = (bb.get(0).trim());
                String old_quantity = "", batch_no = "", expDate = "", p_name = "", billed_quantity = "", free_quantity = "", MRP = "";
                double total_q = 0, p_trade = 0.0;

                String pattern = "yyyy-MM-dd";
                r_date.setPromptText(pattern.toLowerCase());
                r_date.setConverter(new StringConverter<LocalDate>() {
                    DateTimeFormatter dateformater = DateTimeFormatter.ofPattern(pattern);

                    @Override
                    public String toString(LocalDate date) {
                        if (date != null) {
                            return dateformater.format(date);
                        } else {
                            return " ";
                        }
                    }

                    @Override
                    public LocalDate fromString(String string) {
                        if (string != null && !string.isEmpty()) {
                            return LocalDate.parse(string, dateformater);

                        } else {
                            return null;
                        }
                    }
                });


                double qty2=0,qty3=0;
                ResultSet r2 = con.createStatement().executeQuery("Select sum(q_return) from return_stock where bill_id='" + m_bill_id + "'");
                while (r2.next())
                {
                    qty2=r2.getDouble(1);
                }
                ResultSet r3 = con.createStatement().executeQuery("Select sum(quantity) from billing where bill_id='" + m_bill_id + "'");
                while (r3.next())
                {
                    qty3=r3.getDouble(1);
                }
                double r_sum=Double.parseDouble(quantity.getText().trim()) + qty2;

                P.p("qty2----"+qty2);
                P.p("qty3----"+qty3);
                P.p("r_sum----"+r_sum);

                if(r_sum<=qty3) {
                    try {

                        ResultSet r1 = con.createStatement().executeQuery("Select * from billing where bill_id='" + m_bill_id + "'");

                        String invoice_no = "", c_id = "", c_name = "", c_phno = "", product_name = "", product_code = "",
                                qty = "", hsn = "", mrp = "", p_price = "", t_price = "", d_per = "", cgst = "",
                                sgst = "", bill_status = "",bill_id="", PID = "";
                        double d_amt=0.0;
                        while (r1.next()) {

                            bill_id = r1.getString("bill_id");
                            invoice_no = r1.getString("invoice_no");
                            c_id = r1.getString("customer_id");
                            c_name = r1.getString("customer_name");
                            c_phno = r1.getString("customer_phno");
                            product_name = r1.getString("product_name");
                            product_code = r1.getString("product_code");
                            qty = r1.getString("quantity");
                            hsn = r1.getString("hsn");
                            mrp = r1.getString("mrp");
                            p_price = r1.getString("purchase_price");
                            t_price = r1.getString("trade_price");
                            d_per = r1.getString("discount_in_per");
                            cgst = r1.getString("cgst");
                            sgst = r1.getString("sgst");
                            bill_status = r1.getString("bill_status");
                            PID = r1.getString("product_id");
                            d_amt = r1.getDouble("deduction_amount");

                        }

                        ResultSet r = con.createStatement().executeQuery("Select * from stock where id='" + PID+ "'");
                        while (r.next()) {
                            old_quantity = r.getString("stockquantity");
                        }
                        double q_return = 0;

                        int billed_q_total = Integer.parseInt(qty);

                        if (!quantity.getText().trim().isEmpty()) {
                            q_return = Double.parseDouble(quantity.getText().trim());
                        }

                        double return_total_q = q_return;

                        if (return_total_q > billed_q_total) {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("ENTER VALID QUANTITY VALUE");
                            alert.showAndWait();

                        } else {
                            int j =0,i=0;
                            total_q = q_return + Double.parseDouble(old_quantity);

                            i = con.createStatement().executeUpdate("UPDATE stock SET stockquantity='" + total_q + "' where id='" + PID + "'");

                            //calculation
                            double new_qty = return_total_q;

                            double trade_price = new_qty * Double.parseDouble(t_price);

                            double dis_amount = trade_price * Double.parseDouble(d_per) / 100;

                            double tax_value = trade_price - dis_amount;

                            double c_gst = tax_value * Double.parseDouble(cgst) / 100;
                            double s_gst = tax_value * Double.parseDouble(sgst) / 100;
                            double t_gst = c_gst + s_gst;
                            double total = tax_value-d_amt ;

                             String query7 = "delete from billing where bill_id='" + bill_id + "'";
                            con.createStatement().executeUpdate(query7);

                            String query8 = "delete from billing2 where invoice_no='" + invoice_no1.getText() + "'";
                            con.createStatement().executeUpdate(query8);


                            //end of calculation

                            String query4 = "Insert into return_stock ( invoice_no, bill_id," +
                                    "return_date, product_code, product_name, q_return, mrp, trade_price, d_per, d_amount, taxable_value," +
                                    "cgst, cgst_amount, sgst, sgst_amount, net_total) values (?,?,?,?,?,  ?,?,?,?,?,   ?,?,?,?,?,   ?)";

                            PreparedStatement preparedStatement_1 = con.prepareStatement(query4);

                            preparedStatement_1.setString(1, invoice_no);
                            preparedStatement_1.setString(2, bill_id);
                            preparedStatement_1.setString(3, r_date.getValue().toString());
                            preparedStatement_1.setString(4, product_code);
                            preparedStatement_1.setString(5, product_name);
                            preparedStatement_1.setString(6, q_return+"" );
                            preparedStatement_1.setString(7, mrp);
                            preparedStatement_1.setString(8, trade_price+"");
                            preparedStatement_1.setString(9, d_per);
                            preparedStatement_1.setString(10, dis_amount+"");
                            preparedStatement_1.setString(11, tax_value+"");
                            preparedStatement_1.setString(12, cgst);
                            preparedStatement_1.setString(13, c_gst+"");
                            preparedStatement_1.setString(14, sgst);
                            preparedStatement_1.setString(15, s_gst+"");
                            preparedStatement_1.setString(16, total+"");

                            j = preparedStatement_1.executeUpdate();

                            //to update balance in customer table
                            int c=0;
                            if(bill_status.equalsIgnoreCase("credit"))
                            {

                            double b=0,e=0,c_sum=0;
                            ResultSet rs4=con.createStatement().executeQuery("select balance,extra from customer where " +
                                    "customerid='"+c_id+"' or phoneno='"+c_phno+"'");
                            while (rs4.next())
                            {
                            b=rs4.getDouble(1);
                            e=rs4.getDouble(2);
                            }
                            c_sum=b+e;

                            double balance=c_sum - total;
                            c=con.createStatement().executeUpdate(
                                    "update customer set " +
                                    " balance='"+balance+"' " +
                                    ", extra='"+0+"' " +
                                    "where " +
                                    "customerid='"+c_id+"' " +
                                    "and phoneno='"+ c_phno+"'");
                            }

                            if (j > 0) {
                                AlertMessage.inform("STOCK RETURNED SUCCESSFULY");
                                quantity.clear();

                            } else {
                                AlertMessage.error("ERROR IN RETURING STOCK");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        P.loggerLoader(e);
                    } finally {
                        con.close();
                    }
                }
                else
                {
                    AlertMessage.error("ENTER VALID QUANTITY");
                }
            }
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ZoneId zoneId = ZoneId.of("Indian/Maldives");
        LocalDate today = LocalDate.now(zoneId);
        r_date.setValue(today);
        r_date.setFocusTraversable(false);
    }

    public void view_Stock(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("view_return_stock.fxml"));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
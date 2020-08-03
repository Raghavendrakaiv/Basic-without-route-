package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Admin on 14-Jul-17.
 */
public class reg2 implements Initializable {

    public TextField purchase_invoice;
    public TextField dealerSearch;
    public DatePicker StockDate;
    public TextField ProductCode;
    public TextField StockName;
    public TextField Hsn;
    public TextField Quantity;
    public TextField MRP;
    public ComboBox totalgst;
    public TextField Purchaseprice;
    public TextField p_discount;
    public TextField s_price;
    public TextField stockingpriceto;
    public Pane view_pane;
    public TextField p_id;

    Connection connection=null;

    @FXML
    private Pane view_reg_pane1;

    ZoneId zoneId = ZoneId.of("Indian/Maldives");
    LocalDate today = LocalDate.now(zoneId);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            view_stock();
			gstload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gen_reg_rep1(ActionEvent event) throws Exception  {
        view_stock();
    }

    void view_stock() throws Exception
    {
        String query="select " +
                "id, p_invoice ,productcode, hsn, stockaddeddate, " +
                "productname, stockquantity,added_quantity, packing, purchaseprice, " +
                "mrp, cgst, sgst, dealer_name, purchase_discount," +
                "selling_price, total_amount from stock";
        LoadingTableViewDataSelectedRowName.Welcome(query, view_reg_pane1,380,900);
        view_pane.setVisible(false);
    }

    @FXML
    void printPurchasingReport(ActionEvent event) throws Exception
    {
        String query = "SELECT * FROM stock";

        File dir = new File(P.drive_name()+FilePath.FOLDER_PATH);
        dir.mkdir();
        File dir2=new File(P.drive_name()+FilePath.FOLDER_PATH+"STOCK_REPORT");
        dir2.mkdir();
        String path=P.drive_name()+FilePath.FOLDER_PATH+"/STOCK_REPORT/Stock_report.csv";

        Controller.export_excel(query,path);
    }

    public void edit_stock(ActionEvent event) throws Exception
    {

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
            view_pane.setVisible(true);
        }
        catch (Exception e)
        {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("PLEASE SELECT THE ITEM FROM TABLE");
            a.showAndWait();
        }
        try {
            int p_code = Integer.parseInt(bb.get(0));

            connection = DBConnect.getConnection();
            String query = "Select * from stock where id='"+p_code+"'";
            ResultSet rs = connection.createStatement().executeQuery(query);

            while (rs.next()) {

                double t_gst=0;

                String id=rs.getString("id");
                String productcodes = rs.getString("productcode");
                String HSN = rs.getString("hsn");
                String STOCKDATE = rs.getString("stockaddeddate");
                String productnames = rs.getString("productname");
                String stockquantitys = rs.getString("stockquantity");
                String PURCHASEPRICE = rs.getString("purchaseprice");
                String mrps = rs.getString("mrp");
                String DEALER = rs.getString("dealer_name");
                String TOTAL = rs.getString("total_amount");
                double cgst=rs.getDouble("cgst");
                double sgst=rs.getDouble("sgst");
                t_gst=cgst+sgst;

                String p_discount1 = rs.getString("purchase_discount");
                String s_price1 = rs.getString("selling_price");
                String p_invoice=rs.getString("p_invoice");

                ProductCode.setText(productcodes);
                Hsn.setText(HSN);
                StockDate.setValue(Controller.LOCAL_DATE(STOCKDATE));
                StockName.setText(productnames);
                Quantity.setText(stockquantitys);
                Purchaseprice.setText(PURCHASEPRICE);
                totalgst.setValue(t_gst);
                MRP.setText(mrps);
                dealerSearch.setText(DEALER);
                stockingpriceto.setText(TOTAL);
                p_discount.setText(p_discount1);
                s_price.setText(s_price1);
                purchase_invoice.setText(p_invoice);
                p_id.setText(id);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);

        }
        finally
        {
            try
            {
                if (connection != null)
                    connection.close();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void delete_stock(ActionEvent event) throws Exception {
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

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("ARE YOU SURE YOU WANT TO DELETE STOCK REPORT");
        Optional<ButtonType> result=alert.showAndWait();

        if((result.isPresent()) && (result.get()==ButtonType.OK)) {
            try {
                connection = DBConnect.getConnection();
                int p_code = Integer.parseInt(bb.get(0));

                String query = "delete from stock where id='" + p_code + "'";
                PreparedStatement ps = connection.prepareStatement(query);
                int i = ps.executeUpdate();

                if (i > 0)
                {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("STOCK INFORMATION DELETED SUCCESSFULLY");
                    a.showAndWait();

                    view_stock();
                }
                else
                {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("ERROR IN DELETING STOCK INFORMATION");
                    a.showAndWait();
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
    }

    @FXML
    void stockingPriceInTotal(KeyEvent event) throws Exception
    {
        double quantity=0, p_price=0;

        if(!Purchaseprice.getText().trim().isEmpty())
        {
            p_price=Double.parseDouble(Purchaseprice.getText());
        }
        if(!Quantity.getText().trim().isEmpty())
        {
            quantity=Double.parseDouble(Quantity.getText());
        }

        double total_amount =quantity * p_price;

        stockingpriceto.setText(total_amount+"");
    }

    @FXML
    void makeLiveSearchaForDealer(KeyEvent event) throws Exception {
        AutoCompletionBinding<String> autoCompletionBindings = TextFields.bindAutoCompletion(dealerSearch, LiveSearchMe.makeSearch("select DISTINCT(dealer_name) from dealer", "dealer_name"));

    }

    @FXML
    void MakeStockLiveSearch(KeyEvent event) throws Exception {
        AutoCompletionBinding<String> autoCompletionBindings = TextFields.bindAutoCompletion(StockName, LiveSearchMe.makeSearch("select DISTINCT(productname) from stock", "productname"));

    }
    void gstload() throws SQLException, ClassNotFoundException {

        try {
            connection = DBConnect.getConnection();
            totalgst.getItems().removeAll(totalgst.getItems());
            String query1="select sum(amount) from gst group by amount order by amount ASC";
            ArrayList<Double> gstList=new ArrayList<Double>();
            ResultSet set=connection.createStatement().executeQuery(query1);
            while ( set.next())
            {
                gstList.add(set.getDouble(1));
            }

            for( int a=0;  a<gstList.size(); a++)
            {
                double a1=gstList.get(a);
                for(int b=a+1; b<gstList.size(); b++)
                {
                    double b1=gstList.get(b);
                    if(a1>b1)
                    {
                        double temp=a1;
                        a1=b1;
                        b1=temp;
                        gstList.set(a,a1);
                        gstList.set(b,b1);
                    }
                }
            }

            for ( int i=0;i<gstList.size(); i++ )
            {
                totalgst.getItems().add(gstList.get(i));
            }
            if(gstList.size()>0)
            {
                totalgst.setValue(gstList.get(0));
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void save_stock_info(ActionEvent event) throws Exception {

        if(StockName.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("ENTER STOCK NAME");
            alert.showAndWait();
        }
        else {
            try {
                connection = DBConnect.getConnection();

                String selectingQueryForQuantity = "Select * from stock WHERE productname = '"+StockName.getText().trim()+"' and id='"+p_id.getText().trim()+"'";
                double oldQuantityTaken = 0,a_quantity=0;

                ResultSet selectingQueryForQuantityRs = connection.createStatement().executeQuery(selectingQueryForQuantity);

                while (selectingQueryForQuantityRs.next()) {
                    oldQuantityTaken = Double.parseDouble(selectingQueryForQuantityRs.getString("stockquantity"));
                    a_quantity=Double.parseDouble(selectingQueryForQuantityRs.getString("added_quantity"));
                }

                double newQuantity = oldQuantityTaken + Double.parseDouble(Quantity.getText().trim());
                double newquantity2=a_quantity+Double.parseDouble(Quantity.getText().trim());

                double gst=Double.parseDouble(totalgst.getValue().toString())/2;

                PreparedStatement ps = connection.prepareStatement("update stock set " +
                        "productcode='" + ProductCode.getText().trim() + "',hsn='" + Hsn.getText().trim() + "'," +
                        "stockaddeddate='" + StockDate.getValue() + "',productname='"+StockName.getText().trim()+"'," +
                        "stockquantity='" + newQuantity + "',added_quantity='"+newquantity2+"'," +
                        "purchaseprice='" + Purchaseprice.getText().trim() + "',mrp='" + MRP.getText().trim()+ "'," +
                        "cgst='"+gst+"',sgst='"+gst+"'," +
                        "total_amount='" + stockingpriceto.getText() + "'," +
                        "dealer_name='" + dealerSearch.getText().trim() +
                        "',purchase_discount='"+p_discount.getText().trim()+"'," +
                        "selling_price='"+s_price.getText().trim()+"',p_invoice='"+purchase_invoice.getText().trim()+"'" +
                        " where productname = '" + StockName.getText().trim() + "' and id='"+p_id.getText().trim()+"'");

                int i = ps.executeUpdate();

                if (i > 0)
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("STOCK INFORMATION UPDATED");
                    alert.showAndWait();

                    view_stock();
                    view_pane.setVisible(false);

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN UPDATION");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);

            } finally {
                try {
                    if (connection != null)
                        connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

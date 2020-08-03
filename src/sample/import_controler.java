package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class import_controler implements Initializable {


    public TextField productName;
    public TextField productCode;
    public TextField avStock;
    public Pane productListPane;
    public TextField productId;
    public TextField mrp;
    public TextField availableStock;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void loadProductListByNameAndCode(KeyEvent event) throws Exception {
        int stockAQ = 0;
        String condtion = "";
        try{ stockAQ = (int)Double.parseDouble(avStock.getText().trim()); }catch (Exception e){}
        if(     (stockAQ < 0) &&   (productName.getText().trim().isEmpty())  &&  (productCode.getText().trim().isEmpty())  ){

        }else{
            if(stockAQ >=0){
                condtion += " and stockquantity>="+stockAQ+" ";
            }
            if(!productName.getText().trim().isEmpty()){
                condtion += " and productname like '%"+productName.getText().trim()+"%' ";
            }
            if(!productCode.getText().trim().isEmpty()){
                condtion += " and productcode like '%"+productCode.getText().trim()+"%' ";
            }

            String query = "select id, productcode, productname, stockquantity, selling_price, packing as 'size' from stock where id>0 "+condtion+" limit 100 ";
            LoadingTableView.load(query, productListPane, 200,700);
        }
    }


    public void loadDatToFields(ActionEvent event) throws Exception {
        Connection connection = null;
        try {
            int idd = 0;
            if (LoadingTableView.selectItemId().trim().equalsIgnoreCase("NODATA")) {
                AlertMessage.error("PLEASE SELECT THE DATA FROM TABLE");
            } else {
                idd = Integer.parseInt(LoadingTableView.selectItemId().trim());
                String query = "select id, productcode, productname, stockquantity, selling_price, packing as 'size' from stock where id=" + idd;
                connection = DBConnect.connect();
                ResultSet set = connection.createStatement().executeQuery(query);
                if (set.next()) {
                    productId.setText(String.valueOf(idd));
                    mrp.setText(P.df00(set.getString("selling_price")));
                    availableStock.setText(P.df00(set.getString("stockquantity")));
                } else {

                }
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            if(!connection.isClosed()) { connection.close();}
        }
    }

    public void saveData(ActionEvent event) throws Exception {
        Connection connection = null;
        try {
            connection = DBConnect.connect();
            int idd = 0, qqq = 0;
            double rate =0;
            try{qqq =(int) Double.parseDouble(availableStock.getText().trim()); }catch ( Exception e ){ e.printStackTrace(); qqq = -1; }
            try{rate = Double.parseDouble(mrp.getText().trim()); }catch ( Exception e ){ rate = -1; e.printStackTrace(); }
            if (productId.getText().trim().isEmpty()) {
                AlertMessage.error("PLEASE SELECT THE DATA FROM TABLE AND CLICK ON VIEW");
        } else if(qqq<0){
                AlertMessage.error("PLEASE VALID QUANTITY");
            }else if(rate<0){
                AlertMessage.error("PLEASE VALID RATE");
            }else {
                idd = Integer.parseInt(productId.getText().trim());
                String query = "UPDATE stock set stockquantity ='"+qqq+"' , selling_price ='"+P.df00(rate)+"'   where id=" + idd;
                boolean a = connection.createStatement().execute(query);
                if (!a) {
                    productId.clear();
                    mrp.clear();
                    availableStock.clear();
                    AlertMessage.inform("DATA UPDATED");
                    query = "select id, productcode, productname, stockquantity, selling_price, packing as 'size' from stock where id>0 limit 100";
                    LoadingTableView.load(query, productListPane, 200,700);
                } else {
                    AlertMessage.error("ERROR WHILE DATA UPDATE");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            if(!connection.isClosed()) { connection.close();}
        }
    }
}
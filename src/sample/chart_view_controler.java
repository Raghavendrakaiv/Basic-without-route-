package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class chart_view_controler implements Initializable {

    public ComboBox chart_type;

    @FXML
    private BarChart<String,Double> bar_chart;

    @FXML
    private NumberAxis number_axis;

    @FXML
    private CategoryAxis catagory;

    @FXML
    private Label title_label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        chart_type.getItems().removeAll(chart_type.getItems());
        chart_type.getItems().addAll("SELECT CHART TYPE" , "YEAR WISE PURCHASE", "YEAR WISE SALES" , "MONTH WISE PURCHASE" , "MONTH WISE SALES",
        "DATE WISE PURCHASE", "DATE WISE SALES", "PRODUCT WISE PURCHASE" , "PRODUCT WISE SALES");
        chart_type.getSelectionModel().select("SELECT CHART TYPE");
    }

    void clear_all_fields()
    {
        year_purchase.setVisible(false);
        year_sales.setVisible(false);
        month_purchase.setVisible(false);
        month_sales.setVisible(false);
        product_purchase.setVisible(false);
        product_sales.setVisible(false);
        date_purchase.setVisible(false);
        date_sales.setVisible(false);
        bar_chart.getData().clear();

        catagory.setLabel("");
        number_axis.setLabel("");
        title_label.setText(" ");
    }

    public void select_chart_type(ActionEvent actionEvent)
    {
        if (chart_type.getValue().equals("SELECT CHART TYPE")) {

            clear_all_fields();

        } else if (chart_type.getValue().equals("YEAR WISE PURCHASE")) {

            clear_all_fields();
            year_purchase.setVisible(true);
            title_label.setText("YEAR WISE PURCHASE");

        } else if (chart_type.getValue().equals("YEAR WISE SALES")) {

            clear_all_fields();
            year_sales.setVisible(true);
            title_label.setText("YEAR WISE SALES");

        } else if (chart_type.getValue().equals("MONTH WISE PURCHASE")) {

            clear_all_fields();
            month_purchase.setVisible(true);
            title_label.setText("MONTH WISE PURECHASE");
        }
        else if (chart_type.getValue().equals("MONTH WISE SALES")) {

            clear_all_fields();
            month_sales.setVisible(true);
            title_label.setText("MONTH WISE SALES");
        }
        else if(chart_type.getValue().equals("DATE WISE PURCHASE"))
        {
            clear_all_fields();
            date_purchase.setVisible(true);
            title_label.setText("DATE WISE PURCHASE");
        }
        else if(chart_type.getValue().equals("DATE WISE SALES"))
        {
            clear_all_fields();
            date_sales.setVisible(true);
            title_label.setText("DATE WISE SALES");
        }
        else if(chart_type.getValue().equals("PRODUCT WISE PURCHASE"))
        {
            clear_all_fields();
            product_purchase.setVisible(true);
            title_label.setText("PRODUCT NAME WISE PURCHASE");
        }
        else if(chart_type.getValue().equals("PRODUCT WISE SALES"))
        {
            clear_all_fields();
            product_sales.setVisible(true);
            title_label.setText("PRODUCT NAME WISE SALES");
        }
    }

    //year wise purchase-------------------------------------------------
    @FXML
    private Pane year_purchase;

    @FXML
    private TextField year;

    //bar chart object
    XYChart.Series<String, Double> series1;

    public void purchase_year(ActionEvent event) throws Exception {

        int p_year=Integer.parseInt(year.getText());

        bar_chart.setAnimated(false);

        if(year.getText().trim().isEmpty() || year.getText().trim().length()!=4)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE ENTER YEAR IN YYYY FORMAT");
            aa.showAndWait();
        }
        else
        {
            Connection con = null;

            try {

                series1 = new XYChart.Series<>();

                con = DBConnect.getConnection();

                String query = "select SUBSTRING(MONTHNAME(stockaddeddate),1,3), sum(added_quantity) from stock where YEAR(stockaddeddate)='"+p_year+"' group by MONTH(stockaddeddate) order by MONTH(stockaddeddate) ASC";

                ResultSet rs1 = con.createStatement().executeQuery(query);

                catagory.setLabel("MONTH");
                number_axis.setLabel("QUANTITY");

                while (rs1.next()) {

                    series1.getData().add(new XYChart.Data<String, Double>(rs1.getString(1), rs1.getDouble(2)));

                }

                series1.setName(year.getText());

                bar_chart.getData().add(series1);

            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);
            } finally {
                con.close();
            }
        }
    }
//end of year wise purchase-----------------------------------------------------------------------

    //year wise sales-------------------------------------------------
    @FXML
    private Pane year_sales;

    @FXML
    private TextField s_year;

    public void sales_year(ActionEvent event) throws Exception {

        int sa_year=Integer.parseInt(s_year.getText());

        bar_chart.setAnimated(false);

        if(s_year.getText().trim().isEmpty() || s_year.getText().trim().length()!=4)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE ENTER YEAR IN YYYY FORMAT");
            aa.showAndWait();
        }
        else
        {
            Connection con = null;

            try {

                XYChart.Series<String, Double> series1 = new XYChart.Series<>();
                series1.getData().clear();
                con = DBConnect.getConnection();

                String query = "select SUBSTRING(MONTHNAME(bill_date),1,3), sum(net_total) from billing where YEAR(bill_date)='"+sa_year+"' group by MONTH(bill_date) order by MONTH(bill_date) ASC";

                ResultSet rs1 = con.createStatement().executeQuery(query);

                catagory.setLabel("MONTH");
                number_axis.setLabel("NET TOTAL");

                while (rs1.next()) {

                    series1.getData().add(new XYChart.Data<String, Double>(rs1.getString(1), rs1.getDouble(2)));
                }

                series1.setName(s_year.getText());

                bar_chart.getData().add(series1);

            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);
            } finally {
                con.close();
            }
        }
    }

//end of year wise sales-----------------------------------------------------------------------

    //month wise sales-------------------------------------------------
    @FXML
    private Pane month_purchase;

    @FXML
    private TextField p_month;

    @FXML
    private TextField p_year;

    public void purchase_month(ActionEvent event) throws Exception {

        String m_month=p_month.getText();
        String m_year=p_year.getText();

        bar_chart.setAnimated(false);

        if(p_month.getText().trim().isEmpty() || p_month.getText().trim().length()!=2)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE ENTER MONTH IN MM FORMAT");
            aa.showAndWait();
        }
        else if(p_year.getText().trim().isEmpty() || p_year.getText().trim().length()!=4)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE ENTER YEAR IN YYYY FORMAT");
            aa.showAndWait();
        }
        else
        {
            Connection con = null;

            try {

                XYChart.Series<String, Double> series1 = new XYChart.Series<>();
                series1.getData().clear();

                con = DBConnect.getConnection();

                String query = "select distinct(productname),sum(added_quantity) from stock where MONTH(stockaddeddate)='"+m_month+"' and YEAR(stockaddeddate)='"+m_year+"'group by productname order by productname ASC";

                ResultSet rs1 = con.createStatement().executeQuery(query);

                catagory.setLabel("PRODUCT NAME");
                number_axis.setLabel("QUANTITY");

                while (rs1.next()) {

                    series1.getData().add(new XYChart.Data<String, Double>(rs1.getString(1), rs1.getDouble(2)));

                }

                series1.setName(p_month.getText()+"/"+p_year.getText());

                bar_chart.getData().add(series1);

            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);
            } finally {
                con.close();
            }
        }
    }


//end of year wise sales-----------------------------------------------------------------------

    //month wise sales-------------------------------------------------
    @FXML
    private Pane month_sales;

    @FXML
    private TextField s_month;

    @FXML
    private TextField ss_year;

    public void sales_month(ActionEvent event) throws Exception {

        String m_month=s_month.getText();
        String m_year=ss_year.getText();

        bar_chart.setAnimated(false);

        if(s_month.getText().trim().isEmpty() || s_month.getText().trim().length()!=2)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE ENTER MONTH IN MM FORMAT");
            aa.showAndWait();
        }
        else if(ss_year.getText().trim().isEmpty() || ss_year.getText().trim().length()!=4)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE ENTER YEAR IN YYYY FORMAT");
            aa.showAndWait();
        }
        else
        {

            Connection con = null;

            try {

                XYChart.Series<String, Double> series1 = new XYChart.Series<>();
                series1.getData().clear();

                con = DBConnect.getConnection();

                String query = "select distinct(product_name),sum(quantity) from billing where MONTH(bill_date)='"+m_month+"' and YEAR(bill_date)='"+m_year+"'group by product_name order by product_name ASC";

                ResultSet rs1 = con.createStatement().executeQuery(query);

                catagory.setLabel("PRODUCT NAME");
                number_axis.setLabel("QUANTITY");

                while (rs1.next()) {

                    series1.getData().add(new XYChart.Data<String, Double>(rs1.getString(1), rs1.getDouble(2)));

                }

                series1.setName(s_month.getText()+"/"+ss_year.getText());

                bar_chart.getData().add(series1);

            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);
            } finally {
                con.close();
            }
        }
    }

//end of year wise sales-----------------------------------------------------------------------

    //month wise sales-------------------------------------------------
    @FXML
    private Pane product_purchase;

    @FXML
    private TextField product_name;

    @FXML
    private TextField product_month;

    @FXML
    private TextField product_year;

    @FXML
    void product_live_search(KeyEvent event) throws Exception
    {
        AutoCompletionBinding<String> autoCompletionBindings = TextFields.bindAutoCompletion(product_name, LiveSearchMe.makeSearch("select DISTINCT(productname) from stock", "productname"));
    }

    public void product_wise_purchase(ActionEvent event) throws Exception
    {

        String p_name=product_name.getText();

        String p_month=product_month.getText();

        String p_year=product_year.getText();

        bar_chart.setAnimated(false);

        if(product_name.getText().trim().isEmpty())
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE ENTER PRODUCT NAME");
            aa.showAndWait();
        }
        else if(product_month.getText().trim().isEmpty() || product_month.getText().trim().length()!=2)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE ENTER MONTH IN MM FORMAT");
            aa.showAndWait();
        }
        else if(product_year.getText().trim().isEmpty() || product_year.getText().trim().length()!=4)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE ENTER YEAR IN YYYY FORMAT");
            aa.showAndWait();
        }
        else
        {
            Connection con = null;

            try {

                XYChart.Series<String, Double> series1 = new XYChart.Series<>();
                series1.getData().clear();

                con = DBConnect.getConnection();

                String query = "select distinct(productname),sum(added_quantity) from stock where productname='"+p_name+"' and MONTH(stockaddeddate)='"+p_month+"' AND YEAR(stockaddeddate)='"+p_year+"'group by productname order by productname ASC";

                ResultSet rs1 = con.createStatement().executeQuery(query);

                catagory.setLabel("PRODUCT NAME");
                number_axis.setLabel("QUANTITY");

                while (rs1.next()) {

                    series1.getData().add(new XYChart.Data<String, Double>(rs1.getString(1), rs1.getDouble(2)));

                }

                series1.setName(product_name.getText()+"/"+product_month.getText()+"-"+product_year.getText());

                bar_chart.getData().add(series1);

            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);
            } finally {
                con.close();
            }
        }
    }


//end of year wise sales-----------------------------------------------------------------------


    //product wise sales-------------------------------------------------
    @FXML
    private Pane product_sales;

    @FXML
    private TextField sold_product_name;

    @FXML
    private TextField sold_product_month;

    @FXML
    private TextField sold_product_year;

    @FXML
    void medicine_live_search(KeyEvent event) throws Exception {
        AutoCompletionBinding<String> autoCompletionBindings = TextFields.bindAutoCompletion(sold_product_name, LiveSearchMe.makeSearch("select DISTINCT(product_name) from billing", "product_name"));
    }

    public void product_wise_saless(ActionEvent event) throws Exception {


        String p_name=sold_product_name.getText();

        String p_month=sold_product_month.getText();

        String p_year=sold_product_year.getText();

        bar_chart.setAnimated(false);

        if(sold_product_name.getText().trim().isEmpty())
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE ENTER PRODUCT NAME");
            aa.showAndWait();
        }
        else if(sold_product_month.getText().trim().isEmpty() || sold_product_month.getText().trim().length()!=2)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE ENTER MONTH IN MM FORMAT");
            aa.showAndWait();
        }
        else if(sold_product_year.getText().trim().isEmpty() || sold_product_year.getText().trim().length()!=4)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE ENTER YEAR IN YYYY FORMAT");
            aa.showAndWait();
        }
        else
        {
            Connection con = null;

            try {

                XYChart.Series<String, Double> series1 = new XYChart.Series<>();
                series1.getData().clear();

                con = DBConnect.getConnection();

                String query = "select distinct(product_name),sum(quantity) from billing where product_name='"+p_name+"' and MONTH(bill_date)='"+p_month+"' and YEAR(bill_date)='"+p_year+"'group by product_name order by product_name ASC";

                ResultSet rs1 = con.createStatement().executeQuery(query);

                catagory.setLabel("PRODUCT NAME");
                number_axis.setLabel("QUANTITY");

                while (rs1.next()) {

                    series1.getData().add(new XYChart.Data<String, Double>(rs1.getString(1), rs1.getDouble(2)));

                }

                series1.setName(sold_product_name.getText()+"/"+sold_product_month.getText()+"-"+sold_product_year.getText());

                bar_chart.getData().add(series1);

            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);
            } finally {
                con.close();
            }
        }
    }


//end of product wise sales-----------------------------------------------------------------------


    //date wise purchase-------------------------------------------------
    @FXML
    private Pane date_purchase;

    @FXML
    private DatePicker start_date;

    @FXML
    private DatePicker end_date;

    public void date_purchase(ActionEvent event) throws Exception {

        bar_chart.setAnimated(false);

        if(start_date.getValue()==null)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE SELECT STARTING DATE");
            aa.showAndWait();
        }
        else if(end_date.getValue()==null)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE SELECT ENDING DATE");
            aa.showAndWait();
        }
        else
        {
            Connection con = null;

            try {

                XYChart.Series<String, Double> series1 = new XYChart.Series<>();
                series1.getData().clear();

                con = DBConnect.getConnection();

                String query = "select distinct(productname),sum(added_quantity) from stock where stockaddeddate>='"+start_date.getValue()+"' and stockaddeddate<='"+end_date.getValue()+"'group by productname order by productname asc";

                ResultSet rs1 = con.createStatement().executeQuery(query);

                catagory.setLabel("PRODUCT NAME");
                number_axis.setLabel("QUANTITY");

                while (rs1.next()) {

                    series1.getData().add(new XYChart.Data<String, Double>(rs1.getString(1), rs1.getDouble(2)));

                }
                String s= start_date.getValue().toString();
                String s1[]=s.split("-");
                String e=end_date.getValue().toString();
                String e1[]=e.split("-");
                series1.setName(s1[2]+"-"+s1[1]+"-"+s1[0]+"/"+e1[2]+"-"+e1[1]+"-"+e1[0]);

                bar_chart.getData().add(series1);

            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);
            } finally {
                con.close();
            }
        }
    }


//end of date wise purchase-----------------------------------------------------------------------

    //date wise purchase-------------------------------------------------
    @FXML
    private Pane date_sales;

    @FXML
    private DatePicker s_date;

    @FXML
    private DatePicker e_date;

    public void date_sales(ActionEvent event) throws Exception {

        bar_chart.setAnimated(false);

        if(s_date.getValue()==null)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE SELECT STARTING DATE");
            aa.showAndWait();
        }
        else if(e_date.getValue()==null)
        {
            Alert aa=new Alert(Alert.AlertType.INFORMATION);
            aa.setContentText("PLEASE SELECT ENDING DATE");
            aa.showAndWait();
        }
        else
        {
            Connection con = null;

            try {

                XYChart.Series<String, Double> series1 = new XYChart.Series<>();
                series1.getData().clear();

                con = DBConnect.getConnection();

                String query = "select distinct(product_name),sum(quantity) from billing where bill_date>='"+s_date.getValue()+"' and bill_date<='"+e_date.getValue()+"'group by product_name order by product_name asc";

                ResultSet rs1 = con.createStatement().executeQuery(query);

                catagory.setLabel("PRODUCT NAME");
                number_axis.setLabel("QUANTITY");

                while (rs1.next()) {

                    series1.getData().add(new XYChart.Data<String, Double>(rs1.getString(1), rs1.getDouble(2)));

                }
                String s= s_date.getValue().toString();
                String s1[]=s.split("-");
                String e=e_date.getValue().toString();
                String e1[]=e.split("-");
                series1.setName(s1[2]+"-"+s1[1]+"-"+s1[0]+"/"+e1[2]+"-"+e1[1]+"-"+e1[0]);

                series1.setName(s1[2]+"-"+s1[1]+"-"+s1[0]+"/"+e1[2]+"-"+e1[1]+"-"+e1[0]);

                bar_chart.getData().add(series1);

            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);
            } finally {
                con.close();

            }
        }
    }

//end of date wise purchase-----------------------------------------------------------------------
}
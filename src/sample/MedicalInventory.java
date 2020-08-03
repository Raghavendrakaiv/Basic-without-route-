package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.controlsfx.control.textfield.TextFields;
import sample.common.FormatNumberList;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static sample.LoadingDataToTableView.Welcome;


/**
 * Created by Mitra on 10/14/2017.
 */
public class MedicalInventory implements Initializable{

    public DatePicker from_date;
    public DatePicker to_date;
    public Pane ca_report_pane;
    Connection connection = null;
    public Pane displayOfTablePane;
    public Pane input_fieldsPpane;
    // Pane 2
    public TextField customerNumber;
    public ComboBox inventoryfilterList;
    public Pane gstreportdaywisepane;
    public DatePicker date7;
    public DatePicker date8;
    public Pane gstreportinvoicewisepane;
    public DatePicker date9;
    public DatePicker date10;
    public TextField sPatientId1;
    public ComboBox sbilltype;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearAllPane();

        inventoryfilterList.getItems().removeAll(inventoryfilterList.getItems());
        inventoryfilterList.getItems().addAll("SELECT REPORT TYPE",
                                                "PURCHASE TRACKER",
                                                "CURRENT STOCK TRACKER",
                                                "SALES TRACKER",
                                                "CA REPORT",
                                                "GST SALES DAY WISE REPORT" /*,
                                                "GST REPORT INVOICE WISE"*/
                                                );
        inventoryfilterList.getSelectionModel().select("SELECT REPORT TYPE");

        sbilltype.getItems().removeAll(sbilltype.getItems());
        sbilltype.getItems().addAll("BOTH",
                                                "CASH",
                                                "CREDIT",
                                                "CANCELLED");
        sbilltype.getSelectionModel().select("BOTH");
    }

    public void loadInventoryPane(ActionEvent actionEvent) {
        String string1=inventoryfilterList.getValue().toString();
        switch (string1){


            case "SELECT REPORT TYPE" :
                clearAllPane();
                displayOfTablePane.getChildren().clear();
                break;
            //1

            case "PURCHASE TRACKER" :
                clearAllPane();
                completePurchaseDetailsPane.setVisible(true);
                displayOfTablePane.getChildren().clear();
                break;
            //1
            case "CURRENT STOCK TRACKER" :
                clearAllPane();
                completeCurrentStockDetailsPane.setVisible(true);
                displayOfTablePane.getChildren().clear();
                break;
            //1



            case "SALES TRACKER" :
                clearAllPane();
                completeSalesDetailsPane.setVisible(true);
                displayOfTablePane.getChildren().clear();
                break;
            //1

            case "CA REPORT" :
                clearAllPane();
                ca_report_pane.setVisible(true);
                displayOfTablePane.getChildren().clear();
            break;


            case "GST SALES DAY WISE REPORT" :
                clearAllPane();
                gstreportdaywisepane.setVisible(true);
                displayOfTablePane.getChildren().clear();
                break;
            //9

            case "GST REPORT INVOICE WISE" :
                clearAllPane();
                gstreportinvoicewisepane.setVisible(true);
                displayOfTablePane.getChildren().clear();
                break;
        }
    }

    public void clearAllPane()
    {
        gstreportdaywisepane.setVisible(false);
        gstreportinvoicewisepane.setVisible(false);
        completePurchaseDetailsPane.setVisible(false);
        completeCurrentStockDetailsPane.setVisible(false);
        completeSalesDetailsPane.setVisible(false);
        ca_report_pane.setVisible(false);
    }

    boolean datefromtoCheck(LocalDate dateFrom, LocalDate dateto) {
        boolean ok=true;
        long diff= ChronoUnit.DAYS.between(dateFrom,dateto);
        P.p("Diff is "+diff);
        if(diff<0){
            return false;
        }else {
            return true;
        }
    }


    public void createExcelFile(String query, String path) throws Exception {
        Connection con = null;
        try {
            con = DBConnect.connect();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Items details");

            XSSFCellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFRow header = sheet.createRow(0);

            for(int i=0; i<rs.getMetaData().getColumnCount();i++)
            {
                String colname=rs.getMetaData().getColumnName(i+1);
                header.createCell(i).setCellValue(colname.toUpperCase());
                header.getCell(i).setCellStyle(style);
            }


            int index = 1;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(index);
                for (int j=0;j<rs.getMetaData().getColumnCount();j++)
                {
                    row.createCell(j).setCellValue(rs.getString(j+1));
                }

                index++;
            }


            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setContentText("Details Exported to excel sheet and stored in "+path);
            alert.showAndWait();

            ps.close();
            rs.close();
            con.close();
            //auto open
            Desktop dt = Desktop.getDesktop();
            dt.open(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadGstReportDayWiseExcel(ActionEvent actionEvent) throws Exception {
        if(date7.getEditor().getText().isEmpty() || date8.getEditor().getText().isEmpty())
        {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setContentText("PLEASE SELECT THE DATE");
            a.showAndWait();
        }else if(!datefromtoCheck(date7.getValue(),date8.getValue()))
        {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setContentText("PLEASE SELECT CORRECT DATE");
            a.showAndWait();
        }
        else
        {
            String query = "select * from medical_billing where mb_date >='" + date7.getValue() + "'and mb_date<='"+date8.getValue()+"'";
            gstreportDayWise(date7.getValue().toString(),date8.getValue().toString());
        }

    }



    public void loadGstReportInvoiceWiseExcel(ActionEvent actionEvent) throws Exception {
        if(date9.getEditor().getText().isEmpty() || date10.getEditor().getText().isEmpty())
        {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setContentText("PLEASE SELECT THE DATE");
            a.showAndWait();
        }else if(!datefromtoCheck(date9.getValue(),date10.getValue()))
        {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setContentText("PLEASE SELECT CORRECT DATE");
            a.showAndWait();
        }
        else
        {
            String query = "select * from medical_billing where mb_date >='" + date9.getValue() + "'and mb_date<='"+date10.getValue()+"'";
            gstreportInvoiceWise(date9.getValue().toString(),date10.getValue().toString());
        }
    }

    public static void gstreportInvoiceWise(String from, String to) throws Exception {

        Connection connection = null;
        try {


            connection = DBConnect.connect();

            String query3 = "select distinct(amount) from gst order by amount asc";
            ResultSet rs3 = connection.createStatement().executeQuery(query3);

            ArrayList<String> gst_amount = new ArrayList<String>();

            while (rs3.next()) {
                gst_amount.add(rs3.getString(1));
            }
            for ( int i=0 ; i< gst_amount.size(); i++)
            {
                double superer1= Double.parseDouble(gst_amount.get(i));
                for ( int j=i+1 ; j< gst_amount.size(); j++)
                {
                    double superer2= Double.parseDouble(gst_amount.get(j));
                    if(superer1>superer2)
                    {
                        double temp=superer1;
                        superer1=superer2;
                        superer2=temp;
                        gst_amount.set(i,superer1+"");
                        gst_amount.set(j,superer2+"");
                    }
                }
            }

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Sales_gst_report");

            XSSFCellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            ArrayList<String> headerList=new ArrayList<String>();
            headerList.add("Date");
            headerList.add("Customer Name");
            headerList.add("Customer no");
            headerList.add("invoice no");
            for(int i=0; i<gst_amount.size(); i++) {
                headerList.add("BASIC"+Double.parseDouble(gst_amount.get(i))*2);
                headerList.add("CSGT"+Double.parseDouble(gst_amount.get(i)));
                headerList.add("SGST"+Double.parseDouble(gst_amount.get(i)));
            }


            XSSFRow header = sheet.createRow(0);


            for(int i=0; i<headerList.size();i++)
            {
                String colname=headerList.get(i);
                header.createCell(i).setCellValue(colname.toUpperCase());
                header.getCell(i).setCellStyle(style);
            }


            String m_name = " ", bill_date = " ", gst_no = " ";



            String query = "Select distinct(invoice_no) from medical_billing where mb_date >='" + from + "' and mb_date <='" + to + "'";
            ResultSet rs = connection.createStatement().executeQuery(query);
            ArrayList<String> invoice_list = new ArrayList<String>();

            while (rs.next()) {
                invoice_list.add(rs.getString(1));
            }
            double gst_a=0,cgst_a=0,sgst_a=0;

            for (int i = 0; i < invoice_list.size(); i++)
            {
                XSSFRow row = sheet.createRow(i+1);
                P.p("invoice_list number  :  "+invoice_list.get(i));

                P.p(" working :  1");
                String query1 = "select pt_name,mb_date from medical_billing where invoice_no='" + invoice_list.get(i) + "'";
                ResultSet rs1 = connection.createStatement().executeQuery(query1);
                while (rs1.next()) {
                    P.p(" working :  2");
                    m_name = rs1.getString(1);
                    bill_date = rs1.getString(2);
                }
                int index=0;
                row.createCell(index++).setCellValue(bill_date);
                row.createCell(index++).setCellValue(m_name);
                String query2 = "select customer_number from medicalcustomer where customer_name='" + m_name + "'";
                ResultSet rs2 = connection.createStatement().executeQuery(query2);
                while (rs2.next()) {
                    P.p(" working :  3");
                    gst_no = rs2.getString(1);
                }

                row.createCell(index++).setCellValue(gst_no);
                row.createCell(index++).setCellValue(invoice_list.get(i));
                for(int j=0;j<gst_amount.size();j++)
                {
                    cgst_a=0;
                    sgst_a=0;
                    gst_a=0;
                    P.p(" working :  4 for CGST AND SGST = "+gst_amount.get(j) );

                    String query4="select sum(cgst_a),sum(sgst_a) from medical_billing where cgst='"+ gst_amount.get(j)+"' and sgst='"+gst_amount.get(j)+"' and invoice_no='"+invoice_list.get(i)+"'";
                    ResultSet rs4=connection.createStatement().executeQuery(query4);
                    while(rs4.next())
                    {
                        P.p(" working :  5");
                        cgst_a=rs4.getDouble(1);
                        sgst_a=rs4.getDouble(2);
                        gst_a=cgst_a+sgst_a;
                        P.p("");
                        P.p("basic = "+gst_a + " CSGT = "+cgst_a + "  SSGT = "+sgst_a);
                    }
                    row.createCell(index++).setCellValue(gst_a);
                    row.createCell(index++).setCellValue(cgst_a);
                    row.createCell(index++).setCellValue(sgst_a);
                }


            }

            P.p("gst_no"+gst_amount);
            P.p("invoice_list"+invoice_list);
            P.p("m_name"+m_name);
            P.p("bill_date"+bill_date);
            P.p("gst in no"+gst_no);
///////////////////////////////////////////////////////////////////////////////////////////////////////////
            XSSFSheet sheet1 = wb.createSheet("Purchase_gst_report");

            XSSFCellStyle style1 = wb.createCellStyle();
            style1.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFRow header1 = sheet1.createRow(0);

            for(int i=0; i<headerList.size();i++)
            {
                String colname=headerList.get(i);
                header1.createCell(i).setCellValue(colname.toUpperCase());
                header1.getCell(i).setCellStyle(style);
            }


            String m_name1 = " ", bill_date1 = " ", gst_no1 = " ";


            String query11 = "Select distinct(p_invoice_no) from medicalstock where purchase_date >='" + from + "' and purchase_date <='" + to + "'";
            ResultSet rs11 = connection.createStatement().executeQuery(query11);
            ArrayList<String> invoice_list11 = new ArrayList<String>();

            while (rs11.next()) {
                invoice_list11.add(rs11.getString(1));
            }
            double gst_a11=0,cgst_a11=0,sgst_a11=0;

            for (int i = 0; i < invoice_list11.size(); i++)
            {
                XSSFRow row = sheet1.createRow(i+1);
                P.p("invoice_list number  :  "+invoice_list11.get(i));

                P.p(" working :  1");
                String query1 = "select dealer, purchase_date from medicalstock where p_invoice_no='" + invoice_list11.get(i) + "'";
                ResultSet rs1 = connection.createStatement().executeQuery(query1);
                while (rs1.next()) {
                    P.p(" working :  2");
                    m_name = rs1.getString(1);
                    bill_date = rs1.getString(2);
                }
                int index=0;
                row.createCell(index++).setCellValue(bill_date);
                row.createCell(index++).setCellValue(m_name);
                String query2 = "select dealer_gstn_number  from dealer where dealer_name='" + m_name + "'";
                ResultSet rs2 = connection.createStatement().executeQuery(query2);
                while (rs2.next()) {
                    P.p(" working :  3");
                    gst_no = rs2.getString(1);
                }

                row.createCell(index++).setCellValue(gst_no);
                row.createCell(index++).setCellValue(invoice_list11.get(i));
                for(int j=0;j<gst_amount.size();j++)
                {
                    cgst_a=0;
                    sgst_a=0;
                    gst_a=0;
                    P.p(" working :  4 for CGST AND SGST = "+gst_amount.get(j) );

                    String query4="select ptr_sheet, qb_sheets, qf_sheets, discount_p from medicalstock where cgst='"+ gst_amount.get(j)+
                            "' and sgst='"+gst_amount.get(j)+"' and p_invoice_no='"+invoice_list11.get(i)+"'";
                    ResultSet rs4=connection.createStatement().executeQuery(query4);
                    while(rs4.next())
                    {
                        P.p(" working :  5");
                        double gstp= Double.parseDouble(gst_amount.get(j))*2;
                        P.p(" gst : "+gstp);
                        double recieve_Price=rs4.getDouble(1);
                        P.p( "RP   :   "+recieve_Price);
                        double q_bill=rs4.getDouble(2);
                        double q_free=rs4.getDouble(3);
                        double dicount=rs4.getDouble(4);
//                        double tradePrice=(recieve_Price*10000)/((100-dicount)*(100+gstp));
                        double tradePrice=recieve_Price;
                        double discountAmount=(tradePrice*dicount/100);
                        double taxableValue=tradePrice-discountAmount;

                        gst_a=gst_a+((((taxableValue)*gstp)/100)*q_bill)+((((tradePrice)*gstp)/100)*q_free);
                        cgst_a=gst_a/2;
                        sgst_a=gst_a/2;

                        P.p("rec price : "+recieve_Price);
                        P.p(" tradeprice  : "+tradePrice);
                        P.p(" qty  : "+ q_bill+q_free);
                        P.p("discount Amount : "+discountAmount);
                        P.p("taxable value : "+taxableValue);
                        P.p("gst Amount  : "+gst_a);
                        P.p("cgst Amount  : "+cgst_a);
                        P.p("sgst Amount  : "+sgst_a);

                    }
                    P.p("");
                    P.p("basic = "+gst_a + " CSGT = "+cgst_a + "  SSGT = "+sgst_a);
                    row.createCell(index++).setCellValue(gst_a);
                    row.createCell(index++).setCellValue(cgst_a);
                    row.createCell(index++).setCellValue(sgst_a);
                }


            }


            ///////////////////////////////////////////////////////////////////////////////////////////////////////////

            String file=(P.drive_name()+FilePath.GSTREPORT_INVOICEWISE_PATH+"GST_REPORT_"+from+"-"+to+".csv");
            File file1=new File(P.drive_name()+FilePath.FOLDER_PATH);
            file1.mkdir();
            File file2=new File(P.drive_name()+FilePath.GSTREPORT_INVOICEWISE_PATH);
            file2.mkdir();
            FileOutputStream fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("GST REPORT OF INVOICE WISE EXCEL SHEET IS CREATE IN PATH : "+file);
            alert.showAndWait();

            Desktop dt = Desktop.getDesktop();
            dt.open(new File(file));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void gstreportDayWise(String from, String to) throws Exception {

        Connection connection = null;

        try {

            connection = DBConnect.connect();

            XSSFWorkbook wb = new XSSFWorkbook();


            XSSFCellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            ArrayList<String> headerList=new ArrayList<String>();
            headerList.add("SI NO.");
            headerList.add("GST %");
            headerList.add("BASIC AMOUNT");
            headerList.add("DISC AMOUNT");
            headerList.add("TAXABLE AMOUNT");
            headerList.add("TAX AMOUNT");
            headerList.add("AMOUNT");

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            HashMap<Double, Double> gstAmountTotal = new HashMap<Double, Double>();
            String query22 = "select distinct(cgst) from billing where bill_date >='"+from+"' and bill_date<='"+to+"' order by cgst asc";
            ResultSet rs22 = connection.createStatement().executeQuery(query22);
            while (rs22.next()) {
                gstAmountTotal.put(rs22.getDouble(1), Double.valueOf(0));
            }

            XSSFSheet sheet = wb.createSheet("Sales_gst_report");
            int Row=0;
            sheet.addMergedRegion(new CellRangeAddress(Row+1,Row+3,0,6));
            Row=Row+4;
            P.p("header column number : "+ Row);
            XSSFRow header = sheet.createRow(Row);
            for(int i=0; i<headerList.size();i++)
            {
                String colname=headerList.get(i);
                header.createCell(i).setCellValue(colname.toUpperCase());
                header.getCell(i).setCellStyle(style);
            }

            LocalDate date1 = Controller.LOCAL_DATE(from);
            LocalDate date2 = Controller.LOCAL_DATE(to);
            LocalDate date3 = date2.plusDays(1);
            P.p(" from 1 : "+date1);
            P.p(" from 2 : "+date2);
            P.p(" from 3 : "+date3);
            Row++;
            double DDtrade_price=0.0, DDdiscountAMount=0.0, DDtaxableValue=0.0, DDnettotal=0.0, DDgst_a=0;

            while(!date1.equals(date3))
            {

                String query3 = "select distinct(cgst) from billing where bill_date ='"+date1+"' order by cgst asc";
                ResultSet rs3 = connection.createStatement().executeQuery(query3);
                ArrayList<String> gst_amount = new ArrayList<String>();
                while (rs3.next()) {
                    gst_amount.add(rs3.getString(1));
                }

                String query5 = "select distinct(invoice_no) from billing where bill_date='"+date1+"' order by cgst asc";
                ResultSet rs5 = connection.createStatement().executeQuery(query5);
                ArrayList<String> invoiceNumberList = new ArrayList<String>();
                while (rs5.next()) {
                    invoiceNumberList.add(rs5.getString(1));
                }

                P.p("invoiceNumberList : "+invoiceNumberList);
                String invoiceNumbersString = FormatNumberList.format1(invoiceNumberList);
                P.p("after formated"+FormatNumberList.format1(invoiceNumberList));


                P.p("gst size = "+gst_amount.size());

                double Dtrade_price=0.0, DdiscountAMount=0.0, DtaxableValue=0.0, Dnettotal=0.0, Dgst_a=0;
                int i=0;
                boolean datafound=false;
                int count=1;
                for(int j=0;j<gst_amount.size();j++)
                {
                    if(count==1) {
                        Row++;
                        {
                            int index3 = 0;
                            XSSFRow row1 = sheet.createRow(Row);
                            P.p("data column number  :  " + Row);
                            sheet.addMergedRegion(new CellRangeAddress(Row, Row, 0, 6));
                            String OneDayHeader = "Date : " + date1 + "    Sales Invoice Nos : " + invoiceNumbersString;
                            row1.createCell(index3++).setCellValue(OneDayHeader);
                        }
                        count++;
                    }
                    int index=0;

                    double trade_price=0.0, discountAMount=0.0,taxableValue=0.0, nettotal=0.0, gst_a=0, cgst_a=0.0, sgst_a=0.0 ;

                    P.p(" working :  4 for CGST AND SGST = "+gst_amount.get(j) );

                    String query4="select cgst_amount,sgst_amount, quantity, trade_price, discount_in_per, taxable_value, net_total, discount_amount  from billing " +
                            "where cgst="+ gst_amount.get(j)+" and bill_date='"+date1+"'";
                    ResultSet rs4=connection.createStatement().executeQuery(query4);

                    while(rs4.next())
                    {
                        datafound=true;
                        P.p(" working :  5");
                        cgst_a=rs4.getDouble(1);
                        sgst_a=rs4.getDouble(2);
                        gst_a=gst_a+(cgst_a+sgst_a);
                        trade_price=trade_price+(rs4.getDouble(3)*rs4.getDouble(4));
                        discountAMount=discountAMount+((rs4.getDouble(8)));
                        taxableValue=taxableValue+rs4.getDouble(6);
                        nettotal=nettotal+rs4.getDouble(7);
                    }
                    if(gstAmountTotal.containsKey(Double.parseDouble(gst_amount.get(j)))) {
                        gstAmountTotal.put(
                                Double.parseDouble(gst_amount.get(j)),
                                gstAmountTotal.get(Double.parseDouble(gst_amount.get(j)))+gst_a
                        );
                    }
                    if(datafound){
                        ++Row;
                        P.p("header data column number  :  "+Row);
                        P.p("SINO. : "+(++i) );
                        P.p("basic = "+ gst_a + " CSGT = "+cgst_a + "  SSGT = "+sgst_a);
                        P.p("tradePrice Sum : "+trade_price );
                        P.p("DISCOUNT Sum : "+discountAMount);
                        P.p("TAXABLE VALUE Sum : "+taxableValue);
                        P.p("TAX Sum : "+gst_a);
                        P.p("NET TOTAL Sum : "+nettotal);

                        double bbbb=Double.parseDouble(gst_amount.get(j))*2;
                        XSSFRow row = sheet.createRow(Row);
                        P.p("data column number  :  "+Row);
                        row.createCell(index++).setCellValue(i+"");
                        row.createCell(index++).setCellValue(bbbb);
                        row.createCell(index++).setCellValue(trade_price);
                        row.createCell(index++).setCellValue(discountAMount);
                        row.createCell(index++).setCellValue(taxableValue);
                        row.createCell(index++).setCellValue(gst_a);
                        row.createCell(index++).setCellValue(nettotal);

                        Dtrade_price=Dtrade_price+trade_price;
                        DdiscountAMount=DdiscountAMount+discountAMount;
                        DtaxableValue=DtaxableValue+taxableValue;
                        Dgst_a=Dgst_a+gst_a;
                        Dnettotal=Dnettotal+nettotal;
                    }
                }
                if(datafound) {
                    sheet.addMergedRegion(new CellRangeAddress(Row + 1, Row + 1, 0, 1));
                    XSSFRow row = sheet.createRow(++Row);
                    P.p("footer data column number  :  "+Row);
                    int index2 = 0;
                    row.createCell(index2++).setCellValue("SUB TOTAL");
                    row.createCell(index2++).setCellValue("");
                    row.createCell(index2++).setCellValue(Dtrade_price);
                    row.createCell(index2++).setCellValue(DdiscountAMount);
                    row.createCell(index2++).setCellValue(DtaxableValue);
                    row.createCell(index2++).setCellValue(Dgst_a);
                    row.createCell(index2++).setCellValue(Dnettotal);

                    DDdiscountAMount=DDdiscountAMount+DdiscountAMount;
                    DDtrade_price=DDtrade_price+Dtrade_price;
                    DDtaxableValue=DDtaxableValue+DtaxableValue;
                    DDgst_a=DDgst_a+Dgst_a;

                    DDnettotal=DDnettotal+Dnettotal;

                    index2 = 0;
                    XSSFRow row1 = sheet.createRow(++Row);
                    for (int ii = 0; ii < 9; ii++)
                        row1.createCell(index2++).setCellValue(" ");

                }
                P.p("date incremented to : "+ date1);
                date1 = date1.plusDays(1);

            }

            XSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
            cellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
            cellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
            cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
            XSSFCell aa = null;
            P.p( "gstAmountTotal : "+gstAmountTotal );
            Set<Double> keySet = gstAmountTotal.keySet();
            Iterator<Double> iterator = keySet.iterator();
            while(iterator.hasNext()){

                double gstRate = iterator.next();
                double gstAmmt = gstAmountTotal.get(gstRate);
                sheet.addMergedRegion(new CellRangeAddress(Row + 1, Row + 1, 0, 1));
                XSSFRow row = sheet.createRow(++Row);
                P.p("footer data column number  :  "+Row);
                int index2 = 0;

                aa = row.createCell(index2);
                aa.setCellStyle(cellStyle);
                aa.setCellValue("TOTAL");
                aa = row.createCell(index2+1);
                aa.setCellStyle(cellStyle);
                aa=row.createCell(index2+2);
                aa.setCellStyle(cellStyle);
                aa.setCellValue((gstRate*2)+" = ");
                aa=row.createCell(index2+3);
                aa.setCellStyle(cellStyle);
                aa.setCellValue(gstAmmt);
            }

            sheet.addMergedRegion(new CellRangeAddress(Row + 1, Row + 1, 0, 1));
            XSSFRow row = sheet.createRow(++Row);
            P.p("footer data column number  :  "+Row);
            int index2 = 0;

            aa=row.createCell(index2++);
            aa.setCellStyle(cellStyle);
            aa.setCellValue("FINAL TOTAL");

            aa=row.createCell(index2);
            aa.setCellStyle(cellStyle);
            aa=row.createCell(index2+1);
            aa.setCellStyle(cellStyle);
//            row.createCell(index2++).setCellValue("");
//            row.createCell(index2++).setCellValue(DDtrade_price);
//            row.createCell(index2++).setCellValue(DDdiscountAMount);
//            row.createCell(index2++).setCellValue(DDtaxableValue);
            aa=row.createCell(index2+2);
            aa.setCellStyle(cellStyle);
            aa.setCellValue(DDgst_a);
//            row.createCell(index2++).setCellValue(DDnettotal);

            index2 = 0;
            XSSFRow row1 = sheet.createRow(++Row);
            for (int ii = 0; ii < 7; ii++)
                row1.createCell(index2++).setCellValue(" ");

            ////////////////////////////////////////////////////////////////////////////////
            iterator = keySet.iterator();
            while (iterator.hasNext()){
                gstAmountTotal.put(iterator.next(), Double.valueOf(0));
            }
            P.p("Empty gstAmountTotal : "+gstAmountTotal);

            XSSFSheet sheet2 = wb.createSheet("Purchase_gst_report");
            int Roww=0;
            sheet2.addMergedRegion(new CellRangeAddress(Roww+1,Roww+3,0,6));
            Roww=Roww+4;
            P.p("header column number : "+ Roww);
            XSSFRow header1 = sheet2.createRow(Roww);
            for(int i=0; i<headerList.size();i++)
            {
                String colname=headerList.get(i);
                header1.createCell(i).setCellValue(colname.toUpperCase());
                header1.getCell(i).setCellStyle(style);
            }

            LocalDate date4 = Controller.LOCAL_DATE(from);
            LocalDate date5 = Controller.LOCAL_DATE(to);
            LocalDate date6 = date5.plusDays(1);
            P.p(" from 1 : "+date4);
            P.p(" from 2 : "+date5);
            P.p(" from 3 : "+date6);
            Row++;
            DDtrade_price=0.0;
            DDdiscountAMount=0.0;
            DDtaxableValue=0.0;
            DDnettotal=0.0;
            DDgst_a=0;
            while(!date4.equals(date6))
            {

                String query3 = "select distinct(cgst) from stock where stockaddeddate='"+date4+"' order by cgst asc";
                ResultSet rs3 = connection.createStatement().executeQuery(query3);
                ArrayList<Double> gst_amount = new ArrayList<Double>();
                while (rs3.next()) {
                    gst_amount.add(rs3.getDouble(1));
                }

                String query5 = "select distinct(p_invoice) from stock where stockaddeddate='"+date4+"' order by cgst asc";
                ResultSet rs5 = connection.createStatement().executeQuery(query5);
                ArrayList<String> invoiceNumberList = new ArrayList<String>();
                while (rs5.next()) {
                    invoiceNumberList.add(rs5.getString(1));
                }



                P.p("gst size = "+gst_amount.size());

                double Dtrade_price=0.0, DdiscountAMount=0.0, DtaxableValue=0.0, Dnettotal=0.0, Dgst_a=0;
                int i=0;
                boolean datafound=false;
                int count=1;
                for(int j=0;j<gst_amount.size();j++)
                {
                    if(count==1) {
                        Roww++;
                        {
                            int index3 = 0;
                            XSSFRow row2 = sheet2.createRow(Roww);
                            P.p("data column number  :  " + Roww);
                            sheet2.addMergedRegion(new CellRangeAddress(Roww, Roww, 0, 6));
                            String OneDayHeader = "Date : " + date4 + "    SAL-Sales Bill GST : " + invoiceNumberList;
                            row2.createCell(index3++).setCellValue(OneDayHeader);
                        }
                        count++;
                    }
                    int index=0;

                    double trade_price=0.0, discountAMount=0.0,taxableValue=0.0, nettotal=0.0, gst_a=0, cgst_a=0.0, sgst_a=0.0 ;

                    P.p(" working :  4 for CGST AND SGST = "+gst_amount.get(j) );

                    String query4="select stockquantity, purchaseprice, purchase_discount from stock" +
                            " where cgst="+ gst_amount.get(j)+" and stockaddeddate='"+date4+"'";
                    ResultSet rs4=connection.createStatement().executeQuery(query4);

                    while(rs4.next())
                    {
                        datafound=true;
                        double gst= (gst_amount.get(j))*2;
                        double trd1=rs4.getDouble(1)*rs4.getDouble(2);
                        trade_price=trade_price+trd1;
                        double dis=(trd1*rs4.getDouble(3)/100);
                        discountAMount=discountAMount+dis;
                        taxableValue=taxableValue+(trd1-dis);
                        double gstAmountb=(trd1-dis)*gst/100;
                        P.p(" working :  5");
                        gst_a=gst_a+(gstAmountb);
                        nettotal=nettotal+((trd1-dis)+gstAmountb);
                    }
                    if(gstAmountTotal.containsKey((gst_amount.get(j)))) {
                        gstAmountTotal.put(
                                (gst_amount.get(j)),
                                gstAmountTotal.get(gst_amount.get(j))+gst_a
                        );
                    }
                    if(datafound){
                        ++Roww;
                        P.p("header data column number  :  "+Roww);
                        P.p("SINO. : "+(++i) );
                        P.p("basic = "+ gst_a + " CSGT = "+cgst_a + "  SSGT = "+sgst_a);
                        P.p("tradePrice Sum : "+trade_price );
                        P.p("DISCOUNT Sum : "+discountAMount);
                        P.p("TAXABLE VALUE Sum : "+taxableValue);
                        P.p("TAX Sum : "+gst_a);
                        P.p("NET TOTAL Sum : "+nettotal);

                        double bbbb=(gst_amount.get(j))*2;
                        XSSFRow row3 = sheet2.createRow(Roww);
                        P.p("data column number  :  "+Row);
                        row3.createCell(index++).setCellValue(i+"");
                        row3.createCell(index++).setCellValue(bbbb);
                        row3.createCell(index++).setCellValue(trade_price);
                        row3.createCell(index++).setCellValue(discountAMount);
                        row3.createCell(index++).setCellValue(taxableValue);
                        row3.createCell(index++).setCellValue(gst_a);
                        row3.createCell(index++).setCellValue(nettotal);

                        Dtrade_price=Dtrade_price+trade_price;
                        DdiscountAMount=DdiscountAMount+discountAMount;
                        DtaxableValue=DtaxableValue+taxableValue;
                        Dgst_a=Dgst_a+gst_a;
                        Dnettotal=Dnettotal+nettotal;
                    }
                }
                if(datafound) {
                    sheet2.addMergedRegion(new CellRangeAddress(Row + 1, Row + 1, 0, 1));
                    XSSFRow row4 = sheet2.createRow(++Roww);
                    P.p("footer data column number  :  "+Roww);
                    int index3 = 0;
                    row4.createCell(index3++).setCellValue("SUB TOTAL");
                    row4.createCell(index3++).setCellValue("");
                    row4.createCell(index3++).setCellValue(Dtrade_price);
                    row4.createCell(index3++).setCellValue(DdiscountAMount);
                    row4.createCell(index3++).setCellValue(DtaxableValue);
                    row4.createCell(index3++).setCellValue(Dgst_a);
                    row4.createCell(index3++).setCellValue(Dnettotal);

                    DDdiscountAMount=DDdiscountAMount+DdiscountAMount;
                    DDtrade_price=DDtrade_price+Dtrade_price;
                    DDtaxableValue=DDtaxableValue+DtaxableValue;
                    DDgst_a=DDgst_a+Dgst_a;
                  //  DDgst_a=DDgst_a+Dgst_a;
                    DDnettotal=DDnettotal+Dnettotal;

                    index2 = 0;
                    XSSFRow row5 = sheet2.createRow(++Roww);
                    for (int ii = 0; ii < 7; ii++)
                        row5.createCell(index2++).setCellValue(" ");

                }
                P.p("date incremented to : "+ date4);
                date4 = date4.plusDays(1);

            }

            P.p("Purchase gstAmountTotal : "+gstAmountTotal);
            iterator = keySet.iterator();
            while(iterator.hasNext()){
                double gstRate = iterator.next();
                double gstAmmt = gstAmountTotal.get(gstRate);
                sheet2.addMergedRegion(new CellRangeAddress(Roww + 1, Roww + 1, 0, 1));
                row = sheet2.createRow(++Roww);
                P.p("footer data column number  :  "+Roww);
                index2 = 0;
                aa = row.createCell(index2++);
                aa.setCellValue("TOTAL");
                aa.setCellStyle(cellStyle);
                aa = row.createCell(index2);
                aa.setCellStyle(cellStyle);
                aa = row.createCell(index2+1);
                aa.setCellValue((gstRate*2)+" = ");
                aa.setCellStyle(cellStyle);
                aa = row.createCell(index2+2);
                aa.setCellValue(gstAmmt);
                aa.setCellStyle(cellStyle);
            }

            sheet2.addMergedRegion(new CellRangeAddress(Roww + 1, Roww + 1, 0, 1));
            XSSFRow row6 = sheet2.createRow(++Roww);
            P.p("footer data column number  :  "+Roww);
            index2 = 0;
            aa = row6.createCell(index2++);
            aa.setCellValue("TOTAL");
            aa.setCellStyle(cellStyle);

            aa = row6.createCell(index2);
            aa.setCellStyle(cellStyle);
            aa = row6.createCell(index2+1);
            aa.setCellStyle(cellStyle);
//            row6.createCell(index2++).setCellValue("");
//            row6.createCell(index2++).setCellValue(DDtrade_price);
//            row6.createCell(index2++).setCellValue(DDdiscountAMount);
//            row6.createCell(index2++).setCellValue(DDtaxableValue);
            aa = row6.createCell(index2+2);
            aa.setCellStyle(cellStyle);
            aa.setCellValue(DDgst_a);
//            row6.createCell(index2++).setCellValue(DDnettotal);

            index2 = 0;
            XSSFRow row7 = sheet2.createRow(++Roww);
            for (int ii = 0; ii < 9; ii++)
                row7.createCell(index2++).setCellValue(" ");

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            String FilePath1= P.drive_name()+FilePath.FOLDER_PATH+"/GST_REPORT_DAY_WISE/";
            new File(FilePath1).mkdir();
            String file=(FilePath1+"GST_REPORT_DAY_WISE_"+from+"-"+to+".csv");

            FileOutputStream fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();

            AlertMessage.inform("IMPORT EXCEL TEMPLETE SHEET IS CREATE IN PATH : "+file);

            Desktop dt = Desktop.getDesktop();
            dt.open(new File(file));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            P.loggerLoader(e);
        }
        finally {
            connection.close();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Pane completePurchaseDetailsPane;
    public DatePicker pFromDate;
    public DatePicker pToDate;
    public TextField pinvoiceNo;
    public TextField dealerName;
    public TextField productName;
    public TextField pAmount;
    public TextField productcode;

    public String purchaseFields1 = "id, productcode,   stockaddeddate, productname,   hsn,   " +
            " added_quantity," +
            " total_amount,   stockquantity, packing, " +
            " mrp,   selling_price,   purchaseprice, " +
            " dealer_name,   p_invoice,   purchase_discount,   cgst,   sgst";
    public String pDiscAmount = " (purchaseprice - ( purchaseprice * purchase_discount / 100 ) )";
    public String pGstAmount = " ("+pDiscAmount+" + ( "+pDiscAmount+" * (cgst+sgst) / 100) )";
    public String pBNetAmount = " ("+pGstAmount+" * added_quantity )";
    public String pFNetAmount = " (( ptr_unit * (cgst+sgst) / 100  ) * qf_total )";
    public String pNetAmount = " sum("+pBNetAmount+" ) as 'Total Purchase Amount' ";

    public void viewPurchaseDetails(ActionEvent event) throws Exception {
        String conditions = "";
        if(pFromDate.getValue()==null &&
                pToDate.getValue()==null &&
                pinvoiceNo.getText().trim().isEmpty() &&
                dealerName.getText().trim().isEmpty() &&
                productName.getText().trim().isEmpty() &&
                productcode.getText().trim().isEmpty())
        {
            AlertMessage.error("Please enter atleast 'Purchase From Date', 'Purchase To Date', 'Purchase Invoice Number', 'Dealer Name', 'Product Name', 'Product Category' OR 'MFRD Name' to view Purchase Report");
        }else {
            if(!(pFromDate.getValue()==null)){
                conditions  =   conditions + " and stockaddeddate>='"+pFromDate.getValue()+"' ";
            }
              if(!(pToDate.getValue()==null)){
                conditions  =   conditions + " and stockaddeddate<='"+pToDate.getValue()+"' ";
            }
            if(!(pinvoiceNo.getText().trim().isEmpty())){
                conditions  =   conditions + " and p_invoice='"+pinvoiceNo.getText().trim()+"'";
            }
            if(!(dealerName.getText().trim().isEmpty())){
                conditions  =   conditions + " and dealer_name='"+dealerName.getText().trim()+"'";
            }
            if(!(productName.getText().trim().isEmpty())){
                conditions  =   conditions + " and productname='"+productName.getText().trim()+"'";
            }
            if(!(productcode.getText().trim().isEmpty())){
                conditions  =   conditions + " and productcode='"+productcode.getText().trim()+"'";
            }
            String query = "select "+purchaseFields1+" from stock where  id>0 "+conditions;
            Welcome(query, displayOfTablePane, 450,1000);
            P.p( " query : "+query);
            Connection con = null;
            try{
                con = DBConnect.connect();
                String query1 = "select "+pNetAmount +" from stock where id>0 "+conditions ;
                ResultSet set = con.createStatement().executeQuery(query1);
                if(set.next()){
                    pAmount.setText(P.df00(set.getDouble(1)));
                }else{
                    pAmount.setText("0");
                }
            }catch (Exception e){
                e.printStackTrace();
                P.loggerLoader(e);
            }finally {
                if(!con.isClosed())  { con.close(); }
            }
        }
    }

    public void excelReportPurchaseDetails(ActionEvent event) throws Exception {
        String conditions = "";
        if(pFromDate.getValue()==null &&
                pToDate.getValue()==null &&
                pinvoiceNo.getText().trim().isEmpty() &&
                dealerName.getText().trim().isEmpty() &&
                productName.getText().trim().isEmpty() &&
                productcode.getText().trim().isEmpty())
        {
            AlertMessage.error("Please enter atleast 'Purchase From Date', 'Purchase To Date', 'Purchase Invoice Number', 'Dealer Name', 'Product Name', 'Product Category' OR 'MFRD Name' to view Purchase Report");
        }else {
            if(!(pFromDate.getValue()==null)){
                conditions  =   conditions + " and stockaddeddate>='"+pFromDate.getValue()+"' ";
            }
            if(!(pToDate.getValue()==null)){
                conditions  =   conditions + " and stockaddeddate<='"+pToDate.getValue()+"' ";
            }
            if(!(pinvoiceNo.getText().trim().isEmpty())){
                conditions  =   conditions + " and p_invoice='"+pinvoiceNo.getText().trim()+"'";
            }
            if(!(dealerName.getText().trim().isEmpty())){
                conditions  =   conditions + " and dealer_name='"+dealerName.getText().trim()+"'";
            }
            if(!(productName.getText().trim().isEmpty())){
                conditions  =   conditions + " and productname='"+productName.getText().trim()+"'";
            }
            if(!(productcode.getText().trim().isEmpty())){
                conditions  =   conditions + " and productcode='"+productcode.getText().trim()+"'";
            }
            String query = "select "+purchaseFields1+" from stock where  id>0 "+conditions;
            String query1 = "select "+pNetAmount +" from stock where id>0 "+conditions ;
            new File(P.drive_name()+FilePath.FOLDER_PATH).mkdir();
            new File(P.drive_name()+FilePath.FOLDER_PATH+"PurchaseDetails/").mkdir();
            String path = P.drive_name()+FilePath.FOLDER_PATH+"PurchaseDetails/PurchaseDetails.xlsx";
            Controller.createExcelFile(query, path, query1);
        }
    }

    public void liveSearchForInvoiceInPT(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        pinvoiceNo,
                        LiveSearchMe.makeSearch("select DISTINCT(p_invoice) from stock where p_invoice like '%"+pinvoiceNo.getText().trim()+"%' limit 100",
                                "p_invoice")
                );
    }

    public void liveSearchForDealerInPT(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        dealerName,
                        LiveSearchMe.makeSearch("select DISTINCT(dealer_name) from stock where dealer_name like '%"+dealerName.getText().trim()+"%' limit 100",
                                "dealer_name")
                );
    }

    public void liveSearchForProductInPT(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        productName,
                        LiveSearchMe.makeSearch("select DISTINCT(productname) from stock where productname like '%"+productName.getText().trim()+"%' limit 100",
                                "productname")
                );
    }

    public void liveSearchForproductcategoryInPT(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        productcode,
                        LiveSearchMe.makeSearch("select DISTINCT(productcode) from stock where productcode like '%"+productcode.getText().trim()+"%' limit 100",
                                "productcode")
                );
    }




/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Pane completeCurrentStockDetailsPane;
    public DatePicker scFromDate;
    public DatePicker scToDate;
    public TextField scPinvoiceNo;
    public TextField scdealerName;
    public TextField scproductName;
    public TextField scmfrdName;
    public TextField scRangeFrom;
    public TextField scRangeTo;
    public TextField scpAmount;
    public TextField scTotalQty;
    public TextField scExpireDaysFrom;
    public TextField scExpireDaysTo;
    public TextField productcodeInCPT;

    public String scFields1 = "id, productcode,   stockaddeddate, productname,   hsn,   " +
            " added_quantity," +
            " total_amount,   stockquantity, packing, " +
            " mrp,   selling_price,   purchaseprice, " +
            " dealer_name,   p_invoice,   purchase_discount,   cgst,   sgst";
    public String scDiscAmount = " (purchaseprice - ( purchaseprice * purchase_discount / 100 ) )";
    public String scGstAmount = " ("+scDiscAmount+" + ( "+scDiscAmount+" * (cgst+sgst) / 100) )";
    public String scBNetAmount = " ("+scGstAmount+" * added_quantity )";
    public String scFNetAmount = " (( purchaseprice * (cgst+sgst) / 100  ) * qf_total )";
    public String scNetAmount = " sum("+scBNetAmount+") as 'Purchase Amount' ";


    public void viewCurrentStockDetails(ActionEvent event) throws Exception {

        String conditions = "";
        if(scFromDate.getValue()==null &&
                scToDate.getValue()==null &&
                scPinvoiceNo.getText().trim().isEmpty() &&
                scdealerName.getText().trim().isEmpty() &&
                scproductName.getText().trim().isEmpty() &&
                productcodeInCPT.getText().trim().isEmpty() &&
                scRangeFrom.getText().trim().isEmpty() &&
                scRangeTo.getText().trim().isEmpty())
        {
            AlertMessage.error("Please enter atleast 'Purchase From Date', 'Purchase To Date', 'Purchase Invoice Number', 'Dealer Name', 'Product Name' , 'Product Category' OR 'MFRD Name' to view Purchase Report");
        }else {
            if(!(scFromDate.getValue()==null)){
                conditions  =   conditions + " and stockaddeddate>='"+scFromDate.getValue()+"' ";
            }
            if(!(scToDate.getValue()==null)){
                conditions  =   conditions + " and stockaddeddate<='"+scToDate.getValue()+"' ";
            }
            if(!(scPinvoiceNo.getText().trim().isEmpty())){
                conditions  =   conditions + " and p_invoice='"+scPinvoiceNo.getText().trim()+"'";
            }
            if(!(scdealerName.getText().trim().isEmpty())){
                conditions  =   conditions + " and dealer_name='"+scdealerName.getText().trim()+"'";
            }
            if(!(scproductName.getText().trim().isEmpty())){
                conditions  =   conditions + " and productname='"+scproductName.getText().trim()+"'";
            }
            if(!(productcodeInCPT.getText().trim().isEmpty())){
                conditions  =   conditions + " and productcode='"+productcodeInCPT.getText().trim()+"'";
            }
            if(!(scRangeFrom.getText().trim().isEmpty())){
                conditions  =   conditions + " and stockquantity>='"+scRangeFrom.getText().trim()+"'";
            }
            if(!(scRangeTo.getText().trim().isEmpty())){
                conditions  =   conditions + " and stockquantity<='"+scRangeTo.getText().trim()+"'";
            }
            String query = "select "+scFields1+" from stock where  id>0 and stockquantity>0 "+conditions+" ";
            Welcome(query, displayOfTablePane, 450,1000);
            Connection con = null;
            try{
                con = DBConnect.connect();
                String query1 = "select "+scNetAmount +",  sum(stockquantity) as 'Total Quantity' from stock where id>0  and stockquantity >0 "+conditions ;
                P.p(query1);

                ResultSet set = con.createStatement().executeQuery(query1);
                if(set.next()){
                    scpAmount.setText(P.df00(set.getDouble(1)));
                    scTotalQty.setText(P.df00(set.getDouble(2)));
                }else{
                    scpAmount.setText("0");
                    scTotalQty.setText("0");
                }
            }catch (Exception e){
                e.printStackTrace();
                P.loggerLoader(e);
            }finally {
                if(!con.isClosed())  { con.close(); }
            }
        }
    }

    public void excelReportCurrentStockDetails(ActionEvent event) throws Exception {
        String conditions = "";
        if(scFromDate.getValue()==null &&
                scToDate.getValue()==null &&
                scPinvoiceNo.getText().trim().isEmpty() &&
                scdealerName.getText().trim().isEmpty() &&
                scproductName.getText().trim().isEmpty() &&
                productcodeInCPT.getText().trim().isEmpty() &&
                scmfrdName.getText().trim().isEmpty() &&
                scRangeFrom.getText().trim().isEmpty() &&
                scRangeTo.getText().trim().isEmpty() &&
                scExpireDaysFrom.getText().trim().isEmpty() &&
                scExpireDaysTo.getText().trim().isEmpty() )
        {
            AlertMessage.error("Please enter atleast 'Purchase From Date', 'Purchase To Date', 'Purchase Invoice Number', 'Dealer Name', 'Product Name' , 'Product Category' OR 'MFRD Name' to view Purchase Report");
        }else {
            if(!(scFromDate.getValue()==null)){
                conditions  =   conditions + " and stockaddeddate>='"+scFromDate.getValue()+"' ";
            }
            if(!(scToDate.getValue()==null)){
                conditions  =   conditions + " and stockaddeddate<='"+scToDate.getValue()+"' ";
            }
            if(!(scPinvoiceNo.getText().trim().isEmpty())){
                conditions  =   conditions + " and p_invoice='"+scPinvoiceNo.getText().trim()+"'";
            }
            if(!(scdealerName.getText().trim().isEmpty())){
                conditions  =   conditions + " and dealer_name='"+scdealerName.getText().trim()+"'";
            }
            if(!(scproductName.getText().trim().isEmpty())){
                conditions  =   conditions + " and productname='"+scproductName.getText().trim()+"'";
            }
            if(!(productcodeInCPT.getText().trim().isEmpty())){
                conditions  =   conditions + " and productcode='"+productcodeInCPT.getText().trim()+"'";
            }
            if(!(scRangeFrom.getText().trim().isEmpty())){
                conditions  =   conditions + " and stockquantity>='"+scRangeFrom.getText().trim()+"'";
            }
            if(!(scRangeTo.getText().trim().isEmpty())){
                conditions  =   conditions + " and stockquantity<='"+scRangeTo.getText().trim()+"'";
            }
            String query = "select "+scFields1+" from stock where  id>0 and stockquantity>0 "+conditions+" ";
            String query1 = "select "+scNetAmount +",  sum(stockquantity) as 'Total Quantity' from stock where id>0  and stockquantity >0 "+conditions ;
            new File(P.drive_name()+FilePath.FOLDER_PATH).mkdir();
            new File(P.drive_name()+FilePath.FOLDER_PATH+"CurrentStockDetails/").mkdir();
            String path = P.drive_name()+FilePath.FOLDER_PATH+"CurrentStockDetails/CurrentStockDetails.xlsx";
            Controller.createExcelFile(query, path, query1);
        }
    }

    public void liveSearchForDealerInCST(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        scdealerName,
                        LiveSearchMe.makeSearch("select DISTINCT(dealer_name) from stock where dealer_name like '%"+scdealerName.getText().trim()+"%' limit 100",
                                "dealer_name")
                );
    }
    public void liveSearchForProductInCST(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        scproductName,
                        LiveSearchMe.makeSearch("select DISTINCT(productname) from stock where productname like '%"+scproductName.getText().trim()+"%' limit 100",
                                "productname")
                );
    }

    public void liveSearchForproductcodeInCPT(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        productcodeInCPT,
                        LiveSearchMe.makeSearch("select DISTINCT(productcode) from stock where productcode like '%"+productcodeInCPT.getText().trim()+"%' limit 100",
                                "productcode")
                );
    }

    public void liveSearchForInvoiceInPTInCST(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        scPinvoiceNo,
                        LiveSearchMe.makeSearch("select DISTINCT(p_invoice) from stock where p_invoice like '%"+scPinvoiceNo.getText().trim()+"%' limit 100",
                                "p_invoice")
                );
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String sFields1 = " bill_id, bill_date, invoice_no, product_code, product_name, " +
            "product_id, hsn, dealername, " +
            "quantity, mrp, purchase_price, discount_amount, discount_in_per, " +
            "trade_price, cgst, cgst_amount, sgst, sgst_amount, " +
            "taxable_value, net_total, customer_name, customer_phno, customer_id ," +
            "bill_status, billed_by, old_stock, packing ";
    public String sDiscAmount = " (purchase_price - ( purchase_price * discount_amount / 100 ) )";
    public String sGstAmount = " ("+sDiscAmount+" + ( "+sDiscAmount+" * (cgst_amount + sgst_amount) / 100) )";
    public String sBNetAmount = " ("+sGstAmount+" * quantity )";
    public String sNetAmount = " sum("+sBNetAmount+") as 'total Purchase Amount'";
    public String sBillNetAmount = " sum(net_total) as 'total Sales Amount'";
    public String sDiffNetAmount = " (sum(net_total) - sum("+sBNetAmount+") )as 'Difference Amount'";

    public Pane completeSalesDetailsPane;
    public DatePicker sFromDate;
    public DatePicker sToDate;
    public TextField sInvoiceNo;
    public TextField sDealerName;
    public TextField sProductName;
    public TextField sCustomerName;
    public TextField sCustomerId;
    public TextField sPAmount;
    public TextField sAmount;
    public TextField sMargin;
    public TextField sProductCode;
    public TextField sPhonenumber;


    public void viewSalesDetails( ActionEvent event) throws Exception {
        String conditions = "";
        if(sFromDate.getValue()==null &&
                sToDate.getValue()==null &&
                sInvoiceNo.getText().trim().isEmpty() &&
                sDealerName.getText().trim().isEmpty() &&
                sProductName.getText().trim().isEmpty() &&
                sProductCode.getText().trim().isEmpty() &&
                sCustomerName.getText().trim().isEmpty() &&
                sCustomerId.getText().trim().isEmpty() &&
                sPhonenumber.getText().trim().isEmpty())

        {
            AlertMessage.error("Please enter atleast 'Bill From Date', 'Bill To Date', 'Invoice Number', 'Dealer Name', 'Product Name', 'Product Category' " +
                    "' Product Code' , 'Customer Name', 'Customer Id' OR 'Phonenumber' to view Sales Report");
        }else {
            if(!(sFromDate.getValue()==null)){
                conditions  =   conditions + " and bill_date>='"+sFromDate.getValue()+"' ";
            }
            if(!(sToDate.getValue()==null)){
                conditions  =   conditions + " and bill_date<='"+sToDate.getValue()+"' ";
            }
            if(!(sInvoiceNo.getText().trim().isEmpty())){
                conditions  =   conditions + " and invoice_no='"+sInvoiceNo.getText().trim()+"'";
            }
            if(!(sDealerName.getText().trim().isEmpty())){
                conditions  =   conditions + " and dealername='"+sDealerName.getText().trim()+"'";
            }
            if(!(sProductName.getText().trim().isEmpty())){
                conditions  =   conditions + " and product_name='"+sProductName.getText().trim()+"'";
            }
            if(!(sProductCode.getText().trim().isEmpty())){
                conditions  =   conditions + " and product_code='"+sProductCode.getText().trim()+"'";
            }
            if((sbilltype.getValue().toString().equalsIgnoreCase("CASH"))){
                conditions  =   conditions + " and bill_status='billed'";
            }else if((sbilltype.getValue().toString().equalsIgnoreCase("CREDIT"))){
                conditions  =   conditions + " and bill_status='credit'";
            }else if((sbilltype.getValue().toString().equalsIgnoreCase("cancelled"))){
                conditions  =   conditions + " and bill_status='cancelled'";
            }else {
                conditions  =   conditions + " and (bill_status='billed' or bill_status='credit') ";
            }

            if(!(sCustomerName.getText().trim().isEmpty())){
                conditions  =   conditions + " and customer_name='"+sCustomerName.getText().trim()+"'";
            }
            if(!(sCustomerId.getText().trim().isEmpty())){
                conditions  =   conditions + " and customer_id='"+sCustomerId.getText().trim()+"'";
            }
            if(!(sPhonenumber.getText().trim().isEmpty())){
                conditions  =   conditions + " and customer_phno='"+sPhonenumber.getText().trim()+"'";
            }
            String query = "select "+sFields1+" from billing where bill_id > 0 "+conditions+" order by invoice_no";
            P.p("query   : "+query );

            Welcome(query, displayOfTablePane, 450,1000);
            Connection con = null;
            try{
                con = DBConnect.connect();
                String query1 = "select "+sNetAmount +",  "+sBillNetAmount+", "+sDiffNetAmount+" from billing where  bill_id > 0  "+conditions ;
                P.p("query1   : "+query1 );
                ResultSet set = con.createStatement().executeQuery(query1);
                if(set.next()){
                    sPAmount.setText(P.df00(set.getDouble(1)));
                    sAmount.setText(P.df00(set.getDouble(2)));
                    sMargin.setText(P.df00(set.getDouble(3)));
                }else{
                    sPAmount.setText("0");
                    sAmount.setText("0");
                    sMargin.setText("0");
                }
            }catch (Exception e){
                e.printStackTrace();
                P.loggerLoader(e);
            }finally {
                if(!con.isClosed())  { con.close(); }
            }
        }
    }

    public void excelReportSalesDetails(ActionEvent event) throws Exception {
        String conditions = "";
        if(sFromDate.getValue()==null &&
                sToDate.getValue()==null &&
                sInvoiceNo.getText().trim().isEmpty() &&
                sDealerName.getText().trim().isEmpty() &&
                sProductName.getText().trim().isEmpty() &&
                sProductCode.getText().trim().isEmpty() &&
                sCustomerName.getText().trim().isEmpty() &&
                sPhonenumber.getText().trim().isEmpty() &&
                sCustomerId.getText().trim().isEmpty()) {
            AlertMessage.error("Please enter atleast 'Bill From Date', 'Bill To Date', 'Invoice Number', 'Dealer Name', 'Product Name' , 'Product Category' " +
                    "'product_code' , 'Customer Name', 'Customer Id' OR 'Patient Id' to view Sales Report");
        }else {
            if(!(sFromDate.getValue()==null)){
                conditions  =   conditions + " and bill_date>='"+sFromDate.getValue()+"' ";
            }
            if(!(sToDate.getValue()==null)){
                conditions  =   conditions + " and bill_date<='"+sToDate.getValue()+"' ";
            }
            if(!(sInvoiceNo.getText().trim().isEmpty())){
                conditions  =   conditions + " and invoice_no='"+sInvoiceNo.getText().trim()+"'";
            }
            if(!(sDealerName.getText().trim().isEmpty())){
                conditions  =   conditions + " and dealername='"+sDealerName.getText().trim()+"'";
            }
            if(!(sProductName.getText().trim().isEmpty())){
                conditions  =   conditions + " and product_name='"+sProductName.getText().trim()+"'";
            }
            if(!(sProductCode.getText().trim().isEmpty())){
                conditions  =   conditions + " and product_code='"+sProductCode.getText().trim()+"'";
            }
            if(!(sPhonenumber.getText().trim().isEmpty())){
                conditions  =   conditions + " and customer_phno='"+sPhonenumber.getText().trim()+"'";
            }
            if(!(sCustomerName.getText().trim().isEmpty())){
                conditions  =   conditions + " and customer_name='"+sCustomerName.getText().trim()+"'";
            }
            if(!(sCustomerId.getText().trim().isEmpty())){
                conditions  =   conditions + " and customer_id='"+sCustomerId.getText().trim()+"'";
            }
            if((sbilltype.getValue().toString().equalsIgnoreCase("CASH"))){
                conditions  =   conditions + " and bill_status='billed'";
            }else if((sbilltype.getValue().toString().equalsIgnoreCase("CREDIT"))){
                conditions  =   conditions + " and bill_status='credit'";
            }

            String query = "select "+sFields1+" from billing where  bill_id > 0 "+conditions+" order by invoice_no";
            P.p("query   : "+query );
            String query1 = "select "+sNetAmount +",  "+sBillNetAmount+", "+sDiffNetAmount+" from billing where bill_id > 0  "+conditions ;
            new File(P.drive_name()+FilePath.FOLDER_PATH).mkdir();
            new File(P.drive_name()+FilePath.FOLDER_PATH+"SalesDetails/").mkdir();
            String path = P.drive_name()+FilePath.FOLDER_PATH+"SalesDetails/SalesDetails.xlsx";
            Controller.createExcelFile(query, path, query1);
        }
    }

    public void liveSearchForDealerInST(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        sDealerName,
                        LiveSearchMe.makeSearch("select DISTINCT(dealername) from billing where dealername like '%"+sDealerName.getText().trim()+"%' limit 100",
                                "dealername")

                );
    }

    public void liveSearchForProductInST(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        sProductName,
                        LiveSearchMe.makeSearch("select DISTINCT(product_name) from billing where product_name like '%"+sProductName.getText().trim()+"%' limit 100",
                                "product_name")
                );
    }

    public void liveSearchForProductCodeInST(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        sProductCode,
                        LiveSearchMe.makeSearch("select DISTINCT(product_code) from billing where product_code like '%"+sProductCode.getText().trim()+"%' limit 100",
                                "product_code")
                );
    }
    public void liveSearchForCustId(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        sCustomerId,
                        LiveSearchMe.makeSearch("select DISTINCT(customer_id) from billing where customer_id like '%"+sCustomerName.getText().trim()+"%' limit 100",
                                "customer_id")
                );
    }


    public void liveSearchForCustomerInST(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        sCustomerName,
                        LiveSearchMe.makeSearch("select DISTINCT(customer_name) from billing where customer_name like '%"+sCustomerName.getText().trim()+"%' limit 100",
                                "customer_name")
                );
    }

    public void liveSearchForPhonenumberInST(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        sPhonenumber,
                        LiveSearchMe.makeSearch("select DISTINCT(customer_phno) from billing where customer_phno like '%"+sCustomerName.getText().trim()+"%' limit 100",
                                "customer_phno")
                );
    }
    public void liveSearchForInvoiceInST(KeyEvent event) throws Exception {
        TextFields.bindAutoCompletion
                (
                        sInvoiceNo,
                        LiveSearchMe.makeSearch("select DISTINCT(invoice_no) from billing where invoice_no like '%"+sInvoiceNo.getText().trim()+"%' limit 100",
                                "invoice_no")
                );
    }

    public void generate_ca_report(ActionEvent actionEvent) throws Exception {

        connection = DBConnect.connect();

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("CA_Report");

        XSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        ArrayList<String> headerList = new ArrayList<String>();

        headerList.add("DATE");
        headerList.add("BILL NO");
        headerList.add("CUSTOMER NAME");
        headerList.add("GSTIN");
        headerList.add("QUANTITY");
        headerList.add("TAXABLE VALUE");
        headerList.add("GST%");
        headerList.add("CGST AMOUNT");
        headerList.add("SGST AMOUNT");
        headerList.add("TOTAL");


        XSSFRow header = sheet.createRow
                (0);

        for (int i = 0; i < headerList.size(); i++) {
            String colname = headerList.get(i);
            header.createCell(i).setCellValue(colname.toUpperCase());
            header.getCell(i).setCellStyle(style);
        }


        String invoice = " ", cname = " ", cgst = " ", date = "", gstin = "";
        double Dnettotal=0, total = 0, cgstamnt = 0, sgstamnt = 0, quantity =0, taxval =0;

        String query = "select DISTINCT invoice_no,cgst from billing where (bill_status='billed' or bill_status='credit') and bill_date BETWEEN '" + from_date.getValue() + "' and '" + to_date.getValue() + "' ORDER BY invoice_no";
        ResultSet rs = connection.createStatement().executeQuery(query);
        ArrayList<String> invoice_list = new ArrayList<String>();
        ArrayList<String> gst_list = new ArrayList<String>();

        while (rs.next()) {
            invoice_list.add(rs.getString(1));
            gst_list.add(rs.getString(2));


            for (int i = 0; i < gst_list.size(); i++) {

                XSSFRow row = sheet.createRow(i + 1);
                //  P.p("invoice_list number  :  " + invoice_list.get(i));

                // P.p(" working :  1");
                String query1 = "select bill_date,invoice_no, customer_name,gstin_no,quantity,sum(taxable_value) as taxval,cgst*2 ,sum(cgst_amount) as cgstamnt,sum(sgst_amount) as sgstamnt,sum(net_total) as 'total' from billing where bill_date BETWEEN ' " + from_date.getValue() + "' and '" + to_date.getValue() + "' and invoice_no='" + invoice_list.get(i) + "' and cgst='" + gst_list.get(i) + "' ";
                ResultSet rs1 = connection.createStatement().executeQuery(query1);
                while (rs1.next()) {
                    // P.p(" working :  2");
                    date = rs1.getString(1);
                    invoice = rs1.getString(2);
                    cname = rs1.getString(3);
                    gstin = rs1.getString(4);
                    quantity = rs1.getDouble(5);
                    taxval = rs1.getDouble(6);
                    cgst = rs1.getString(7);
                    cgstamnt = rs1.getDouble(8);
                    sgstamnt = rs1.getDouble(9);
                    total = rs1.getDouble(10);


                    int index = 0;
                    row.createCell(index++).setCellValue(date);
                    row.createCell(index++).setCellValue(invoice);
                    row.createCell(index++).setCellValue(cname);
                    row.createCell(index++).setCellValue(gstin);
                    row.createCell(index++).setCellValue(quantity);
                    row.createCell(index++).setCellValue(taxval);
                    row.createCell(index++).setCellValue(cgst);
                    row.createCell(index++).setCellValue(cgstamnt);
                    row.createCell(index++).setCellValue(sgstamnt);
                    row.createCell(index++).setCellValue(total);

                }
            }
        }

        String file = (P.drive_name() + FilePath.CA_REPORT_PATH + "CA_REPORT_" + "-" + ".csv");
        File file1 = new File(P.drive_name() + FilePath.FOLDER_PATH);
        file1.mkdir();
        File file2 = new File(P.drive_name() + FilePath.CA_REPORT_PATH);
        file2.mkdir();
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("GST REPORT OF INVOICE WISE EXCEL SHEET IS CREATE IN PATH : " + file);
        alert.showAndWait();

        Desktop dt = Desktop.getDesktop();
        dt.open(new File(file));
    }

    }



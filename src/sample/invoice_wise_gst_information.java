package sample;

import javafx.scene.control.Alert;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by kings on 07-12-17.
 */
public class invoice_wise_gst_information {

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



            String query = "Select distinct(invoice_no) from billing where bill_date >='" + from + "' and bill_date <='" + to + "'";
            ResultSet rs = connection.createStatement().executeQuery(query);
            ArrayList<String> invoice_list = new ArrayList<String>();

            while (rs.next()) {
                invoice_list.add(rs.getString(1));
            }
            double gst_a=0,cgst_a=0,sgst_a=0,basic=0;

            for (int i = 0; i < invoice_list.size(); i++)
            {
                XSSFRow row = sheet.createRow(i+1);
                P.p("invoice_list number  :  "+invoice_list.get(i));

                P.p(" working :  1");
                String query1 = "select customer_name, bill_date from billing where invoice_no='" + invoice_list.get(i) + "'";

                ResultSet rs1 = connection.createStatement().executeQuery(query1);
                while (rs1.next()) {
                    P.p(" working :  2");
                    m_name = rs1.getString(1);
                    bill_date = rs1.getString(2);
                }
                int index=0;
                row.createCell(index++).setCellValue(bill_date);
                row.createCell(index++).setCellValue(m_name);

                String query2 = "select phoneno from customer where customername='" + m_name + "'";

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
                    basic=0;
                    P.p(" working :  4 for CGST AND SGST = "+gst_amount.get(j) );

                    String query4="select sum(cgst_amount),sum(sgst_amount),taxable_value from billing where cgst='"+ gst_amount.get(j)+"' and sgst='"+gst_amount.get(j)+"' and invoice_no='"+invoice_list.get(i)+"'";
                    ResultSet rs4=connection.createStatement().executeQuery(query4);
                    while(rs4.next())
                    {
                        P.p(" working :  5");
                        cgst_a=rs4.getDouble(1);
                        sgst_a=rs4.getDouble(2);
                        basic=basic + rs4.getDouble(3);
                        gst_a=cgst_a+sgst_a;
                        P.p("");
                        P.p("basic = "+gst_a + " CSGT = "+cgst_a + "  SSGT = "+sgst_a);
                    }
                    row.createCell(index++).setCellValue(basic);
                    row.createCell(index++).setCellValue(cgst_a);
                    row.createCell(index++).setCellValue(sgst_a);
                }
            }
            P.p("gst_no"+gst_amount);
            P.p("invoice_list"+invoice_list);
            P.p("m_name"+m_name);
            P.p("bill_date"+bill_date);
            P.p("gst in no"+gst_no);

            //////////////////////////////////////////////////////////////////////////////////////////////////////////

            XSSFSheet sheet1 = wb.createSheet("Purchase_gst_report");

            XSSFCellStyle style1 = wb.createCellStyle();
            style1.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFRow header1 = sheet1.createRow(0);
            int Row=1;
            for(int i=0; i<headerList.size();i++)
            {
                String colname=headerList.get(i);
                header1.createCell(i).setCellValue(colname.toUpperCase());
                header1.getCell(i).setCellStyle(style);
            }

            String m_name1 = " ", bill_date1 = " ", gst_no1 = " ";

            String query12 = "Select distinct(dealer_name) from stock where stockaddeddate >='" + from + "' and stockaddeddate <='" + to + "'";
            ResultSet rs12 = connection.createStatement().executeQuery(query12);
            ArrayList<String> dealer_list = new ArrayList<String>();

            while (rs12.next()) {
                dealer_list.add(rs12.getString(1));
            }
            for(int dd=0;dd<dealer_list.size();dd++) {
                String d_name=dealer_list.get(dd);

                String query11 = "Select distinct(p_invoice) from stock " +
                        "where " +
                        "stockaddeddate >='" + from + "' " +
                        "and stockaddeddate <='" + to + "' " +
                        " and dealer_name='"+d_name+"'";
                ResultSet rs11 = connection.createStatement().executeQuery(query11);
                ArrayList<String> invoice_list11 = new ArrayList<String>();

                while (rs11.next()) {
                    invoice_list11.add(rs11.getString(1));
                }

                double gst_a11 = 0, cgst_a11 = 0, sgst_a11 = 0;

                for (int i = 0; i < invoice_list11.size(); i++) {
                    XSSFRow row = sheet1.createRow(Row++);
                    P.p("invoice_list number  :  " + invoice_list11.get(i));

                    P.p(" working :  1");
                    String query1 = "select dealer_name, stockaddeddate from stock " +
                            "where " +
                            "p_invoice='" + invoice_list11.get(i) + "' and dealer_name='"+d_name+"'";

                    ResultSet rs1 = connection.createStatement().executeQuery(query1);
                    while (rs1.next()) {
                        P.p(" working :  2");
                        m_name = rs1.getString(1);
                        bill_date = rs1.getString(2);
                    }
                    int index = 0;
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
                    for (int j = 0; j < gst_amount.size(); j++) {
                        cgst_a = 0;
                        sgst_a = 0;
                        gst_a = 0;
                        basic=0;
                        P.p(" working :  4 for CGST AND SGST = " + gst_amount.get(j));

                        String query4 = "select purchaseprice, stockquantity, purchase_discount from stock " +
                                "where cgst='" + gst_amount.get(j) +
                                "' and sgst='" + gst_amount.get(j) +
                                "' and p_invoice='" + invoice_list11.get(i) +
                                "' and dealer_name='"+d_name+"'";

                        ResultSet rs4 = connection.createStatement().executeQuery(query4);
                        while (rs4.next()) {
                            P.p(" working :  5");
                            double gstp = Double.parseDouble(gst_amount.get(j)) * 2;
                            P.p(" gst : " + gstp);
                            double recieve_Price = rs4.getDouble(1);
                            P.p("RP   :   " + recieve_Price);
                            double q_bill = rs4.getDouble(2);
                            double dicount = rs4.getDouble(3);
                            double tradePrice = recieve_Price;
                            double discountAmount = (tradePrice * dicount / 100);
                            double taxableValue = tradePrice - discountAmount;

                            gst_a = gst_a + ((((taxableValue) * gstp) / 100) * q_bill);
                            cgst_a = gst_a / 2;
                            sgst_a = gst_a / 2;
                            basic=basic+(taxableValue * q_bill);

                            P.p("rec price : " + recieve_Price);
                            P.p(" tradeprice  : " + tradePrice);
                            P.p(" qty  : " + q_bill);
                            P.p("discount Amount : " + discountAmount);
                            P.p("taxable value : " + taxableValue);
                            P.p("gst Amount  : " + gst_a);
                            P.p("cgst Amount  : " + cgst_a);
                            P.p("sgst Amount  : " + sgst_a);

                        }
                        P.p("");
                        P.p("basic = " + basic + " CSGT = " + cgst_a + "  SSGT = " + sgst_a);
                        row.createCell(index++).setCellValue(basic);
                        row.createCell(index++).setCellValue(cgst_a);
                        row.createCell(index++).setCellValue(sgst_a);
                    }
                }
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            String FilePath1= P.drive_name()+FilePath.FOLDER_PATH+"/GST_REPORT_DAY_WISE/";
            String file=(FilePath1+"GST_REPORT_INVOICE_WISE_"+from+"-"+to+".csv");
            File file1=new File(FilePath1);
            file1.mkdir();

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
}
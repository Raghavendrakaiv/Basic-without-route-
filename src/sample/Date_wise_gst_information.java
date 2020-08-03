package sample;

import javafx.scene.control.Alert;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by kings on 05-12-17.
 */
public class Date_wise_gst_information {

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

                String query3 = "select distinct(cgst) from billing where bill_date='"+date1+"' order by cgst asc";
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
                            String OneDayHeader = "Date : " + date1 + "    SAL-Sales Bill GST : " + invoiceNumberList;
                            row1.createCell(index3++).setCellValue(OneDayHeader);
                        }
                        count++;
                    }
                    int index=0;

                    double trade_price=0.0, discountAMount=0.0,taxableValue=0.0, nettotal=0.0, gst_a=0, cgst_a=0.0, sgst_a=0.0 ;

                    P.p(" working :  4 for CGST AND SGST = "+gst_amount.get(j) );

                    String query4="select cgst_amount,sgst_amount, quantity, trade_price, discount_in_per, taxable_value, net_total  from billing " +
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
                        discountAMount=discountAMount+((rs4.getDouble(4)*rs4.getDouble(5)/100)*rs4.getDouble(3));
                        taxableValue=taxableValue+rs4.getDouble(6);
                        nettotal=nettotal+rs4.getDouble(7);
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
            sheet.addMergedRegion(new CellRangeAddress(Row + 1, Row + 1, 0, 1));
            XSSFRow row = sheet.createRow(++Row);
            P.p("footer data column number  :  "+Row);
            int index2 = 0;
            row.createCell(index2++).setCellValue("TOTAL");
            row.createCell(index2++).setCellValue("");
            row.createCell(index2++).setCellValue(DDtrade_price);
            row.createCell(index2++).setCellValue(DDdiscountAMount);
            row.createCell(index2++).setCellValue(DDtaxableValue);
            row.createCell(index2++).setCellValue(DDgst_a);
            row.createCell(index2++).setCellValue(DDnettotal);

            index2 = 0;
            XSSFRow row1 = sheet.createRow(++Row);
            for (int ii = 0; ii < 7; ii++)
                row1.createCell(index2++).setCellValue(" ");

            ////////////////////////////////////////////////////////////////////////////////
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
                        //double trd2=rs4.getDouble(2)*rs4.getDouble(3);

                        trade_price=trade_price+trd1;

                        double dis=(trd1*rs4.getDouble(3)/100);
                        discountAMount=discountAMount+dis;
                        taxableValue=taxableValue+(trd1-dis);
                        double gstAmountb=(trd1-dis)*gst/100;
                        //double gstAmountf=trd2*gst/100;
                        P.p(" working :  5");
                        gst_a=gst_a+(gstAmountb);
                        nettotal=nettotal+((trd1-dis)+gstAmountb);
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
                    sheet.addMergedRegion(new CellRangeAddress(Row + 1, Row + 1, 0, 1));
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
                    DDnettotal=DDnettotal+Dnettotal;

                    index2 = 0;
                    XSSFRow row5 = sheet2.createRow(++Roww);
                    for (int ii = 0; ii < 7; ii++)
                        row5.createCell(index2++).setCellValue(" ");

                }
                P.p("date incremented to : "+ date4);
                date4 = date4.plusDays(1);

            }
            sheet.addMergedRegion(new CellRangeAddress(Roww + 1, Roww + 1, 0, 1));
            XSSFRow row6 = sheet2.createRow(++Roww);
            P.p("footer data column number  :  "+Roww);
            index2 = 0;
            row6.createCell(index2++).setCellValue("TOTAL");
            row6.createCell(index2++).setCellValue("");
            row6.createCell(index2++).setCellValue(DDtrade_price);
            row6.createCell(index2++).setCellValue(DDdiscountAMount);
            row6.createCell(index2++).setCellValue(DDtaxableValue);
            row6.createCell(index2++).setCellValue(DDgst_a);
            row6.createCell(index2++).setCellValue(DDnettotal);

            index2 = 0;
            XSSFRow row7 = sheet2.createRow(++Roww);
            for (int ii = 0; ii < 9; ii++)
                row7.createCell(index2++).setCellValue(" ");

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            String FilePath1= P.drive_name()+FilePath.FOLDER_PATH+"/GST_REPORT_DAY_WISE/";
            String file=(FilePath1+"GST_REPORT_DAY_WISE_"+from+"-"+to+".csv");
            File file1=new File(FilePath1);
            file1.mkdir();
            FileOutputStream fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("IMPORT EXCEL TEMPLETE SHEET IS CREATE IN PATH : "+file);
            alert.showAndWait();

            Desktop dt = Desktop.getDesktop();
            dt.open(new File(file));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);        }
    }
}

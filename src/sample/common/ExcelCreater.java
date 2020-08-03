package sample.common;

/**
 * Created by kings on 19-01-18.
 */


import javafx.scene.control.Alert;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import sample.DBConnect;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by Mitra on 01/09/18.
 */
public class ExcelCreater {




    public  static void createExcelFile(String query, String path){


        Connection con = null;
        try {
            con = DBConnect.connect();
            ResultSet rs = con.createStatement().executeQuery(query);

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
            XSSFRow row = null;

            while (rs.next()) {
                 row = sheet.createRow(index);
                for (int j=0;j<rs.getMetaData().getColumnCount();j++)
                {
//                    row.createCell(j).setCellValue(rs.getString(j+1));
                    try {
                        if (rs.getString(j + 1).matches("^[-+]?\\d+(\\.\\d+)?$")) {
                            row.createCell(j).setCellValue(Double.parseDouble(rs.getString(j + 1)));
                        } else {
                            row.createCell(j).setCellValue(rs.getString(j + 1));
                        }
                    }catch (Exception e){
                        row.createCell(j).setCellValue("");
                    }
                }
                index++;
            }

            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Details Exported to excel sheet and stored in "+path);
            alert.showAndWait();
            con.close();
            Desktop dt = Desktop.getDesktop();
            dt.open(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            try {
                if(!con.isClosed()) { con.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public  static void createExcelFile(String query, String path,String query2){
        Connection con = null;
        try {
            con = DBConnect.connect();
            ResultSet rs = con.createStatement().executeQuery(query);

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Items details");

            XSSFCellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFRow header = sheet.createRow(1);

            int rowCount = rs.getMetaData().getColumnCount();
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                String colname = rs.getMetaData().getColumnName(i + 1);
                header.createCell(i).setCellValue(colname.toUpperCase());
                header.getCell(i).setCellStyle(style);
            }

            int index = 2;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(index);
                for (int j = 0; j < rs.getMetaData().getColumnCount(); j++) {
//                    row.createCell(j).setCellValue(rs.getString(j + 1));
                    try {
                        if (rs.getString(j + 1).matches("^[-+]?\\d+(\\.\\d+)?$")) {
                            row.createCell(j).setCellValue(Double.parseDouble(rs.getString(j + 1)));
                        } else {
                            row.createCell(j).setCellValue(rs.getString(j + 1));
                        }
                    }catch (Exception e){
                        row.createCell(j).setCellValue("");
                    }
                }
                index++;
            }

            rs = con.createStatement().executeQuery(query2);
            double totalAmount=0;
            while(rs.next()){
                int rowNumbers=rs.getMetaData().getColumnCount();
                sheet.addMergedRegion(new CellRangeAddress(index, index, 0, rowCount));
                XSSFRow rowLast = sheet.createRow(index++);
                String finalData="  ----------------------------------------------------------------------- ";
                rowLast.createCell(0).setCellValue(finalData);
                for(int i=0; i<rowNumbers; i++){
                    sheet.addMergedRegion(new CellRangeAddress(index, index, 0, rowCount));
                    rowLast = sheet.createRow(index++);
                    finalData=rs.getMetaData().getColumnName(i+1).toUpperCase()+"  :  "+ sample.P.df00(rs.getDouble(i+1));
                    rowLast.createCell(0).setCellValue(finalData);
                }
                break;
            }

            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Details Exported to excel sheet and stored in " + path);
            alert.showAndWait();

            con.close();

            Desktop dt = Desktop.getDesktop();
            dt.open(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            try {
                if(!con.isClosed()) { con.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public  static void createExcelFile(String query, String path,String query2, String pageHeader){
        Connection con = null;
        try {
            con = DBConnect.connect();
            ResultSet rs = con.createStatement().executeQuery(query);
            int rowCount = rs.getMetaData().getColumnCount()-1;

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Items details");

            XSSFCellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFFont headerFont =  wb.createFont();
            headerFont.setBold(true);
            headerFont.setColor(HSSFColor.BLUE.index);
            headerFont.setFontHeight(15);

            XSSFRow pageheader = sheet.createRow(0);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, rowCount));
            XSSFCell cell = pageheader.createCell(0);
            XSSFCellStyle headerStyle = wb.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFont(headerFont);
            cell.setCellValue(pageHeader);
            cell.setCellStyle(headerStyle);


            XSSFRow header = sheet.createRow(1);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                String colname = rs.getMetaData().getColumnName(i + 1);
                header.createCell(i).setCellValue(colname.toUpperCase());
                header.getCell(i).setCellStyle(style);
            }

            int index = 2;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(index);
                for (int j = 0; j < rs.getMetaData().getColumnCount(); j++) {
                    try {
                        if (rs.getString(j + 1).matches("^[-+]?\\d+(\\.\\d+)?$")) {
                            row.createCell(j).setCellValue(Double.parseDouble(rs.getString(j + 1)));
                        } else {
                            row.createCell(j).setCellValue(rs.getString(j + 1));
                        }
                    }catch (Exception e){
                        row.createCell(j).setCellValue("");
                    }
                }
                index++;
            }

            if(!query2.trim().isEmpty()) {
                try {
                    rs = con.createStatement().executeQuery(query2);
                    while (rs.next()) {
                        int rowNumbers = rs.getMetaData().getColumnCount();
                        sheet.addMergedRegion(new CellRangeAddress(index, index, 0, rowCount));
                        XSSFRow rowLast = sheet.createRow(index++);
                        String finalData = "  ----------------------------------------------------------------------- ";
                        rowLast.createCell(0).setCellValue(finalData);
                        for (int i = 0; i < rowNumbers; i++) {
                            sheet.addMergedRegion(new CellRangeAddress(index, index, 0, rowCount));
                            rowLast = sheet.createRow(index++);
                            finalData = rs.getMetaData().getColumnName(i + 1).toUpperCase() + "  :  " + sample.P.df00(rs.getDouble(i + 1));
                            rowLast.createCell(0).setCellValue(finalData);
                        }
                        break;
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    P.loggerLoader(e);
                }
            }

            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Details Exported to excel sheet and stored in " + path);
            alert.showAndWait();

            con.close();

            Desktop dt = Desktop.getDesktop();
            dt.open(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            try {
                if(!con.isClosed()) { con.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

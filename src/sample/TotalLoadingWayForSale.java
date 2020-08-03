package sample;

import javafx.scene.control.Alert;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Admin on 06-Sep-17.
 */
public class TotalLoadingWayForSale {
    public static float totalrep1=0;
    public static float totalFinalAmountrep1=0;
    public static float totalFinalAmountrep2=0;
    public static float totalrep2=0;
    public static float totalrep=0;
    public static float totalFinalAmountrep=0;
    public static float totalnew=0;
    public static String atot;

    public static void totalnewtableloadforsale(String query, String path) throws Exception {

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
            alert.setContentText("Details Exported to excel sheet and stored in "+path);
            alert.showAndWait();

            ps.close();
            rs.close();
            con.close();

            Desktop dt = Desktop.getDesktop();
            dt.open(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);
        }
    }
}

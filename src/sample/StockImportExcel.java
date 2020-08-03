package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sample.*;
import sample.dao.StockDao;
import sample.dto.StockDto;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

public class StockImportExcel {

    public Label path_label;

    public void createTemplteExcel(ActionEvent actionEvent) throws Exception {

        File file2=new File(P.drive_name()+FilePath.FOLDER_PATH+ "/TEMPLETE");
        boolean aa = file2.mkdir();
        File file1=new File(P.drive_name()+FilePath.FOLDER_PATH+"/TEMPLETE");
        boolean bb=file1.mkdir();
        String FILE=file1+"/Import_templete.xlsx";
        path_label.setText("Path : ->"+FILE);
        createExcelTEMPLEFile(FILE);
    }

    public static String getCelldata(Row row, int index) throws Exception {
        String data = "";
        try
        {

            if (row.getCell(index).getCellType() == CELL_TYPE_STRING)
            {
                data = row.getCell(index).getStringCellValue();
            }
            else if (row.getCell(index).getCellType() == CELL_TYPE_NUMERIC)
            {
                double bb = row.getCell(index).getNumericCellValue();
                data = bb + "";
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
        return data;
    }


    public static String getStringCelldata(Row row, int index) throws Exception {
        String data = "";
        try
        {
            if (row.getCell(index).getCellType() == CELL_TYPE_STRING)
            {
                data = row.getCell(index).getStringCellValue();
            }
            else if (row.getCell(index).getCellType() == CELL_TYPE_NUMERIC)
            {
                int bb = (int) row.getCell(index).getNumericCellValue();
                data = bb + "";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            P.loggerLoader(e);
        }
        return data;
    }

    void createExcelTEMPLEFile( String path) throws Exception {
        Connection con = null;
        try {

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Items details");

            XSSFCellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFRow header = sheet.createRow(0);

            ArrayList<String> headerList=new ArrayList<String>();


            headerList.add("DATE(YYYY)");
            headerList.add("DATE(MM)");
            headerList.add("DATE(DD)");
            headerList.add("Invoice No");
            headerList.add("Dealer Name");
            headerList.add("Product Name");
            headerList.add("Quantity");
            headerList.add("Purchase Price");
            headerList.add("Purchase Discount");
            headerList.add("GST Rate(CGST+SGST)");
            headerList.add("MRP");
            headerList.add("Selling Price");
            headerList.add("Packing");
            headerList.add("Product Code");
            headerList.add("HSN Code");


            for(int i=0; i<headerList.size();i++)
            {
                String colname=headerList.get(i);
                header.createCell(i).setCellValue(colname.toUpperCase());
                header.getCell(i).setCellStyle(style);
            }


            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("IMPORT EXCEL TEMPLETE SHEET IS CREATE IN PATH : "+path);
            alert.showAndWait();

            Desktop dt = Desktop.getDesktop();
            dt.open(new File(path));

        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }

    public void updatecustomerbalance(ActionEvent actionEvent) throws Exception {
        StockDto dto=new StockDto();
        Connection connection=null;
        try{
            connection = DBConnect.connect();

            FileChooser fChooser = new FileChooser();
            FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("Xlsx (.xlsx, .csv)", "*.xlsx", "*.csv");
            fChooser.getExtensionFilters().add(extentionFilter);
            //Set to user directory or go to default if cannot access
            String userDirectoryString = System.getProperty("user.home");
            File userDirectory = new File(userDirectoryString);

            if (!userDirectory.canRead()) {
                userDirectory = new File("c:/");
            }
            fChooser.setInitialDirectory(userDirectory);

            //Choose the file
            File chosenFile = fChooser.showOpenDialog(null);

            String path = "";
            if (chosenFile != null) {
                path = chosenFile.getPath();
                File file = new File(path);

                System.out.println("File Pathe = " + file);
                FileInputStream fis = new FileInputStream(file);

            }

            FileInputStream fis = new FileInputStream(new File(path));


            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            Row row;

            XSSFCellStyle ErrorCellstyle = wb.createCellStyle();
            ErrorCellstyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            ErrorCellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int totalitemInXl=sheet.getLastRowNum();
            int totalItemAddedtoDatabase=0;
            boolean dataIsCorrect = true;
            //For excel sheet checking purpose
            ArrayList<StockDto> stockDtos =  new ArrayList<StockDto>();
            ArrayList<String> erroeLog= new ArrayList<String>();

            for (int i = 0; i < totalitemInXl; i++) {

                int b = 1 + i;
                dataIsCorrect = true;
                row = sheet.getRow(b);
                String date;

                // 0 stock added date yyyy
                date = "";
                try {
                    String dateyyy = getStringCelldata(row, 0);
                    P.p("  added date   :    " + dateyyy);
                    int py = Integer.parseInt(dateyyy);

                    System.out.println("mitra==year==" + py);
                    if ((py + "").trim().length() != 4) {
                        String error = "Miskate in row : " + (b) + " and column 1 : cell should contain Year in 'YYYY' pattern\n";
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Miskate in row : " + (b) + " and column 1 : cell should contain Year in 'YYYY' pattern\n";
                    erroeLog.add(error);
                }


                //  1   stock added date MM
                try {
                    int pm = Integer.parseInt((getStringCelldata(row, 1)));
                    System.out.println("mitra====" + pm);
                    date = date + "-" + pm;
                    if (pm <= 0 || pm > 12) {
                        String error = "Miskate in row : " + b + " and column 2 : cell should contain month in 'MM' pattern\n";
                        row.getCell(1).setCellStyle(ErrorCellstyle);
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Miskate in row : " + b + " and column 2 : cell should contain month in 'MM' pattern\n";
                    row.getCell(1).setCellStyle(ErrorCellstyle);
                    erroeLog.add(error);
                }

                //  2   stock added date dd
                try {
                    int pd = Integer.parseInt((getStringCelldata(row, 2)));
                    date = date + "-" + pd;

                    System.out.println("mitra====" + pd);
                    if (pd <= 0 || pd > 31) {
                        dataIsCorrect = false;
                        String error = "Miskate in row : " + b + " and column 3 : cell should contain day in 'DD' pattern\n";
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Miskate in row : " + b + " and column 3 : cell should contain day in 'DD' pattern\n";
                    erroeLog.add(error);
                }


                //3  invoice number
                try {
                    String aa = (getStringCelldata(row, 3));
                    System.out.println("mitra====" + aa);
                    if (aa.trim().length() < 1) {
                        String error = "Miskate in row : " + (b) + " and column 4 : cell should contain purchase_invoice_number\n";
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Miskate in row : " + (b) + " and column 4 : cell should contain purchase_invoice_number\n";
                    erroeLog.add(error);
                }


                //  4  dealer name
                try {
                    String dealer = (getStringCelldata(row, 4));
                    System.out.println("mitra====" + dealer);
                    String query1 = "select * from dealer where dealer_name='" + dealer + "'";
                    boolean dealerfound = false;

                    ResultSet set1 = connection.createStatement().executeQuery(query1);
                    while (set1.next()) {
                        dealerfound = true;
                    }
                    if (!dealerfound) {
                        dataIsCorrect = false;
                        String error = "Miskate in row : " + b + " and column 5 : Dealer not found\n";
                        erroeLog.add(error);
                    }

                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Miskate in row : " + (b) + " and column 5 : cell should contain name\n";
                    erroeLog.add(error);
                }


                //5 product name
                try {
                    String aa = (getStringCelldata(row, 5));
                    System.out.println("mitra====" + aa);
                    if (aa.trim().length() < 1) {
                        dataIsCorrect = false;
                        String error = "Miskate in row : " + (b) + " and column 6 : cell should contain product_name\n";
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Miskate in row : " + (b) + " and column 6 : cell should contain product_name\n";
                    erroeLog.add(error);
                }

                //6 quantity
                try {
                    int quantity = Integer.parseInt(getStringCelldata(row, 6));
                    System.out.println("mitra====" + quantity);

                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Miskate in row : " + b + " and column 7 : quantity cell should contain number \n";
                    erroeLog.add(error);
                    e.printStackTrace();
                }


                //7 purchase price
                try {
                    double quantity = Double.parseDouble(getCelldata(row, 7));
                    System.out.println("mitra====" + quantity);

                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Miskate in row : " + b + " and column 8 : price_per_gram cell should contain number \n";
                    erroeLog.add(error);
                }


                //8 purchase discount
                try {
                    double quantity = Double.parseDouble(getCelldata(row, 8));
                    System.out.println("mitra====" + quantity);

                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Miskate in row : " + b + " and column 9 : price_per_gram cell should contain number \n";
                    erroeLog.add(error);
                }


                //9 cgst+igst
                if ((getCelldata(row, 9).isEmpty() && getCelldata(row, 10).isEmpty())) {
                    String error = "ENTER EITHER CGST+SGST OR IGST";
                } else {

                    try {
                        double gst = Double.parseDouble(getCelldata(row, 9));
                        System.out.println("mitra====" + gst);
                        String query1 = "select * from gst where amount='" + P.df00(gst / 2) + "' and type='cgst'";
                        boolean dealerfound = false;

                        ResultSet set1 = connection.createStatement().executeQuery(query1);
                        while (set1.next()) {
                            dealerfound = true;
                        }
                        if (!dealerfound) {
                            dataIsCorrect = false;
                            String error = "Miskate in row : " + b + " and column 10 : gst not found\n";
                            erroeLog.add(error);
                        }

                    } catch (Exception e) {
                        dataIsCorrect = false;
                        String error = "Miskate in row : " + b + " and column 10 : cgst+scgst cell should contain number \n";
                        erroeLog.add(error);
                        e.printStackTrace();
                    }


                    //10 mrp
                    try {
                        double mrp = Double.parseDouble(getCelldata(row, 10));
                        System.out.println("mitra====" + mrp);

                    } catch (Exception e) {
                        dataIsCorrect = false;
                        String error = "Miskate in row : " + b + " and column 11 : price_per_gram cell should contain number \n";
                        erroeLog.add(error);
                    }

                    //11 selling price
                    try {
                        double quantity = Double.parseDouble(getCelldata(row, 11));
                        System.out.println("mitra====" + quantity);

                    } catch (Exception e) {
                        dataIsCorrect = false;
                        String error = "Miskate in row : " + b + " and column 12 : price_per_gram cell should contain number \n";
                        erroeLog.add(error);
                    }

                    //12 packing

                    try {
                        String aa = (getStringCelldata(row, 12));
                        System.out.println("mitra====" + aa);
                        if (aa.trim().length() < 1) {
                            dataIsCorrect = false;
                            String error = "Miskate in row : " + (b) + " and column 13 : cell should contain packing\n";
                            erroeLog.add(error);
                        }
                    } catch (Exception e) {
                        dataIsCorrect = false;
                        String error = "Miskate in row : " + (b) + " and column 13 : cell should contain packing\n";
                        erroeLog.add(error);
                    }


                    //13 product code

                    try {
                        String aa = (getStringCelldata(row, 13));
                        System.out.println("mitra====" + aa);
                        if (aa.trim().length() < 1) {
                            dataIsCorrect = false;
                            String error = "Miskate in row : " + (b) + " and column 14 : cell should contain product_code\n";
                            erroeLog.add(error);
                        }
                    } catch (Exception e) {
                        dataIsCorrect = false;
                        String error = "Miskate in row : " + (b) + " and column 14 : cell should contain product_code\n";
                        erroeLog.add(error);
                    }


                    //14 HSN
                    try {
                        String aa = (getStringCelldata(row, 14));
                        System.out.println("mitra====" + aa);
                        if (aa.trim().length() < 1) {
                            dataIsCorrect = false;
                            String error = "Miskate in row : " + (b) + " and column 15 : cell should contain hsn\n";
                            erroeLog.add(error);
                        }
                    } catch (Exception e) {
                        dataIsCorrect = false;
                        String error = "Miskate in row : " + (b) + " and column 15 : cell should contain hsn\n";
                        erroeLog.add(error);
                    }

                }

            }
            if(erroeLog.size()>0)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                String error="";
                for(int ii=0; ii<erroeLog.size();ii++)
                {
                    error=error+erroeLog.get(ii);
                }
                alert.setContentText(error);
                alert.showAndWait();
            }else {
                StockDao d=new StockDao();

                boolean b = false;


                for (int i = 0; i < totalitemInXl; i++) {
                    row = sheet.getRow(i + 1);


                    String date;

                    // 0 stock added date yyyy
                    date="";
                    String dateyyy=getStringCelldata(row, 0);
                    P.p("  added date   :    "+dateyyy);
                    int py= Integer.parseInt(dateyyy);
                    date=date+py;



                    //  1   stock added date MM

                    int pm= Integer.parseInt((getStringCelldata(row, 1)));
                    System.out.println("mitra===="+pm);
                    date=date+"-"+pm;


                    //  2   stock added date dd

                    int pd= Integer.parseInt((getStringCelldata(row, 2)));
                    date=date+"-"+pd;
                    dto.setStockaddeddate(date);
                    System.out.println("mitra===="+pd);


                    //3 purchase invoice number

                    String aa= (getStringCelldata(row,3));
                    System.out.println("mitra===="+aa);
                    dto.setP_invoice(aa);


                    //  4  dealer name

                    String dealer= (getStringCelldata(row,4));
                    System.out.println("mitra===="+dealer);
                    dto.setDealer_name(dealer);


                    //5 product name

                    String p_name= (getStringCelldata(row,5));
                    System.out.println("mitra===="+p_name);
                    dto.setProductname(p_name);


                    //6 quantity

                    double quantity= Double.parseDouble(getCelldata(row,6));
                    System.out.println("mitra===="+quantity);
                    dto.setStockquantity(quantity);
                    dto.setAdded_quantity(quantity);


                    //7 purchase price

                    double price_grm= Double.parseDouble(getCelldata(row,7));
                    System.out.println("mitra===="+price_grm);
                    dto.setPurchaseprice(Double.parseDouble(P.df00(price_grm)));

                    //8 purchase discount

                    double price_dis= Double.parseDouble(getCelldata(row,8));
                    System.out.println("mitra===="+price_dis);
                    dto.setPurchase_discount(Double.parseDouble(P.df00(price_dis)));

                    //9 cgst+igst

                    double gst= Double.parseDouble(getCelldata(row,9));
                    System.out.println("mitra===="+gst);
                    dto.setCgst(Double.parseDouble(P.df00(gst/2)));
                    dto.setSgst(Double.parseDouble(P.df00(gst/2)));


                    //10 mrp

                    double mrp= Double.parseDouble(getCelldata(row,10));
                    System.out.println("mitra===="+mrp);
                    dto.setMrp(Double.parseDouble(P.df00(mrp)));


                    //11 selling price

                    double sp= Double.parseDouble(getCelldata(row,11));
                    System.out.println("mitra===="+sp);
                    dto.setSelling_price(Double.parseDouble(P.df00(sp)));


                    //12 packing

                    String pack= (getStringCelldata(row,12));
                    System.out.println("mitra===="+pack);
                    dto.setPacking(pack);



                    //13 product code

                    String p_code= (getStringCelldata(row,13));
                    System.out.println("mitra===="+p_code);
                    dto.setProductcode(p_code);


                    //14 HSN

                    String hsn= (getStringCelldata(row,14));
                    System.out.println("mitra===="+hsn);
                    dto.setHsn(hsn);
                    double totalAmt = dto.getAdded_quantity() * dto.getPurchaseprice();
                    double disAmt = totalAmt * dto.getPurchase_discount() / 100;
                    double taxAmt = totalAmt - (disAmt);
                    double netAmt = taxAmt + (taxAmt * (dto.getCgst()*2) / 100);
                    dto.setTotal_amount(netAmt);
                    b = d.insert(dto);

                }


                if(b==true){
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("Details Imported to Database");
                    a.showAndWait();
                    Controller cn=new Controller();
                    cn.view_stock_details();
                }
            }


        }catch (Exception e)
        {
            e.printStackTrace();
            P.loggerLoader(e);

        }

    }
}
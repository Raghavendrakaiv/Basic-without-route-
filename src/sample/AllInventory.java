package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.DAYS;

public class AllInventory implements Initializable {

    public ComboBox inventory_type;
    public Pane view_pane;
    public DatePicker date1;
    public DatePicker date2;
    public Pane gst_info_pane2;
    public DatePicker s_date;
    public DatePicker e_date;
    public TextField s_year;
    public TextField p_year;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        inventory_type.getItems().removeAll(inventory_type.getItems());
        inventory_type.getItems().addAll("Select Report Type", "Sales Report", "Purchase Report",
                "Date Wise Sales", "Product Name Wise Sales Report", "Date Wise Purchase", "Dealer Report",
                "Brief Sales Report", "Date Wise GST Report", "Invoice Wise GST Report");
        inventory_type.getSelectionModel().select("Select Report Type");

    }

    public void clear_all_pane()
    {
        salespane1.setVisible(false);
        purchasepane.setVisible(false);
        dateRepPaneFrame.setVisible(false);
        itemRegPane.setVisible(false);
        datepurchasepane.setVisible(false);
        DealerRegPane1.setVisible(false);
        brief_sales_report.setVisible(false);
        gst_info_pane.setVisible(false);
        gst_info_pane2.setVisible(false);
        view_pane.getChildren().clear();
    }

    public void select_inventory_type(ActionEvent actionEvent) {
        if (inventory_type.getValue().equals("Select Report Type")) {
            clear_all_pane();
        } else if (inventory_type.getValue().equals("Sales Report")) {
            clear_all_pane();
            salespane1.setVisible(true);

            salescombo.getItems().removeAll(salescombo.getItems());
            salescombo.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
            salescombo.getSelectionModel().select("01");
        } else if (inventory_type.getValue().equals("Purchase Report")) {
            clear_all_pane();
            purchasepane.setVisible(true);

            purchaseCombo.getItems().removeAll(purchaseCombo.getItems());
            purchaseCombo.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
            purchaseCombo.getSelectionModel().select("01");
        } else if (inventory_type.getValue().equals("Date Wise Sales")) {
            clear_all_pane();
            dateRepPaneFrame.setVisible(true);
        } else if (inventory_type.getValue().equals("Product Name Wise Sales Report")) {
            clear_all_pane();
            itemRegPane.setVisible(true);
        } else if (inventory_type.getValue().equals("Date Wise Purchase")) {
            clear_all_pane();
            datepurchasepane.setVisible(true);
        } else if (inventory_type.getValue().equals("Dealer Report")) {
            clear_all_pane();
            DealerRegPane1.setVisible(true);
        } else if (inventory_type.getValue().equals("Brief Sales Report")) {
            clear_all_pane();
            brief_sales_report.setVisible(true);
        } else if (inventory_type.getValue().equals("Date Wise GST Report")) {
            clear_all_pane();
            gst_info_pane.setVisible(true);
        } else if (inventory_type.getValue().equals("Invoice Wise GST Report")) {
            clear_all_pane();
            gst_info_pane2.setVisible(true);
        }
    }

    @FXML
    private Pane salespane1;

    @FXML
    private ComboBox salescombo;

    @FXML
    private TextField reportDealers1;

    @FXML
    private Pane purchasepane;

    @FXML
    private ComboBox purchaseCombo;

    @FXML
    private Pane dateRepPaneFrame;

    @FXML
    private DatePicker dateend1;

    @FXML
    private DatePicker datestart1;

    @FXML
    private Pane itemRegPane;

    @FXML
    private TextField reportItem;

    @FXML
    private Pane datepurchasepane;

    @FXML
    private DatePicker dateend1111;

    @FXML
    private DatePicker datestart1111;

    @FXML
    private Pane DealerRegPane1;

    @FXML
    void dateSelecterRep(ActionEvent event) throws Exception {
        long daysBetween = DAYS.between(datestart1.getValue(), dateend1.getValue());
        if (daysBetween >= 0) {
            String query = "select * from billing where bill_date>='" + datestart1.getValue() + "' AND bill_date<='" + dateend1.getValue() + "'";
            LoadingDataToTableView.Welcome(query, view_pane, 500, 900);
        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("SELECT VALID DATES");
            alert1.showAndWait();
        }
    }


    @FXML
    void dateSelecterReps11(ActionEvent event) throws Exception {
        long daysBetween = DAYS.between(datestart1111.getValue(), dateend1111.getValue());
        if (daysBetween >= 0) {
            String query = "select * from stock where stockaddeddate>='" + datestart1111.getValue() + "' AND stockaddeddate<='" + dateend1111.getValue() + "'";
            LoadingDataToTableView.Welcome(query, view_pane, 500, 900);
        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("SELECT VALID DATES");
            alert1.showAndWait();
        }
    }


    @FXML
    void itemSelectorRep(ActionEvent event) throws Exception {

        if (reportItem.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("SELECT PRODUCT NAME");
            alert1.showAndWait();
        } else {
            String query = "SELECT * FROM billing where product_name='" + reportItem.getText().trim() + "' ";
            LoadingDataToTableView.Welcome(query, view_pane, 500, 900);
        }
    }

    public static void export_excel(String query, String path) throws Exception {
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

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                String colname = rs.getMetaData().getColumnName(i + 1);
                header.createCell(i).setCellValue(colname.toUpperCase());
                header.getCell(i).setCellStyle(style);
            }


            int index = 1;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(index);
                for (int j = 0; j < rs.getMetaData().getColumnCount(); j++) {
                    row.createCell(j).setCellValue(rs.getString(j + 1));
                }

                index++;
            }

            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Details Exported to excel sheet and stored in " + path);
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

    @FXML
    void searchitemproduct(KeyEvent event) throws Exception {
        AutoCompletionBinding<String> autoCompletionBindings = TextFields.bindAutoCompletion(reportItem, LiveSearchMe.makeSearch("select DISTINCT(productname) from stock", "productname"));
    }

    @FXML
    void selectMonth(ActionEvent event) throws Exception {
        if (s_year.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ENTER YEAR");
            alert.showAndWait();
        } else if (s_year.getText().trim().length() != 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("YEAR SHOULD CONTAIN YYYY FORMAT");
            alert.showAndWait();
        } else {
            String month = salescombo.getValue().toString();
            String year = s_year.getText().trim();
            String query = "SELECT * FROM billing where MONTH(bill_date)='" + month + "' and YEAR(bill_date)='" + year + "'";
            LoadingDataToTableView.Welcome(query, view_pane, 500, 900);
        }
    }

    @FXML
    void selectPurchaseMonth(ActionEvent event) throws Exception {
        if (p_year.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ENTER YEAR");
            alert.showAndWait();
        } else if (p_year.getText().length() != 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("YEAR SHOULD CONTAIN YYYY FORMAT");
            alert.showAndWait();
        } else {
            String month = purchaseCombo.getValue().toString();
            String year = p_year.getText().trim();
            String query = "SELECT * FROM stock where MONTH(stockaddeddate)='" + month + "' and YEAR(stockaddeddate)='" + year + "'";

            LoadingDataToTableView.Welcome(query, view_pane, 500, 900);
        }
    }

    @FXML
    void searchdealerproduct(KeyEvent event) throws Exception {
        AutoCompletionBinding<String> autoCompletionBindings = TextFields.bindAutoCompletion(reportDealers1, LiveSearchMe.makeSearch("select DISTINCT(dealer_name) from stock", "dealer_name"));
    }

    @FXML
    void dealerSelectorRep(ActionEvent event) throws Exception {
        if (reportDealers1.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("SELECT DEALER NAME");
            alert1.showAndWait();
        } else {
            String query = "SELECT * FROM stock where dealer_name='" + reportDealers1.getText().trim() + "' ";
            LoadingDataToTableView.Welcome(query, view_pane, 500, 900);
        }
    }

    @FXML
    void salesInRepInXL1(ActionEvent event) throws Exception {
        if (s_year.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ENTER YEAR");
            alert.showAndWait();
        } else if (s_year.getText().length() != 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("YEAR SHOULD CONTAIN YYYY FORMAT");
            alert.showAndWait();
        } else {
            String month = salescombo.getValue().toString();
            String year = s_year.getText();

            File dir = new File(P.drive_name() + FilePath.FOLDER_PATH);
            dir.mkdir();
            File dir2 = new File(P.drive_name() + FilePath.FOLDER_PATH + "MONTH_WISE_SALES");
            dir2.mkdir();
            String path = P.drive_name() + FilePath.FOLDER_PATH + "/MONTH_WISE_SALES/SalesRep.csv";

            String query = "SELECT * FROM billing where MONTH(bill_date)='" + month + "' and YEAR(bill_date)='" + year + "'";
            export_excel(query, path);
        }
    }

    @FXML
    void exportPurchaseToXL1(ActionEvent event) throws Exception {
        if (p_year.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ENTER YEAR");
            alert.showAndWait();
        } else if (p_year.getText().length() != 4) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("YEAR SHOULD CONTAIN YYYY FORMAT");
            alert.showAndWait();
        } else {
            String month = purchaseCombo.getValue().toString();
            String year = p_year.getText().trim();
            File dir = new File(P.drive_name() + FilePath.FOLDER_PATH);
            dir.mkdir();
            File dir2 = new File(P.drive_name() + FilePath.FOLDER_PATH + "MONTH_WISE_PURCHASE");
            dir2.mkdir();
            String path = P.drive_name() + FilePath.FOLDER_PATH + "/MONTH_WISE_PURCHASE/PurchaseRep.csv";
            String query = "SELECT * FROM stock where MONTH(stockaddeddate)='" + month + "' and YEAR(stockaddeddate)='" + year + "'";
            export_excel(query, path);
        }
    }


    @FXML
    void repByDateXL1(ActionEvent event) throws Exception {
        long daysBetween = DAYS.between(datestart1.getValue(), dateend1.getValue());
        if (daysBetween >= 0) {
            File dir = new File(P.drive_name() + FilePath.FOLDER_PATH);
            dir.mkdir();
            File dir2 = new File(P.drive_name() + FilePath.FOLDER_PATH + "DATE_WISE_SALES");
            dir2.mkdir();
            String path = P.drive_name() + FilePath.FOLDER_PATH + "/DATE_WISE_SALES/DateWisesalesRep.csv";
            String query = "select * from billing where bill_date>='" + datestart1.getValue() + "' AND bill_date<='" + dateend1.getValue() + "'";
            export_excel(query, path);
        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("SELECT VALID DATES");
            alert1.showAndWait();
        }
    }


    @FXML
    void itemRepInXL1(ActionEvent event) throws Exception {
        if (reportItem.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("SELECT PRODUCT NAME");
            alert1.showAndWait();
        } else {
            File dir = new File(P.drive_name() + FilePath.FOLDER_PATH);
            dir.mkdir();
            File dir2 = new File(P.drive_name() + FilePath.FOLDER_PATH + "NAME_WISE_SALES");
            dir2.mkdir();
            String path = P.drive_name() + FilePath.FOLDER_PATH + "/NAME_WISE_SALES/ProductNameWisesalesRep.csv";
            String query = "SELECT * FROM billing where product_name='" + reportItem.getText().trim() + "' ";
            export_excel(query, path);
        }
    }

    @FXML
    void dateWisePurChaseRepInXL1(ActionEvent event) throws Exception {

        long daysBetween = DAYS.between(datestart1111.getValue(), dateend1111.getValue());
        if (daysBetween >= 0) {
            File dir = new File(P.drive_name() + FilePath.FOLDER_PATH);
            dir.mkdir();
            File dir2 = new File(P.drive_name() + FilePath.FOLDER_PATH + "DATE_WISE_PURCHASE");
            dir2.mkdir();
            String path = P.drive_name() + FilePath.FOLDER_PATH + "/DATE_WISE_PURCHASE/DateWisePurchaseRep.csv";

            String query = "select * from stock where stockaddeddate>='" + datestart1111.getValue() + "' AND stockaddeddate<='" + dateend1111.getValue() + "'";
            export_excel(query, path);
        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("SELECT VALID DATES");
            alert1.showAndWait();
        }
    }

    @FXML
    void dealerWiseReportInXl1(ActionEvent event) throws Exception {
        if (reportDealers1.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("SELECT DEALER NAME");
            alert1.showAndWait();
        } else {
            File dir = new File(P.drive_name() + FilePath.FOLDER_PATH);
            dir.mkdir();
            File dir2 = new File(P.drive_name() + FilePath.FOLDER_PATH + "DEALER_WISE_PURCHASE");
            dir2.mkdir();

            String path = P.drive_name() + FilePath.FOLDER_PATH + "/DEALER_WISE_PURCHASE/DealerWiseRep.csv";

            String query = "SELECT * FROM stock where dealer_name='" + reportDealers1.getText() + "' ";
            export_excel(query, path);
        }
    }

    @FXML
    private Pane brief_sales_report;

    @FXML
    void selectMonthIs(ActionEvent event) throws Exception {

        long daysBetween = DAYS.between(date1.getValue(), date2.getValue());
        if (daysBetween >= 0) {
            String query = "SELECT * FROM billing2 where bill_date >='" + date1.getValue() + "' and bill_date<='" + date2.getValue() + "'";
            LoadingDataToTableView.Welcome(query, view_pane, 500, 900);
        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("SELECT VALID DATES");
            alert1.showAndWait();
        }
    }

    @FXML
    void salesInRepInXL1Is(ActionEvent event) throws Exception {
        long daysBetween = DAYS.between(date1.getValue(), date2.getValue());
        if (daysBetween >= 0) {
            File dir = new File(P.drive_name() + FilePath.FOLDER_PATH);
            dir.mkdir();
            File dir2 = new File(P.drive_name() + FilePath.FOLDER_PATH + "BRIEF_SALES_REPORT");
            dir2.mkdir();

            String path = P.drive_name() + FilePath.FOLDER_PATH + "/BRIEF_SALES_REPORT/BriefSalesRep.csv";

            String query = "SELECT * FROM billing2 where bill_date >='" + date1.getValue() + "' and bill_date<='" + date2.getValue() + "'";
            export_excel(query, path);
        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("SELECT VALID DATES");
            alert1.showAndWait();
        }
    }

    @FXML
    private Pane gst_info_pane;

    @FXML
    private DatePicker datestart11ss;

    @FXML
    private DatePicker dateend11ss;

    //date wise gst information
    public void gst_report_in_excel(ActionEvent event) throws Exception {
        long daysBetween = DAYS.between(datestart11ss.getValue(), dateend11ss.getValue());
        if (daysBetween >= 0) {
            Date_wise_gst_information.gstreportDayWise(datestart11ss.getValue().toString(), dateend11ss.getValue().toString());
        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("SELECT VALID DATES");
            alert1.showAndWait();
        }
    }

    public void day_wise_gst_info(ActionEvent event) throws Exception {
        long daysBetween = DAYS.between(s_date.getValue(), e_date.getValue());
        if (daysBetween >= 0) {
            invoice_wise_gst_information.gstreportInvoiceWise(s_date.getValue().toString(), e_date.getValue().toString());
        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("SELECT VALID DATES");
            alert1.showAndWait();
        }
    }
}
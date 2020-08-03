package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import sample.common.CommonDao;
import sample.common.ExcelCreater;
import sample.common.TableOperation;
import sample.dao.ManualBillingDao;
import sample.dto.ManualBillingDto;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Mitra on 10/12/18.
 */
public class ManualBilling implements Initializable {

    public DatePicker date;
    public TextField invoiceNo;
    public TextField cName;
    public TextField cPhoneNo;
    public TextArea cDetails;
    public TextField productName;
    public TextField packing;
    public TextField hsnCode;
    public TextField rate;
    public TextField qty;
    public TextField discPer;
    public TextField discAmount;
    public TextField taxableAmount;
    public ComboBox gstRate;
    public TextField gstAmount;
    public TextField netAmount;
    public TableView billedProductListTable;
    public TextField totalAmount;
    public TextField deductionAmount;
    public TextField finalAmount;
    public TextArea dispatchDetailsTA;
    public TextArea paymentDetailsTA;


    public DatePicker dateFrom;
    public DatePicker dateTo;
    public TextField invoiceNo2;

    public TextField cName2;
    public TextField cPhno2;
    public TableView manualBillingTracherTableView;
    public TextField totalTaxableAmount;
    public TextField totalGSTAmount;
    public TextField totalBillAmount;
    public Pane productName2Pane;
    public ComboBox paymentType;
    public TextField productName2;


    ManualBillingDto mbDto = new ManualBillingDto();
    ManualBillingDao mbDao = new ManualBillingDao();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        date.setValue(LocalDate.now());
        loadGstRate();
        loadSavedBill();
    }

    private void loadGstRate() {
        Connection connection = null;
        try {
            connection = DBConnect.getConnection();
            gstRate.getItems().removeAll(gstRate.getItems());
            String query1 = "select amount from gst where type='CGST' order by amount ASC";
            ArrayList<Double> gstList = new ArrayList<Double>();
            ResultSet set = connection.createStatement().executeQuery(query1);
            while (set.next()) {
                gstList.add(set.getDouble(1));
            }

            for (int i = 0; i < gstList.size(); i++) {
                gstRate.getItems().add(gstList.get(i)*2);
            }
            if (gstList.size() > 0) {
                gstRate.setValue(gstList.get(0));
            }

            paymentType.getItems().removeAll(paymentType.getItems());
            paymentType.getItems().addAll("Select Payment Mode", "Cash", "Cheque","Credit Card", "Online Banking");
            paymentType.getSelectionModel().select("Select Payment Mode");

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {   if (!connection.isClosed()) {   connection.close(); }   } catch (SQLException e) {  e.printStackTrace();    }
        }
    }

    private void loadSavedBill() {
        int inNo = 0;
        mbDto = mbDao.select(" and bill_status='saved' ");
        P.p("mbDto : "+mbDto);
        if(mbDto.getId()>0){
            inNo = mbDto.getInvoice_no();
            if(inNo>0){
                invoiceNo.setText(String.valueOf(inNo));
                loadInvoiceDetails();
            }
        }
    }

    public void calculateNetAmount(KeyEvent event) {
        calculateNetAmount();
    }

    private void calculateNetAmount() {
        try{
            double rateAmt = 0, qtyNo = 0, disPer =0, disAmt = 0, taxableAmt = 0, gstPer = 0, gstAmt = 0, netAmt = 0;
            try{ rateAmt = Double.parseDouble(rate.getText().trim()); }catch (Exception e){ rateAmt=-1; }
            try{ qtyNo = Double.parseDouble(qty.getText().trim()); }catch (Exception e){ qtyNo=-1; }
            try{ disPer = Double.parseDouble(discPer.getText().trim()); }catch (Exception e){ disPer=-1; }
            try{ disAmt= Double.parseDouble(discAmount.getText().trim()); }catch (Exception e){ disAmt=0; }
            try{ gstPer = Double.parseDouble(gstRate.getValue().toString().trim()); }catch (Exception e){ gstPer=-1; }

            if(rateAmt>0 && qtyNo>0){
                if(disPer>=0){
                    disAmt =  (((rateAmt*qtyNo)*disPer)/100);
                }
                discAmount.setText(P.df00(disAmt));
                taxableAmt = (rateAmt*qtyNo) - disAmt;
                taxableAmount.setText(P.df00(taxableAmt));
                gstAmt = taxableAmt*gstPer/100;
                gstAmount.setText(P.df00(gstAmt));
                netAmt = taxableAmt + gstAmt;
                netAmount.setText(P.df00(netAmt));
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }

    public void calculateNetAmount1(KeyEvent event) {
        discPer.clear();
        try{
            double rateAmt = 0, qtyNo = 0, disPer =0, disAmt = 0, taxableAmt = 0, gstPer = 0, gstAmt = 0, netAmt = 0;
            try{ rateAmt = Double.parseDouble(rate.getText().trim()); }catch (Exception e){ rateAmt=-1; }
            try{ qtyNo = Double.parseDouble(qty.getText().trim()); }catch (Exception e){ qtyNo=-1; }
            try{ disAmt= Double.parseDouble(discAmount.getText().trim()); }catch (Exception e){ disAmt=0; }
            try{ gstPer = Double.parseDouble(gstRate.getValue().toString().trim()); }catch (Exception e){ gstPer=-1; }

            if(rateAmt>0 && qtyNo>0){
                taxableAmt = (rateAmt*qtyNo) - disAmt;
                taxableAmount.setText(P.df00(taxableAmt));
                gstAmt = taxableAmt*gstPer/100;
                gstAmount.setText(P.df00(gstAmt));
                netAmt = taxableAmt + gstAmt;
                netAmount.setText(P.df00(netAmt));
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }

    public void calculateNetAmount2(ActionEvent event) {
        calculateNetAmount();
    }

    public void addProductList1(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER") || event.getCode().toString().equals("SPACE") ){
            addProductList();
        }
    }
    public void addProductList2(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().toString().equals("PRIMARY")){
            addProductList();
        }
    }
    private void addProductList() {
        try{
            if(invoiceNo.getText().trim().isEmpty()){
                generateInvoiceNo();
            }
            int inNo = 0;
            boolean isInvoiceNoValid = true;
            try{
                inNo = Integer.parseInt(invoiceNo.getText().trim());
                if(inNo<=0)
                    isInvoiceNoValid = false;
            }catch (Exception e){ isInvoiceNoValid = false; }

            double rateAmt = 0, qtyNo = 0, disPer =0, disAmt = 0, taxableAmt = 0, gstPer = 0, gstAmt = 0, netAmt = 0;
            try{ rateAmt = Double.parseDouble(rate.getText().trim()); }catch (Exception e){ rateAmt=-1; }
            try{ qtyNo = Double.parseDouble(qty.getText().trim()); }catch (Exception e){ qtyNo=-1; }
            if(discPer.getText().trim().isEmpty()){ disPer = 0; }else{
                try{ disPer = Double.parseDouble(discPer.getText().trim()); }catch (Exception e){ disPer=-1; }
            }
            if(discAmount.getText().trim().isEmpty()){ disAmt = 0; }else{
                try{ disAmt = Double.parseDouble(discAmount.getText().trim()); }catch (Exception e){ disAmt=-1; }
            }
            try{ taxableAmt = Double.parseDouble(taxableAmount.getText().trim()); }catch (Exception e){ taxableAmt=-1; }
            try{ gstPer = Double.parseDouble(gstRate.getValue().toString().trim()); }catch (Exception e){ gstPer=-1; }
            try{ gstAmt = Double.parseDouble(gstAmount.getText().trim()); }catch (Exception e){ gstAmt=-1; }
            try{ netAmt = Double.parseDouble(netAmount.getText().trim()); }catch (Exception e){ netAmt=-1; }

            if(!isInvoiceNoValid){
                AlertMessage.error("Invalid Invoice No");
            }else if(date.getValue()==null) {
                AlertMessage.error("Invalid Date");
            }else if(productName.getText().trim().isEmpty()) {
                AlertMessage.error("Please enter Product name");
            }else if(rateAmt<=0) {
                AlertMessage.error("Please enter Valid Rate");
            }else if(qtyNo<=0) {
                AlertMessage.error("Please enter Valid Quantity");
            }else if(disPer<0) {
                AlertMessage.error("Please enter Valid Discount %");
            }else if(disAmt<0) {
                AlertMessage.error("Please enter Valid Discount Amount");
            }else if(taxableAmt<0) {
                AlertMessage.error("Invalid Taxable Amount");
            }else if(gstAmt<0) {
                AlertMessage.error("Invalid GST Amount");
            }else if(netAmt<0) {
                AlertMessage.error("Invalid Net Amount");
            }else{

                mbDto = new ManualBillingDto();
                mbDto.setDate(String.valueOf(date.getValue()));
                mbDto.setInvoice_no(inNo);
                mbDto.setCustomer_name(cName.getText().trim());
                mbDto.setCustomer_phno(cPhoneNo.getText().trim());
                mbDto.setCustomer_details(cDetails.getText().trim());
                mbDto.setItemName(productName.getText().trim());
                mbDto.setPacking(packing.getText().trim());
                mbDto.setHsnCode(hsnCode.getText().trim());
                mbDto.setQuantity(qtyNo);
                mbDto.setRate(rateAmt);
                mbDto.setDiscPer(disPer);
                mbDto.setDiscAmount(disAmt);
                mbDto.setTaxableAmount(taxableAmt);
                mbDto.setCgst(gstPer/2);
                mbDto.setCgst_amount(gstAmt/2);
                mbDto.setSgst(gstPer/2);
                mbDto.setSgst_amount(gstAmt/2);
                mbDto.setNet_total(netAmt);
                mbDto.setBill_status("saved");
                mbDto.setBilled_by(Controller.loginUserName);
                mbDto.setDeductionAmount(0);
                mbDto.setBillAmount(0);
                mbDto.setDispatchDetails(dispatchDetailsTA.getText().trim());
                mbDto.setPaymentDetails(paymentDetailsTA.getText().trim());
                mbDto.setPaymentType("");

                if(mbDao.insert(mbDto)){
                    AlertMessage.inform("Product added Successfully");
                    calculateTotalAmount();
                    calculateFinalAmount();

                    double deduAmt = 0;
                    try{ deduAmt  = Double.parseDouble(deductionAmount.getText().trim()); }catch (Exception e){ deduAmt = 0; }
                    if(!mbDao.update(
                            " customer_name='"+cName.getText().trim()+"', " +
                                    " customer_phno='"+cPhoneNo.getText().trim()+"', " +
                                    " customer_details='"+cDetails.getText().trim()+"', " +
                                    " billed_by='"+Controller.loginUserName+"', "+
                                    " deductionAmount='"+deduAmt+"', "+
                                    " billAmount='"+finalAmount.getText().trim()+"', "+
                                    " dispatchDetails='"+dispatchDetailsTA.getText().trim()+"', "+
                                    " paymentDetails='"+paymentDetailsTA.getText().trim()+"' "
                            ,
                            " and invoice_no='"+inNo+"' ")){
                       P.loggerLoader("Error updating Manula Billing 1");
                    }
                    loadManualBillingTableView();
                    clearData2();
                }else{
                    AlertMessage.error("Error while adding Product");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }

    private void clearData2() {
        productName.clear();
        packing.clear();
        hsnCode.clear();
        rate.clear();
        qty.clear();
        discPer.clear();
        discAmount.clear();
        taxableAmount.clear();
        gstRate.setValue("0");
        gstAmount.clear();
        netAmount.clear();
        productName.requestFocus();
    }

    public void loadInvoiceDetails(KeyEvent event) {
        loadInvoiceDetails();
    }

    private void loadInvoiceDetails() {
        loadManualBillingTableView();
        if(!invoiceNo.getText().trim().isEmpty()) {
            mbDto = mbDao.select(" and invoice_no='"+invoiceNo.getText().trim()+"' ");
            if(mbDto.getId()>0){
                date.setValue(LocalDate.parse(mbDto.getDate()));
                cName.setText(mbDto.getCustomer_name());
                cPhoneNo.setText(mbDto.getCustomer_phno());
                cDetails.setText(mbDto.getCustomer_details());
                deductionAmount.setText(String.valueOf(mbDto.getDeductionAmount()));
                finalAmount.setText(String.valueOf(mbDto.getBillAmount()));
                dispatchDetailsTA.setText(mbDto.getDispatchDetails());
                paymentDetailsTA.setText(mbDto.getPaymentDetails());
                paymentType.setValue(mbDto.getPaymentType());
                calculateTotalAmount();
                calculateFinalAmount();
            }else{
                clearData1();
            }
        }else{
            clearData1();
        }
    }

    private void clearData1 (){
        date.setValue(LocalDate.now());
        cName.clear();
        cPhoneNo.clear();
        cDetails.clear();
        totalAmount.clear();
        deductionAmount.clear();
        finalAmount.clear();
        dispatchDetailsTA.clear();
        paymentDetailsTA.clear();
        billedProductListTable.getItems().removeAll(billedProductListTable.getItems());
        paymentType.setValue("Select Payment Mode");
    }

    private void loadManualBillingTableView() {
        String columns = "id, date, invoice_no, customer_name, itemName, packing, hsnCode, quantity, rate,  discPer, discAmount, taxableAmount, cgst, cgst_amount,  sgst, sgst_amount, net_total, deductionAmount, billAmount";
        String query = "select "+columns+" from manualbilling where id>0 and invoice_no='"+invoiceNo.getText().trim()+"' ";
        TableOperation.load(query, billedProductListTable);
    }

    private void calculateTotalAmount() {
        totalAmount.setText(P.df00(mbDao.getAmount(" sum(net_total) ", " and invoice_no='"+invoiceNo.getText().trim()+"' ")));
    }

    private void generateInvoiceNo() {
        invoiceNo.setText(String.valueOf(mbDao.getMax(" max(invoice_no) ", "")+1));
    }

    public void deleteFromProductList1(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER") || event.getCode().toString().equals("SPACE") ){
            deleteFromProductList();
        }
    }
    public void deleteFromProductList2(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().toString().equals("PRIMARY")){
            deleteFromProductList();
        }
    }
    private void deleteFromProductList() {
        try{
            int mbId = 0;
            try{ mbId = Integer.parseInt(TableOperation.selectItemId(billedProductListTable)); }catch (Exception e){ mbId=-1; }
            if(mbId<=0){
                AlertMessage.error("Please select the Product to delete from Bill");
            }else{
                Alert alert  = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Do you want to delete the Product from list");
                alert.showAndWait();
                if(alert.getResult().getText().equalsIgnoreCase("OK")){

                    if(mbDao.delete(" and id='"+mbId+"' ")){
                        AlertMessage.inform("Product deleted from Bill");
                        loadManualBillingTableView();
                        calculateTotalAmount();
                        calculateFinalAmount();
                        productName.requestFocus();
                    }else{
                        AlertMessage.error("Error while Deleting Product from Bill");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }

    public void deleteAllFromProductList1(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER") || event.getCode().toString().equals("SPACE") ){
            deleteAllFromProductList();
        }
    }
    public void deleteAllFromProductList2(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().toString().equals("PRIMARY")){
            deleteAllFromProductList();
        }
    }
    private void deleteAllFromProductList() {
        try{
            try{
                int inNo = 0;
                try{ inNo = Integer.parseInt(invoiceNo.getText().trim()); }catch (Exception e){ inNo=-1; }
                if(inNo<=0){
                    AlertMessage.error("Please enter valid Invoice No to delete Product from Bill");
                }else{
                    Alert alert  = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Do you want to delete the Product from list");
                    alert.showAndWait();
                    if(alert.getResult().getText().equalsIgnoreCase("OK")){

                        if(mbDao.delete(" and invoice_no='"+inNo+"' ")){
                            AlertMessage.inform("Products deleted from Bill");
                            invoiceNo.clear();
                            clearData1();
                            cName.requestFocus();
                        }else{
                            AlertMessage.error("Error while Deleting Products from Bill");
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                P.loggerLoader(e);
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }
    public void calculateFinalAmount3(KeyEvent event) {
        calculateFinalAmount();
    }
    private void calculateFinalAmount() {
        try{
            double totalAmt = 0, deductionAmt = 0, finalAmt = 0;
            try{ totalAmt = Double.parseDouble(totalAmount.getText().trim()); }catch (Exception e){ totalAmt = -1; }
            try{ deductionAmt = Double.parseDouble(deductionAmount.getText().trim()); }catch (Exception e){ deductionAmt = 0; }
            if(totalAmt>0){
                finalAmt = totalAmt - deductionAmt;
                finalAmount.setText(P.df00(finalAmt));
            }else{
                finalAmount.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }

    public void generateInvoice1(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER") || event.getCode().toString().equals("SPACE") ){
            generateInvoice();
        }
    }
    public void generateInvoice2(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().toString().equals("PRIMARY")){
            generateInvoice();
        }
    }
    private void generateInvoice() {
        try{
            int inNo = 0;
            boolean isInvoiceNoValid = true;
            try{
                inNo = Integer.parseInt(invoiceNo.getText().trim());
                if(inNo<=0)
                    isInvoiceNoValid = false;
            }catch (Exception e){ isInvoiceNoValid = false; }
            ArrayList<ManualBillingDto> mbDtos = new ArrayList<ManualBillingDto>();
            if(inNo>0) {
                mbDtos = mbDao.selectAll(" and invoice_no='" + inNo + "' ");
            }

            if(!isInvoiceNoValid){
                AlertMessage.error("Invalid Invoice No");
            }else if(mbDtos.size()<=0){
                AlertMessage.error("No Data in this Invoice No");
            }else if(date.getValue()==null){
                AlertMessage.error("Please enter or select Bill Date");
            }else if(cName.getText().trim().isEmpty()){
                AlertMessage.error("Please enter Customer Name");
            }else {
                double deduAmt = 0;
                try {
                    deduAmt = Double.parseDouble(deductionAmount.getText().trim());
                } catch (Exception e) {
                    deduAmt = 0;
                }
                if (!mbDao.update(
                        " customer_name='" + cName.getText().trim() + "', " +
                                " customer_phno='" + cPhoneNo.getText().trim() + "', " +
                                " customer_details='" + cDetails.getText().trim() + "', " +
                                " billed_by='" + Controller.loginUserName + "', " +
                                " deductionAmount='" + deduAmt + "', " +
                                " billAmount='" + finalAmount.getText().trim() + "', " +
                                " dispatchDetails='" + dispatchDetailsTA.getText().trim() + "', " +
                                " paymentDetails='" + paymentDetailsTA.getText().trim() + "', "+
                                " bill_status='billed', "+
                                " paymentType='" + ((paymentType.getValue().toString().equalsIgnoreCase("Select Payment Mode"))?"":paymentType.getValue().toString()) + "' "
                        ,
                        " and invoice_no='" + inNo + "' ")) {
                    AlertMessage.error("Error while updation information");
                }else{
                    new File(P.drive_name() + FilePath.FOLDER_PATH).mkdir();
                    new File(P.drive_name() + FilePath.FOLDER_PATH + "MANUAL_RECIPTS").mkdir();
                    String path = P.drive_name() + FilePath.FOLDER_PATH + "/MANUAL_RECIPTS/";
                    String FILE = path + "/GST_INVOICE_" + inNo + ".pdf";

                    Document doc = new Document();
                    PdfWriter.getInstance(doc, new FileOutputStream(FILE));
                    Rectangle pageSize = new Rectangle(PageSize.A4.getWidth(),PageSize.A4.getHeight()/2);
                    doc.setPageSize(PageSize.A4);
                    doc.setMargins(10, 10, 10, 5);
                    doc.open();
                    int noofitems = mbDtos.size();
                    while (true) {
                        if (noofitems > 0) {
                            pdf_sheet_creatorA5_2(1, noofitems, doc , inNo);
                        }
                        break;
                    }
                    doc.close();
                    Desktop.getDesktop().open(new File(FILE));
                    cName.requestFocus();
                    clearData1();
                    clearData2();
                    invoiceNo.clear();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }


    public void pdf_sheet_creatorA5_2(int startfrom, int endwith, Document doc, int inNo) throws SQLException, DocumentException, FileNotFoundException, ClassNotFoundException {
        Connection connection = null;
        try {
            connection = DBConnect.connect();
            {
                String name = "", address = "", phno = "", logoPath="", mailId="", gst_number="";

                mbDto = mbDao.select(" and invoice_no='"+inNo+"' ");
                ArrayList<ManualBillingDto> mbDtos = mbDao.selectAll(" and invoice_no='"+inNo+"' ");

                PdfPTable tableM1_1=new PdfPTable(1);
                tableM1_1.setWidthPercentage(95);

                PdfPTable tableM1_2=new PdfPTable(1);
                tableM1_2.setWidthPercentage(100);
                PdfPCell mainCell2;

                ResultSet set2 = connection.createStatement().executeQuery("select * from profile ");
                while (set2.next()) {
                    name = set2.getString("name");
                    address = set2.getString("mail_addres");
                    mailId  =   set2.getString("comemailid");
                    phno = set2.getString("contact");
                    logoPath=set2.getString("logo");
                    gst_number  =   set2.getString("gstin");
                }

                PdfPTable table1_2=new PdfPTable(2);
                table1_2.setWidthPercentage(95);
                table1_2.setWidths(new int[ ]{2,10});
                com.itextpdf.text.Image logo=null;
                try {
                    logo = com.itextpdf.text.Image.getInstance(logoPath);
                    logo.setBorder(Rectangle.NO_BORDER);
                }catch (Exception e){ e.printStackTrace(); P.loggerLoader(e);  }


                try{
                    table1_2.addCell(logo);
                }catch (Exception e){
                    table1_2.addCell(" ");
                    e.printStackTrace();
                    P.loggerLoader(e);
                }

                Paragraph paragraph6 = new Paragraph();
                paragraph6.setAlignment(Element.ALIGN_CENTER);
                paragraph6.add(new Chunk("GST INVOICE\n", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 8, com.itextpdf.text.Font.BOLD)));
                paragraph6.add(new Chunk(name, new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 15, com.itextpdf.text.Font.BOLD)));
                paragraph6.add(new Chunk("\n"+address, new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 8, com.itextpdf.text.Font.BOLD)));
                String demo="";
                if((!phno.isEmpty())  && (!mailId.isEmpty())){  demo =   phno+"  ,   "+mailId;  }
                else{ demo  =   phno+mailId;   }
                if (!demo.isEmpty()) {  paragraph6.add(new Chunk("\n" + demo, new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 8, com.itextpdf.text.Font.BOLD)));  }
                if (!gst_number.isEmpty()) {  paragraph6.add(new Chunk("\nGSTIN : " + gst_number, new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 8, com.itextpdf.text.Font.BOLD)));  }
                paragraph6.add(new Chunk("\nINVOICE NO:" + inNo, new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 8, com.itextpdf.text.Font.BOLD)));
                String d = mbDto.getDate();
                String d1[] = d.split("-");
                paragraph6.add(new Phrase("                                    DATE :" + d1[1] + "-" + d1[0] + "-" + d1[2], new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 8, com.itextpdf.text.Font.BOLD)));

                PdfPCell cell1_3=new PdfPCell(paragraph6);
                cell1_3.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1_2.addCell(cell1_3);

                mainCell2   =   new PdfPCell(table1_2);
                tableM1_2.addCell(mainCell2);


                com.itextpdf.text.Font font3 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 9, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font font33 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 7);
                com.itextpdf.text.Font font333 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 7, com.itextpdf.text.Font.BOLD);

                Paragraph paragraph8 = new Paragraph();
                paragraph8.add(new Chunk("CUSTOMER NAME : " + mbDto.getCustomer_name(), font3));
                paragraph8.add(new Chunk("\n" + mbDto.getCustomer_phno(), font33));
                paragraph8.add(new Chunk("\n" + mbDto.getCustomer_details(), font33));
                PdfPTable table2 = new PdfPTable(2);
                table2.setWidthPercentage(95);
                PdfPCell cell2 = new PdfPCell(paragraph8);
                cell2.setBorder(Rectangle.TOP);
                table2.addCell(cell2);

                paragraph8 = new Paragraph();
                paragraph8.add(new Chunk("Dispatch Details : " , font3));
                paragraph8.add(new Chunk("\n"+mbDto.getDispatchDetails(), font33));
                cell2 = new PdfPCell(paragraph8);
                cell2.setBorder(Rectangle.TOP);
                table2.addCell(cell2);

                mainCell2   =   new PdfPCell(table2);
                tableM1_2.addCell(mainCell2);

                PdfPTable table1 = new PdfPTable(11);

                table1.setWidths(new int[]{2, 15, 5, 3, 5, 4, 5, 5, 3, 5, 6});
                com.itextpdf.text.Font font1 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 9, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font font1_1 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 9.5f, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font font2 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 8);
                com.itextpdf.text.Font font2_1 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 9, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font font22 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 7);

                table1.addCell(new Phrase(new Chunk("Sl", font1_1)));
                table1.addCell(new Phrase(new Chunk("Description", font1_1)));
                table1.addCell(new Phrase(new Chunk("HSN ", font1_1)));
                table1.addCell(new Phrase(new Chunk("Qty", font1_1)));
                table1.addCell(new Phrase(new Chunk("PACK", font1_1)));
                table1.addCell(new Phrase(new Chunk("Rate", font1_1)));
                table1.addCell(new Phrase(new Chunk("Disc Amount", font1_1)));
                table1.addCell(new Phrase(new Chunk("Taxable Amount", font1_1)));
                table1.addCell(new Phrase(new Chunk("GST%", font1_1)));
                table1.addCell(new Phrase(new Chunk("GST Amount", font1_1)));
                table1.addCell(new Phrase(new Chunk("Net Amount", font1_1)));

                int startSINO = startfrom;
                int endtSINO = startfrom + 10;
                int sino = 1, num = 0;

                DottedCell dcell=new DottedCell(2);

                table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);

                int countLiines = 0;
                for(int i=0; i<mbDtos.size(); i++) {
                    if ((sino >= startSINO) && (sino < endtSINO)) {
                        countLiines++;
                        mbDto = mbDtos.get(i);
                        table1.addCell(Billing.createCell1(String.valueOf(sino), font2_1, Element.ALIGN_CENTER, Rectangle.RIGHT, dcell));
                        table1.addCell(Billing.createCell1(mbDto.getItemName(), font2_1, Element.ALIGN_LEFT, Rectangle.RIGHT, dcell));
                        table1.addCell(Billing.createCell1(mbDto.getHsnCode(), font2_1, Element.ALIGN_LEFT, Rectangle.RIGHT, dcell));
                        table1.addCell(Billing.createCell1(String.valueOf(mbDto.getQuantity()), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(Billing.createCell1(mbDto.getPacking(), font2_1, Element.ALIGN_LEFT, Rectangle.RIGHT, dcell));
                        table1.addCell(Billing.createCell1(P.df00(mbDto.getRate()), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(Billing.createCell1(P.df00(mbDto.getDiscAmount()), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(Billing.createCell1(P.df00(mbDto.getTaxableAmount()), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(Billing.createCell1(P.df00(mbDto.getCgst()*2), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(Billing.createCell1(P.df00(mbDto.getCgst_amount() * 2), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                        table1.addCell(Billing.createCell1(P.df00(mbDto.getNet_total()), font2_1, Element.ALIGN_RIGHT, Rectangle.RIGHT, dcell));
                    }
                    sino++;
                    if (sino >= endtSINO) {
                        break;
                    }
                }
                if (countLiines < 10) {
                    int j = 10 - countLiines;

                    Paragraph par40 = new Paragraph(new Chunk(" ", font2_1));
                    PdfPCell Cel40 = new PdfPCell(par40);
                    Cel40.setBorder(Rectangle.RIGHT);
                    Cel40.setCellEvent(dcell);

                    for (int k = 0; k < (11 * j); k++) {
                        table1.addCell(Cel40);
                    }
                }
                table1.getDefaultCell().setBorder(Rectangle.BOX);
                String conditions = " and invoice_no = '"+inNo+"' ";
                double txAmt = mbDao.getAmount(" sum(taxableAmount) ", conditions);
                double gstAmt = mbDao.getAmount(" sum(cgst_amount+sgst_amount) ", conditions);
                double netAmt = mbDao.getAmount(" sum(net_total) ", conditions);
                table1.addCell(new Phrase(new Chunk("", font2_1)));
                table1.addCell(Billing.createCell2("TOTAL", font2_1 , Element.ALIGN_CENTER, Rectangle.BOX));
                table1.addCell(new Phrase(new Chunk("", font2_1)));
                table1.addCell(new Phrase(new Chunk("", font2_1)));
                table1.addCell(new Phrase(new Chunk("", font2_1)));
                table1.addCell(new Phrase(new Chunk("", font2_1)));
                table1.addCell(new Phrase(new Chunk("", font2_1)));
                table1.addCell(Billing.createCell2(P.df00(txAmt), font1_1 , Element.ALIGN_CENTER, Rectangle.BOX));
                table1.addCell(new Phrase(new Chunk("", font2_1)));

                table1.addCell(Billing.createCell2(P.df00(gstAmt), font1_1 , Element.ALIGN_CENTER, Rectangle.BOX));
                table1.addCell(Billing.createCell2(P.df00(netAmt), font1_1 , Element.ALIGN_CENTER, Rectangle.BOX));

                PdfPTable table4 = new PdfPTable(1);
                table4.setWidthPercentage(95);
                table4.addCell(new PdfPCell(table1));

                mainCell2   =   new PdfPCell(table4);
                tableM1_2.addCell(mainCell2);


                ResultSet sett1 = connection.createStatement().executeQuery("select * from profile");
                String terms = " ", busness_name = " ", accDetails=" ";
                while (sett1.next()) {
                    terms = sett1.getString("terms") + "\n" + sett1.getString("declaration");
                    busness_name = sett1.getString("name");
                    accDetails= "ACC NAME : "+sett1.getString("acc_name")+
                            "\nACC NO : "+sett1.getString("acc_num")+
                            "\nACC TYPE : "+sett1.getString("acc_type")+
                            "\nIFSC COE : "+sett1.getString("ifsc");
                    break;
                }

                PdfPTable table = new PdfPTable(2);
                table.setWidths(new int[]{6, 4});
                table.setWidthPercentage(95);

                NumToWordss numToWordss = new NumToWordss();
                double famount = 0;
                String amountInString = "", finalAMountText = "";

                amountInString = numToWordss.convert((long) mbDto.getBillAmount())+" Rupees only";

                Paragraph amo=new Paragraph();
                amo.add(new Chunk("Rupees in words   :    \n", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 9)));
                amo.add(new Chunk(amountInString.toUpperCase() , new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 8, com.itextpdf.text.Font.BOLD)));
                table.addCell(new PdfPCell(amo));

                {
                    PdfPTable tableTotal=new PdfPTable(1);
                    tableTotal.setWidthPercentage(100);

                    PdfPTable tableGstSummary=new PdfPTable(5);
                    tableGstSummary.setWidths(new int[]{2,5,4,4,5});
                    tableGstSummary.setWidthPercentage(100);

                    ResultSet setgstSummary=mbDao.select( connection,
                            " cgst+sgst, sum(taxableAmount), sum(cgst_amount), sum(sgst_amount), sum(cgst_amount+sgst_amount) " ,
                            " and invoice_no = '"+inNo+"' and (cgst+sgst)>0  group by  cgst+sgst ORDER BY cgst+sgst");

                    com.itextpdf.text.Font fontg1=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 6, com.itextpdf.text.Font.BOLD );
                    com.itextpdf.text.Font fontg2=new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 6 );
                    tableGstSummary.addCell(Billing.createCell2("@", fontg1, 1, 15));
                    tableGstSummary.addCell(Billing.createCell2("TXBL AMOUNT", fontg1, 1, 15));
                    tableGstSummary.addCell(Billing.createCell2("CGST", fontg1, 1, 15));
                    tableGstSummary.addCell(Billing.createCell2("SGST", fontg1, 1, 15));
                    tableGstSummary.addCell(Billing.createCell2("GST AMOUNT", fontg1, 1, 15));

                    boolean dataFound=false;
                    while(setgstSummary.next()){
                        dataFound=true;
                        tableGstSummary.addCell(Billing.createCell1(setgstSummary.getString(1), fontg2, 1, 15, dcell));
                        tableGstSummary.addCell(Billing.createCell1(setgstSummary.getString(2), fontg2, 1, 15,dcell));
                        tableGstSummary.addCell(Billing.createCell1(setgstSummary.getString(3), fontg2, 1, 15,dcell));
                        tableGstSummary.addCell(Billing.createCell1(setgstSummary.getString(4), fontg2, 1, 15,dcell));
                        tableGstSummary.addCell(Billing.createCell1(setgstSummary.getString(5), fontg2, 1, 15,dcell));
                    }
                    if(dataFound){
                        tableTotal.addCell(new PdfPCell(tableGstSummary));
                    }
                    table.addCell(new PdfPCell( tableTotal));
                }


                Paragraph paragraph33 = new Paragraph();
                paragraph33.add(new Chunk("Bill Amount  : " + P.df00(netAmt), new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 10, com.itextpdf.text.Font.BOLD)));
                if(mbDto.getDeductionAmount()>0){
                    paragraph33.add(new Chunk("         Deduction Amount : " + P.df00(mbDto.getDeductionAmount()), new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 10, com.itextpdf.text.Font.BOLD)));
                }
                finalAMountText = "         Amount Receivable  : " + P.df00(mbDto.getBillAmount());
                paragraph33.add(new Chunk(finalAMountText, new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 12, com.itextpdf.text.Font.BOLD)));

                PdfPCell cell = new PdfPCell(paragraph33);
                cell.setColspan(2);
                table.addCell(cell);

                PdfPTable table2_2=new PdfPTable(2);
                table2_2.setWidths(new int[]{5,5});

                Paragraph paragraph35 = new Paragraph();
                paragraph35.add(new Chunk("Terms and Condition  :   ", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 8, com.itextpdf.text.Font.BOLD)));
                paragraph35.add(new Chunk("\n" + terms, new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 7)));
                PdfPCell cell12 = new PdfPCell(paragraph35);
                table2_2.addCell(cell12);


                Paragraph paragraph7 = new Paragraph();
                paragraph7.add(new Chunk("Payment Mode : ", font333));
                paragraph7.add(new Chunk(mbDto.getPaymentType(), font33));
                paragraph7.add(new Chunk("\nPayment Details : ", font333));
                paragraph7.add(new Chunk("\n"+mbDto.getPaymentDetails(), font33));
                cell12 = new PdfPCell(paragraph7);
                table2_2.addCell(cell12);

                table.addCell(new PdfPCell(table2_2));

                Paragraph paragraph36 = new Paragraph();
                paragraph36.add(new Chunk("For  " + busness_name, new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 10)));
                paragraph36.add(new Chunk("\n\n\nAuthorised Signature", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 10)));
                paragraph36.setAlignment(Element.ALIGN_CENTER);
                PdfPCell cell13 = new PdfPCell(paragraph36);
                cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell13);

                mainCell2   =   new PdfPCell(table);
                tableM1_2.addCell(mainCell2);

                PdfPTable tablelast=new PdfPTable(3);
                cell=new PdfPCell(new Phrase(" "));
                cell.setBorder(Rectangle.NO_BORDER);
                tablelast.addCell(cell);

                Paragraph paragraph3 = new Paragraph(new Phrase("Computer Generated Bill", FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 10, BaseColor.GRAY)));
                paragraph3.setAlignment(Element.ALIGN_CENTER);
                cell=new PdfPCell(paragraph3);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablelast.addCell(cell);

                Paragraph paragraph4 = new Paragraph(new Phrase("Software by MITRA SOFTWARES-08182-298188        ", FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 6, BaseColor.GRAY)));
                paragraph4.setAlignment(Element.ALIGN_RIGHT);
                cell=new PdfPCell(paragraph4);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(Rectangle.NO_BORDER);
                tablelast.addCell(cell);

                mainCell2=new PdfPCell(tablelast);
                mainCell2.setBorder(Rectangle.NO_BORDER);
                tableM1_2.addCell(mainCell2);

                PdfPCell mainCell1  =   new PdfPCell();
                double cellHeight   =   (PageSize.A4.getHeight()-20)/2;
                mainCell1.setFixedHeight((float) cellHeight);
                mainCell1.setBorder(Rectangle.NO_BORDER);
                mainCell1.addElement(tableM1_2);

                tableM1_1.addCell(mainCell1);
                //tableM1_1.addCell(mainCell1);
                doc.add(tableM1_1);
                if (sino <= endwith) {
                    doc.newPage();
                    pdf_sheet_creatorA5_2(endtSINO, endwith, doc, inNo);
                }

            }

        } catch (Exception e1) {
            e1.printStackTrace();
            P.loggerLoader(e1);
        }
    }

    CommonDao commonDao = new CommonDao();
   /* public void searchProductName2(KeyEvent event) {
        ArrayList<ArrayList> dtos = commonDao.selectAll(" distinct(itemName) "," manualbilling ","");
        ArrayList<String> supplierNameslist = new ArrayList<String>();
        for(ArrayList<String> dto : dtos) {
            supplierNameslist.add(dto.get(0));
        }
        sample.common.LiveSearchMe.makeSearchOnCombo(productName2, supplierNameslist);
    }*/

    public void searchManualBillingDetails1(KeyEvent event) {
        if( event.getCode().toString().equals("ENTER") || event.getCode().toString().equals("SPACE") ){
            searchManualBillingDetails();
        }
    }
    public void searchManualBillingDetails2(MouseEvent mouseEvent) {
        if( mouseEvent.getButton().toString().equals("PRIMARY") ){
            searchManualBillingDetails();
        }
    }
    private void searchManualBillingDetails() {
        try{

            String conditions = "";
            if(dateFrom.getValue()!=null){
                conditions += " and date>='"+dateFrom.getValue()+"' ";
            }
            if(dateTo.getValue()!=null){
                conditions += " and date<='"+dateTo.getValue()+"' ";
            }
            if(!invoiceNo2.getText().trim().isEmpty()){
                conditions += " and invoice_no='"+invoiceNo2.getText().trim()+"' ";
            }
            if(productName2.getText()!=null){
                if(!productName2.getText().toString().trim().isEmpty()) {
                    conditions += " and itemName='" + productName2.getText().toString().trim() + "' ";
                }
            }
            if(!cName2.getText().trim().isEmpty()){
                conditions += " and customer_name='"+cName2.getText().trim()+"' ";
            }
            if(!cPhno2.getText().trim().isEmpty()){
                conditions += " and customer_phno='"+cPhno2.getText().trim()+"' ";
            }
            if(conditions.trim().isEmpty()){
                AlertMessage.error("Please select the filtering fields to get Report");
            }else{

                String query = " select * from manualbilling where id>0 "+conditions;
                TableOperation.load(query, manualBillingTracherTableView);

                ArrayList<ArrayList> datas = commonDao.selectAll(
                        " sum(taxableAmount) as 'Taxable Amount', " +
                                " sum(cgst_amount+sgst_amount) as 'GST Amount', " +
                                " sum(net_total) as 'Bill Amount'  "
                        ,
                        " manualbilling"
                        ,
                        "where id>0 "+conditions
                );
                for(ArrayList<String> data: datas){
                    if(data.size()>0){
                        totalTaxableAmount.setText(P.df00(data.get(0)));
                        totalGSTAmount.setText(P.df00(data.get(1)));
                        totalBillAmount.setText(P.df00(data.get(2)));
                    }else{
                        totalTaxableAmount.clear();
                        totalGSTAmount.clear();
                        totalBillAmount.clear();
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }

    }

    public void excelManualBillingDetails1(KeyEvent event) {
        if( event.getCode().toString().equals("ENTER") || event.getCode().toString().equals("SPACE") ){
            excelManualBillingDetails();
        }
    }
    public void excelManualBillingDetails2(MouseEvent mouseEvent) {
        if( mouseEvent.getButton().toString().equals("PRIMARY") ){
            excelManualBillingDetails();
        }
    }
    private void excelManualBillingDetails() {
        try{

            String conditions = "";
            if(dateFrom.getValue()!=null){
                conditions += " and date>='"+dateFrom.getValue()+"' ";
            }
            if(dateTo.getValue()!=null){
                conditions += " and date<='"+dateTo.getValue()+"' ";
            }
            if(!invoiceNo2.getText().trim().isEmpty()){
                conditions += " and invoice_no='"+invoiceNo2.getText().trim()+"' ";
            }
            if(productName2.getText()!=null){
                if(!productName2.getText().trim().isEmpty()) {
                    conditions += " and itemName='" + productName2.getText().toString().trim() + "' ";
                }
            }
            if(!cName2.getText().trim().isEmpty()){
                conditions += " and customer_name='"+cName2.getText().trim()+"' ";
            }
            if(!cPhno2.getText().trim().isEmpty()){
                conditions += " and customer_phno='"+cPhno2.getText().trim()+"' ";
            }
            if(conditions.trim().isEmpty()){
                AlertMessage.error("Please select the filtering fields to get Report");
            }else{

                String query = " select * from manualbilling where id>0 "+conditions;
                String query2 = " select sum(taxableAmount) as 'Taxable Amount', " +
                        " sum(cgst_amount+sgst_amount) as 'GST Amount', " +
                        " sum(net_total) as 'Bill Amount' from manualbilling where id>0 "+conditions;
                new File(P.drive_name()+FilePath.FOLDER_PATH).mkdir();
                new File(P.drive_name()+FilePath.FOLDER_PATH+"Reports/").mkdir();
                new File(P.drive_name()+FilePath.FOLDER_PATH+"Reports/ManualBilling_Reports/").mkdir();
                String path = P.drive_name()+FilePath.FOLDER_PATH+"Reports/ManualBilling_Reports/ManualBilling_Report.csv";
                ExcelCreater.createExcelFile(query, path, query2, "ManualBilling Report");
                dateFrom.requestFocus();

            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }


    public void searchProductName2(KeyEvent keyEvent) {
        AutoCompletionBinding<String> autoCompletionBindings = TextFields.bindAutoCompletion(productName2, LiveSearchMe.makeSearch("select DISTINCT(itemName) from manualbilling", "itemName"));

    }
}

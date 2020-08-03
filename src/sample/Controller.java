package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
import static sample.digital_clock.backupDataWithDatabase;

public class Controller implements Initializable {

    public TextArea declaration;
    public ComboBox totalgst;
    public Label date_display;
    public Label clock;
    public JFXButton chart1;
    public TextField p_discount;
    public TextField s_price;
    public TextField purchase_invoice;
    public Pane view_details;
    public AnchorPane barcode_pane;
    public TextField brand_name;
    public TextField product_name;
    public TextField barcode_name;
    public TextField QUANTITY;
    public TextField mrp;
    public TextField salespricewithtax;
    public TextField no_of_times;
    public AnchorPane dashboard;
    public CategoryAxis d_p_xaxis;
    public NumberAxis d_p_yaxis;
    public CategoryAxis m_p_xaxis;
    public NumberAxis m_p_yaxis;
    public CategoryAxis d_s_xaxis;
    public NumberAxis d_s_yaxis;
    public CategoryAxis m_s_xaxis;
    public NumberAxis m_s_yaxis;
    public Label p_year;
    public Label p_year_1;
    public Label p_year2;
    public Label p_year_2;
    public Label s_year;
    public Label s_year_1;
    public Label s_year2;
    public Label s_year_2;
    public TextField p_id;
    public ComboBox dashboard_combo;
    public Pane dashboard_pane;
    public Pane stock_alert_pane;
    public TextField alert_dealer;
    public TextField alert_quantity;
    public TextField licence;
    public TextField pancard_no;
    public TextField packing;
    public DatePicker dealerPayementDate;
    public TextField dealerNameWhilePayment;
    public TextField dealerPayment;
    public TextField dealerPaymentOhterDetails;
    public Pane dealerPaymentDeatilsListPane;
    public DatePicker dealerPayementDateFrom;
    public DatePicker dealerPayementDateTo;
    public TextField dealerPaymentTotal;
    public TextField mrpCode;
    public TextField dummyPiece;
    public TextField dummyCells;
    public TextField brand_name2;
    public TextField dealerPurchaseAmount;
    public TextField dealerBalance;

    @FXML
    private TextField text1;

    @FXML
    private TextField dealerSearch;

    @FXML
    private ImageView profileImageView;

    @FXML
    private PasswordField text2;

    ComboBox chs = new ComboBox();

    private static DecimalFormat df3 = new DecimalFormat("#.##");

    public static String userNameArray;
    public static String passwordArray;
    public static String loginUserName = "";

    public static Connection connection = null;
    public static String userEmpName;

    public static String getUserEmpName() {
        return userEmpName;
    }

    public static void setUserEmpName(String userEmpName) {
        Controller.userEmpName = userEmpName;
    }

    //login functions

    @FXML
    void login(ActionEvent event) throws Exception {

        try {
            //to clear output text file(logger txt file)

            connection = DBConnect.getConnection();

            Statement statement = connection.createStatement();

            String username = text1.getText().trim().toString();
            String password = text2.getText().trim().toString();

            loginUserName = username;
            setUserEmpName(loginUserName);
            String user_type = "";
            int i = 0, j = 0, k = 0;
            ResultSet r = statement.executeQuery("select * from sample");
            while (r.next()) {
                j++;
            }
            LocalDate date1 = LocalDate.now();
            LocalDate date2 = LocalDate.now().plusDays(7);

            if (j == 0) {
                connection = DBConnect.getConnection();
                String check_login = "Insert into sample (a_date,e_date) values(?,?)";
                PreparedStatement preparedStatement_1 = connection.prepareStatement(check_login);
                preparedStatement_1.setString(1, String.valueOf(date1));
                preparedStatement_1.setString(2, String.valueOf(date2));
                preparedStatement_1.execute();
            } else {
                Statement s = connection.createStatement();
                ResultSet rs = s.executeQuery("select * from sample where e_date<='" + date1 + "'");
                while (rs.next()) {
                    k++;
                }
            }

            Statement statement2 = connection.createStatement();
            ResultSet res = statement2.executeQuery("Select * from login where Username='" + username + "' and Password='" + password + "'");

            while (res.next()) {
                userNameArray = res.getString("Username");
                passwordArray = res.getString("Password");
                user_type = res.getString("type");
                i++;
            }
            if (i == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Username and Password Doesn't Match, please confirm");
                alert.showAndWait();
            } else if (k > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("YOURS TRAIL VERSION COMPLETED");
                alert.showAndWait();
            } else {
                if (userNameArray.equals(username) && passwordArray.equals(password) && k == 0 && user_type.equalsIgnoreCase("Admin")) {
                    backupDataWithDatabase();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("action.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Login");
                    alert.setContentText("Login Successful");
                    alert.showAndWait();

                    ((Node) event.getSource()).getScene().getWindow().hide();

                    Stage stage = new Stage();
                    stage.setTitle("Admin");
                    stage.setScene(new Scene(root1, 1100, 800));
                    stage.show();
                } else if (user_type.equals("employee")) {
                    if (userNameArray.equals(username) && passwordArray.equals(password) && k == 0) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("empaction.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Login");
                        alert.setContentText("Login Successful");
                        alert.showAndWait();

                        ((Node) event.getSource()).getScene().getWindow().hide();

                        Stage stage = new Stage();
                        stage.setTitle("Employee");
                        stage.setScene(new Scene(root1, 1100, 700));
                        stage.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("INVALID USER");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            d_date_time();

            dashboard_combo.getItems().removeAll(dashboard_combo.getItems());
            dashboard_combo.getItems().addAll("Select Type", "Stock Alert", "Fast Moving Product", "Slow Moving Product");
            dashboard_combo.getSelectionModel().select("Select Type");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LocalDate today1 = LocalDate.now(ZoneId.of("Indian/Maldives"));

    //date time display
    void d_date_time() throws Exception {
        ZoneId zoneId = ZoneId.of("Indian/Maldives");
        LocalDate today = LocalDate.now(zoneId);
        int day = today.getDayOfMonth();
        int month = today.getMonthValue();
        int year = today.getYear();
        date_display.setText(day + " - " + month + " - " + year);
        new digital_clock(clock);
    }


    @FXML
    private AnchorPane profilePane;

    @FXML
    private AnchorPane Middlepane1;

    @FXML
    private TextField EmpId;

    @FXML
    private TextField UserName;

    @FXML
    private PasswordField Password;

    @FXML
    private PasswordField Confpassword;

    @FXML
    private TextField EmailId;

    @FXML
    private TextField MobileNo;

    @FXML
    private TextField Empaddress;

    public static String idArray = "";
    public static int incidArray;
    public String profileImageStringpdf;
    public String mailAddressPdf1;
    public String mailAddressPdf2;
    public String emailidPdf3;
    public String contactpdf;
    public String gstinpdf;
    public String acc_namepdf;
    public String acc_numpdf;
    public String acc_typepdf;
    public String ifscpdf;
    public String termspdf;
    public String declarationpdf;
    public String jurisidictionpdf;

//add employee

    @FXML
    void AddEmployee(ActionEvent event) throws Exception {
        Middlepane1.setVisible(true);
        Middlepane2.setVisible(false);
        Middlepane4.setVisible(false);
        Middlepane5.setVisible(false);
        Middlepane5.setVisible(false);
        Middlepane6.setVisible(false);
        profilePane.setVisible(false);
        barcode_pane.setVisible(false);
        dashboard.setVisible(false);
    }

    @FXML
    void EmployeeAdd(ActionEvent event) throws Exception {

        connection = DBConnect.getConnection();
        String query = "select username from login";
        ResultSet rs = connection.createStatement().executeQuery(query);
        ArrayList<String> name = new ArrayList();
        boolean employee_found = false;
        while (rs.next()) {
            name.add(rs.getString(1));
        }
        for (int i = 0; i < name.size(); i++) {
            if (name.get(i).equalsIgnoreCase(UserName.getText().trim())) {
                employee_found = true;
                break;
            }
        }

        if (UserName.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER EMPLOYEE NAME");
            alert1.showAndWait();

        } else if (!Password.getText().trim().equals(Confpassword.getText().trim())) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PASSWORD AND CONFIRM PASSWORD NOT EQUAL");
            alert1.showAndWait();

        } else if (MobileNo.getText().trim().isEmpty() || MobileNo.getText().trim().length() != 10) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("ENTER VALID MOBILE NUMBER");
            alert1.showAndWait();

        } else if (Empaddress.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER ADDRESS");
            alert1.showAndWait();
        } else if (employee_found == true) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("USERNAME ALREADY EXIST TRY WITH OTHER USERNAME");
            alert1.showAndWait();
        } else {

            try {

                int i = 0, j = 0;

                String query1 = "Insert into employee(empid, employee_name, username, password, " +
                        "email_id, mobile_no, address) values(?,?,?,?,?,  ?,?)";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement1.setString(1, EmpId.getText().trim());
                preparedStatement1.setString(2, UserName.getText().trim());
                preparedStatement1.setString(3, UserName.getText().trim());
                preparedStatement1.setString(4, Password.getText().trim());
                preparedStatement1.setString(5, EmailId.getText().trim());
                preparedStatement1.setString(6, MobileNo.getText().trim());
                preparedStatement1.setString(7, Empaddress.getText().trim());

                i = preparedStatement1.executeUpdate();

                String query2 = "Insert into login(Username,Password,Type) values(?,?,?)";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);

                preparedStatement2.setString(1, UserName.getText().trim());
                preparedStatement2.setString(2, Password.getText().trim());
                preparedStatement2.setString(3, "employee");

                j = preparedStatement2.executeUpdate();

                if (i > 0 && j > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("EMPLOYEE INFORMATION ADDED SUCCESSFULLY");
                    alert.showAndWait();

                    refresh_empoyee();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN SAVING DATA");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);

            } finally {
                try {
                    if (connection != null)
                        connection.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsString = sw.toString();
                    Logger.log(exceptionAsString);
                }
            }
        }
    }


    @FXML
    void Id_Emp(MouseEvent event) throws Exception {

        int i = 0;
        try {
            connection = DBConnect.getConnection();
            String empIdIs = "";
            Statement stm1 = connection.createStatement();

            ResultSet rs = stm1.executeQuery("select max(empid) as id from employee");
            while (rs.next()) {
                empIdIs = rs.getString("id");
                i++;
            }
            if (empIdIs != null && i > 0) {

                incidArray = (Integer.parseInt(empIdIs) + 1);
                EmpId.setText(String.valueOf(incidArray));

            } else {
                EmpId.setText(String.valueOf(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);
        } finally {
            try {
                if (connection != null)
                    connection.close();

            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);
            }
        }
    }


    @FXML
    void Reset_Employee(ActionEvent event) {
        refresh_empoyee();
    }

    void refresh_empoyee() {
        EmpId.clear();
        UserName.clear();
        Password.clear();
        Confpassword.clear();
        EmailId.clear();
        MobileNo.clear();
        Empaddress.clear();
    }

    @FXML
    void ViewAllEmployee(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("reg.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        System.out.println("Employee details");
        Stage stage = new Stage();
        stage.setTitle("Employee Details");
        stage.setScene(new Scene(root1, 900, 700));
        stage.show();
    }

    //End Of Employee List

    //Start of Add Stock List
    @FXML
    private AnchorPane Middlepane2;

    @FXML
    private TextField ProductCode;

    @FXML
    private TextField Hsn;

    @FXML
    private DatePicker StockDate;

    @FXML
    private TextField StockName;

    @FXML
    private TextField Quantity;

    @FXML
    private TextField Purchaseprice;

    @FXML
    private TextField MRP;

    @FXML
    private TextField stockingpriceto;

    ZoneId zoneId = ZoneId.of("Indian/Maldives");
    LocalDate today = LocalDate.now(zoneId);

    @FXML
    void AddStock(ActionEvent event) throws SQLException, ClassNotFoundException {
        Middlepane1.setVisible(false);
        Middlepane2.setVisible(true);
        Middlepane4.setVisible(false);
        Middlepane5.setVisible(false);
        Middlepane6.setVisible(false);
        profilePane.setVisible(false);
        barcode_pane.setVisible(false);
        dashboard.setVisible(false);

        gstload();
        StockDate.setValue(today);
        Quantity.setText("0");
        MRP.setText("0");
        Purchaseprice.setText("0");
        s_price.setText("0");
        p_discount.setText("0");
    }

    void gstload() throws SQLException, ClassNotFoundException {
        try {
            connection = DBConnect.getConnection();
            totalgst.getItems().removeAll(totalgst.getItems());
            String query1 = "select sum(amount) from gst group by amount order by amount ASC";
            ArrayList<Double> gstList = new ArrayList<Double>();
            ResultSet set = connection.createStatement().executeQuery(query1);
            while (set.next()) {
                gstList.add(set.getDouble(1));
            }

            for (int a = 0; a < gstList.size(); a++) {
                double a1 = gstList.get(a);
                for (int b = a + 1; b < gstList.size(); b++) {
                    double b1 = gstList.get(b);
                    if (a1 > b1) {
                        double temp = a1;
                        a1 = b1;
                        b1 = temp;
                        gstList.set(a, a1);
                        gstList.set(b, b1);
                    }
                }
            }

            for (int i = 0; i < gstList.size(); i++) {
                totalgst.getItems().add(gstList.get(i));
            }
            if (gstList.size() > 0) {
                totalgst.setValue(gstList.get(0));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void view_invoice_details(KeyEvent keyEvent) throws Exception {

        view_stock_details();

    }

    void view_stock_details() throws Exception {

        String invoice = "";
        if (purchase_invoice.getText().trim().isEmpty()) {
            invoice = "0";
        } else {
            invoice = purchase_invoice.getText().trim();
        }
        String query = "Select " +
                "id as 'ID', p_invoice as 'PURCHASE INVOICE',productcode as 'PRODUCT CODE', hsn as 'HSN', stockaddeddate as 'STOCK ADDED DATE', " +
                "productname as 'PRODUCT NAME', stockquantity as 'AVAILABLE QUANTITY', added_quantity as 'ADDED QUANTITY', packing as 'PACKING', purchaseprice as 'PURCHASE PRICE', " +
                "mrp as 'MRP', cgst as 'CGST', sgst as 'SGST', dealer_name as 'DEALER NAME', " +
                "purchase_discount as 'DISCOUNT', selling_price as 'SELLING PRICE' , total_amount as 'TOTAL' from stock where p_invoice='" + invoice + "'";
        LoadingTableViewDataSelectedRowName.Welcome(query, view_details, 170, 754);
    }


    @FXML
    void Add_Stock(ActionEvent event) throws Exception {
        connection = DBConnect.getConnection();
        double gst = 0, s_quantity = 0, p_price = 0, mrp = 0, p_dis = 0, s_p = 0;
        String invoice = "No";
        int s = 0, g = 0;
        if (totalgst.getValue().equals("No")) {
            gst = 0.0;
        } else {
            gst = Double.parseDouble(totalgst.getValue().toString()) / 2;
        }
        if (StockName.getText().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER STOCK NAME");
            alert1.showAndWait();
        } else {
            s_quantity = Double.parseDouble(Quantity.getText().toString().trim());
            p_price = Double.parseDouble(Purchaseprice.getText().toString().trim());
            mrp = Double.parseDouble(MRP.getText().toString().trim());
            p_dis = Double.parseDouble(p_discount.getText().trim());
            s_p = Double.parseDouble(s_price.getText().trim());
            invoice = purchase_invoice.getText().trim();

            try {
                String query1 = "Insert into stock(productcode, hsn, stockaddeddate, productname, stockquantity," +
                        "added_quantity, purchaseprice, mrp, cgst, sgst," +
                        "total_amount, dealer_name, purchase_discount, selling_price, p_invoice, " +
                        " packing ) " +
                        "values(?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,?   ,?)";

                PreparedStatement preparedStatement = connection.prepareStatement(query1);

                preparedStatement.setString(1, ProductCode.getText().trim());
                preparedStatement.setString(2, Hsn.getText().trim());
                preparedStatement.setString(3, StockDate.getValue() + "");
                preparedStatement.setString(4, StockName.getText().trim().toUpperCase());
                preparedStatement.setString(5, s_quantity + "");
                preparedStatement.setString(6, s_quantity + "");
                preparedStatement.setString(7, P.df00(p_price));
                preparedStatement.setString(8, P.df00(mrp));
                preparedStatement.setString(9, P.df00(gst));
                preparedStatement.setString(10, P.df00(gst));
                preparedStatement.setString(11, P.df00(stockingpriceto.getText().trim()));
                preparedStatement.setString(12, dealerSearch.getText().trim());
                preparedStatement.setString(13, P.df00(p_dis));
                preparedStatement.setString(14, P.df00(s_p));
                preparedStatement.setString(15, invoice);
                preparedStatement.setString(16, packing.getText().trim().toUpperCase());

                P.p("insert---->" + preparedStatement);

                int i = preparedStatement.executeUpdate();

                if (i > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("STOCK INFORMATION ADDED SUCCESSFULLY");
                    alert.showAndWait();
                    int dPiece = 0;
                    try {
                        dPiece = (int) Double.parseDouble(dummyPiece.getText().trim());
                    } catch (Exception e) {
                    }
                    createPdf(
                            brand_name2.getText().trim(),
                            StockName.getText().trim(),
                            ProductCode.getText().trim(),
                            packing.getText().trim(),
                            mrpCode.getText().trim(),
                            s_price.getText().trim(),
                            (int) Double.parseDouble((Quantity.getText().trim())),
                            dPiece
                    );

                    stock_fileld_refresh();
                    view_stock_details();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN SAVING STOCK INFORMATION");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);

            } finally {
                try {
                    if (connection != null)
                        connection.close();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
    }


    @FXML
    void refreshBill(ActionEvent event) throws SQLException, ClassNotFoundException {
        stock_fileld_refresh();
        purchase_invoice.clear();
        dealerSearch.clear();
        StockDate.setValue(today);
    }

    void stock_fileld_refresh() throws SQLException, ClassNotFoundException {
        gstload();
        ProductCode.clear();
        Hsn.clear();
        StockName.clear();
        Quantity.setText("0");
        MRP.setText("0");
        Purchaseprice.setText("0");
        s_price.setText("0");
        p_discount.setText("0");
        stockingpriceto.clear();
        p_id.clear();
        packing.clear();
        dummyPiece.clear();
        mrpCode.clear();
    }


    @FXML
    void EditStock(ActionEvent event) throws Exception {
        connection = DBConnect.getConnection();
        try {

            ObservableList oa = LoadingTableViewDataSelectedRowName.selectItem();

            ArrayList aa = new ArrayList();
            aa.add(oa.get(0));

            ArrayList newArray = new ArrayList();
            newArray = aa;
            String old = String.valueOf(newArray.get(0));
            ArrayList<String> bb = new ArrayList<String>();

            old = old.replace("[", "");
            old = old.replace("]", "");

            String log[] = old.split(",");
            String log1[] = old.split(",");

            bb.add(log1[0].trim());
            int check = 0;
            try {
                bb.add(log1[1].trim());
            } catch (Exception e) {

                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("PLEASE SELECT STOCK FROM SELECTION TABLE");
                a.showAndWait();
                check++;
            }

            if (check == 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to Edit stock record");
                Optional<ButtonType> result = alert.showAndWait();

                if ((result.isPresent()) && (result.get() == ButtonType.OK)) {


                    String selectingQueryForQuantity = "Select * from stock " +
                            "WHERE id='" + bb.get(0) + "'";
                    double oldQuantityTaken = 0, a_quantity = 0,oldtotal=0,final_total = 0,final_total1=0;

                    ResultSet selectingQueryForQuantityRs = connection.createStatement().executeQuery(selectingQueryForQuantity);

                    while (selectingQueryForQuantityRs.next()) {
                        oldQuantityTaken = Double.parseDouble(selectingQueryForQuantityRs.getString("stockquantity"));
                        a_quantity = Double.parseDouble(selectingQueryForQuantityRs.getString("added_quantity"));
                        oldtotal = Double.parseDouble(selectingQueryForQuantityRs.getString("total_amount"));
                        final_total1 = Double.parseDouble(stockingpriceto.getText());
                        final_total = final_total1+oldtotal;
                    }

                       double newQuantity = oldQuantityTaken + Double.parseDouble(Quantity.getText().trim());
                       double new_quantity2 = a_quantity + Double.parseDouble(Quantity.getText().trim());

                    double gst = Double.parseDouble(totalgst.getValue().toString()) / 2;

                    PreparedStatement ps = connection.prepareStatement("update stock set " +
                            "productcode='" + ProductCode.getText().trim() + "',hsn='" + Hsn.getText().trim() + "'," +
                            "stockaddeddate='" + StockDate.getValue() + "', productname='" + StockName.getText().trim().trim() + "'," +
                            "stockquantity='" + newQuantity + "', added_quantity='" + new_quantity2 + "'," +
                            "purchaseprice='" + P.df00(Purchaseprice.getText().trim()) + "',mrp='" + P.df00(MRP.getText().trim()) + "'," +
                            "cgst='" + P.df00(gst) + "',sgst='" + P.df00(gst) + "'," +
                            "total_amount='" + P.df00(final_total) + "'," +
                            "dealer_name='" + dealerSearch.getText().trim() +
                            "',purchase_discount='" + P.df00(p_discount.getText().trim()) + "', " +
                            "selling_price='" + P.df00(s_price.getText().trim()) + "', " +
                            "packing='" + packing.getText().trim().trim() + "', " +
                            "p_invoice='" + purchase_invoice.getText().trim() + "' " +
                            " where id='" + bb.get(0) + "'");

                    int i = ps.executeUpdate();

                    P.p("stock---" + ps);


                    if (i > 0) {
                        alert.setContentText("STOCK INFORMATION UPDATED");
                        alert.showAndWait();

                        stock_fileld_refresh();
                        view_stock_details();
                    } else {
                        Alert alertE = new Alert(Alert.AlertType.ERROR);
                        alertE.setContentText("ERROR IN UPDATION");
                        alertE.showAndWait();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);

        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void Stock_Alert(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("reg2.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("stock_alerts");
        stage.setScene(new Scene(root1, 900, 700));
        stage.show();
    }

    @FXML
    void Stock_Delete(ActionEvent event) throws Exception {
        if (purchase_invoice.getText().trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("PLEASE ENTER PURCHASE INVOICE NUMBER");
            a.showAndWait();
        } else {


            ObservableList oa = LoadingTableViewDataSelectedRowName.selectItem();

            ArrayList aa = new ArrayList();
            aa.add(oa.get(0));

            ArrayList newArray = new ArrayList();
            newArray = aa;
            String old = String.valueOf(newArray.get(0));
            ArrayList<String> bb = new ArrayList<String>();

            old = old.replace("[", "");
            old = old.replace("]", "");

            String log[] = old.split(",");
            String log1[] = old.split(",");

            bb.add(log1[0].trim());
            int check = 0;
            try {
                bb.add(log1[1].trim());
            } catch (Exception e) {

                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("PLEASE SELECT STOCK FROM SELECTION TABLE");
                a.showAndWait();
                check++;
            }
            if (check == 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to delete stock record");
                Optional<ButtonType> result = alert.showAndWait();

                if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                    try {
                        connection = DBConnect.getConnection();
                        int p_code = Integer.parseInt(bb.get(0));
                        P.p("mitra--------->" + p_code);

                        String query = "delete from stock where id='" + p_code + "'";
                        PreparedStatement ps = connection.prepareStatement(query);
                        int i = ps.executeUpdate();

                        if (i > 0) {
                            Alert a = new Alert(Alert.AlertType.INFORMATION);
                            a.setContentText("STOCK INFORMATION DELETED SUCCESSFULLY");
                            a.showAndWait();
                            view_stock_details();
                        } else {
                            Alert a = new Alert(Alert.AlertType.ERROR);
                            a.setContentText("ERROR IN DELETING STOCK INFORMATION");
                            a.showAndWait();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String exceptionAsString = sw.toString();
                        Logger.log(exceptionAsString);
                    }
                }
            }
        }
    }

    @FXML
    void View_Stock(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("reg1.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        System.out.println("stock details");
        Stage stage = new Stage();
        stage.setTitle("Stock Details");
        stage.setScene(new Scene(root1, 900, 700));
        stage.show();
    }

    public static final LocalDate LOCAL_DATE(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }

    @FXML
    void Select_File(ActionEvent event) throws Exception {
        /*if (purchase_invoice.getText().trim().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("PLEASE ENTER PURCHASE INVOICE NUMBER");
            a.showAndWait();
        } else {*/
        ObservableList oa = LoadingTableViewDataSelectedRowName.selectItem();

        ArrayList aa = new ArrayList();
        aa.add(oa.get(0));

        ArrayList newArray = new ArrayList();
        newArray = aa;
        String old = String.valueOf(newArray.get(0));
        ArrayList<String> bb = new ArrayList<String>();

        old = old.replace("[", "");
        old = old.replace("]", "");

        String log[] = old.split(",");
        String log1[] = old.split(",");

        int check = 0;
        bb.add(log1[0].trim());
        try {
            bb.add(log1[1].trim());
        } catch (Exception e) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("PLEASE SELECT STOCK FROM SELECTION TABLE");
            a.showAndWait();
            check++;
        }
        if (check == 0) {
            try {
                int p_code = Integer.parseInt(bb.get(0).trim());

                connection = DBConnect.getConnection();
                String query = "Select * from stock where id='" + p_code + "'";
                ResultSet rs = connection.createStatement().executeQuery(query);
                double t_gst = 0;

                while (rs.next()) {

                    String id = rs.getString("id");
                    String productcodes = rs.getString("productcode");
                    String HSN = rs.getString("hsn");
                    String STOCKDATE = rs.getString("stockaddeddate");
                    String productnames = rs.getString("productname");
                    String stockquantitys = rs.getString("stockquantity");
                    String PURCHASEPRICE = rs.getString("purchaseprice");
                    String mrps = rs.getString("mrp");
                    String DEALER = rs.getString("dealer_name");
                    String TOTAL = rs.getString("total_amount");
                    double cgst = rs.getDouble("cgst");
                    double sgst = rs.getDouble("sgst");
                    String p_discount1 = rs.getString("purchase_discount");
                    String s_price1 = rs.getString("selling_price");
                    String p_invoice = rs.getString("p_invoice");
                    String packing_type = rs.getString("packing");

                    t_gst = cgst + sgst;

                    p_id.setText(id);
                    ProductCode.setText(productcodes);
                    Hsn.setText(HSN);
                    StockDate.setValue(LOCAL_DATE(STOCKDATE));
                    StockName.setText(productnames);
                    Quantity.setText(stockquantitys);
                    Purchaseprice.setText(PURCHASEPRICE);
                    totalgst.setValue(t_gst);
                    MRP.setText(mrps);
                    dealerSearch.setText(DEALER);
                    stockingpriceto.setText(TOTAL);
                    p_discount.setText(p_discount1);
                    s_price.setText(s_price1);
                    purchase_invoice.setText(p_invoice);
                    packing.setText(packing_type);

                }

            } catch (Exception e) {
                e.printStackTrace();
                P.loggerLoader(e);
            } finally {
                try {
                    if (connection != null)
                        connection.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //  }

    @FXML
    void MakeStockLiveSearch(KeyEvent event) throws Exception {
        AutoCompletionBinding<String> autoCompletionBindings = TextFields.bindAutoCompletion(StockName, LiveSearchMe.makeSearch("select DISTINCT(productname) from stock", "productname"));
    }

    @FXML
    void stockingPriceInTotal(KeyEvent event) throws Exception {
        calculate();

    }
    void calculate()
    {
    double quantity = 0, p_price = 0, t_gst = 0, discount = 0;

        if(!Purchaseprice.getText().trim().isEmpty())

    {
        p_price = Double.parseDouble(Purchaseprice.getText());
    }
        if(!Quantity.getText().trim().isEmpty())

    {
        quantity = Double.parseDouble(Quantity.getText());
    }
        if(!p_discount.getText().

    trim().

    isEmpty())

    {
        discount = Double.parseDouble(p_discount.getText().trim());
    }

    t_gst =Double.parseDouble(totalgst.getValue().

    toString());

    double amount = quantity * p_price;
    double d_amount = amount * discount / 100;
    double taxable_amount = amount - d_amount;
    double gst_amount = taxable_amount * t_gst / 100;
    double total_amount = taxable_amount + gst_amount;
        stockingpriceto.setText(P.df00(total_amount));

        P.p("p_price   :"+p_price);
        P.p("quantity  :"+quantity);
        P.p("amount   :"+amount);
        P.p("discount   :"+discount);
        P.p("d_amount   :"+d_amount);
        P.p("taxable_amount   :"+taxable_amount);
        P.p("t_gst   :"+t_gst);
        P.p("gst_amount   :"+gst_amount);
        P.p("total_amount   :"+total_amount);
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
    void exportstockxls(ActionEvent event) throws Exception {

        String query = "Select * from stock";
        File dir = new File(P.drive_name() + FilePath.FOLDER_PATH);
        dir.mkdir();
        File dir2 = new File(P.drive_name() + FilePath.FOLDER_PATH + "EXPORT");
        dir2.mkdir();
        String path = P.drive_name() + FilePath.FOLDER_PATH + "/EXPORT/STOCK.csv";
        export_excel(query, path);
    }

    @FXML
    void importToDatabase(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stock_import_page.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    public void returnBill(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("return.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }


    //End of add stock

    //customer start...

    @FXML
    private AnchorPane Middlepane6;

    @FXML
    private TextField CustomerId;

    @FXML
    private TextField CustomerNamesAdd;

    @FXML
    private TextField CustomerPhone;

    @FXML
    private TextField CustomerState;

    @FXML
    private TextField StateCode;

    @FXML
    private TextField GSTINNumber;

    @FXML
    private TextField CustomerAddress;


    @FXML
    void Add_Customer(ActionEvent event) throws Exception {
        Middlepane1.setVisible(false);
        Middlepane2.setVisible(false);
        Middlepane4.setVisible(false);
        Middlepane5.setVisible(false);
        Middlepane6.setVisible(true);
        profilePane.setVisible(false);
        barcode_pane.setVisible(false);
        dashboard.setVisible(false);

    }


    @FXML
    void AddCustomers(ActionEvent event) throws Exception, IOException {

        connection = DBConnect.getConnection();
        int k = 0;
        String query = "select phoneno from customer where phoneno='" + CustomerPhone.getText().trim() + "'";
        ResultSet rs = connection.createStatement().executeQuery(query);
        while (rs.next()) {
            k++;
        }

        if (CustomerNamesAdd.getText().isEmpty()) {

            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER CUSTOMER NAME");
            alert1.showAndWait();
        } else if (CustomerPhone.getText().isEmpty() || CustomerPhone.getText().trim().length()!=10 ) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER VALID CONTACT NUMBER");
            alert1.showAndWait();
        } else if (k > 0) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("CUSTOMER PHONE NUMBER ALREADY EXIST");
            alert1.showAndWait();
        } else {
            try {

                String query1 = "Insert into customer(customerid, customername, phoneno," +
                        "state_statecode, gstinno, customeraddress, balance,  extra ) " +
                        "values( ?,?,?,?,?,    ?,?,? )";

                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);

                preparedStatement1.setString(1, CustomerId.getText().trim());
                preparedStatement1.setString(2, CustomerNamesAdd.getText().trim());
                preparedStatement1.setString(3, CustomerPhone.getText().trim());
                preparedStatement1.setString(4, CustomerState.getText().trim());
                preparedStatement1.setString(5, GSTINNumber.getText().trim());
                preparedStatement1.setString(6, CustomerAddress.getText().trim());
                preparedStatement1.setString(7, "0");
                preparedStatement1.setString(8, "0");

                int i = preparedStatement1.executeUpdate();

                if (i > 0) {
                    AlertMessage.inform("CUSTOMER INFORMATION ADDED SUCCESSFULLY");
                    refresh_customer();
                } else {
                    AlertMessage.error("ERROR IN SAVING CUSTOMER INFORMATION");
                }
            } catch (Exception e) {
                e.printStackTrace();
                P.loggerLoader(e);
            } finally { if (connection != null) connection.close(); }
        }
    }

    @FXML
    void CustomerRefresh(ActionEvent event) throws Exception {
        refresh_customer();
    }


    void refresh_customer() throws Exception {
        CustomerId.clear();
        CustomerNamesAdd.clear();
        CustomerPhone.clear();
        CustomerAddress.clear();
        CustomerState.clear();
        GSTINNumber.clear();
    }

    @FXML
    void CustomerViewing(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("reg4.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        System.out.println("Customer details");
        Stage stage = new Stage();
        stage.setTitle("Customer_details");
        stage.setScene(new Scene(root1, 900, 700));
        stage.show();
    }

    @FXML
    void AddCustomerId(MouseEvent event) throws Exception {
        try {
            connection = DBConnect.getConnection();

            String customerId = "";
            Statement stm1 = connection.createStatement();

            ResultSet rs = stm1.executeQuery("select max(customerid) from customer");

            while (rs.next()) {
                customerId = rs.getString("max(customerid)");

            }
            incidArray = customerId == null ? 1 : (Integer.parseInt(customerId) + 1);
            CustomerId.setText(String.valueOf(incidArray));

        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);
        } finally {
            if (connection != null)
                connection.close();
        }
    }


// customer end

//dealer start

    @FXML
    private AnchorPane Middlepane5;

    @FXML
    private TextField DealerNo;

    @FXML
    private TextField DealerName;

    @FXML
    private TextField DealerPhone;

    @FXML
    private TextField DealerAddedProductName;

    @FXML
    private TextField DealerAddedQuantity;

    @FXML
    private TextArea DealerAddedPrice;

    @FXML
    private TextArea DealerAddedAddress;

    @FXML
    void Add_Dealer(ActionEvent event) {
        Middlepane1.setVisible(false);
        Middlepane2.setVisible(false);
        Middlepane4.setVisible(false);
        Middlepane5.setVisible(true);
        Middlepane6.setVisible(false);
        profilePane.setVisible(false);
        barcode_pane.setVisible(false);
        dashboard.setVisible(false);
        dealerPayementDate.setValue(today);
    }

    @FXML
    void AddDealers(ActionEvent event) throws Exception {
        connection = DBConnect.getConnection();
        ArrayList<String> dealer_list = new ArrayList<String>();
        int k = 0;
        String query = "select dealer_name from dealer";
        ResultSet rs = connection.createStatement().executeQuery(query);
        while (rs.next()) {
            dealer_list.add(rs.getString(1));
        }
        for (int j = 0; j < dealer_list.size(); j++) {
            if (DealerName.getText().trim().equalsIgnoreCase(dealer_list.get(j))) {
                k++;
                break;
            }
        }
        if (DealerName.getText().isEmpty()) {

            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER DEALER NAME");
            alert1.showAndWait();
        } else if (DealerPhone.getText().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER CONTACT NUMBER");
            alert1.showAndWait();
        } else if (k > 0) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("DEALER NAME ALREADY EXIST");
            alert1.showAndWait();
        } else {
            try {

                String query1 = "Insert into dealer(dealer_id , dealer_name , dealer_gstn_number , " +
                        "dealer_contact_number, dealer_pan, dealer_account_details, dealer_address) " +
                        "values ( ?,?,?,?,?,   ?,? )";

                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);

                preparedStatement1.setString(1, DealerNo.getText().trim());
                preparedStatement1.setString(2, DealerName.getText().trim());
                preparedStatement1.setString(3, DealerAddedProductName.getText().trim());
                preparedStatement1.setString(4, DealerPhone.getText().trim());
                preparedStatement1.setString(5, DealerAddedQuantity.getText().trim());
                preparedStatement1.setString(6, DealerAddedPrice.getText().trim());
                preparedStatement1.setString(7, DealerAddedAddress.getText().trim());

                int i = preparedStatement1.executeUpdate();

                if (i > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("DEALER INFORMATION ADDED SUCCESSFULLY");
                    alert.showAndWait();

                    dealer_refresh();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN SAVING DEALER INFORMATION");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);

            } finally {
                if (connection != null)
                    connection.close();
            }
        }
    }

    @FXML
    void makeLiveSearchaForDealer(KeyEvent event) throws Exception {
        AutoCompletionBinding<String> autoCompletionBindings = TextFields.bindAutoCompletion(dealerSearch, LiveSearchMe.makeSearch("select DISTINCT(dealer_name) from dealer", "dealer_name"));
    }

    @FXML
    void Refresh_Dealer(ActionEvent event) {
        dealer_refresh();
    }

    void dealer_refresh() {
        DealerNo.clear();
        DealerName.clear();
        DealerPhone.clear();
        DealerAddedProductName.clear();
        DealerAddedQuantity.clear();
        DealerAddedPrice.clear();
        DealerAddedAddress.clear();
    }

    @FXML
    void DealerNoAdded(MouseEvent event) throws Exception {

        try {
            connection = DBConnect.getConnection();
            Statement stm1 = connection.createStatement();
            String dealerCount = "";
            ResultSet rs = stm1.executeQuery("select max(dealer_id) from dealer");
            System.out.println(idArray);
            while (rs.next()) {
                dealerCount = rs.getString("max(dealer_id)");

            }

            incidArray = dealerCount == null ? 1 : (Integer.parseInt(dealerCount) + 1);
            DealerNo.setText(String.valueOf(incidArray));
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);

        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @FXML
    void DealerView_all(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("reg3.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        System.out.println("Dealer details");
        Stage stage = new Stage();
        stage.setTitle("Dealer_details");
        stage.setScene(new Scene(root1, 900, 700));
        stage.show();
    }


//dealer end

//GST Start

    @FXML
    private AnchorPane Middlepane4;

    @FXML
    private TextField GstAmount;

    @FXML
    private Pane Middlepane44;


    @FXML
    void Add_Gst(ActionEvent event) throws Exception {
        Middlepane1.setVisible(false);
        Middlepane2.setVisible(false);
        Middlepane4.setVisible(true);
        Middlepane5.setVisible(false);
        Middlepane6.setVisible(false);
        profilePane.setVisible(false);
        barcode_pane.setVisible(false);
        dashboard.setVisible(false);

        viewgst();
    }

    @FXML
    void AddingGst(ActionEvent event) throws Exception {
        connection = DBConnect.getConnection();
        double gst = Double.parseDouble(GstAmount.getText().trim()) / 2;

        int k = 0;
        String query = "Select * from gst where amount='" + gst + "'";
        ResultSet rs = connection.createStatement().executeQuery(query);
        while (rs.next()) {
            k++;
        }

        if (GstAmount.getText().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("ENTER GST AMOUNT");
            alert1.showAndWait();
        } else if (k > 0) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("GST AMOUNT ALREADY EXIST");
            alert1.showAndWait();
        } else {
            //for cgst
            String query1 = "Insert into gst(type,amount) values(?,?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setString(1, "CGST");
            preparedStatement1.setString(2, gst + "");
            int i = preparedStatement1.executeUpdate();
            //for sgst
            String query2 = "Insert into gst(type,amount) values(?,?)";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setString(1, "SGST");
            preparedStatement2.setString(2, gst + "");
            int j = preparedStatement2.executeUpdate();

            if (i > 0 && j > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("ADDED SUCCESSFULLY");
                alert.showAndWait();

                GstAmount.clear();
                viewgst();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ERROR IN ADDING GST AMOUNT");
                alert.showAndWait();
            }

            if (connection != null)
                connection.close();
        }
    }

    @FXML
    void DeletingGst(ActionEvent event) throws Exception {
        if (GstAmount.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ENTER TOTAL GST AMOUNT");
            alert.showAndWait();
        } else {
            try {
                double gst = Double.parseDouble(GstAmount.getText()) / 2;
                connection = DBConnect.connect();
                String query = "delete from gst where amount='" + gst + "'";
                PreparedStatement ps = connection.prepareStatement(query);
                int i = ps.executeUpdate();
                if (i > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("DELETED SUCCESSFULLY");
                    alert.showAndWait();

                    GstAmount.clear();
                    viewgst();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ENTER VALID GST");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);

            } finally {
                if (connection != null)
                    connection.close();
            }
        }

    }

    void viewgst() throws Exception {
        String queryIs = "SELECT * FROM gst";
        LoadingDataToTableView.Welcome(queryIs, Middlepane44);
    }

    //GST end


    @FXML
    void Billing(ActionEvent event) throws Exception {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Billing.fxml"));
            Scene scene = new Scene(root, 1000, 700);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);
        }
    }

    @FXML
    void MedicalInventory(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("inventory_M.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

//    @FXML
//    void Inventory(ActionEvent event) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("inventory_file.fxml"));
//        Parent root1 = (Parent) fxmlLoader.load();
//        Stage stage = new Stage();
//        stage.setScene(new Scene(root1));
//        stage.show();
//    }


    public void view_chart_method(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chart_view.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }


    @FXML
    void createProfile(ActionEvent event) throws Exception {
        Middlepane1.setVisible(false);
        Middlepane2.setVisible(false);
        Middlepane4.setVisible(false);
        Middlepane5.setVisible(false);
        Middlepane6.setVisible(false);
        profilePane.setVisible(true);
        barcode_pane.setVisible(false);
        dashboard.setVisible(false);

        try {
            connection = DBConnect.connect();
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery("select * from profile ");
            String l_no="",pan_no="";

            while (resultSet.next()) {

                profileImageStringpdf = resultSet.getString("logo");
                mailAddressPdf1 = resultSet.getString("name");
                mailAddressPdf2 = resultSet.getString("mail_addres");
                emailidPdf3 = resultSet.getString("comemailid");
                contactpdf = resultSet.getString("contact");
                gstinpdf = resultSet.getString("gstin");
                l_no = resultSet.getString("licence_no");
                acc_namepdf = resultSet.getString("acc_name");
                acc_numpdf = resultSet.getString("acc_num");
                acc_typepdf = resultSet.getString("acc_type");
                ifscpdf = resultSet.getString("ifsc");
                pan_no = resultSet.getString("pan_no");
                termspdf = resultSet.getString("terms");
                declarationpdf = resultSet.getString("declaration");
                jurisidictionpdf = resultSet.getString("jurisidiction");
            }

            Image image1 = new Image(profileImageStringpdf, 1000, 1500, true, true);

            buname.setText(mailAddressPdf1);
            mail.setText(mailAddressPdf2);
            imageView.setImage(image1);
            buemailid.setText(emailidPdf3);
            contactnum.setText(contactpdf);
            gstin.setText(gstinpdf);
            licence.setText(l_no);
            bank_aa_name.setText(acc_namepdf);
            acc_num.setText(acc_numpdf);
            acc_type.setText(acc_typepdf);
            ifsc.setText(ifscpdf);
            pancard_no.setText(pan_no);
            terms.setText(termspdf);
            declaration.setText(declarationpdf);
            jurisid.setText(jurisidictionpdf);


        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);

        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @FXML
    void logoutbacktologin(MouseEvent event) throws Exception {
        ((Node) event.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    //profile generation
    @FXML
    private ImageView imageView;

    String imageNameString;
    String newName;

    @FXML
    void chooseFileFromThisPage(ActionEvent event) throws Exception {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ImageFile", "*.*"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            newName = "file:///" + selectedFile.getAbsolutePath();
            System.out.print(newName);
            Image image = new Image(newName, 1000, 1500, true, true);
            imageView.setImage(image);
        }
    }

    @FXML
    private TextField buname;

    @FXML
    private TextField mail;

    @FXML
    private TextField gstin;

    @FXML
    private TextField ifsc;

    @FXML
    private TextField acc_type;

    @FXML
    private TextField bank_aa_name;

    @FXML
    private TextField acc_num;

    @FXML
    private TextArea terms;

    @FXML
    private TextField jurisid;

    @FXML
    private TextField buemailid;

    @FXML
    private TextField contactnum;

    @FXML
    void profile_creator(ActionEvent event) throws Exception {
        int count = 0;
        String busName = buname.getText();
        String mailAddress = mail.getText();
        String emailId = buemailid.getText();
        String contactNumber = contactnum.getText();
        String gstNumber = gstin.getText();
        String accName = bank_aa_name.getText();
        String accNum = acc_num.getText();
        String accType = acc_type.getText();
        String ifsc1 = ifsc.getText();
        String terms1 = terms.getText();
        String declaration1 = declaration.getText();
        String jury = jurisid.getText();

        imageNameString = newName;
        connection = DBConnect.getConnection();
        String query = "SELECT * FROM profile";
        ResultSet rs = connection.createStatement().executeQuery(query);
        while (rs.next()) {
            count++;
        }
        if (count > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("PROFILE ALREADY CREATED UPDATE PREVIOUS DATA");
            alert.showAndWait();
        } else {
            try {
                String insertQuery = "INSERT into profile(name, mail_addres, logo, comemailid, " +
                        "contact, gstin, licence_no, acc_name, acc_num, acc_type, ifsc, pan_no, terms, declaration, jurisidiction)  " +
                        "VALUES(?,?,?,?,?,   ?,?,?,?,?,  ?,?,?,?,?)";

                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                preparedStatement.setString(1, busName);
                preparedStatement.setString(2, mailAddress);
                preparedStatement.setString(3, imageNameString);
                preparedStatement.setString(4, emailId);
                preparedStatement.setString(5, contactNumber);
                preparedStatement.setString(6, gstNumber);
                preparedStatement.setString(7, licence.getText().trim());
                preparedStatement.setString(8, accName);
                preparedStatement.setString(9, accNum);
                preparedStatement.setString(10, accType);
                preparedStatement.setString(11, ifsc1);
                preparedStatement.setString(12, pancard_no.getText().trim());
                preparedStatement.setString(13, terms1);
                preparedStatement.setString(14, declaration1);
                preparedStatement.setString(15, jury);

                int i = preparedStatement.executeUpdate();
                if (i > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("DATA SAVED SUCCESSFULY");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN SAVING PROFILE INFORMATION");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);

            } finally {
                if (connection != null)
                    connection.close();
            }
        }
    }

    @FXML
    void update_pro(ActionEvent event) throws Exception {
        try {
            String updateProfileQuery = "";
            connection = DBConnect.connect();
            System.out.println("image is=========" + newName);
            if (newName == null) {
                updateProfileQuery = "UPDATE profile SET name='" + buname.getText() + "',mail_addres='" + mail.getText() + "' ,comemailid='" + buemailid.getText() + "'," +
                        "contact='" + contactnum.getText() + "',gstin='" + gstin.getText() + "',licence_no='"+licence.getText()+"'," +
                        "acc_name='" + bank_aa_name.getText() + "',acc_num='" + acc_num.getText() + "',acc_type='" + acc_type.getText() + "'," +
                        "ifsc='" + ifsc.getText() + "',pan_no='"+pancard_no.getText()+"',terms='" + terms.getText() + "'," +
                        "declaration='" + declaration.getText() + "',jurisidiction='" + jurisid.getText() + "'";
            } else {
                newName = newName.replace("\\", "\\\\");
                updateProfileQuery = "UPDATE profile SET name='" + buname.getText() + "',mail_addres='" + mail.getText() + "'," +
                        "logo='" + newName + "',comemailid='" + buemailid.getText() + "',contact='" + contactnum.getText() + "'," +
                        "gstin='" + gstin.getText() + "',licence_no='"+licence.getText()+"',acc_name='" + bank_aa_name.getText() + "'," +
                        "acc_num='" + acc_num.getText() + "',acc_type='" + acc_type.getText() + "',ifsc='" + ifsc.getText() + "'," +
                        "pan_no='"+pancard_no.getText()+"',terms='" + terms.getText() + "',declaration='" + declaration.getText() + "'," +
                        "jurisidiction='" + jurisid.getText() + "'";
            }

            PreparedStatement ps = connection.prepareStatement(updateProfileQuery);
            int i = ps.executeUpdate();

            if (i > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("PROFILE INFORMATION UPDATED");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ERROR IN UPDATING PROFILE DATA");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);

        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }


    @FXML
    void changePassWord(MouseEvent event) throws Exception {
        ((Node) event.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample2.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    private TextField text11;

    @FXML
    private PasswordField text22;

    @FXML
    void update(ActionEvent event) throws Exception {

        if (text11.getText().isEmpty() || text22.getText().isEmpty()) {

            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER USERNAME AND PASSWORD");
            alert1.showAndWait();
        } else {
            try {

                connection = DBConnect.getConnection();

                String username = text11.getText();
                String password = text22.getText();

                PreparedStatement ps = connection.prepareStatement("update  login set Username='" + username + "' ,  Password='" + password + "' where Type='Admin'");

                int i = ps.executeUpdate();

                if (i > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("USERNAME AND PASSWORD UPDATED SUCCESSFULY");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN UPDATION ENTER CURRECT INFORMATION");
                    alert.showAndWait();
                }

                final Node source = (Node) event.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                stage.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                System.out.println("stock details");
                Stage stage1 = new Stage();
                stage1.setTitle("stock_details");
                stage1.setScene(new Scene(root1, 900, 700));
                stage1.show();

            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Logger.log(exceptionAsString);

            } finally {
                if (connection != null)
                    connection.close();

            }
        }
    }
//barcode generation

    public void view_barcode(ActionEvent event) {
        Middlepane1.setVisible(false);
        Middlepane2.setVisible(false);
        Middlepane4.setVisible(false);
        Middlepane5.setVisible(false);
        Middlepane6.setVisible(false);
        profilePane.setVisible(false);
        barcode_pane.setVisible(true);
        dashboard.setVisible(false);
    }


    public void copynameToBarcode(KeyEvent keyEvent) {

        barcode_name.setText(product_name.getText());
    }

    public void generate_barcode(ActionEvent actionEvent) throws Exception {
        if (brand_name.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("PLEASE ENTER BRAND / COMPANY NAME");
            alert.showAndWait();
        } else if (product_name.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("PLEASE ENTER PRODUCT NAME ");
            alert.showAndWait();
        } else if (false) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("PRODUCT CODE SHOULD CONTAIN 10 CHARACTER ");
            alert.showAndWait();
        } else if (mrp.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("PLEASE ENTER MRP OF PRODUCT");
            alert.showAndWait();
        } else if (salespricewithtax.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("PLEASE ENTER OUR PRICE OF PRODUCT");
            alert.showAndWait();
        } else if (no_of_times.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("PLEASE ENTER NUMBER OF STICKERS");
            alert.showAndWait();
        } else {
            createPdf();
            //clear fields
            brand_name.clear();
            product_name.clear();
            barcode_name.clear();
            mrp.clear();
            QUANTITY.clear();
            salespricewithtax.clear();
            no_of_times.clear();
        }
    }

    public void createPdf() throws Exception
    {
        try {
            Document document = new Document();

            new File(P.drive_name() + FilePath.FOLDER_PATH).mkdir();
            new File(P.drive_name() + FilePath.FOLDER_PATH + "BARCODE").mkdir();

            String path1 = P.drive_name() + FilePath.FOLDER_PATH + "/BARCODE/";
            String name = product_name.getText().trim();

            String FILE = path1 + "/" + name + ".pdf";

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
            float sizew = PageSize.A4.getWidth();
            Rectangle page = new Rectangle(sizew, PageSize.A4.getHeight());
            document.setPageSize(PageSize.A4);
            document.setMargins(7, 7, 3, 3);
            document.open();

            Font font2_bold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font font3 = new Font(Font.FontFamily.HELVETICA, 9);
            Font font3_bold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
            Font font4_bold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

            Phrase p1 = new Phrase();
            p1.add(new Chunk(brand_name.getText(), font2_bold));
            p1.add(new Chunk("\n" + product_name.getText(), font2_bold));
            PdfPCell cell_4 = new PdfPCell(p1);

            cell_4.setPaddingLeft(7);
            cell_4.setPaddingRight(7);
            cell_4.setPaddingTop(10);


//        barcode generation code
            Barcode128 code128 = new Barcode128();
            code128.setSize(9);
            code128.setCode(barcode_name.getText());
            code128.setCodeType(Barcode128.CODE128);
            PdfContentByte cb = writer.getDirectContent();
            com.itextpdf.text.Image code128Image = code128.createImageWithBarcode(cb, null, null);

//        end of generation
            PdfPCell cell = new PdfPCell(code128Image);
            cell.setPaddingLeft(7);
            cell.setPaddingRight(7);

            Phrase p = new Phrase();
            p.add(new Chunk("SIZE  :", font3_bold));
            p.add(new Chunk(QUANTITY.getText().trim(), font3));
            p.add(new Chunk("\nMRP  : ", font4_bold));
            p.add(new Chunk(mrp.getText(), font4_bold));
            p.add(new Chunk("\nRATE :", font3_bold));
            p.add(new Chunk(salespricewithtax.getText(), font3_bold));
            PdfPCell cell_3 = new PdfPCell(p);
            cell_3.setPaddingLeft(7);
            cell_3.setPaddingRight(7);

            cell.setBorder(Rectangle.NO_BORDER);
            cell_3.setBorder(Rectangle.NO_BORDER);
            cell_4.setBorder(Rectangle.NO_BORDER);

            PdfPTable table1 = new PdfPTable(1);
            table1.addCell(cell_4);
            table1.addCell(cell);
            table1.addCell(cell_3);

            double cellH = (PageSize.A4.getHeight()-6)/6;

            int check = (Integer.parseInt(no_of_times.getText().trim()) % 5);
            P.p("mohana check 1 : " + check);
            PdfPTable table = new PdfPTable(5);
            table.setSplitLate(false);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);



            PdfPCell aa = new PdfPCell(new Phrase(" "));
            aa.setFixedHeight((float) cellH);
            aa.setBorder(Rectangle.NO_BORDER);

            int dPiece = 0;
            try{ dPiece = (int) Double.parseDouble(dummyCells.getText().trim());   }catch (Exception w){}


            for (int i=0; i<dPiece ; i++){
                table.addCell(aa);
            }

            aa = new PdfPCell(table1);
            aa.setFixedHeight((float) cellH);
            aa.setBorder(Rectangle.NO_BORDER);
            for (int b = 0; b < Integer.parseInt(no_of_times.getText().trim()); b++) {
                table.addCell(aa);
            }


            for (int i = (5-check); i >= 0; i--) {
                table.addCell(" ");
            }
            document.add(table);
            document.close();
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File(FILE);
                    Desktop.getDesktop().open(myFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createPdf(String brandname, String pName, String pCode, String size, String mrp, String sPrice, int nPiece, int  dPiece) throws Exception {
        try {
            P.p("ASnul 1 : "+brandname);
            P.p("ASnul 2 : "+pName);
            P.p("ASnul 3 : "+pCode);
            P.p("ASnul 4 : "+size);
            P.p("ASnul 5 : "+mrp);
            P.p("ASnul 6 : "+sPrice);
            P.p("ASnul 7 : "+nPiece);
            P.p("ASnul 8 : "+dPiece);


            Document document = new Document();

            new File(P.drive_name() + FilePath.FOLDER_PATH).mkdir();
            new File(P.drive_name() + FilePath.FOLDER_PATH + "BARCODE").mkdir();

            String path1 = P.drive_name() + FilePath.FOLDER_PATH + "BARCODE";
            String name = pName;

            String FILE = path1 + "/" + name /*+"("+LocalDateTime.now()+")"*/+ ".pdf";
            P.p("file1"+FILE);

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
            float sizew = PageSize.A4.getWidth();
            Rectangle page = new Rectangle(sizew, PageSize.A4.getHeight());
            document.setPageSize(PageSize.A4);
            document.setMargins(7, 7, 3, 3);
            document.open();

            Font font2_bold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font font3 = new Font(Font.FontFamily.HELVETICA, 9);
            Font font3_bold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
            Font font4_bold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

            Phrase p1 = new Phrase();
            p1.add(new Chunk(brandname, font2_bold));
            p1.add(new Chunk("\n" + name, font2_bold));
            PdfPCell cell_4 = new PdfPCell(p1);

            cell_4.setPaddingLeft(7);
            cell_4.setPaddingRight(7);
            cell_4.setPaddingTop(10);


//        barcode generation code
            Barcode128 code128 = new Barcode128();
            code128.setSize(9);
            code128.setCode(pCode);
            code128.setCodeType(Barcode128.CODE128);
            PdfContentByte cb = writer.getDirectContent();
            com.itextpdf.text.Image code128Image = code128.createImageWithBarcode(cb, null, null);

//        end of generation
            PdfPCell cell = new PdfPCell(code128Image);
            cell.setPaddingLeft(7);
            cell.setPaddingRight(7);

            Phrase p = new Phrase();
            p.add(new Chunk("\nSIZE  :", font3_bold));
            p.add(new Chunk(size, font3));
            p.add(new Chunk("\nMRP  : ", font4_bold));
            p.add(new Chunk(mrp, font4_bold));
            p.add(new Chunk("\nRATE :", font3_bold));
            p.add(new Chunk(sPrice, font3_bold));
            PdfPCell cell_3 = new PdfPCell(p);
            cell_3.setPaddingLeft(7);
            cell_3.setPaddingRight(7);

            cell.setBorder(Rectangle.NO_BORDER);
            cell_3.setBorder(Rectangle.NO_BORDER);
            cell_4.setBorder(Rectangle.NO_BORDER);

            PdfPTable table1 = new PdfPTable(1);
            table1.addCell(cell_4);
            table1.addCell(cell);
            table1.addCell(cell_3);


            int check = ((nPiece + dPiece) % 5);
            P.p("mohana check 1 : " + check);
            PdfPTable table = new PdfPTable(5);
            table.setSplitLate(false);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            double cellH = (PageSize.A4.getHeight()-6)/6;

            PdfPCell aa = new PdfPCell(new Phrase(" "));
            aa.setFixedHeight((float) cellH);
            aa.setBorder(Rectangle.NO_BORDER);

            P.p("mohana check 2 : " + nPiece);
            for (int i = 0; i < dPiece; i++) {
                table.addCell(aa);
                P.p("dummy cell "+(i+1));
            }

            aa = new PdfPCell(table1);
            aa.setFixedHeight((float) cellH);
            aa.setBorder(Rectangle.NO_BORDER);

            for (int b = 0; b < (nPiece); b++) {
                P.p("mohana check 3 : " + nPiece);
                table.addCell(aa);
            }
            System.out.println(" no_of_times.getText() : " + nPiece);
            System.out.println(nPiece + " % 2 : " + check);
            for (int i = (5-check); i >= 0; i--) {
                table.addCell(" ");
            }
            document.add(table);
            document.close();
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File(FILE);
                    Desktop.getDesktop().open(myFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public int errorFoundInExcelReading = 0;

    @FXML
    void importToDatabase_barcode(ActionEvent event) throws Exception {

        try {

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

                System.out.println("File Path = " + file);
                FileInputStream fis = new FileInputStream(file);
            }


            FileInputStream fis = new FileInputStream(new File(path));

            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            Row row;

            Document document = new Document();

            new File(P.drive_name() + FilePath.FOLDER_PATH).mkdir();
            new File(P.drive_name() + FilePath.FOLDER_PATH + "BARCODE").mkdir();


            String path1 = P.drive_name() + FilePath.FOLDER_PATH + "/BARCODE/";
            String name = product_name.getText().trim();

            String FILE = path1 + "/" + name + ".pdf";

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE));
            float sizew = PageSize.A4.getWidth();
            Rectangle page = new Rectangle(sizew, PageSize.A4.getHeight());
            document.setMargins(7,7,3,3);
            document.setPageSize(PageSize.A4);
            document.open();

            Font font2_bold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font font3 = new Font(Font.FontFamily.HELVETICA, 9);
            Font font3_bold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
            Font font4_bold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);


            PdfPTable table = new PdfPTable(5);
            table.setSplitLate(false);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);


            int totalStickers = 0;
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i + 1);
                System.out.print(" row number : " + row);
                //1
                String companyname = getCelldata(row, 0);
                if (companyname.trim().isEmpty()) {
                    errorFoundInExcelReading++;
                    P.p("error   1");
                }

                //2
                String Productname = getCelldata(row, 1);
                if (Productname.trim().isEmpty()) {
                    errorFoundInExcelReading++;
                    P.p("error   2");
                }

                //3
                String productcode = getCelldata(row, 2);
                if (productcode.trim().isEmpty()) {
                    errorFoundInExcelReading++;
                    P.p("error   3");
                }
                if (false) {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setContentText("PRODUCT CODE SHOULD CONTAIN 10 CHARACTER ONLY ");
                    alert1.showAndWait();
                    errorFoundInExcelReading++;
                }

                //4
                String quantity = getCelldata(row, 3);
                if (quantity.trim().isEmpty()) {
                    errorFoundInExcelReading++;
                    P.p("error   4");
                }

                //5
                String MRP = getCelldata(row, 4);
                if (MRP.trim().isEmpty()) {
                    errorFoundInExcelReading++;
                    P.p("error   5");
                }

                //6
                String SALES_PRICE = getCelldata(row, 5);
                if (SALES_PRICE.trim().isEmpty()) {
                    errorFoundInExcelReading++;
                    P.p("error   6");
                }

                //7
                int NUMBER_OF_STICKERS = getCellNumdata(row, 6);
                if ((NUMBER_OF_STICKERS + "").trim().isEmpty()) {
                    errorFoundInExcelReading++;
                    P.p("error   7");
                }
                totalStickers = totalStickers + NUMBER_OF_STICKERS;

                //cell 1
                Phrase p1 = new Phrase();
                p1.add(new Chunk(companyname, font2_bold));
                p1.add(new Chunk("\n" + Productname, font2_bold));
                PdfPCell cell1 = new PdfPCell(p1);
                cell1.setPaddingLeft(7);
                cell1.setPaddingRight(7);
                cell1.setPaddingTop(10);

                //cell2
//              barcode generation code
                Barcode128 code128 = new Barcode128();
                code128.setSize(8);
                code128.setCode(productcode);
                code128.setCodeType(Barcode128.CODE128);
                PdfContentByte cb = writer.getDirectContent();
                com.itextpdf.text.Image code128Image = code128.createImageWithBarcode(cb, null, null);
//              end of generation
                PdfPCell cell2 = new PdfPCell(code128Image);
                cell2.setPaddingLeft(7);
                cell2.setPaddingRight(7);

                //cell3
                Phrase p = new Phrase();
                p.add(new Chunk("SIZE : ", font3_bold));
                p.add(new Chunk(quantity, font3));
                p.add(new Chunk("\nMRP  : ", font4_bold));
                p.add(new Chunk(MRP, font4_bold));
                p.add(new Chunk("\nRATE : ", font3_bold));
                p.add(new Chunk(SALES_PRICE.trim(), font3_bold));
                PdfPCell cell3 = new PdfPCell(p);
                cell3.setPaddingLeft(7);
                cell3.setPaddingRight(7);

                cell1.setBorder(Rectangle.NO_BORDER);
                cell2.setBorder(Rectangle.NO_BORDER);
                cell3.setBorder(Rectangle.NO_BORDER);

                PdfPTable table1 = new PdfPTable(1);
                table1.setWidthPercentage(100);
                table1.addCell(cell1);
                table1.addCell(cell2);
                table1.addCell(cell3);

                int check = (NUMBER_OF_STICKERS % 5);
                System.out.println("NUMBER_OF_STICKERS  : " + NUMBER_OF_STICKERS);
                System.out.println("CHECH  : " + check);
                double cellH = (PageSize.A4.getHeight()-6)/6;

                PdfPCell aa = new PdfPCell(table1);
                aa.setFixedHeight((float) cellH);
                aa.setBorder(Rectangle.NO_BORDER);
                for (int b = 0; b < NUMBER_OF_STICKERS; b++) {
                    table.addCell(aa);
                }

                if (errorFoundInExcelReading > 0) {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setContentText("EXCEL SHEET DATA IS NOT CORRECT OR EMPTY,\n PLEASE VERIFY ONCE AND RETRY AGAIN");
                    alert1.showAndWait();
                    break;
                }
            }
            int check = (totalStickers % 5);
            System.out.println("NUMBER_OF_STICKERS  : " + totalStickers);
            System.out.println("CHECH  : " + check);
            for (int a = (5-check); a > 0; a--) {
                table.addCell("");
            }

            if (errorFoundInExcelReading == 0) {
                document.add(table);
                document.close();
                if (Desktop.isDesktopSupported()) {
                    try {
                        File myFile = new File(FILE);
                        Desktop.getDesktop().open(myFile);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public String getCelldata(Row row, int index) {
        String data = "";
        try {
            if (row.getCell(index).getCellType() == CELL_TYPE_STRING) {
                data = row.getCell(index).getStringCellValue();
            } else if (row.getCell(index).getCellType() == CELL_TYPE_NUMERIC) {
                double bb = row.getCell(index).getNumericCellValue();
                data = String.valueOf(bb);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("EXCEL SHEET DATA IS NOT CORRECT OR EMPTY,\n PLEASE VERIFY ONCE AND RETRY AGAIN");
            alert.showAndWait();
            errorFoundInExcelReading++;
        }
        return data;
    }

    public int getCellNumdata(Row row, int index) {
        int data = 0;
        try {
            if (row.getCell(index).getCellType() == CELL_TYPE_NUMERIC) {
                double data1 = row.getCell(index).getNumericCellValue();
                System.out.println(" double data1 : " + data1);
                data = (int) data1;
                System.out.println(" double data : " + data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("EXCEL SHEET DATA IS NOT CORRECT OR EMPTY,\n PLEASE VERIFY ONCE AND RETRY AGAIN");
            alert.showAndWait();
            errorFoundInExcelReading++;
        }
        return data;
    }

    @FXML
    private BarChart<String, Double> d_p_chart;

    @FXML
    private BarChart<String, Double> m_p_chart;

    @FXML
    private BarChart<String, Double> d_s_chart;

    @FXML
    private BarChart<String, Double> m_s_chart;

    DecimalFormat formatter = new DecimalFormat("#,###.00");

    public void view_dashboard(ActionEvent event) throws Exception {
        Middlepane1.setVisible(false);
        Middlepane2.setVisible(false);
        Middlepane4.setVisible(false);
        Middlepane5.setVisible(false);
        Middlepane6.setVisible(false);
        profilePane.setVisible(false);
        barcode_pane.setVisible(false);
        dashboard.setVisible(true);

        d_p_chart.getData().clear();
        m_p_chart.getData().clear();
        d_s_chart.getData().clear();
        m_s_chart.getData().clear();

        XYChart.Series<String, Double> series1;

        XYChart.Series<String, Double> series2;

        XYChart.Series<String, Double> series3;

        XYChart.Series<String, Double> series4;

        Connection con = null;

        try {
            //date wise purchase(today)
            d_p_chart.setAnimated(false);
            ZoneId zoneId = ZoneId.of("Indian/Maldives");
            LocalDate today = LocalDate.now(zoneId);

            String day = String.valueOf(today.getDayOfMonth());
            String month = String.valueOf(today.getMonthValue());
            int year = today.getYear();

            String day1 = "", month1 = "";

            if (day.length() == 1) {
                day1 = "0" + day;
            } else {
                day1 = day;
            }
            if (month.length() == 1) {
                month1 = "0" + month;
            } else {
                month1 = month;
            }

            P.p("day1" + day1);
            P.p("month" + month1);

            series1 = new XYChart.Series<>();

            con = DBConnect.getConnection();

            String query = "select distinct(productname), sum(total_amount) from stock " +
                    "where stockaddeddate='" + year + month1 + day1 + "' group by productname order by productname ASC";

            ResultSet rs1 = con.createStatement().executeQuery(query);

            d_p_xaxis.setLabel("PRODUCT NAME");
            d_p_yaxis.setLabel("TOTAL AMOUNT");

            while (rs1.next()) {

                series1.getData().add(new XYChart.Data<String, Double>(rs1.getString(1), rs1.getDouble(2)));
            }

            series1.setName("TODAY'S PURCHASE");

            d_p_chart.getData().add(series1);

            //end of today's purchase

            //month wise purchase

            m_p_chart.setAnimated(false);

            series2 = new XYChart.Series<>();

            String query_m = "select distinct(productname), sum(total_amount) from stock" +
                    " where MONTH(stockaddeddate)='" + month1 + "' and YEAR(stockaddeddate)='" + year + "'group " +
                    "by productname order by productname ASC";

            ResultSet rs2 = con.createStatement().executeQuery(query_m);

            m_p_xaxis.setLabel("PRODUCT NAME");
            m_p_yaxis.setLabel("TOTAL AMOUNT");

            while (rs2.next()) {

                series2.getData().add(new XYChart.Data<String, Double>(rs2.getString(1), rs2.getDouble(2)));
            }

            series2.setName(today.getMonth() + "'S PURCHASE");

            m_p_chart.getData().add(series2);
            //end of month purchase

            //today's sales
            d_s_chart.setAnimated(false);

            series3 = new XYChart.Series<>();

            String query_s = "select distinct(product_name), sum(net_total) from billing " +
                    "where bill_date='" + year + month1 + day1 + "' group by product_name order by product_name ASC";

            ResultSet rs3 = con.createStatement().executeQuery(query_s);

            d_s_xaxis.setLabel("PRODUCT NAME");
            d_s_yaxis.setLabel("NET TOTAL");

            while (rs3.next()) {
                series3.getData().add(new XYChart.Data<String, Double>(rs3.getString(1), rs3.getDouble(2)));
            }

            series3.setName("TODAY'S SALES");

            d_s_chart.getData().add(series3);

            //end of today's sales

            //month wise sales

            m_s_chart.setAnimated(false);

            series4 = new XYChart.Series<>();

            String query_sm = "select distinct(product_name), sum(net_total) from billing " +
                    "where MONTH(bill_date)='" + month1 + "' and YEAR(bill_date)='" + year + "' " +
                    "group by product_name order by product_name asc";

            ResultSet rs4 = con.createStatement().executeQuery(query_sm);

            m_s_xaxis.setLabel("PRODUCT NAME");
            m_s_yaxis.setLabel("NET TOTAL");

            while (rs4.next()) {
                series4.getData().add(new XYChart.Data<String, Double>(rs4.getString(1), rs4.getDouble(2)));
            }

            series4.setName(today.getMonth() + "'S SALES");

            m_s_chart.getData().add(series4);

            //end of month wise sales

            //tab details
            //purchase
            p_year.setText(year - 1 + "");
            p_year2.setText(year + "");


            double amount = 0;
            long p_sum = 0;
            String p = "select sum(total_amount) from stock where YEAR(stockaddeddate)='" + year + "'";
            ResultSet rr1 = con.createStatement().executeQuery(p);
            while (rr1.next()) {
                amount = rr1.getDouble(1);
                p_sum = (long) (amount);
            }

            p_year_2.setText(formatter.format(p_sum) + "");


            double amount1 = 0;
            long p_sum1 = 0;
            String p1 = "select sum(total_amount) from stock where YEAR(stockaddeddate)='" + (year - 1) + "'";
            ResultSet rr2 = con.createStatement().executeQuery(p1);
            while (rr2.next()) {
                amount1 = rr2.getDouble(1);

                p_sum1 = (long) (amount1);
            }
            p_year_1.setText(formatter.format(p_sum1) + "");
            //end of purchase
            //sales
            s_year.setText(year - 1 + "");
            s_year2.setText(year + "");
            long s_sum = 0;
            String s = "select sum(net_total) from billing where YEAR(bill_date)='" + (year - 1) + "'";
            ResultSet ss = con.createStatement().executeQuery(s);
            while (ss.next()) {
                s_sum = ss.getLong(1);
            }
            s_year_1.setText(formatter.format(s_sum) + "");
            long s_sum1 = 0;
            String s1 = "select sum(net_total) from billing where YEAR(bill_date)='" + (year) + "'";
            ResultSet ss1 = con.createStatement().executeQuery(s1);
            while (ss1.next()) {
                s_sum1 = ss1.getLong(1);
            }
            s_year_2.setText(formatter.format(s_sum1) + "");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }


    public void view_dashboard_pane(ActionEvent event) throws Exception {
        if (dashboard_combo.getValue().equals("Select Type")) {
            dashboard_pane.getChildren().clear();
        } else if (dashboard_combo.getValue().equals("Stock Alert")) {
            stock_alert_pane.setVisible(true);
            String query = "Select dealer_name, productname, productcode, stockquantity, mrp, selling_price, stockaddeddate from stock where stockquantity<=100";
            LoadingDataToTableView.Welcome(query, dashboard_pane, 300, 750);
        } else if (dashboard_combo.getValue().equals("Fast Moving Product")) {
            stock_alert_pane.setVisible(false);
            String query = "select product_name,product_code,sum(quantity) as 'quantity',mrp,trade_price from billing " +
                    "group by product_name order by sum(quantity) DESC";
            LoadingDataToTableView.Welcome(query, dashboard_pane, 300, 750);
        } else if (dashboard_combo.getValue().equals("Slow Moving Product")) {
            stock_alert_pane.setVisible(false);
            String query = "select product_name,product_code,sum(quantity) as 'quantity' ,mrp,trade_price from billing " +
                    "group by product_name order by sum(quantity) ASC";
            LoadingDataToTableView.Welcome(query, dashboard_pane, 300, 750);
        }
    }


    public void view_stock_alert(ActionEvent event) throws Exception {
        String query = "select dealer_name, productname, productcode, stockquantity, mrp, " +
                "selling_price, stockaddeddate from stock where stockquantity<=100";
        if (alert_quantity.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("PLEASE ENTER STOCK QUANTITY LIMIT");
            alert.showAndWait();
        } else {
            if (alert_dealer.getText().trim().isEmpty()) {
                query = "select dealer_name, productname, productcode, stockquantity, mrp, " +
                        "selling_price, stockaddeddate from stock " +
                        "where stockquantity<='" + alert_quantity.getText().trim() + "'";
            } else {
                query = "select dealer_name, productname, productcode, stockquantity, mrp, " +
                        "selling_price, stockaddeddate from stock " +
                        "where stockquantity<='" + alert_quantity.getText().trim() + "' and " +
                        "dealer_name='" + alert_dealer.getText().trim() + "'";
            }

            LoadingDataToTableView.Welcome(query, dashboard_pane, 300, 750);
        }
    }

    public void dealer_livesearch(KeyEvent keyEvent) throws Exception {

        AutoCompletionBinding<String> autoCompletionBindings = TextFields.bindAutoCompletion(alert_dealer, LiveSearchMe.makeSearch("select DISTINCT(dealer_name) from dealer", "dealer_name"));
    }

    public void stock_alert_in_excel(ActionEvent event) throws Exception {

        String query = "select dealer_name, productname, productcode, stockquantity, mrp, " +
                "selling_price, stockaddeddate from stock where stockquantity<=100";
        File dir = new File(P.drive_name() + FilePath.FOLDER_PATH);
        dir.mkdir();
        File dir2 = new File(P.drive_name() + FilePath.FOLDER_PATH + "STOCK_ALERT_EXCEL_REPORT");
        dir2.mkdir();
        String path = P.drive_name() + FilePath.FOLDER_PATH + "/STOCK_ALERT_EXCEL_REPORT/STOCK_ALERT.csv";
        if (alert_quantity.getText().trim().isEmpty() && alert_dealer.getText().trim().isEmpty()) {
            query = "select dealer_name, productname, productcode, stockquantity, mrp, " +
                    "selling_price, stockaddeddate from stock " +
                    "where stockquantity<='100'";
        } else if (!alert_quantity.getText().trim().isEmpty() && alert_dealer.getText().trim().isEmpty()) {
            query = "select dealer_name, productname, productcode, stockquantity, mrp, " +
                    "selling_price, stockaddeddate from stock " +
                    "where stockquantity<='" + alert_quantity.getText().trim() + "'";
        } else if (alert_quantity.getText().trim().isEmpty() && !alert_dealer.getText().trim().isEmpty()) {
            query = "select dealer_name, productname, productcode, stockquantity, mrp, " +
                    "selling_price, stockaddeddate from stock " +
                    "where dealer_name='" + alert_dealer.getText().trim() + "'";
        } else if (!alert_quantity.getText().trim().isEmpty() && !alert_dealer.getText().trim().isEmpty()) {
            query = "select dealer_name, productname, productcode, stockquantity, mrp, " +
                    "selling_price, stockaddeddate from stock " +
                    "where stockquantity<='" + alert_quantity.getText().trim() + "' and " +
                    "dealer_name='" + alert_dealer.getText().trim() + "'";
        }
        export_excel(query, path);
    }

    public void barcode_templete(ActionEvent event) throws Exception {

        File dir = new File(P.drive_name() + FilePath.FOLDER_PATH);
        dir.mkdir();
        File dir2 = new File(P.drive_name() + FilePath.FOLDER_PATH + "BARCODE_TEMPLETE");
        dir2.mkdir();

        String path = P.drive_name() + FilePath.FOLDER_PATH + "/BARCODE_TEMPLETE/BARCODE.csv";

        createExcelTEMPLEFile(path);
    }


    public static void createExcelFile(String query, String path)  {
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

            AlertMessage.error("Details Exported to excel sheet and stored in "+path);

            Desktop dt = Desktop.getDesktop();
            dt.open(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(con.isClosed()) { con.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void createExcelFile(String query, String path, String query2)  {
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

            rs = con.createStatement().executeQuery(query2);
            double totalAmount=0;
            while(rs.next()){
                int rowNumbers=rs.getMetaData().getColumnCount();
                XSSFRow rowLast = sheet.createRow(index++);
                String finalData="  ----------------------------------------------------------------------- ";
                rowLast.createCell(0).setCellValue(finalData);
                for(int i=0; i<rowNumbers; i++){
                    rowLast = sheet.createRow(index++);
                    finalData=rs.getMetaData().getColumnName(i+1).toUpperCase()+"  :  "+P.df00(rs.getDouble(i+1));
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
            try {
                P.loggerLoader(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void createExcelTEMPLEFile(String path) throws Exception {
        try {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Items details");

            XSSFCellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFRow header = sheet.createRow(0);

            ArrayList<String> headerList = new ArrayList<String>();

            headerList.add("BRAND_NAME");
            headerList.add("PRODUCT_NAME");
            headerList.add("PRODUCT_CODE");
            headerList.add("SIZE");
            headerList.add("MRP");
            headerList.add("OUR_PRICE");
            headerList.add("NUMBER_OF_STICKERS");

            for (int i = 0; i < headerList.size(); i++) {
                String colname = headerList.get(i);
                header.createCell(i).setCellValue(colname.toUpperCase());
                header.getCell(i).setCellStyle(style);
            }

            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("IMPORT EXCEL TEMPLETE SHEET IS CREATE IN PATH : " + path);
            alert.showAndWait();

            Desktop dt = Desktop.getDesktop();
            dt.open(new File(path));

        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }


    public void payDealerPayment(ActionEvent event) throws Exception {
        Connection connection = null;
        try{
            connection = DBConnect.getConnection();
            boolean amountIsCorrect=false;
            double dPaymentAmount=0;
            try {
                dPaymentAmount  =   Double.parseDouble(dealerPayment.getText().trim());
                amountIsCorrect =   true;
                if(dPaymentAmount<=0){
                    amountIsCorrect =   false;
                }
            }catch (Exception e){}


            if(dealerNameWhilePayment.getText().trim().isEmpty()){
                AlertMessage.error("PLEASE ENETR DEALER NAME");
            }else if(dealerPayementDate.getValue()==null){
                AlertMessage.error("PLEASE DATE OF PAYMENT");
            }else if(!amountIsCorrect){
                AlertMessage.error("PLEASE ENETR VALID AMOUNT");
            }else{
                String insertQuery  =   "insert into dealer_payment_table ( dealer_name, payment_amount, date, otherDetails ) " +
                        "values (?,?,?,?)";
                PreparedStatement ps1=connection.prepareStatement(insertQuery);
                ps1.setString(1, dealerNameWhilePayment.getText().trim().toUpperCase());
                ps1.setString(2, P.df00(dPaymentAmount));
                ps1.setString(3, dealerPayementDate.getValue().toString());
                ps1.setString(4, dealerPaymentOhterDetails.getText().trim());
                int i=ps1.executeUpdate();
                if(i>0){
                    AlertMessage.inform("PAYMENT DETAILS ENTERED SUCCESFULLY ");
                    dealerPayementDate.setValue(today);
                    dealerPayment.clear();
                    dealerPaymentOhterDetails.clear();
                    String query="select * from dealer_payment_table where dealer_name='"+dealerNameWhilePayment.getText().trim()+"'";
                    LoadingTableViewDataSelectedRowName.Welcome(query, dealerPaymentDeatilsListPane, 200,480);
                    dealerNameWhilePayment.clear();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            if(!connection.isClosed()){ connection.isClosed(); }
        }
    }

    public void liveSearchForDealer(KeyEvent event) throws Exception {
        AutoCompletionBinding<String> autoCompletionBindings = TextFields.bindAutoCompletion(dealerNameWhilePayment, LiveSearchMe.makeSearch("select DISTINCT(dealer_name) from dealer", "dealer_name"));
    }

    public void viewDealerPaymentDetails(ActionEvent event) throws Exception {
        Connection connection = null;
        try{
            String query="select * from dealer_payment_table where id>0 ";
            if(dealerNameWhilePayment.getText().trim().isEmpty()  && dealerPayementDateFrom.getValue()==null && dealerPayementDateTo.getValue()==null)
            {
                AlertMessage.error("Please enter atleast'Dealer Name' , 'Date from'  OR  'Date To' to get the Payment Details");
            }else {
                String conditions="";
                if(!dealerNameWhilePayment.getText().trim().isEmpty()){
                conditions=conditions + " and dealer_name = '"+dealerNameWhilePayment.getText().trim()+"' ";
                }
                if(!(dealerPayementDateFrom.getValue()==null)){
                    conditions=conditions + " and date >= '"+dealerPayementDateFrom.getValue()+"' ";
                }
                if(!(dealerPayementDateTo.getValue()==null)){
                    conditions=conditions + " and date <= '"+dealerPayementDateTo.getValue()+"' ";
                }
                query = query + conditions + " order by dealer_name desc ";
                P.p("query 1 : "+query  );
                LoadingTableViewDataSelectedRowName.Welcome(query, dealerPaymentDeatilsListPane, 250, 480);
                query = "select sum(payment_amount) from dealer_payment_table where id>0 " + conditions;
                P.p("query 2 : "+query  );
                { connection = DBConnect.getConnection(); }
                ResultSet set = connection.createStatement().executeQuery(query);
                if(set.next()){
                    dealerPaymentTotal.setText(P.df00(set.getDouble(1)));
                }else {
                    dealerPaymentTotal.setText(P.df00(0));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            if(!connection.isClosed()){ connection.close(); }
        }
    }

    public void excelReportDealerPaymentDetails(ActionEvent event) throws Exception {
        Connection connection = null;
        try{
            String query="select * from dealer_payment_table where id>0 ";
            if(dealerNameWhilePayment.getText().trim().isEmpty()  && dealerPayementDateFrom.getValue()==null && dealerPayementDateTo.getValue()==null)
            {
                AlertMessage.error("Please enter atleast'Dealer Name' , 'Date from'  OR  'Date To' to get the Payment Details");
            }else {
                String conditions="";
                if(!dealerNameWhilePayment.getText().trim().isEmpty()){
                    conditions=conditions + " and dealer_name = '"+dealerNameWhilePayment.getText().trim()+"' ";
                }
                if(!(dealerPayementDateFrom.getValue()==null)){
                    conditions=conditions + " and date >= '"+dealerPayementDateFrom.getValue()+"' ";
                }
                if(!(dealerPayementDateTo.getValue()==null)){
                    conditions=conditions + " and date <= '"+dealerPayementDateTo.getValue()+"' ";
                }
                query = query + conditions + " order by dealer_name desc ";
                P.p("query 1 : "+query  );
                new File(P.drive_name()+FilePath.FOLDER_PATH).mkdir();
                new File(P.drive_name()+FilePath.PAYEMNT_DETAILS_PATH).mkdir();
                String path=P.drive_name()+FilePath.PAYEMNT_DETAILS_PATH+"Dealer_Payment.csv";
                export_excel(query, path);
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            if(!connection.isClosed()){ connection.close(); }
        }
    }

    public void view_expencePane(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ExpenseEntry.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Expense Details");
        stage.setScene(new Scene(root1, 1000, 700));
        stage.show();
    }

    public void openBillCancellationWindow(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("openBillCancellationWindow.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Bill Cancellation");
        stage.setScene(new Scene(root1, 1000, 700));
        stage.show();
    }

    public void copyMRPToMRPCode(KeyEvent event) {
        mrpCode.setText(MRP.getText().trim());
    }


    public void openManualBillingWindow1(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER") || event.getCode().toString().equals("SPACE") ){
            openManualBillingWindow();
        }
    }
    public void openManualBillingWindow2(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().toString().equals("PRIMARY")){
            openManualBillingWindow();
        }
    }
    private void openManualBillingWindow() {
        try{
            FXMLLoader fxmlLoader =  new FXMLLoader(getClass().getResource("ManualBilling.fxml"));
            Stage manualBillingStage = new Stage();
            manualBillingStage.setTitle("Manual Billing");
            manualBillingStage.setScene(new Scene(fxmlLoader.load()));
            manualBillingStage.show();

        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }

    public void openExcelFormatImportWindow1(KeyEvent event) {
        if( event.getCode().toString().equalsIgnoreCase("ENTER") || event.getCode().toString().equalsIgnoreCase("SPACE") ){
            openExcelFormatImportWindow();
        }
    }
    public void openExcelFormatImportWindow2(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().toString().equalsIgnoreCase("PRIMARY") ){
            openExcelFormatImportWindow();
        }
    }
    private void openExcelFormatImportWindow() {
        try{
            FXMLLoader fxmlLoader =  new FXMLLoader(getClass().getResource("StockImportExcel.fxml"));
            Stage stockImportStage = new Stage();
            stockImportStage.setTitle("Stock Import From Excel Sheet");
            stockImportStage.setScene(new Scene(fxmlLoader.load()));
            stockImportStage.show();
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }


    public void display_products(KeyEvent keyEvent) throws Exception {
        displayProductListInPane(" and productcode='"+ProductCode.getText().trim()+"'");
    }

    void displayProductListInPane(String condition ) throws Exception {
        try{
            String query = "Select " +
                    "id, p_invoice ,productcode, hsn, stockaddeddate, " +
                    "productname, stockquantity, added_quantity, packing, purchaseprice, " +
                    "mrp, cgst, sgst, dealer_name, " +
                    "purchase_discount , selling_price , total_amount from stock where id>0 " + condition + " limit 500 ";
            LoadingTableViewDataSelectedRowName.Welcome(query, view_details, 170, 754);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void load_items(KeyEvent keyEvent) throws Exception {
        displayProductListInPane(" and productname='"+StockName.getText().trim()+"'");

    }

    public void cal()
    {
        Runtime run = Runtime.getRuntime();
        for (int i = 0; i < 1; i++)
        {
            try {
                //  run.exec("C:\\Windows\\system32\\calc.exe");
                run.exec("calc");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void view_cal(ActionEvent actionEvent) {
        cal();
    }
    public void eb() throws URISyntaxException, IOException {
        Desktop d = Desktop.getDesktop();
        d.browse(new URI("www.expressbilling.in"));
    }

    public void open_eb(MouseEvent mouseEvent) throws IOException, URISyntaxException {

        eb();
    }

    public void open_youtube(ActionEvent actionEvent) throws URISyntaxException, IOException {

        Desktop d = Desktop.getDesktop();
        d.browse(new URI("https://youtu.be/oeppinBwMhQ"));
    }

    public void open_total(ActionEvent actionEvent) {
       calculate();
    }
}
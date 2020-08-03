package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Mitra on 03/20/18.
 */
public class ExpenseEntry implements Initializable {

    public TextField expenseID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        expensePayementDate.setValue(Controller.today1);
    }

    public DatePicker expensePayementDate;
    public TextField expenseNameWhilePayment;
    public TextField paymentAmount;
    public TextField expensePaymentOhterDetails;
    public Pane expensePaymentDeatilsListPane;
    public DatePicker expensePayementDateFrom;
    public DatePicker expensePayementDateTo;
    public TextField expensePaymentTotal;
    public static int incidArray;
    public void liveSearchForExpense(KeyEvent event) throws Exception {
        AutoCompletionBinding<String> autoCompletionBindings = TextFields.bindAutoCompletion(expenseNameWhilePayment, LiveSearchMe.makeSearch("select DISTINCT(expense_name) from expense_table", "expense_name"));
    }

    public void payExpensePayment(ActionEvent event) throws Exception {
        Connection connection = null;
        try {
            connection = DBConnect.getConnection();
            boolean amountIsCorrect = false;
            double dPaymentAmount = 0;
            try {
                dPaymentAmount = Double.parseDouble(paymentAmount.getText().trim());
                P.p("dPaymentAmount  : " + dPaymentAmount);
                amountIsCorrect = true;
                if (dPaymentAmount <= 0) {
                    amountIsCorrect = false;
                }
            } catch (Exception e) {
            }


            if (expenseNameWhilePayment.getText().trim().isEmpty()) {
                AlertMessage.error("PLEASE ENETR EXPENSE NAME");
            } else if (expensePayementDate.getValue() == null) {
                AlertMessage.error("PLEASE DATE OF PAYMENT");
            } else if (!amountIsCorrect) {
                AlertMessage.error("PLEASE ENETR VALID AMOUNT");
            } else {
                String insertQuery = "insert into expense_table ( expense_name, expense_amount, date, expense_other_Details ) " +
                        "values (?,?,?,?)";
                PreparedStatement ps1 = connection.prepareStatement(insertQuery);
                ps1.setString(1, expenseNameWhilePayment.getText().trim().toUpperCase());
                ps1.setString(2, P.df00(dPaymentAmount));
                ps1.setString(3, expensePayementDate.getValue().toString());
                ps1.setString(4, expensePaymentOhterDetails.getText().trim());
                int i = ps1.executeUpdate();
                if (i > 0) {
                    AlertMessage.inform("PAYMENT DETAILS ENTERED SUCCESFULLY ");
                    expensePayementDate.setValue(Controller.today1);
                    paymentAmount.clear();
                    expensePaymentOhterDetails.clear();
                    expenseID.clear();
                    String query = "select * from expense_table where expense_name='" + expenseNameWhilePayment.getText().trim() + "'";
                    LoadingTableViewDataSelectedRowName.Welcome(query, expensePaymentDeatilsListPane, 250, 700);
                    expenseNameWhilePayment.clear();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        } finally {
            if (!connection.isClosed()) {
                connection.isClosed();
            }
        }
    }

    public void viewExpensePaymentDetails(ActionEvent event) throws Exception {
        Connection connection = null;
        try {
            String query = "select * from expense_table where id>0 ";
            if (expenseNameWhilePayment.getText().trim().isEmpty() && expensePayementDateFrom.getValue() == null && expensePayementDateTo.getValue() == null) {
                AlertMessage.error("Please enter atleast'Expense Name' , 'Date from'  OR  'Date To' to get the Payment Details");
            } else {
                String conditions = "";
                if (!expenseNameWhilePayment.getText().trim().isEmpty()) {
                    conditions = conditions + " and expense_name = '" + expenseNameWhilePayment.getText().trim() + "' ";
                }
                if (!(expensePayementDateFrom.getValue() == null)) {
                    conditions = conditions + " and date >= '" + expensePayementDateFrom.getValue() + "' ";
                }
                if (!(expensePayementDateTo.getValue() == null)) {
                    conditions = conditions + " and date <= '" + expensePayementDateTo.getValue() + "' ";
                }
                query = query + conditions;
                P.p("query 1 : " + query);
                LoadingTableViewDataSelectedRowName.Welcome(query, expensePaymentDeatilsListPane, 250, 700);
                query = "select sum(expense_amount) from expense_table where id>0 " + conditions;
                P.p("query 2 : " + query);
                {
                    connection = DBConnect.getConnection();
                }
                ResultSet set = connection.createStatement().executeQuery(query);
                if (set.next()) {
                    expensePaymentTotal.setText(P.df00(set.getDouble(1)));
                } else {
                    expensePaymentTotal.setText(P.df00(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        } finally {
            if (!connection.isClosed()) {
                connection.close();
            }
        }
    }

    public void excelReportExpensePaymentDetails(ActionEvent event) throws Exception {
        Connection connection = null;
        try {
            String query = "select * from expense_table where id>0 ";
            if (expenseNameWhilePayment.getText().trim().isEmpty() && expensePayementDateFrom.getValue() == null && expensePayementDateTo.getValue() == null) {
                AlertMessage.error("Please enter atleast'Expense Name' , 'Date from'  OR  'Date To' to get the Payment Details");
            } else {
                String conditions = "";
                if (!expenseNameWhilePayment.getText().trim().isEmpty()) {
                    conditions = conditions + " and expense_name = '" + expenseNameWhilePayment.getText().trim() + "' ";
                }
                if (!(expensePayementDateFrom.getValue() == null)) {
                    conditions = conditions + " and date >= '" + expensePayementDateFrom.getValue() + "' ";
                }
                if (!(expensePayementDateTo.getValue() == null)) {
                    conditions = conditions + " and date <= '" + expensePayementDateTo.getValue() + "' ";
                }
                query = query + conditions;
                P.p("query 1 : " + query);
                new File(P.drive_name() + FilePath.FOLDER_PATH).mkdir();
                new File(P.drive_name() + FilePath.PAYEMNT_DETAILS_PATH).mkdir();
                String path = P.drive_name() + FilePath.PAYEMNT_DETAILS_PATH + "Expense_Payment.csv";
                Controller.export_excel(query, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            P.loggerLoader(e);
        } finally {
            if (!connection.isClosed()) {
                connection.close();
            }
        }
    }


    public void editexpense(ActionEvent actionEvent) {
        Connection connection = null;
        ObservableList oa = LoadingTableViewDataSelectedRowName.selectItem();

        ArrayList aa = new ArrayList();
        aa.add(oa.get(0));

        ArrayList newArray = new ArrayList();
        newArray = aa;
        String old = String.valueOf(newArray.get(0));
        ArrayList<String> bb = new ArrayList<String>();
        int check = 0;

        old = old.replace("[", "");
        old = old.replace("]", "");

        String log[] = old.split(",");
        String log1[] = old.split(",");

        bb.add(log1[0].trim());
        try {
            bb.add(log1[1].trim());
            expensePaymentDeatilsListPane.setVisible(true);
        } catch (Exception e) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("PLEASE SELECT EXPENSE DETAIL");
            a.showAndWait();
            check++;
        }
        if (check == 0) {
            try {
                connection = DBConnect.getConnection();

                int c_id = Integer.parseInt(bb.get(0).trim());

                String query = "Select * from expense_table where id='" + c_id + "'";
                ResultSet rs = connection.createStatement().executeQuery(query);

                while (rs.next()) {
                    expenseID.setText(rs.getString(1));
                    expenseNameWhilePayment.setText(rs.getString(2));
                    //   phoneNum.setText(rs.getString(3));
                    paymentAmount.setText(rs.getString(3));
                    expensePaymentOhterDetails.setText(rs.getString(4));
                    expensePayementDate.setValue(LocalDate.parse(rs.getString(5)));
                    // vehicle_no1.setText(rs.getString(10));
                    //   routeListCombo.setValue(rs.getString("route_Name"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                P.loggerLoader(e);
            }
        }
    }

    public void updateexpense(ActionEvent actionEvent) throws Exception {
        Connection connection = null;
        if (expenseNameWhilePayment.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER EXPENSE NAME");
            alert1.showAndWait();
        }
        else {
            try {
                connection = DBConnect.getConnection();

                PreparedStatement ps = connection.prepareStatement
                        ("update expense_table set " +
                                "expense_name='" + expenseNameWhilePayment.getText().trim() + "', " +
                                "expense_amount='" + paymentAmount.getText().trim() + "' ,    " +
                                "expense_other_details='" + expensePaymentOhterDetails.getText().trim() + "' ," +
                                "date='"+expensePayementDate.getValue().toString()+"' "+
                                " where id='" + expenseID.getText().trim() + "'");

                int i = ps.executeUpdate();

                if (i > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("EXPENSE INFORMATION UPDATED SUCCESSFULLY");
                    alert.showAndWait();

                    refresh_customer();
                    viewExpensePaymentDetails(actionEvent);

                } else if(i<0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN SAVING EXPENSE INFORMATION");
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

    public void deleteexpense(ActionEvent actionEvent) throws Exception {

        Connection connection = null;
        ObservableList oa = LoadingTableViewDataSelectedRowName.selectItem();

        ArrayList aa = new ArrayList();
        aa.add(oa.get(0));

        ArrayList newArray = new ArrayList();
        newArray = aa;
        String old = String.valueOf(newArray.get(0));
        ArrayList<String> bb = new ArrayList<String>();
        int check = 0;

        old = old.replace("[", "");
        old = old.replace("]", "");

        String log[] = old.split(",");
        String log1[] = old.split(",");

        bb.add(log1[0].trim());
        try {
            bb.add(log1[1].trim());
        } catch (Exception e) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("PLEASE SELECT EXPENSE FROM TABLE");
            a.showAndWait();
            check++;
        }
        if (check == 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("ARE YOU SURE YOU WANT TO DELETE EXPENSE RECORD");
            Optional<ButtonType> result = alert.showAndWait();

            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                try {
                    connection = DBConnect.getConnection();
                    int c_id = Integer.parseInt(bb.get(0).trim());

                    String query = "delete from expense_table where id='" + c_id + "'";
                    PreparedStatement ps = connection.prepareStatement(query);
                    int i = ps.executeUpdate();

                    if (i > 0) {
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setContentText("EXPENSE INFORMATION DELETED SUCCESSFULLY");
                        a.showAndWait();
                        // expensePaymentDeatilsListPane.setVisible(true);
                        viewExpensePaymentDetails(actionEvent);
                    } else {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("ERROR IN DELETING EXPENSE INFORMATION");
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

    void refresh_customer() throws Exception {
        expenseNameWhilePayment.clear();
        expenseID.clear();
        paymentAmount.clear();
        //paymentAmount.clear();
        expensePaymentOhterDetails.clear();
        expensePayementDate.setValue(Controller.today1);

    }

    public void expenseid(MouseEvent mouseEvent) throws Exception {

        Connection connection=null;

        int i = 0;
        try {
            connection = DBConnect.getConnection();
            String empIdIs = "";
            Statement stm1 = connection.createStatement();

            ResultSet rs = stm1.executeQuery("select max(id) as id from expense_table");
            while (rs.next()) {
                empIdIs = rs.getString("id");
                i++;
            }
            if (empIdIs != null && i > 0) {

                incidArray = (Integer.parseInt(empIdIs) + 1);
                expenseID.setText(String.valueOf(incidArray));

            } else {
                expenseID.setText(String.valueOf(1));
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

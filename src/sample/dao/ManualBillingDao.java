package sample.dao;

import sample.DBConnect;
import sample.P;
import sample.dto.ManualBillingDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Mitra on 10/12/18.
 */

public class ManualBillingDao {

    public Connection connection = null;
    public  String query = "";
    public final String tableNaame = "manualbilling";

    public boolean insert(ManualBillingDto dto){
        boolean status = false;
        try{
            connection = DBConnect.connect();
            query = "INSERT INTO "+tableNaame+" ( " +
                    " date, invoice_no, customer_name, customer_phno, customer_details, " +
                    " itemName, packing, hsnCode, quantity, rate, " +
                    " discPer, discAmount, taxableAmount, cgst, cgst_amount, " +
                    " sgst, sgst_amount, net_total, bill_status, billed_by, " +
                    " deductionAmount, billAmount, dispatchDetails, paymentDetails, paymentType " +
                    " ) values (   ?,?,?,?,?,  ?,?,?,?,?,    ?,?,?,?,?,    ?,?,?,?,?,    ?,?,?,?,?   )";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dto.getDate());
            stmt.setInt(2, dto.getInvoice_no());
            stmt.setString(3, dto.getCustomer_name());
            stmt.setString(4, dto.getCustomer_phno());
            stmt.setString(5, dto.getCustomer_details());
            stmt.setString(6, dto.getItemName());
            stmt.setString(7, dto.getPacking());
            stmt.setString(8, dto.getHsnCode());
            stmt.setDouble(9, dto.getQuantity());
            stmt.setDouble(10, dto.getRate());
            stmt.setDouble(11, dto.getDiscPer());
            stmt.setDouble(12, dto.getDiscAmount());
            stmt.setDouble(13, dto.getTaxableAmount());
            stmt.setDouble(14, dto.getCgst());
            stmt.setDouble(15, dto.getCgst_amount());
            stmt.setDouble(16, dto.getSgst());
            stmt.setDouble(17, dto.getSgst_amount());
            stmt.setDouble(18, dto.getNet_total());
            stmt.setString(19, dto.getBill_status());
            stmt.setString(20, dto.getBilled_by());
            stmt.setDouble(21, dto.getDeductionAmount());
            stmt.setDouble(22, dto.getBillAmount());
            stmt.setString(23, dto.getDispatchDetails());
            stmt.setString(24, dto.getPaymentDetails());
            stmt.setString(25, dto.getPaymentType());

            status = !stmt.execute();
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  status;
    }

    public long insert2(ManualBillingDto dto){
        boolean status = false;
        long key = 0;
        try{
            connection = DBConnect.connect();
            query = "INSERT INTO "+tableNaame+" ( " +
                    " date, invoice_no, customer_name, customer_phno, customer_details, " +
                    " itemName, packing, hsnCode, quantity, rate, " +
                    " discPer, discAmount, taxableAmount, cgst, cgst_amount, " +
                    " sgst, sgst_amount, net_total, bill_status, billed_by, " +
                    " deductionAmount, billAmount, dispatchDetails, paymentDetails, paymentType " +
                    " ) values (   ?,?,?,?,?,  ?,?,?,?,?,    ?,?,?,?,?,    ?,?,?,?,?,    ?,?,?,?,?   )";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dto.getDate());
            stmt.setInt(2, dto.getInvoice_no());
            stmt.setString(3, dto.getCustomer_name());
            stmt.setString(4, dto.getCustomer_phno());
            stmt.setString(5, dto.getCustomer_details());
            stmt.setString(6, dto.getItemName());
            stmt.setString(7, dto.getPacking());
            stmt.setString(8, dto.getHsnCode());
            stmt.setDouble(9, dto.getQuantity());
            stmt.setDouble(10, dto.getRate());
            stmt.setDouble(11, dto.getDiscPer());
            stmt.setDouble(12, dto.getDiscAmount());
            stmt.setDouble(13, dto.getTaxableAmount());
            stmt.setDouble(14, dto.getCgst());
            stmt.setDouble(15, dto.getCgst_amount());
            stmt.setDouble(16, dto.getSgst());
            stmt.setDouble(17, dto.getSgst_amount());
            stmt.setDouble(18, dto.getNet_total());
            stmt.setString(19, dto.getBill_status());
            stmt.setString(20, dto.getBilled_by());
            stmt.setDouble(21, dto.getDeductionAmount());
            stmt.setDouble(22, dto.getBillAmount());
            stmt.setString(23, dto.getDispatchDetails());
            stmt.setString(24, dto.getPaymentDetails());
            stmt.setString(25, dto.getPaymentType());

            status = !stmt.execute();
            if (status){
                ResultSet set = stmt.getGeneratedKeys();
                if(set.next()){ key = set.getLong(1);}
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  key;
    }

    public boolean delete(String condition){
        boolean status = false;
        try{
            connection = DBConnect.connect();
            query = "DELETE from "+tableNaame+" where id>0 "+condition;
            status = !connection.createStatement().execute(query);
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  status;
    }

    public boolean update(ManualBillingDto dto){
        boolean status = false;
        try{
            connection = DBConnect.connect();
            query = "UPDATE "+tableNaame+" set " +
                    " date=?, invoice_no=?, customer_name=?, customer_phno=?, customer_details=?, " +
                    " itemName=?, packing=?, hsnCode=?, quantity=?, rate=?, " +
                    " discPer=?, discAmount=?, taxableAmount=?, cgst=?, cgst_amount=?, " +
                    " sgst=?, sgst_amount=?, net_total=?, bill_status=?, billed_by=?, " +
                    " deductionAmount=?, billAmount=?, dispatchDetails=?, paymentDetails=?, paymentType=? " +
                    " where id="+dto.getId();
             PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dto.getDate());
            stmt.setInt(2, dto.getInvoice_no());
            stmt.setString(3, dto.getCustomer_name());
            stmt.setString(4, dto.getCustomer_phno());
            stmt.setString(5, dto.getCustomer_details());
            stmt.setString(6, dto.getItemName());
            stmt.setString(7, dto.getPacking());
            stmt.setString(8, dto.getHsnCode());
            stmt.setDouble(9, dto.getQuantity());
            stmt.setDouble(10, dto.getRate());
            stmt.setDouble(11, dto.getDiscPer());
            stmt.setDouble(12, dto.getDiscAmount());
            stmt.setDouble(13, dto.getTaxableAmount());
            stmt.setDouble(14, dto.getCgst());
            stmt.setDouble(15, dto.getCgst_amount());
            stmt.setDouble(16, dto.getSgst());
            stmt.setDouble(17, dto.getSgst_amount());
            stmt.setDouble(18, dto.getNet_total());
            stmt.setString(19, dto.getBill_status());
            stmt.setString(20, dto.getBilled_by());
            stmt.setDouble(21, dto.getDeductionAmount());
            stmt.setDouble(22, dto.getBillAmount());
            stmt.setString(23, dto.getDispatchDetails());
            stmt.setString(24, dto.getPaymentDetails());
            stmt.setString(25, dto.getPaymentType());

            status = !stmt.execute();
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  status;
    }

    public boolean update(String columns ,  String conditions){
        boolean status = false;
        try{
            connection = DBConnect.connect();
            query = "UPDATE "+tableNaame+" set "+columns+" where id>0 "+conditions;
            status = !connection.createStatement().execute(query);
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  status;
    }

    public ManualBillingDto select(String conditions){
        ManualBillingDto dto = new ManualBillingDto();
        try{
            connection = DBConnect.connect();
            query = "Select * from "+tableNaame+" where id>0 "+conditions;
            ResultSet set = connection.createStatement().executeQuery(query);
            if(set.next()){
                dto = new ManualBillingDto();
                dto.setId(set.getInt("id"));
                dto.setDate(set.getString("date"));
                dto.setInvoice_no(set.getInt("invoice_no"));
                dto.setCustomer_name(set.getString("customer_name"));
                dto.setCustomer_phno(set.getString("customer_phno"));
                dto.setCustomer_details(set.getString("customer_details"));
                dto.setItemName(set.getString("itemName"));
                dto.setPacking(set.getString("packing"));
                dto.setHsnCode(set.getString("hsnCode"));
                dto.setQuantity(set.getDouble("quantity"));
                dto.setRate(set.getDouble("rate"));
                dto.setDiscPer(set.getDouble("discPer"));
                dto.setDiscAmount(set.getDouble("discAmount"));
                dto.setTaxableAmount(set.getDouble("taxableAmount"));
                dto.setCgst(set.getDouble("cgst"));
                dto.setCgst_amount(set.getDouble("cgst_amount"));
                dto.setSgst(set.getDouble("sgst"));
                dto.setSgst_amount(set.getDouble("sgst_amount"));
                dto.setNet_total(set.getDouble("net_total"));
                dto.setBill_status(set.getString("bill_status"));
                dto.setBilled_by(set.getString("billed_by"));
                dto.setDeductionAmount(set.getDouble("deductionAmount"));
                dto.setBillAmount(set.getDouble("billAmount"));
                dto.setDispatchDetails(set.getString("dispatchDetails"));
                dto.setPaymentDetails(set.getString("paymentDetails"));
                dto.setPaymentType(set.getString("paymentType"));
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  dto;
    }

    public ArrayList<ManualBillingDto> selectAll(String conditions){
        ArrayList<ManualBillingDto> dtos = new ArrayList<>();
        ManualBillingDto dto = new ManualBillingDto();
        try{
            connection = DBConnect.connect();
            query = "Select * from "+tableNaame+" where id>0 "+conditions;
            ResultSet set = connection.createStatement().executeQuery(query);
            while(set.next()){
                dto = new ManualBillingDto();
                dto.setId(set.getInt("id"));
                dto.setDate(set.getString("date"));
                dto.setInvoice_no(set.getInt("invoice_no"));
                dto.setCustomer_name(set.getString("customer_name"));
                dto.setCustomer_phno(set.getString("customer_phno"));
                dto.setCustomer_details(set.getString("customer_details"));
                dto.setItemName(set.getString("itemName"));
                dto.setPacking(set.getString("packing"));
                dto.setHsnCode(set.getString("hsnCode"));
                dto.setQuantity(set.getDouble("quantity"));
                dto.setRate(set.getDouble("rate"));
                dto.setDiscPer(set.getDouble("discPer"));
                dto.setDiscAmount(set.getDouble("discAmount"));
                dto.setTaxableAmount(set.getDouble("taxableAmount"));
                dto.setCgst(set.getDouble("cgst"));
                dto.setCgst_amount(set.getDouble("cgst_amount"));
                dto.setSgst(set.getDouble("sgst"));
                dto.setSgst_amount(set.getDouble("sgst_amount"));
                dto.setNet_total(set.getDouble("net_total"));
                dto.setBill_status(set.getString("bill_status"));
                dto.setBilled_by(set.getString("billed_by"));
                dto.setDeductionAmount(set.getDouble("deductionAmount"));
                dto.setBillAmount(set.getDouble("billAmount"));
                dto.setDispatchDetails(set.getString("dispatchDetails"));
                dto.setPaymentDetails(set.getString("paymentDetails"));
                dto.setPaymentType(set.getString("paymentType"));

                dtos.add(dto);
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  dtos;
    }

    public ResultSet select(Connection connection, String columns, String conditions) {
        ResultSet set = null;
        try {
            query = "select " + columns + " from "+tableNaame+" where id>0 " + conditions;
            set = connection.createStatement().executeQuery(query);
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }
        return set;
    }

    public double getAmount(String columns, String conditions) {
        double amount = 0;
        Connection connection = null;
        try {
            connection = DBConnect.connect();
            ResultSet set = select(connection, columns, conditions);
            if (set.next()){
                amount = set.getDouble(1);
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            try {   if(!connection.isClosed()) { connection.close(); }  } catch (SQLException e) {  e.printStackTrace();    }
            return amount;
        }
    }

    public int getMax(String columns, String conditions) {
        int no = 0;
        Connection connection = null;
        try {
            connection = DBConnect.connect();
            ResultSet set = select(connection, columns, conditions);
            if (set.next()){
                no = set.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            try {   if(!connection.isClosed()) { connection.close(); }  } catch (SQLException e) {  e.printStackTrace();    }
            return no;
        }
    }

}

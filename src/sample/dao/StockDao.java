package sample.dao;

import sample.DBConnect;
import sample.P;
import sample.dto.StockDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Mitra on 10/24/18.
 */
public class StockDao {

    public Connection connection = null;
    public  String query = "";
    public final String tableNaame = "stock";

    public boolean insert(StockDto dto){
        boolean status = false;
        try{
            connection = DBConnect.connect();
            query = "INSERT INTO "+tableNaame+" ( " +
                    " productcode, hsn, stockaddeddate, productname, stockquantity, " +
                    " added_quantity, purchaseprice, mrp, cgst, sgst, " +
                    " total_amount, dealer_name, purchase_discount, selling_price, p_invoice,  " +
                    " packing " +
                    " ) values (   ?,?,?,?,?,  ?,?,?,?,?,    ?,?,?,?,?,   ?   )";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dto.getProductcode());
            stmt.setString(2, dto.getHsn());
            stmt.setString(3, dto.getStockaddeddate());
            stmt.setString(4, dto.getProductname());
            stmt.setDouble(5, dto.getStockquantity());
            stmt.setDouble(6, dto.getAdded_quantity());
            stmt.setDouble(7, dto.getPurchaseprice());
            stmt.setDouble(8, dto.getMrp());
            stmt.setDouble(9, dto.getCgst());
            stmt.setDouble(10, dto.getSgst());
            stmt.setDouble(11, dto.getTotal_amount());
            stmt.setString(12, dto.getDealer_name());
            stmt.setDouble(13, dto.getPurchase_discount());
            stmt.setDouble(14, dto.getSelling_price());
            stmt.setString(15, dto.getP_invoice());
            stmt.setString(16, dto.getPacking());
            status = !stmt.execute();
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  status;
    }

    public long insert2(StockDto dto){
        boolean status = false;
        long key = 0;
        try{
            connection = DBConnect.connect();
            query = "INSERT INTO "+tableNaame+" ( " +
                    " productcode, hsn, stockaddeddate, productname, stockquantity, " +
                    " added_quantity, purchaseprice, mrp, cgst, sgst, " +
                    " total_amount, dealer_name, purchase_discount, selling_price, p_invoice,  " +
                    " packing " +
                    " ) values (   ?,?,?,?,?,  ?,?,?,?,?,    ?,?,?,?,?,   ?   )";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dto.getProductcode());
            stmt.setString(2, dto.getHsn());
            stmt.setString(3, dto.getStockaddeddate());
            stmt.setString(4, dto.getProductname());
            stmt.setDouble(5, dto.getStockquantity());
            stmt.setDouble(6, dto.getAdded_quantity());
            stmt.setDouble(7, dto.getPurchaseprice());
            stmt.setDouble(8, dto.getMrp());
            stmt.setDouble(9, dto.getCgst());
            stmt.setDouble(10, dto.getSgst());
            stmt.setDouble(11, dto.getTotal_amount());
            stmt.setString(12, dto.getDealer_name());
            stmt.setDouble(13, dto.getPurchase_discount());
            stmt.setDouble(14, dto.getSelling_price());
            stmt.setString(15, dto.getP_invoice());
            stmt.setString(16, dto.getPacking());

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

    public boolean update(StockDto dto){
        boolean status = false;
        try{
            connection = DBConnect.connect();
            query = "UPDATE "+tableNaame+" set " +
                    " productcode=?, hsn=?, stockaddeddate=?, productname=?, stockquantity=?, " +
                    " added_quantity=?, purchaseprice=?, mrp=?, cgst=?, sgst=?, " +
                    " total_amount=?, dealer_name=?, purchase_discount=?, selling_price=?, p_invoice=?,  " +
                    " packing=? " +
                    " where id="+dto.getId();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dto.getProductcode());
            stmt.setString(2, dto.getHsn());
            stmt.setString(3, dto.getStockaddeddate());
            stmt.setString(4, dto.getProductname());
            stmt.setDouble(5, dto.getStockquantity());
            stmt.setDouble(6, dto.getAdded_quantity());
            stmt.setDouble(7, dto.getPurchaseprice());
            stmt.setDouble(8, dto.getMrp());
            stmt.setDouble(9, dto.getCgst());
            stmt.setDouble(10, dto.getSgst());
            stmt.setDouble(11, dto.getTotal_amount());
            stmt.setString(12, dto.getDealer_name());
            stmt.setDouble(13, dto.getPurchase_discount());
            stmt.setDouble(14, dto.getSelling_price());
            stmt.setString(15, dto.getP_invoice());
            stmt.setString(16, dto.getPacking());

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

    public StockDto select(String conditions){
        StockDto dto = new StockDto();
        try{
            connection = DBConnect.connect();
            query = "Select * from "+tableNaame+" where id>0 "+conditions;
            ResultSet set = connection.createStatement().executeQuery(query);
            if(set.next()){
                dto = new StockDto();
                dto.setProductcode(set.getString("productcode"));
                dto.setHsn(set.getString("hsn"));
                dto.setStockaddeddate(set.getString("stockaddeddate"));
                dto.setProductname(set.getString("productname"));
                dto.setStockquantity(set.getDouble("stockquantity"));
                dto.setAdded_quantity(set.getDouble("added_quantity"));
                dto.setPurchaseprice(set.getDouble("purchaseprice"));
                dto.setMrp(set.getDouble("mrp"));
                dto.setCgst(set.getDouble("cgst"));
                dto.setSgst(set.getDouble("sgst"));
                dto.setTotal_amount(set.getDouble("total_amount"));
                dto.setDealer_name(set.getString("dealer_name"));
                dto.setId(set.getInt("id"));
                dto.setPurchase_discount(set.getDouble("purchase_discount"));
                dto.setSelling_price(set.getDouble("selling_price"));
                dto.setP_invoice(set.getString("p_invoice"));
                dto.setPacking(set.getString("packing"));

            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  dto;
    }

    public ArrayList<StockDto> selectAll(String conditions){
        ArrayList<StockDto> dtos = new ArrayList<>();
        StockDto dto = new StockDto();
        try{
            connection = DBConnect.connect();
            query = "Select * from "+tableNaame+" where id>0 "+conditions;
            ResultSet set = connection.createStatement().executeQuery(query);
            while(set.next()){
                dto = new StockDto();
                dto.setProductcode(set.getString("productcode"));
                dto.setHsn(set.getString("hsn"));
                dto.setStockaddeddate(set.getString("stockaddeddate"));
                dto.setProductname(set.getString("productname"));
                dto.setStockquantity(set.getDouble("stockquantity"));
                dto.setAdded_quantity(set.getDouble("added_quantity"));
                dto.setPurchaseprice(set.getDouble("purchaseprice"));
                dto.setMrp(set.getDouble("mrp"));
                dto.setCgst(set.getDouble("cgst"));
                dto.setSgst(set.getDouble("sgst"));
                dto.setTotal_amount(set.getDouble("total_amount"));
                dto.setDealer_name(set.getString("dealer_name"));
                dto.setId(set.getInt("id"));
                dto.setPurchase_discount(set.getDouble("purchase_discount"));
                dto.setSelling_price(set.getDouble("selling_price"));
                dto.setP_invoice(set.getString("p_invoice"));
                dto.setPacking(set.getString("packing"));

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

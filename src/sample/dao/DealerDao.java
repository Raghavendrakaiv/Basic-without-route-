package sample.dao;

import sample.DBConnect;
import sample.P;
import sample.dto.DealerDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Mitra on 10/24/18.
 */
public class DealerDao {

    public Connection connection = null;
    public  String query = "";
    public final String tableNaame = "dealer";

    public boolean insert(DealerDto dto){
        boolean status = false;
        try{
            connection = DBConnect.connect();
            query = "INSERT INTO "+tableNaame+" ( " +
                    " dealer_id, dealer_name, dealer_gstn_number, dealer_contact_number, dealer_pan, " +
                    " dealer_account_details, dealer_address " +
                    " ) values (   ?,?,?,?,?,  ?,?  )";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, dto.getDealer_id());
            stmt.setString(2, dto.getDealer_name());
            stmt.setString(3, dto.getDealer_gstn_number());
            stmt.setString(4, dto.getDealer_contact_number());
            stmt.setString(5, dto.getDealer_pan());
            stmt.setString(6, dto.getDealer_account_details());
            stmt.setString(7, dto.getDealer_address());

            status = !stmt.execute();
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  status;
    }

    public long insert2(DealerDto dto){
        boolean status = false;
        long key = 0;
        try{
            connection = DBConnect.connect();
            query = "INSERT INTO "+tableNaame+" ( " +
                    " dealer_id, dealer_name, dealer_gstn_number, dealer_contact_number, dealer_pan, " +
                    " dealer_account_details, dealer_address " +
                    " ) values (   ?,?,?,?,?,  ?,?  )";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, dto.getDealer_id());
            stmt.setString(2, dto.getDealer_name());
            stmt.setString(3, dto.getDealer_gstn_number());
            stmt.setString(4, dto.getDealer_contact_number());
            stmt.setString(5, dto.getDealer_pan());
            stmt.setString(6, dto.getDealer_account_details());
            stmt.setString(7, dto.getDealer_address());
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

    public boolean update(DealerDto dto){
        boolean status = false;
        try{
            connection = DBConnect.connect();
            query = "UPDATE "+tableNaame+" set " +
                    " dealer_id=?, dealer_name=?, dealer_gstn_number=?, dealer_contact_number=?, dealer_pan=?, " +
                    " dealer_account_details=?, dealer_address=? " +
                    " where id="+dto.getId();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, dto.getDealer_id());
            stmt.setString(2, dto.getDealer_name());
            stmt.setString(3, dto.getDealer_gstn_number());
            stmt.setString(4, dto.getDealer_contact_number());
            stmt.setString(5, dto.getDealer_pan());
            stmt.setString(6, dto.getDealer_account_details());
            stmt.setString(7, dto.getDealer_address());

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

    public DealerDto select(String conditions){
        DealerDto dto = new DealerDto();
        try{
            connection = DBConnect.connect();
            query = "Select * from "+tableNaame+" where id>0 "+conditions;
            ResultSet set = connection.createStatement().executeQuery(query);
            if(set.next()){
                dto = new DealerDto();
                dto.setId(set.getInt("id"));
                dto.setDealer_id(set.getInt("dealer_id"));
                dto.setDealer_name(set.getString("dealer_name"));
                dto.setDealer_gstn_number(set.getString("dealer_gstn_number"));
                dto.setDealer_contact_number(set.getString("dealer_contact_number"));
                dto.setDealer_pan(set.getString("dealer_pan"));
                dto.setDealer_account_details(set.getString("dealer_account_details"));
                dto.setDealer_address(set.getString("dealer_address"));

            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  dto;
    }

    public ArrayList<DealerDto> selectAll(String conditions){
        ArrayList<DealerDto> dtos = new ArrayList<>();
        DealerDto dto = new DealerDto();
        try{
            connection = DBConnect.connect();
            query = "Select * from "+tableNaame+" where id>0 "+conditions;
            ResultSet set = connection.createStatement().executeQuery(query);
            while(set.next()){
                dto = new DealerDto();
                dto.setId(set.getInt("id"));
                dto.setDealer_id(set.getInt("dealer_id"));
                dto.setDealer_name(set.getString("dealer_name"));
                dto.setDealer_gstn_number(set.getString("dealer_gstn_number"));
                dto.setDealer_contact_number(set.getString("dealer_contact_number"));
                dto.setDealer_pan(set.getString("dealer_pan"));
                dto.setDealer_account_details(set.getString("dealer_account_details"));
                dto.setDealer_address(set.getString("dealer_address"));
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


}

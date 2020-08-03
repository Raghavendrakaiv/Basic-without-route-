package sample.dao;

import sample.DBConnect;
import sample.P;
import sample.dto.GstDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Mitra on 09/11/18.
 */
public class GstDao {

    public Connection connection = null;
    public  String query = "";
    public final String tableNaame = "gst";

    public boolean insert(GstDto dto){
        boolean status = false;
        try{
            connection = DBConnect.connect();
            query = "INSERT INTO "+tableNaame+" ( type, amount ) values (?,?) ";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dto.getType());
            stmt.setDouble(2, dto.getAmount());

            status = !stmt.execute();
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  status;
    }

    public long insert2(GstDto dto){
        boolean status = false;
        long key = 0;
        try{
            connection = DBConnect.connect();
            query = "INSERT INTO "+tableNaame+" ( type, amount ) values (?,?) ";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dto.getType());
            stmt.setDouble(2, dto.getAmount());

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
            query = "DELETE from "+tableNaame+" where amount=>0 "+condition;
            status = !connection.createStatement().execute(query);
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  status;
    }

    public boolean update(GstDto dto, String condition){
        boolean status = false;
        try{
            connection = DBConnect.connect();
            query = "UPDATE "+tableNaame+" set type=?, amount=? where "+condition;
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, dto.getType());
            stmt.setDouble(2, dto.getAmount());

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
            query = "UPDATE gst "+tableNaame+" "+columns+" where amount>=0 "+conditions;
            status = !connection.createStatement().execute(query);
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  status;
    }

    public GstDto select(String conditions){
        GstDto dto = new GstDto();
        try{
            connection = DBConnect.connect();
            query = "Select * from "+tableNaame+" where amount>=0 "+conditions;
            ResultSet set = connection.createStatement().executeQuery(query);
            if(set.next()){
                dto = new GstDto();
                dto.setType(set.getString("type"));
                dto.setAmount(set.getDouble("amount"));
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  dto;
    }

    public ArrayList<GstDto> selectAll(String conditions){
        ArrayList<GstDto> dtos = new ArrayList<>();
        GstDto dto = new GstDto();
        try{
            connection = DBConnect.connect();
            query = "Select * from "+tableNaame+" where amount>=0 "+conditions;
            ResultSet set = connection.createStatement().executeQuery(query);
            while(set.next()){
                dto = new GstDto();
                dto.setType(set.getString("type"));
                dto.setAmount(set.getDouble("amount"));

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

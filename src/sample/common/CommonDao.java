package sample.common;

import sample.DBConnect;
import sample.P;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Mitra on 10/01/18.
 */
public class CommonDao {

    Connection connection = null;
    String query = "";

    public ArrayList<ArrayList> selectAll(String columns , String tables, String conditions){
        ArrayList<String> data = null;
        ArrayList<ArrayList> datas = new ArrayList<ArrayList>();
        try{
            connection = DBConnect.connect();
            query = "Select "+columns+" from "+tables+" "+conditions;
            P.p("query from commonDao : "+query);
            ResultSet set = connection.createStatement().executeQuery(query);
            int columnCount = set.getMetaData().getColumnCount();
            while(set.next()){
                data = new ArrayList<String>();
                for(int i=1; i<=columnCount; i++){
                    data.add(i-1,set.getString(i));
                }
                datas.add(data);
            }
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally { try { connection.close(); } catch (SQLException e) { e.printStackTrace(); } }
        return  datas;
    }

}

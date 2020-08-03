package sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LiveSearchMe {

    public static ArrayList makeSearch(String query, String columnName)  {
        Connection c = null;
        ArrayList<String> words = new ArrayList<String>();
        try {
            c = DBConnect.connect();
            Statement stm = c.createStatement();
            ResultSet rs1 = stm.executeQuery(query);
            while (rs1.next()) {
                words.add(rs1.getString(columnName));
            }
            return words;
        }catch (Exception e){
            e.printStackTrace();
            P.loggerLoader(e);
        }finally {
            try {
                if(!c.isClosed()){ c.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return words;
        }

    }
}

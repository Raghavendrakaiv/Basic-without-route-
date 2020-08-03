package sample.common;

import javafx.scene.control.ComboBox;
import sample.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class LiveSearchMe {

    public static ArrayList makeSearch(String query, String columnName) throws Exception {
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
            if(!c.isClosed()){ c.close(); }
            return words;
        }
    }


    public static void makeSearchOnCombo(ComboBox comboBox, ArrayList data1) {
        new AutoCompleteComboBoxListener(comboBox, data1);
    }
}

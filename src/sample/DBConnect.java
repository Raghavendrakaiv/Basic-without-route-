package sample;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnect {

    private static Connection conn;
    private static String url = "jdbc:mysql://localhost:3306/new_basic_bill";

    private static String user = "root";
    private static String pass = "root";

    public static Connection connect() throws Exception {
        try{
            System.out.println("Here i called "+P.ipaddress_name().toString());
            System.out.println("Posting to git here");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(ClassNotFoundException cnfe){
            System.err.println("Error: "+cnfe.getMessage());
        }catch(InstantiationException ie){
            System.err.println("Error: "+ie.getMessage());
        }catch(IllegalAccessException iae){
            System.err.println("Error: "+iae.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String databaseName=P.dname();
        String dUser=P.duser();
        String dPword=P.dpword();
        if(P.ipaddress_name() != null){
            url = "jdbc:mysql://"+P.ipaddress_name()+"/"+databaseName;
            P.p("  url    :  "+url);
        }

        System.out.println("this is the url of the filnal "+url);
        try {
            conn = DriverManager.getConnection(url, dUser, dPword);
            P.p("connection--->" + conn);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            P.loggerLoader(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ERROR IN DATABASE CONNECTION '"+url+"'");
            alert.showAndWait();
        }
        return conn;
    }
    public static Connection getConnection() throws Exception {
        if(conn !=null && !conn.isClosed())
            return conn;
        connect();
        return conn;
    }

    public static void close() throws Exception {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            P.loggerLoader(e);
        }
    }
}
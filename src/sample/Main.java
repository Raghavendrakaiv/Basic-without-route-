package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.activation.WinRegistry;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static sample.activation.WinRegistry.HKEY_CURRENT_USER;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //createing json file in c drive
        try {
            boolean json_1= new File(FilePath.JSON_PATH_1).exists();
            P.p("mitra***************-----"+json_1);
            if(json_1==false)
            {
                new File(FilePath.JSON_PATH_1).mkdir();
                new File(FilePath.JSON_PATH_2).mkdir();
                String FILE=FilePath.JSON_PATH_2+"/db.txt";
                PrintWriter writer = new PrintWriter(FILE);
                writer.println("{");
                writer.print("\n\"DRIVE\": \"D:/\",");
                writer.println("\n");
                writer.print("\n\"DUSER\": \"root\",");
                writer.println("\n");
                writer.print("\n\"DPWORD\": \"root\",");
                writer.println("\n");
                writer.print("\n\"DNAME\": \"EXPRESS_BILLING_KMS\",");
                writer.println("\n");
                writer.println("\n\"IP_ADDRESS\": \"localhost\"");
                writer.println("}");
                writer.close();
            }

            new File(P.drive_name() + FilePath.FOLDER_PATH).mkdir();
            String FILE = P.drive_name() + FilePath.FOLDER_PATH + "/output.txt";
            PrintWriter writer = new PrintWriter(FILE);
            writer.print("");
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);
        }
        //end of writing json file
        //checking of file
        boolean test_file=true;
        try {
            String a=P.ipaddress_name();
            String b=P.drive_name();
            if(a.trim().isEmpty() || b.trim().isEmpty())
            {
                test_file=false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(!test_file)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("IP_DRIVE_CONFIG FILE IS NOT FOUND");
            alert.showAndWait();
        }
        //end of checking file

        Connection con = null;
        try {

            con = DBConnect.connect();

            Statement statement = con.createStatement();

            ResultSet res = statement.executeQuery("Select * from activate where status='success'");
            int i = 0;

            while (res.next()) {
                i++;
            }

            if (i > 0)
            {
                try {
                    String value = WinRegistry.readString(
                            HKEY_CURRENT_USER,                             //HKEY
                            "IRSRCB",           //Key
                            "Dial");                                              //ValueName

                    System.out.println("Windows Distribution = " + value);

                    if (value.equals("independent"))
                    {
                        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
                       // makeFrameFullSize(900);

                        /*Image icon = new Image("/img/logo.png");
                        primaryStage.getIcons().add(icon);*/
                        primaryStage.setTitle("Express Billing");
                        primaryStage.setScene(new Scene(root, 900, 700));
                        primaryStage.show();
                    }
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("This is pirated");
                        alert.setContentText("You are a victim of software. Your complaint has been registered in Mitra Softwares");
                        alert.showAndWait();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            } else {
                Parent root = FXMLLoader.load(getClass().getResource("activation/activity_cd_key.fxml"));
                primaryStage.setTitle("Express Billing");
                primaryStage.setScene(new Scene(root, 900, 700));
                primaryStage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }


}

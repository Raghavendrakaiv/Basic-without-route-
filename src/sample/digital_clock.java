package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class digital_clock extends Label {

    public static DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm:ss a");

    int count=0;

    public digital_clock(Label l) throws Exception {
        bindToTime(l);
        data_backup();
    }

    public void bindToTime(Label l) {
       Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
            event -> l.setText(LocalTime.now().format(SHORT_TIME_FORMATTER))),
            new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void data_backup() throws Exception {
        Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(15), event -> {
            try {
                P.p("back up method is calling");
                backupDataWithDatabase();
            }
            catch (Exception e) {
            e.printStackTrace();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static boolean backupDataWithDatabase() throws Exception{
        //to get path
        String p2 = null,p3=null;
        Connection con=null;
        con=DBConnect.getConnection();
        ResultSet rs=con.createStatement().executeQuery("select @@basedir");
        while (rs.next())
        {
            p2=rs.getString("@@basedir");
        }
        p3=p2.replace("\\","/");
        //end of
        String dumpExePath=p3+"/bin/mysqldump.exe";
        String host=P.ipaddress_name();
        String port="3306";
        String user=P.duser();
        String password=P.dpword();
        String database=P.dname();

        Month month = Controller.today1.getMonth();
        int year = Controller.today1.getYear();
        int day = Controller.today1.getDayOfMonth();

        //creating folder
        new File(P.drive_name()+FilePath.FOLDER_PATH).mkdir();
        new File(P.drive_name()+FilePath.DATA_BACKUP_PATH).mkdir();
        new File(P.drive_name()+FilePath.DATA_BACKUP_PATH+year).mkdir();
        new File(P.drive_name()+FilePath.DATA_BACKUP_PATH+year+"/"+month.name()).mkdir();
        new File(P.drive_name()+FilePath.DATA_BACKUP_PATH+year+"/"+month.name()+"/"+day).mkdir();


        String backupPath=P.drive_name()+FilePath.DATA_BACKUP_PATH+year+"/"+month.name()+"/"+day+"/" ;

        boolean status = false;
        try {
            Process p = null;

            String filepath = database+"_backup.sql";

            String batchCommand = "";
            if (password != "") {
                //Backup with database
                batchCommand = dumpExePath + " -h " + host + " -u " + user + " --password=" + password + " " + database + " -r \"" + backupPath + "" + filepath + "\"";
            } else {
                batchCommand = dumpExePath + " -h " + host + " -u " + user + " " + database + " -r \"" + backupPath + "" + filepath + "\"";
            }

            P.p("batchCommand  :  "+batchCommand);
            Runtime runtime = Runtime.getRuntime();
            p = runtime.exec(batchCommand);
            int processComplete=0;
            try{
                processComplete = p.waitFor();

            }catch (Exception e)
            {
                e.printStackTrace();
            }

            if (processComplete == 0) {
                status = true;
                System.out.println("Backup created successfully for with DB " + database + " in " + host + ":" + port);
            } else {
                status = false;
                System.out.println("Could not create the backup for with DB " + database + " in " + host + ":" + port);
            }

        } catch (IOException ioe) {
            System.out.println(ioe.getCause());
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
        return status;
    }


    /*public static boolean backupDataWithDatabase() throws Exception {
        String database_file_Path=P.drive_name()+FilePath.DATABASE_PATH;
        File databaseFile=new File(database_file_Path);

        new File(P.drive_name()+FilePath.FOLDER_PATH).mkdir();
        new File(P.drive_name()+FilePath.BACKUP_PATH).mkdir();
        copyFileUsingStream(new File(database_file_Path),new File(P.drive_name()+FilePath.BACKUP_PATH+"bill_Back_up.db"));
        return true;
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }*/
}
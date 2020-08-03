package sample;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by kings on 04-12-17.
 */
public class Logger {
    public static void log(String message) throws Exception {

        String FILE=P.drive_name()+FilePath.FOLDER_PATH+"/output.txt";
        PrintWriter out = new PrintWriter(new FileWriter(FILE, true), true);
        out.write(message);
        out.close();
    }
}
package sample.common;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sample.FilePath;
import sample.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;

/**
 * Created by kings on 01-12-17.
 */
public class P {

    public static DecimalFormat df3 = new DecimalFormat("#.##");

    public  static void p(String string) {
        try {
            System.out.println(string);
        }catch (Exception e){ P.loggerLoader("Error while P.p()"); }
    }

    public static String df00(double number) {

        String number_string = df3.format(number);
        int i = number_string.indexOf(".");
        if (i < 0) {
            number_string = number_string + ".00";
            return number_string;
        }
        if (i >= 0) {
            String number_bb = number_string.substring(number_string.indexOf(".") + 1, number_string.length());
            int length = number_bb.length();
            if (length == 1) {
                return number_string + "0";
            }
            if (length == 2) {
                return number_string;
            }
            if (length > 2) {
                return number_string.substring(0, number_string.indexOf(".") + 2);
            }

            return number_string;
        }
        return "";
    }

    public static String tC(double number) {

        String number_string = df3.format(number);
        int i = number_string.indexOf(".");
        if (i < 0) {
            number_string = number_string + ".00";
            return number_string;
        }
        if (i >= 0) {
            String number_bb = number_string.substring(number_string.indexOf(".") + 1, number_string.length());
            int length = number_bb.length();
            if (length == 1) {
                return number_string + "0";
            }
            if (length == 2) {
                return number_string;
            }
            if (length > 2) {
                return number_string.substring(0, number_string.indexOf(".") + 2);
            }

            return number_string;
        }
        return "";
    }

    public static String df00(String number) {

        String number_string = df3.format(Double.parseDouble(number));
        int i = number_string.indexOf(".");
        if (i < 0) {
            number_string = number_string + ".00";
            return number_string;
        }
        if (i >= 0) {
            String number_bb = number_string.substring(number_string.indexOf(".") + 1, number_string.length());
            int length = number_bb.length();
            if (length == 1) {
                return number_string + "0";
            }
            if (length == 2) {
                return number_string;
            }
            if (length > 2) {
                return number_string.substring(0, number_string.indexOf(".") + 2);
            }
            return number_string;
        }
        return "";
    }

    public static String drive_name() throws Exception {
        return getDataFromIPtext("DRIVE");
    }

    public static String dname() throws Exception {
        return getDataFromIPtext("DNAME");
    }

    public static String duser() throws Exception {
        return getDataFromIPtext("DUSER");
    }

    public static String dpword() throws Exception {
        return getDataFromIPtext("DPWORD");
    }

    public static String ipaddress_name() throws Exception {
        return getDataFromIPtext("IP_ADDRESS");
    }

    public static String getDataFromIPtext(String key) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        String s = new String(Files.readAllBytes(Paths.get(FilePath.JSON_PATH_2+"db.txt")));
        Object obj = parser.parse(s);
        String d= String.valueOf(((JSONObject) obj).get(key));
        System.out.println(d);
        return d;
    }

    static String aa="\n--------------------------------00000-------------------------------------\n";
    public static void loggerLoader(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        try {
            Logger.log(aa+exceptionAsString+aa);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    public static void loggerLoader(String message) {
        try {
            Logger.log(aa+message+aa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PdfPCell createCell(String data, Font font, int aligment, int border, DottedCell dcell) {
        Paragraph par8 = new Paragraph(new Chunk(data, font));
        par8.setAlignment(aligment);
        PdfPCell Cel8 = new PdfPCell(par8);
        Cel8.setBorder(border);
        Cel8.setCellEvent(dcell);
        Cel8.setHorizontalAlignment(aligment);
        return Cel8;
    }

    public static PdfPCell createCell(String data, Font font, int aligment, int border)
    {        Paragraph par8 = new Paragraph(new Chunk(data, font));
        par8.setAlignment(aligment);
        PdfPCell Cel8 = new PdfPCell(par8);
        Cel8.setBorder(border);
        Cel8.setHorizontalAlignment(aligment);
        return Cel8;

    }


}

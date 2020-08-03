package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;


public class CreditCustomerReport {
    private static DecimalFormat df3 = new DecimalFormat("#.##");
    public static float totalrep1;
    public static String atot;


    static void createPdf(String query) throws Exception {
        Connection c = null;

        try {
            c = DBConnect.connect();
            String pattern = "dd-MM-yyyy";

            float totalFinalAmountrep1 = 0;

            File dir = new File(P.drive_name()+FilePath.FOLDER_PATH);
            dir.mkdir();
            File dir2=new File(P.drive_name()+FilePath.FOLDER_PATH+"CREDIT_CUSTOMER_REPORT");
            dir2.mkdir();

            String path=P.drive_name()+FilePath.FOLDER_PATH+"/CREDIT_CUSTOMER_REPORT/";

            String FILE = path+"/"+"credit_customer_report" + ".pdf";

            Document doc = new Document(PageSize.A4);

            PdfWriter.getInstance(doc, new FileOutputStream(FILE));
            doc.open();
            Font f = FontFactory.getFont(FontFactory.TIMES_BOLD, 10, Font.BOLD, BaseColor.BLACK);
            Paragraph p = new Paragraph("CREDIT CUSTOMER REPORT\n\n", f);
            p.setAlignment(Element.ALIGN_CENTER);
            doc.add(p);


            PdfPTable table = new PdfPTable(5);
            PdfPCell c4 = new PdfPCell(new Phrase("Sl No.", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c4);

            PdfPCell c1 = new PdfPCell(new Phrase("Customer ID", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(c1);
            PdfPCell c2 = new PdfPCell(new Phrase("Customer Name", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c2);

            PdfPCell c3 = new PdfPCell(new Phrase("Balance Amount", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c3);

            PdfPCell c5 = new PdfPCell(new Phrase("Extra Amount", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c5);

            table.completeRow();
            table.getDefaultCell().setFixedHeight(20);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.setWidthPercentage(100);
            table.completeRow();

            String sql = query;

            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            double co=0;
            while (rs.next()) {

                String sp4 = rs.getString("sp_id");
                PdfPCell sp14 = new PdfPCell(new Phrase(sp4, new Font(Font.FontFamily.TIMES_ROMAN, 8)));

                String sp1 = rs.getString("customer_id");
                PdfPCell sp11 = new PdfPCell(new Phrase(sp1, new Font(Font.FontFamily.TIMES_ROMAN, 8)));

                String sp2 = rs.getString("customer_name");
                PdfPCell sp12 = new PdfPCell(new Phrase(sp2, new Font(Font.FontFamily.TIMES_ROMAN, 8)));

                String sp3 = rs.getString("sp_bal_amount");
                PdfPCell sp13 = new PdfPCell(new Phrase(sp3, new Font(Font.FontFamily.TIMES_ROMAN, 8)));

                String sp5 = rs.getString("sp_extra_amount");
                PdfPCell sp15 = new PdfPCell(new Phrase(sp5, new Font(Font.FontFamily.TIMES_ROMAN, 8)));

                String individualQuery = "Select SUM(sp_bal_amount) from sp_table";
                try {
                    ResultSet totalQuery = c.createStatement().executeQuery(individualQuery);
                    while (totalQuery.next()) {
                        co = totalQuery.getDouble(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsString = sw.toString();
                    Logger.log(exceptionAsString);
                }

                totalFinalAmountrep1 = totalFinalAmountrep1 + totalrep1;
                table.addCell(sp14);
                table.addCell(sp11);
                table.addCell(sp12);
                table.addCell(sp13);
                table.addCell(sp15);

            }
            String stringR2 = df3.format(co);

            PdfPCell sp1totFinal1 = new PdfPCell(new Phrase(stringR2, new Font(Font.FontFamily.TIMES_ROMAN, 8)));

            table.addCell("Total");
            table.addCell("");
            table.addCell("");

            table.addCell(sp1totFinal1);

            rs.close();
            doc.add(table);
            doc.close();
            c.close();

            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File(FILE);
                    Desktop.getDesktop().open(myFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsString = sw.toString();
                    Logger.log(exceptionAsString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.log(exceptionAsString);

        } finally {
            try {
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
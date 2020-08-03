package sample.common;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;

/**
 * Created by rajath on 07-06-2018.
 */
public class PdfWork {

    public static PdfPCell createCell1(String text , Font font, int border, int alignment){
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk(text, font));
        paragraph.setAlignment(alignment);
        PdfPCell cell = new PdfPCell(paragraph);
        cell.setBorder(border);
        cell.setHorizontalAlignment(alignment);
        return cell;
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

    public static String toCamelCase(final String init) {
        if (init==null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length()==init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }

}

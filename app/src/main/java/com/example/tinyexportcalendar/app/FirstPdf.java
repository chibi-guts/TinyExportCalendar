package com.example.tinyexportcalendar.app;

import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class FirstPdf {
    private static String FILE = "c:/temp/FirstPdf.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 8,
            Font.BOLD);
    private static Font smallNormal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
            Font.NORMAL);
    private static Font smallNormalGray = new Font(Font.FontFamily.TIMES_ROMAN, 8,
            Font.NORMAL, BaseColor.GRAY);
    private static Font smallNormalWhite = new Font(Font.FontFamily.TIMES_ROMAN, 8,
            Font.NORMAL, BaseColor.WHITE);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    public static void main(String[] args) {
        try {
            Document document = new Document();
            FILE = Environment.getExternalStorageDirectory().getAbsolutePath()
                    +"/documents/mydatesfile.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addMetaData(document);
            addContent(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document) {
        document.addTitle("PDF calendar table");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Maria Kadukova");
        document.addCreator("Maria Kadukova");
    }

    private static void addContent(Document document) throws DocumentException {
        HashMap<Integer, Integer> keysMap= new HashMap<Integer, Integer>();
        Set<Integer> years = new HashSet<Integer>();
        Map<String,?> keys = MainActivity.sharedPref.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values", entry.getKey() + ": " +
                    entry.getValue());
            years.add((int)(Integer.decode(entry.getKey().split("_")[0])/12));
        }

        for(Integer entry : years){
            Log.d("PDF-years", String.valueOf(entry));
            document.add(createTable(entry));
        }
    }

    private static PdfPTable createTable(int year)
            throws DocumentException {
        GregorianCalendar pdfRefCalendar = new GregorianCalendar();
        pdfRefCalendar.set(Calendar.YEAR, MainActivity.YEAR_MIN+year);
        PdfPTable table = new PdfPTable(32);
        table.setHeaderRows(1);
        int color;
        int monthBorders;
        int[] monthDates = new int[31];
        Set<String> curArr = new HashSet<String>();

        Set<String> def = new HashSet<String>();
        for (int i=0; i<12; i++) {
            pdfRefCalendar.set(Calendar.MONTH, i);
            pdfRefCalendar.set(Calendar.DAY_OF_MONTH, 1);
            monthBorders = pdfRefCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)-1;
            Arrays.fill(monthDates, 0);
            for(int t=0; t<3; t++) {
                curArr = MainActivity.sharedPref.getStringSet(String.valueOf((year)*12+i)+'_'+t, def);
                for(String entry: curArr){
                    monthDates[Integer.decode(entry)] = t+1;
                }
            }

            PdfPCell c1 = new PdfPCell(new Phrase(MainActivity.monthNames[i], smallNormal));
            table.addCell(c1);
            table.setWidths(new int[]{2, 1,1,1,1,1,1,1,
                    1,1,1,1,1,1,1,1,
                    1,1,1,1,1,1,1,1,
                    1,1,1,1,1,1,1,1});
            Font chosenFont;
            for (int j = 0; j < 31; j++) {
                chosenFont = smallNormal;
                color = Color.WHITE;
                if(monthDates[j]>0)
                    color = MainActivity.markingColors[monthDates[j]-1];
                if(j > monthBorders)
                    chosenFont = smallNormalWhite;
                Phrase phrase = new Phrase(cutString(String.valueOf(j+1)), chosenFont);
                c1 = new PdfPCell(phrase);
                c1.setBackgroundColor(new BaseColor(Color.red(color), Color.green(color), Color.blue(color)));
                table.addCell(c1);
            }
        }
        return table;
    }

    static String cutString(String num) {
        switch (num.length()) {
            case 1:
                return " " + num;
            case 2:
                return num;
            default:
                return "  ";
        }
    }

    static int[] parseStringArray(String[] strArr){
        int[] res = new int[strArr.length];
        for(int a=0; a<strArr.length; a++){
            res[a] = Integer.parseInt(strArr[a]);
        }
        return res;
    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
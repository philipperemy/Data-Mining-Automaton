package fr.automated.trading.systems.utils.excel;

import fr.automated.trading.systems.utils.utils.Utils;
import jxl.LabelCell;
import jxl.Workbook;
import jxl.write.*;

import java.io.File;
import java.io.IOException;

public class ExcelAPI {

    WritableWorkbook workbook;
    WritableSheet sheet;

    public ExcelAPI(String outputFilename) {
        try {
            workbook = Workbook.createWorkbook(new File(outputFilename));
            sheet = workbook.createSheet("First Sheet", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void colorBad(int i, int j) {
        try {
            WritableCell c = sheet.getWritableCell(i,j);
            WritableCellFormat newFormat = new WritableCellFormat(c.getCellFormat());
            newFormat.setBackground(Colour.LIME);
            c.setCellFormat(newFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void colorGood(int i, int j) {
        try {
            WritableCell c = sheet.getWritableCell(1,1);
            WritableCellFormat newFormat = new WritableCellFormat(c.getCellFormat());
            newFormat.setBackground(Colour.GOLD);
            c.setCellFormat(newFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(int i, int j, String record) {
        try {
            Label label = new Label(i,j, record);
            sheet.addCell(label);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(int i, int j, double record) {
        write(i,j, String.valueOf(Utils.truncate(record,3)));
    }

    public void write(int i, int j, boolean bool) {
        if(bool)
            write(i,j, "true");
        else
            write(i,j, "false");
    }

    public void write(int i, int j, int record) {
        write(i,j, String.valueOf(Utils.truncate(record,3)));
    }

    public String read(int i, int j) {
        LabelCell cell = (LabelCell) sheet.getCell(i,j);
        return cell.getString();
    }

    public void close() {
        try {
            sheet.getSettings().setDefaultColumnWidth(16);
            workbook.write();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }
}

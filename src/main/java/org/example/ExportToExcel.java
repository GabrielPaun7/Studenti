package org.example;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExportToExcel implements Exporter {
    private final String filename;

    public ExportToExcel(String filename) {
        this.filename = filename;
    }

    @Override
    public void export(ArrayList<Student> studenti) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Student Details");

            int rowNum = 0;

            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("Numar matricol");
            header.createCell(1).setCellValue("Prenume");
            header.createCell(2).setCellValue("Nume");
            header.createCell(3).setCellValue("Formatie de studiu");

            for (Student s : studenti) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(s.numarMatricol == null ? "" : s.numarMatricol);
                row.createCell(1).setCellValue(s.prenume);
                row.createCell(2).setCellValue(s.nume);
                row.createCell(3).setCellValue(s.formatieDeStudiu);
            }

            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(filename)) {
                workbook.write(out);
                System.out.println("Export Excel realizat in fisierul: " + filename);
            }
        } catch (IOException e) {
            System.out.println("Eroare la exportul Excel: " + e.getMessage());
        }
    }
}

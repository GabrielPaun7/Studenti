package org.example;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ExportToExcel implements Exporter {

    private final String filename;

    public ExportToExcel(String filename) {
        this.filename = filename;
    }

    @Override
    public void export(ArrayList<Student> studenti) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            scrieStudenti(workbook, studenti);
            scrieSemigrupe(workbook, studenti);
            scrieStatisticiSemigrupe(workbook, studenti);

            try (FileOutputStream fileOut = new FileOutputStream(filename)) {
                workbook.write(fileOut);
            }

            System.out.println("Export Excel realizat: " + filename);

        } catch (IOException e) {
            System.out.println("Eroare la exportul Excel: " + e.getMessage());
        }
    }

    private void scrieStudenti(XSSFWorkbook workbook, ArrayList<Student> studenti) {
        XSSFSheet sheet = workbook.createSheet("Studenti");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Numar matricol");
        header.createCell(1).setCellValue("Prenume");
        header.createCell(2).setCellValue("Nume");
        header.createCell(3).setCellValue("Formatie de studiu");
        header.createCell(4).setCellValue("Nota");

        Catalog catalog = Catalog.getInstance();

        for (int i = 0; i < studenti.size(); i++) {
            Student s = studenti.get(i);
            Row row = sheet.createRow(i + 1);

            row.createCell(0).setCellValue(text(s.numarMatricol));
            row.createCell(1).setCellValue(text(s.prenume));
            row.createCell(2).setCellValue(text(s.nume));
            row.createCell(3).setCellValue(text(s.formatieDeStudiu));

            // Luăm nota din Catalog. Dacă e -1, înseamnă că nu are notă.
            int nota = catalog.getNotaStudent(s.numarMatricol);
            if (nota != -1) {
                row.createCell(4).setCellValue(nota);
            } else {
                row.createCell(4).setCellValue("-");
            }
        }
    }

    private void scrieSemigrupe(XSSFWorkbook workbook, ArrayList<Student> studenti) {
        XSSFSheet sheet = workbook.createSheet("Semigrupe");

        ArrayList<Student> sortati = studenti.stream()
                .sorted((s1, s2) -> {
                    int cmp = text(s1.nume).compareToIgnoreCase(text(s2.nume));
                    return cmp == 0 ? text(s1.prenume).compareToIgnoreCase(text(s2.prenume)) : cmp;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        int jumatate = sortati.size() / 2;

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Semigrupa");
        header.createCell(1).setCellValue("Numar matricol");
        header.createCell(2).setCellValue("Prenume");
        header.createCell(3).setCellValue("Nume");
        header.createCell(4).setCellValue("Formatie de studiu");
        header.createCell(5).setCellValue("Nota");

        Catalog catalog = Catalog.getInstance();

        for (int i = 0; i < sortati.size(); i++) {
            Student s = sortati.get(i);
            Row row = sheet.createRow(i + 1);

            row.createCell(0).setCellValue(i < jumatate ? "Semigrupa 1" : "Semigrupa 2");
            row.createCell(1).setCellValue(text(s.numarMatricol));
            row.createCell(2).setCellValue(text(s.prenume));
            row.createCell(3).setCellValue(text(s.nume));
            row.createCell(4).setCellValue(text(s.formatieDeStudiu));

            int nota = catalog.getNotaStudent(s.numarMatricol);
            if (nota != -1) {
                row.createCell(5).setCellValue(nota);
            } else {
                row.createCell(5).setCellValue("-");
            }
        }
    }

    private void scrieStatisticiSemigrupe(XSSFWorkbook workbook, ArrayList<Student> studenti) {
        XSSFSheet sheet = workbook.createSheet("Statistici semigrupe");

        ArrayList<Student> sortati = studenti.stream()
                .sorted((s1, s2) -> {
                    int cmp = text(s1.nume).compareToIgnoreCase(text(s2.nume));
                    return cmp == 0 ? text(s1.prenume).compareToIgnoreCase(text(s2.prenume)) : cmp;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        int jumatate = sortati.size() / 2;

        ArrayList<Student> semigrupa1 = sortati.stream()
                .limit(jumatate)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Student> semigrupa2 = sortati.stream()
                .skip(jumatate)
                .collect(Collectors.toCollection(ArrayList::new));

        int rand = 0;
        rand = scrieStatisticaPentruSemigrupa(sheet, rand, "Semigrupa 1", semigrupa1);
        rand += 2;
        scrieStatisticaPentruSemigrupa(sheet, rand, "Semigrupa 2", semigrupa2);
    }

    private int scrieStatisticaPentruSemigrupa(XSSFSheet sheet, int rand, String numeSemigrupa, ArrayList<Student> listaStudenti) {
        Catalog catalog = Catalog.getInstance();

        Row titlu = sheet.createRow(rand++);
        titlu.createCell(0).setCellValue(numeSemigrupa);

        // Calculăm media semigrupei
        double media = listaStudenti.stream()
                .mapToInt(s -> catalog.getNotaStudent(s.numarMatricol))
                .filter(nota -> nota != -1)
                .average()
                .orElse(0);

        Row mediaRow = sheet.createRow(rand++);
        mediaRow.createCell(0).setCellValue("Media");
        mediaRow.createCell(1).setCellValue(media);

        Row header = sheet.createRow(rand++);
        header.createCell(0).setCellValue("Nota");
        header.createCell(1).setCellValue("Numar studenti");

        // Generăm histograma pentru semigrupă
        Map<Integer, Long> histograma = listaStudenti.stream()
                .mapToInt(s -> catalog.getNotaStudent(s.numarMatricol))
                .filter(nota -> nota != -1)
                .boxed()
                .collect(Collectors.groupingBy(nota -> nota, TreeMap::new, Collectors.counting()));

        for (Map.Entry<Integer, Long> entry : histograma.entrySet()) {
            Row row = sheet.createRow(rand++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
        }

        return rand;
    }

    private String text(String valoare) {
        return valoare == null ? "" : valoare.trim();
    }
}
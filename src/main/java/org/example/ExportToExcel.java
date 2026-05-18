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
            scrieHistogramaGenerala(workbook, studenti);
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

        for (int i = 0; i < studenti.size(); i++) {
            Student s = studenti.get(i);

            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(text(s.numarMatricol));
            row.createCell(1).setCellValue(text(s.prenume));
            row.createCell(2).setCellValue(text(s.nume));
            row.createCell(3).setCellValue(text(s.formatieDeStudiu));

            if (s.nota != null) {
                row.createCell(4).setCellValue(s.nota);
            } else {
                row.createCell(4).setCellValue("fara nota");
            }
        }

        autosize(sheet, 4);
    }

    private void scrieHistogramaGenerala(XSSFWorkbook workbook, ArrayList<Student> studenti) {
        XSSFSheet sheet = workbook.createSheet("Histograma note");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Nota");
        header.createCell(1).setCellValue("Numar studenti");
        header.createCell(2).setCellValue("Histograma");

        Map<Integer, Long> histograma = calculeazaHistograma(studenti);

        int rand = 1;

        for (Map.Entry<Integer, Long> entry : histograma.entrySet()) {
            Row row = sheet.createRow(rand++);

            int nota = entry.getKey();
            long numarStudenti = entry.getValue();

            row.createCell(0).setCellValue(nota);
            row.createCell(1).setCellValue(numarStudenti);
            row.createCell(2).setCellValue("*".repeat((int) numarStudenti));
        }

        Row mediaRow = sheet.createRow(rand + 1);
        mediaRow.createCell(0).setCellValue("Media generala");
        mediaRow.createCell(1).setCellValue(calculeazaMedia(studenti));

        autosize(sheet, 2);
    }

    private void scrieSemigrupe(XSSFWorkbook workbook, ArrayList<Student> studenti) {
        XSSFSheet sheet = workbook.createSheet("Semigrupe");

        ArrayList<Student> sortati = studenti.stream()
                .sorted((s1, s2) -> {
                    int cmp = text(s1.nume).compareToIgnoreCase(text(s2.nume));

                    if (cmp == 0) {
                        return text(s1.prenume).compareToIgnoreCase(text(s2.prenume));
                    }

                    return cmp;
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

        for (int i = 0; i < sortati.size(); i++) {
            Student s = sortati.get(i);

            Row row = sheet.createRow(i + 1);

            if (i < jumatate) {
                row.createCell(0).setCellValue("Semigrupa 1");
            } else {
                row.createCell(0).setCellValue("Semigrupa 2");
            }

            row.createCell(1).setCellValue(text(s.numarMatricol));
            row.createCell(2).setCellValue(text(s.prenume));
            row.createCell(3).setCellValue(text(s.nume));
            row.createCell(4).setCellValue(text(s.formatieDeStudiu));

            if (s.nota != null) {
                row.createCell(5).setCellValue(s.nota);
            } else {
                row.createCell(5).setCellValue("fara nota");
            }
        }

        autosize(sheet, 5);
    }

    private void scrieStatisticiSemigrupe(XSSFWorkbook workbook, ArrayList<Student> studenti) {
        XSSFSheet sheet = workbook.createSheet("Statistici semigrupe");

        ArrayList<Student> sortati = studenti.stream()
                .sorted((s1, s2) -> {
                    int cmp = text(s1.nume).compareToIgnoreCase(text(s2.nume));

                    if (cmp == 0) {
                        return text(s1.prenume).compareToIgnoreCase(text(s2.prenume));
                    }

                    return cmp;
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

        autosize(sheet, 3);
    }

    private int scrieStatisticaPentruSemigrupa(XSSFSheet sheet, int rand, String numeSemigrupa, ArrayList<Student> studenti) {
        Row titlu = sheet.createRow(rand++);
        titlu.createCell(0).setCellValue(numeSemigrupa);

        Row media = sheet.createRow(rand++);
        media.createCell(0).setCellValue("Media");
        media.createCell(1).setCellValue(calculeazaMedia(studenti));

        Row header = sheet.createRow(rand++);
        header.createCell(0).setCellValue("Nota");
        header.createCell(1).setCellValue("Numar studenti");
        header.createCell(2).setCellValue("Histograma");

        Map<Integer, Long> histograma = calculeazaHistograma(studenti);

        for (Map.Entry<Integer, Long> entry : histograma.entrySet()) {
            Row row = sheet.createRow(rand++);

            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
            row.createCell(2).setCellValue("*".repeat(entry.getValue().intValue()));
        }

        return rand;
    }

    private Map<Integer, Long> calculeazaHistograma(ArrayList<Student> studenti) {
        return studenti.stream()
                .filter(s -> s.nota != null)
                .collect(Collectors.groupingBy(
                        s -> s.nota,
                        TreeMap::new,
                        Collectors.counting()
                ));
    }

    private double calculeazaMedia(ArrayList<Student> studenti) {
        return studenti.stream()
                .filter(s -> s.nota != null)
                .mapToInt(s -> s.nota)
                .average()
                .orElse(0);
    }

    private String text(String valoare) {
        if (valoare == null) {
            return "";
        }

        return valoare.trim();
    }

    private void autosize(XSSFSheet sheet, int ultimaColoana) {
        for (int i = 0; i <= ultimaColoana; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;

public class Main {

    // Create a new workbook
    static XSSFWorkbook workbook = new XSSFWorkbook();

    // Create a sheet
    static XSSFSheet sheet = workbook.createSheet("Student Details");

    public static void main(String[] args) {

        Importer importer = getImporterFromFile("studenti.csv");

        ArrayList<Student> studenti = importList(importer);

        Map<String, Integer> note = citireNote("Note.csv");
        Map<Student, Integer> noteStudenti = createMap(studenti, note);

        index(studenti);


        exportList(studenti, getExporterToFile("StudentData.csv"));
        exportList(studenti, getExporterToFile("StudentData.xlsx"));

        System.out.println("Lista studentilor:");
        for (Student s : studenti) {
            System.out.println(s + " | numar matricol: " + s.numarMatricol + " | nota: " + printNota(noteStudenti, s));
        }

        Student s = new Student(null, "Alex", "Doc0b", "C22/1");

        if (estePrezent(studenti, s)) {
            System.out.println("Studentul este prezent in lista.");
            System.out.println("Numar matricol: " + s.numarMatricol + " | nota: " + printNota(noteStudenti, s));
        } else {
            System.out.println("Studentul NU este prezent in lista.");
        }
    }

    private static boolean estePrezent(ArrayList<Student> studenti, Student s) {
        return false;
    }

    private static String printNota(Map<Student, Integer> noteStudenti, Student s) {
        return "";
    }

    private static void index(ArrayList<Student> studenti) {
    }

    private static Map<Student, Integer> createMap(ArrayList<Student> studenti, Map<String, Integer> note) {
        return Map.of();
    }

    private static Map<String, Integer> citireNote(String s) {
        return Map.of();
    }

    private static Importer getImporterFromFile(String filename) {
        String fileExtension = filename.substring(filename.lastIndexOf("."));

        switch (fileExtension) {
            case ".xlsx":
                return new ImportFromExcel(filename);
            case ".csv":
                return new ImportFromFile(filename);
            case ".txt":
                return new ImportFromFile(filename);
            default:
                throw new IllegalArgumentException("Unknown file extension: " + fileExtension);
        }
    }

    private static Exporter getExporterToFile(String filename) {
        String fileExtension = filename.substring(filename.lastIndexOf("."));

        switch (fileExtension) {
            case ".xlsx":
                return new ExportToExcel(filename);
            case ".csv":
                return new ExportToFile(filename);
            default:
                throw new IllegalArgumentException("Unknown file extension: " + fileExtension);
        }
    }

    private static ArrayList<Student> importList(Importer importer) {
        return importer.importStudents();
    }

    private static void exportList(ArrayList<Student> studenti, Exporter exporter) {
        exporter.export(studenti);
    }

    static ArrayList<Student> citesteStudentiDinCsv(String caleFisier) {
        ArrayList<Student> studenti = new ArrayList<>();

        try {
            for (String linie : Files.readAllLines(Path.of(caleFisier))) {
                if (linie.isBlank()) {
                    continue;
                }

                String[] campuri = linie.split(",");

                if (campuri.length != 4) {
                    System.out.println("Linie invalida: " + linie);
                    continue;
                }

                String numarMatricol = campuri[0].trim();
                if (numarMatricol.isEmpty()) {
                    numarMatricol = null;
                }

                String prenume = campuri[1].trim();
                String nume = campuri[2].trim();
                String formatieDeStudiu = campuri[3].trim();

                studenti.add(new Student(numarMatricol, prenume, nume, formatieDeStudiu));
            }
        } catch (IOException e) {
            System.out.println("Eroare la citirea fisierului: " + e.getMessage());
        }

        return studenti;
    }


}
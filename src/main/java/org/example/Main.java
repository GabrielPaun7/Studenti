package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        Importer importer = getImporterFromFile("studenti.csv");

        ArrayList<Student> studenti = importList(importer);

        Map<String, Integer> note = citireNote("Note.csv");

        adaugaNoteStudentilor(studenti, note);

        rezolvaCerinteStream(studenti);


        exportList(studenti, getExporterToFile("StudentData.csv"));
        exportList(studenti, getExporterToFile("StudentData.xlsx"));

        System.out.println("\nLista studentilor:");
        for (Student s : studenti) {
            System.out.println(s);
        }

        Student s = new Student("001", "Alex", "Oprea", "TDDH221/1");

        if (estePrezent(studenti, s)) {
            System.out.println("\nStudentul este prezent in lista.");
        } else {
            System.out.println("\nStudentul NU este prezent in lista.");
        }
    }

    private static boolean estePrezent(ArrayList<Student> studenti, Student s) {
        return studenti.stream()
                .anyMatch(student ->
                        egal(student.numarMatricol, s.numarMatricol)
                                || (
                                egal(student.prenume, s.prenume)
                                        && egal(student.nume, s.nume)
                                        && egal(student.formatieDeStudiu, s.formatieDeStudiu)
                        )
                );
    }

    private static boolean egal(String a, String b) {
        if (a == null || b == null) {
            return false;
        }

        return a.trim().equalsIgnoreCase(b.trim());
    }

    private static void adaugaNoteStudentilor(ArrayList<Student> studenti, Map<String, Integer> note) {
        for (Student s : studenti) {
            if (s.numarMatricol == null) {
                continue;
            }

            String numarMatricol = s.numarMatricol.trim();

            if (note.containsKey(numarMatricol)) {
                s.nota = note.get(numarMatricol);
            }
        }
    }

    private static Map<String, Integer> citireNote(String numeFisier) {
        Map<String, Integer> note = new HashMap<>();

        try {
            for (String linie : Files.readAllLines(Path.of(numeFisier))) {
                if (linie.isBlank()) {
                    continue;
                }

                String[] campuri = linie.split("[,;]");

                if (campuri.length < 2) {
                    continue;
                }

                String numarMatricol = campuri[0].trim();
                String notaText = campuri[1].trim();

                if (numarMatricol.isEmpty()) {
                    System.out.println("Linie ignorata, lipseste numarul matricol: " + linie);
                    continue;
                }

                try {
                    int nota = Integer.parseInt(notaText);

                    if (nota < 1 || nota > 10) {
                        System.out.println("Nota invalida, trebuie sa fie intre 1 si 10: " + linie);
                        continue;
                    }

                    note.put(numarMatricol, nota);
                } catch (NumberFormatException e) {
                    System.out.println("Nota invalida pe linia: " + linie);
                }
            }
        } catch (IOException e) {
            System.out.println("Eroare la citirea notelor: " + e.getMessage());
        }

        return note;
    }

    private static void rezolvaCerinteStream(ArrayList<Student> studenti) {

        System.out.println("\n1. Studenti cu nota 10:");

        studenti.stream()
                .filter(s -> s.nota != null)
                .filter(s -> s.nota == 10)
                .forEach(System.out::println);

        System.out.println("\n2. ID-uri / numere matricole ale studentilor cu nota <= 4:");

        studenti.stream()
                .filter(s -> s.nota != null)
                .filter(s -> s.nota <= 4)
                .map(s -> s.numarMatricol)
                .forEach(System.out::println);

        System.out.println("\n3. Media notelor:");

        double media = studenti.stream()
                .filter(s -> s.nota != null)
                .mapToInt(s -> s.nota)
                .average()
                .orElse(0);

        System.out.println(media);

        System.out.println("\n4. Histograma notelor:");

        Map<Integer, Long> histograma = studenti.stream()
                .filter(s -> s.nota != null)
                .collect(Collectors.groupingBy(
                        s -> s.nota,
                        TreeMap::new,
                        Collectors.counting()
                ));

        histograma.forEach((nota, numarStudenti) ->
                System.out.println(nota + " -> " + numarStudenti)
        );

        System.out.println("\n5. Semigrupe sortate alfabetic:");

        ArrayList<Student> sortati = studenti.stream()
                .sorted(Comparator.comparing((Student s) -> text(s.nume), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(s -> text(s.prenume), String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toCollection(ArrayList::new));

        int jumatate = sortati.size() / 2;

        ArrayList<Student> semigrupa1 = sortati.stream()
                .limit(jumatate)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Student> semigrupa2 = sortati.stream()
                .skip(jumatate)
                .collect(Collectors.toCollection(ArrayList::new));

        System.out.println("\nSemigrupa 1:");
        semigrupa1.forEach(System.out::println);
        afiseazaMediaSiHistograma(semigrupa1);

        System.out.println("\nSemigrupa 2:");
        semigrupa2.forEach(System.out::println);
        afiseazaMediaSiHistograma(semigrupa2);
    }

    private static void afiseazaMediaSiHistograma(ArrayList<Student> studenti) {

        double media = studenti.stream()
                .filter(s -> s.nota != null)
                .mapToInt(s -> s.nota)
                .average()
                .orElse(0);

        System.out.println("Media: " + media);

        Map<Integer, Long> histograma = studenti.stream()
                .filter(s -> s.nota != null)
                .collect(Collectors.groupingBy(
                        s -> s.nota,
                        TreeMap::new,
                        Collectors.counting()
                ));

        System.out.println("Histograma:");

        histograma.forEach((nota, numarStudenti) ->
                System.out.println(nota + " -> " + numarStudenti)
        );
    }

    private static String text(String valoare) {
        if (valoare == null) {
            return "";
        }

        return valoare.trim();
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
}
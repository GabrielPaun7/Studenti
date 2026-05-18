package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Catalog {

    private static Catalog instance;
    private ArrayList<Student> studenti = new ArrayList<>();
    private Map<String, Integer> note = new HashMap<>();

    private Catalog() {} // Constructor privat pentru Singleton

    public static Catalog getInstance() {
        if (instance == null) instance = new Catalog();
        return instance;
    }
    public int getNotaStudent(String numarMatricol) {
        if (numarMatricol == null) return -1;
        return note.getOrDefault(numarMatricol.trim(), -1);
    }

    public void incarcaDate(String fisierStudenti, String fisierNote) {
        studenti = getImporterFromFile(fisierStudenti).importStudents();
        note = citireNote(fisierNote); // Notele rămân doar în această mapă
    }

    // Adăugat înapoi: Afișează studenții pe ecran
    public void afiseazaStudenti() {
        System.out.println("\nLista studentilor:");
        for (Student s : studenti) {
            System.out.println(s);
        }
    }

    // Adăugat înapoi: Verifică dacă studentul există deja
    public boolean estePrezent(Student s) {
        return studenti.stream()
                .anyMatch(student ->
                        (student.numarMatricol != null && student.numarMatricol.trim().equalsIgnoreCase(s.numarMatricol)) ||
                                (student.nume != null && student.nume.trim().equalsIgnoreCase(s.nume) &&
                                        student.prenume != null && student.prenume.trim().equalsIgnoreCase(s.prenume))
                );
    }

    // Adăugat înapoi: Exportă lista în fișier
    public void exporta(String filename) {
        getExporterToFile(filename).export(studenti);
    }

    public void Stream() {
        // 1. Studenți de nota 10
        System.out.println("\n1. Studenti cu nota 10:");
        studenti.stream()
                .filter(s -> notaStudent(s) == 10)
                .forEach(System.out::println);

        // 2. Restanțieri (nota <= 4)
        System.out.println("\n2. ID-uri studenti cu nota <= 4:");
        studenti.stream()
                .filter(s -> notaStudent(s) != -1 && notaStudent(s) <= 4)
                .map(s -> s.numarMatricol)
                .forEach(System.out::println);

        // 3. Media generală
        System.out.println("\n3. Media notelor:");
        double media = studenti.stream()
                .filter(s -> notaStudent(s) != -1)
                .mapToInt(this::notaStudent)
                .average()
                .orElse(0);
        System.out.println(media);

        // 4. Histograma
        System.out.println("\n4. Histograma notelor:");
        afiseazaStatistici(studenti);

        // 5. Împărțire în semigrupe (Sortat alfabetic)
        System.out.println("\n5. Semigrupe sortate alfabetic:");
        List<Student> sortati = studenti.stream()
                .sorted(Comparator.comparing((Student s) -> s.nume != null ? s.nume : "", String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(s -> s.prenume != null ? s.prenume : "", String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        int jumatate = sortati.size() / 2;
        if (jumatate > 0) {
            ArrayList<Student> sg1 = new ArrayList<>(sortati.subList(0, jumatate));
            ArrayList<Student> sg2 = new ArrayList<>(sortati.subList(jumatate, sortati.size()));

            System.out.println("\nSemigrupa 1:");
            sg1.forEach(System.out::println);
            afiseazaStatistici(sg1);

            System.out.println("\nSemigrupa 2:");
            sg2.forEach(System.out::println);
            afiseazaStatistici(sg2);
        }
    }

    // Metodă ajutătoare: ia nota din Map. Dacă nu are notă, returnează -1
    private int notaStudent(Student s) {
        if (s.numarMatricol == null) return -1;
        return note.getOrDefault(s.numarMatricol.trim(), -1);
    }

    private void afiseazaStatistici(ArrayList<Student> lista) {
        Map<Integer, Long> histograma = lista.stream()
                .filter(s -> notaStudent(s) != -1)
                .collect(Collectors.groupingBy(this::notaStudent, TreeMap::new, Collectors.counting()));

        histograma.forEach((nota, nr) -> System.out.println(nota + " -> " + nr));
    }

    private Map<String, Integer> citireNote(String numeFisier) {
        Map<String, Integer> noteCitite = new HashMap<>();
        try {
            for (String linie : Files.readAllLines(Path.of(numeFisier))) {
                if (linie.isBlank()) continue;
                String[] campuri = linie.split("[,;]");
                if (campuri.length < 2) continue;

                int nota = Integer.parseInt(campuri[1].trim());
                if (nota >= 1 && nota <= 10) {
                    noteCitite.put(campuri[0].trim(), nota);
                }
            }
        } catch (Exception e) {
            System.out.println("Eroare la citirea notelor.");
        }
        return noteCitite;
    }

    private Importer getImporterFromFile(String f) {
        return f.endsWith(".xlsx") ? new ImportFromExcel(f) : new ImportFromFile(f);
    }

    private Exporter getExporterToFile(String f) {
        return f.endsWith(".xlsx") ? new ExportToExcel(f) : new ExportToFile(f);
    }
}
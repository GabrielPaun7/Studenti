package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        ArrayList<Student> studenti = citesteStudentiDinCsv("studenti.csv");
        Map<String, Integer> note = citireNote("Note.csv");

        Map<Student, Integer> noteStudenti = createMap(studenti, note);

        index(studenti);

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

    static Map<String, Integer> citireNote(String filename) {
        Map<String, Integer> note = new HashMap<>();

        try {
            for (String linie : Files.readAllLines(Path.of(filename))) {
                if (linie.isBlank()) {
                    continue;
                }

                String[] campuri = linie.split(",");

                if (campuri.length != 2) {
                    System.out.println("Linie invalida: " + linie);
                    continue;
                }

                String numarMatricol = campuri[0].trim();
                if (numarMatricol.isEmpty()) {
                    numarMatricol = null;
                }

                int nota = Integer.parseInt(campuri[1].trim());
                note.put(numarMatricol, nota);
            }
        } catch (IOException e) {
            System.out.println("Eroare la citirea fisierului de note: " + e.getMessage());
        }

        return note;
    }

    static Map<Student, Integer> createMap(ArrayList<Student> studenti, Map<String, Integer> note) {
        Map<Student, Integer> noteStudenti = new HashMap<>();

        for (Student student : studenti) {
            noteStudenti.put(student, nota(note, student));
        }

        return noteStudenti;
    }

    static Integer nota(Map<String, Integer> note, Student student) {
        return note.get(student.numarMatricol);
    }

    static String printNota(Map<Student, Integer> noteStudenti, Student student) {
        Integer valoare = noteStudenti.get(student);
        if (valoare == null) {
            return "fara nota";
        }
        return String.valueOf(valoare);
    }

    static void index(ArrayList<Student> studenti) {
        Collections.sort(studenti, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                if (s1.formatieDeStudiu.equals(s2.formatieDeStudiu)) {
                    if (s1.nume.equals(s2.nume)) {
                        if (s1.prenume.equals(s2.prenume)) {
                            if (s1.numarMatricol == null && s2.numarMatricol == null) {
                                return 0;
                            }
                            if (s1.numarMatricol == null) {
                                return -1;
                            }
                            if (s2.numarMatricol == null) {
                                return 1;
                            }
                            return s1.numarMatricol.compareTo(s2.numarMatricol);
                        }
                        return s1.prenume.compareTo(s2.prenume);
                    }
                    return s1.nume.compareTo(s2.nume);
                }
                return s1.formatieDeStudiu.compareTo(s2.formatieDeStudiu);
            }
        });
    }

    static boolean estePrezent(ArrayList<Student> studenti, Student s) {
        return studenti.contains(s);
    }
}
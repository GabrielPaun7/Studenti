package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ImportFromFile implements Importer {
    private final String filename;

    public ImportFromFile(String filename) {
        this.filename = filename;
    }

    @Override
    public ArrayList<Student> importStudents() {
        ArrayList<Student> studenti = new ArrayList<>();

        try {
            for (String linie : Files.readAllLines(Path.of(filename))) {
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

            System.out.println("Import realizat din fisierul: " + filename);
        } catch (IOException e) {
            System.out.println("Eroare la import: " + e.getMessage());
        }

        return studenti;
    }
}

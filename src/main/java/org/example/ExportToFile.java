package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class ExportToFile implements Exporter {
    private final String filename;

    public ExportToFile(String filename) {
        this.filename = filename;
    }

    @Override
    public void export(ArrayList<Student> studenti) {
        ArrayList<String> linii = new ArrayList<>();

        for (Student s : studenti) {
            String linie =
                    (s.numarMatricol == null ? "" : s.numarMatricol) + "," +
                            s.prenume + "," +
                            s.nume + "," +
                            s.formatieDeStudiu;

            linii.add(linie);
        }

        try {
            Files.write(Path.of(filename), linii);
            System.out.println("Export realizat in fisierul: " + filename);
        } catch (IOException e) {
            System.out.println("Eroare la export: " + e.getMessage());
        }
    }
}

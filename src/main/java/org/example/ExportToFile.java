package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class ExportToFile implements Exporter {

    private final String filename;

    public ExportToFile(String filename) {
        this.filename = filename;
    }

    @Override
    public void export(ArrayList<Student> studenti) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {

            writer.println("Numar matricol,Prenume,Nume,Formatie de studiu,Nota");

            for (Student s : studenti) {
                writer.println(
                        safe(s.numarMatricol) + "," +
                                safe(s.prenume) + "," +
                                safe(s.nume) + "," +
                                safe(s.formatieDeStudiu) + "," +
                                safe(s.afisareNota())
                );
            }

            System.out.println("Export CSV realizat: " + filename);

        } catch (IOException e) {
            System.out.println("Eroare la exportul CSV: " + e.getMessage());
        }
    }

    private String safe(String text) {
        if (text == null) {
            return "";
        }

        if (text.contains(",") || text.contains("\"")) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }

        return text;
    }
}
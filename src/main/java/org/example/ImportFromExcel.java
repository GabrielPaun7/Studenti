package org.example;

import java.util.ArrayList;

public class ImportFromExcel implements Importer {
    private final String filename;


    public ImportFromExcel(String filename) {
        this.filename = filename;
    }

    @Override
    public ArrayList<Student> importStudents() {
        System.out.println("Importul din Excel nu este implementat inca pentru: " + filename);
        return new ArrayList<>();
    }
}

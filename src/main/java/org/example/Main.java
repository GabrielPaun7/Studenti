package org.example;

public class Main {

    public static void main(String[] args) {

        // 1. Preluăm instanța unică a catalogului (Singleton)
        Catalog catalog = Catalog.getInstance();

        // 2. Încărcăm datele separat (clasa Catalog se va ocupa intern de maparea lor)
        catalog.incarcaDate("studenti.csv", "Note.csv");

        // 3. Rulăm procesările statistice (Stream-uri, medii, histograme, semigrupe)
        catalog.Stream();

        // 4. Exportăm listele în format CSV și Excel
        catalog.exporta("StudentData.csv");
        catalog.exporta("StudentData.xlsx");

        // 5. Afișăm toată lista de studenți din sistem
        catalog.afiseazaStudenti();

        // 6. Verificăm prezența unui anumit student în baza de date
        // Observă că obiectul Student nu mai cere nicio notă în constructor!
        Student s = new Student("001", "Alex", "Oprea", "TDDH221/1");

        if (catalog.estePrezent(s)) {
            System.out.println("\nStudentul este prezent in lista.");
        } else {
            System.out.println("\nStudentul NU este prezent in lista.");
        }
    }
}
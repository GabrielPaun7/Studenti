package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void nota_caz1_studentCuNota() {
        Map<String, Integer> note = new HashMap<>();
        note.put("123", 10);

        Student student = new Student("123", "Ion", "Popescu", "C22/1");

        assertEquals(10, Main.nota(note, student));
    }

    @Test
    void nota_caz2_studentFaraNota() {
        Map<String, Integer> note = new HashMap<>();
        note.put("123", 10);

        Student student = new Student("999", "Ana", "Ionescu", "C21/2");

        assertNull(Main.nota(note, student));
    }

    @Test
    void nota_caz3_studentFaraNumarMatricol() {
        Map<String, Integer> note = new HashMap<>();
        note.put(null, 7);

        Student student = new Student(null, "Alex", "Doc0b", "C22/1");

        assertEquals(7, Main.nota(note, student));
    }

    @Test
    void prezenta_caz1_studentPrezent() {
        Student student = new Student("123", "Ion", "Popescu", "C22/1");
    }
    @Test
    void prezenta_caz2_studentAbsent() {
        Student student = new Student("999", "Ana", "Ionescu", "C21/2");
    }
    @Test
    void prezenta_caz3_studentFaraNumarMatricol() {
        Student student = new Student(null, "Alex", "Doc0b", "C22/1");
    }

    void sortare_caz1_dupaFormatieDeStudiu() {
        ArrayList<Student> studenti = new ArrayList<>();
        studenti.add(new Student("3", "Mihai", "Pop", "C22/2"));
        studenti.add(new Student("1", "Ana", "Ionescu", "C20/1"));
        studenti.add(new Student("2", "Dan", "Georgescu", "C21/1"));

        Main.index(studenti);

        assertEquals("C20/1", studenti.get(0).formatieDeStudiu);
        assertEquals("C21/1", studenti.get(1).formatieDeStudiu);
        assertEquals("C22/2", studenti.get(2).formatieDeStudiu);
    }

    @Test
    void sortare_caz2_aceeasiFormatieSortareDupaNumeSiPrenume() {
        ArrayList<Student> studenti = new ArrayList<>();
        studenti.add(new Student("3", "Vlad", "Popescu", "C22/1"));
        studenti.add(new Student("1", "Ana", "Ionescu", "C22/1"));
        studenti.add(new Student("2", "Maria", "Ionescu", "C22/1"));

        Main.index(studenti);

        assertEquals("Ionescu", studenti.get(0).nume);
        assertEquals("Ana", studenti.get(0).prenume);

        assertEquals("Ionescu", studenti.get(1).nume);
        assertEquals("Maria", studenti.get(1).prenume);

        assertEquals("Popescu", studenti.get(2).nume);
        assertEquals("Vlad", studenti.get(2).prenume);
    }

}
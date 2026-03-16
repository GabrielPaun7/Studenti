package org.example;

import java.util.Objects;

public class Student {
    String numarMatricol;
    String prenume;
    String nume;
    String formatieDeStudiu;
    int nota;

    public Student(String numarMatricol, String prenume, String nume, String formatieDeStudiu) {
        this.numarMatricol = numarMatricol;
        this.prenume = prenume;
        this.nume = nume;
        this.formatieDeStudiu = formatieDeStudiu;
    }

    public Student(String numarMatricol, String prenume, String nume, String formatieDeStudiu, int nota) {
        this.numarMatricol = numarMatricol;
        this.prenume = prenume;
        this.nume = nume;
        this.formatieDeStudiu = formatieDeStudiu;
        this.nota = nota;
    }

    @Override
    public String toString() {
        return numarMatricol + " " + prenume + " " + nume + " " + formatieDeStudiu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return Objects.equals(prenume, student.prenume)
                && Objects.equals(nume, student.nume)
                && Objects.equals(formatieDeStudiu, student.formatieDeStudiu)
                && Objects.equals(numarMatricol, student.numarMatricol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numarMatricol, prenume, nume, formatieDeStudiu);
    }
}
package org.example;

import java.util.Objects;

public class Student {
    public String numarMatricol;
    public String prenume;
    public String nume;
    public String formatieDeStudiu;



    public Student(String numarMatricol, String prenume, String nume, String formatieDeStudiu) {
        this.numarMatricol = numarMatricol;
        this.prenume = prenume;
        this.nume = nume;
        this.formatieDeStudiu = formatieDeStudiu;

    }

    public Student(String numarMatricol, String prenume, String nume, String formatieDeStudiu, Integer nota) {
        this.numarMatricol = numarMatricol;
        this.prenume = prenume;
        this.nume = nume;
        this.formatieDeStudiu = formatieDeStudiu;

    }



    @Override
    public String toString() {
        return "Student{" +
                "numarMatricol='" + numarMatricol + '\'' +
                ", prenume='" + prenume + '\'' +
                ", nume='" + nume + '\'' +
                ", formatieDeStudiu='" + formatieDeStudiu + '\'' +

                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Student student)) {
            return false;
        }

        return Objects.equals(numarMatricol, student.numarMatricol)
                && Objects.equals(prenume, student.prenume)
                && Objects.equals(nume, student.nume)
                && Objects.equals(formatieDeStudiu, student.formatieDeStudiu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numarMatricol, prenume, nume, formatieDeStudiu);
    }
}
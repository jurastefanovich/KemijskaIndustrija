package com.example.stefanovic.kemijskaindustrija.Threads;

import java.time.LocalDate;
//Primarily used to determine if a service is done
public class DateComparator implements Comparable<LocalDate>{
    public boolean compareTo(LocalDate o1, LocalDate o2) {
        return o1.isAfter(o2);
    }
    @Override
    public int compareTo(LocalDate o) {
        return 0;
    }
}

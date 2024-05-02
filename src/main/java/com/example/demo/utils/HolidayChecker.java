package com.example.demo.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class HolidayChecker {

    private static final Set<LocalDate> holidays = new HashSet<>();

    static {
        holidays.add(LocalDate.of(1, 1, 1)); //New Year
        holidays.add(LocalDate.of(1, 1, 2));
        holidays.add(LocalDate.of(1, 3, 8));// Female day
        holidays.add(LocalDate.of(1, 3, 21)); // Nauruz
        holidays.add(LocalDate.of(1, 3, 22));
        holidays.add(LocalDate.of(1, 3, 23));
        holidays.add(LocalDate.of(1, 5, 1));
        holidays.add(LocalDate.of(1, 5, 7));
        holidays.add(LocalDate.of(1, 5, 9));
        holidays.add(LocalDate.of(1, 7, 6));
        holidays.add(LocalDate.of(1, 8, 30));
        holidays.add(LocalDate.of(1, 10, 25));
        holidays.add(LocalDate.of(1, 12, 16));
    }

    public static boolean isHoliday() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();


        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return true;
        } else if (holidays.contains(LocalDate.of(1, today.getMonthValue(), today.getDayOfMonth()))) {
            return true;
        } else {
            return false;
        }
    }
}

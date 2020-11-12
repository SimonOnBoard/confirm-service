package com.itis.javalab.student.rabbitmq.documents.utils;

import java.sql.Date;
import java.util.Calendar;

public class DatesComparator {
    public static boolean compareDates(Date first, Date second) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(first);
        cal2.setTime(second);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}

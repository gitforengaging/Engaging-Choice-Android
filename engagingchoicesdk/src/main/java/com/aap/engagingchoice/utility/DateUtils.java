package com.aap.engagingchoice.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class is for DateUtils
 */
public class DateUtils {

    public static String getDate(String dateInString) {
        String newDate = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-d", Locale.getDefault());
        DateFormat format1 = new SimpleDateFormat("MMM d, yyyy ", Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dateInString);
            newDate = format1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(newDate); // Sat Jan 02 00:00:00 GMT 2010
        return newDate;
    }
}

package com.github.niketion.bansystem.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static long parseDuration(String duration) {
        if (duration.length() < 2) {
            throw new IllegalArgumentException("Invalid duration format. Must include a number followed by a unit (s/m/h/d).");
        }

        long timeValue = Long.parseLong(duration.substring(0, duration.length() - 1));
        char timeUnit = duration.charAt(duration.length() - 1);

        long millisToAdd;

        switch (timeUnit) {
            case 's':
                millisToAdd = timeValue * 1000L;
                break;
            case 'm':
                millisToAdd = timeValue * 1000L * 60L;
                break;
            case 'h':
                millisToAdd = timeValue * 1000L * 60L * 60L;
                break;
            case 'd':
                millisToAdd = timeValue * 1000L * 60L * 60L * 24L;
                break;
            default:
                throw new IllegalArgumentException("Invalid time unit. Use 's' for seconds, 'm' for minutes, 'h' for hours, or 'd' for days.");
        }

        return System.currentTimeMillis() + millisToAdd;
    }


    public static String formatToStartOfDay(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
        return formatter.format(date);
    }

}

package com.personal.wordmaster.utils;

import com.personal.wordmaster.constant.AppConstant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat DATE_FORMAT =
        new SimpleDateFormat(AppConstant.DATE_FORMAT, Locale.getDefault());

    public static String getTodayDate() {
        return DATE_FORMAT.format(new Date());
    }

    public static String formatDate(long timestamp) {
        return DATE_FORMAT.format(new Date(timestamp));
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static boolean isSameDay(long time1, long time2) {
        return formatDate(time1).equals(formatDate(time2));
    }
}

package com.thumbstage.hydrogen.utils;

import android.webkit.URLUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

    static DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static boolean isUrl(String url) {
        return URLUtil.isValidUrl(url);
    }

    public static Date string2Date(String dateStr) {
        Date date = null;
        try {
            date = dateformat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String date2String(Date date) {
        return dateformat.format(date);
    }
}

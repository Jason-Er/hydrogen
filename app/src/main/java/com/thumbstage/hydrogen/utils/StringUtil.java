package com.thumbstage.hydrogen.utils;

import android.text.TextUtils;
import android.webkit.URLUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String DEFAULT_USERID = "anonymous";
    static DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    static DateFormat topicFormat = new SimpleDateFormat("MM-dd HH:mm");

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

    public static String date2String4Show(Date date) { return topicFormat.format(date); }

    public static String getSuffix(String url) {
        String suffixes="avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc";
        Pattern pat=Pattern.compile("[\\w]+[\\.]("+suffixes+")");
        Matcher mc=pat.matcher(url);//条件匹配
        String substring = "";
        while(mc.find()){
            substring = mc.group();//截取文件名后缀名
        }
        return substring;
    }

    public static String getTimePlusRandom() {
        Date date = new Date();
        String str = dateformat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;
        return rannum + str;
    }

    public static boolean isValidMail(String mail) {
        boolean status = false;
        if(!TextUtils.isEmpty(mail)) {
            Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(mail);
            status =  matcher.find();
        }
        return status;
    }

    public String getFileType(String url) {
        String fileTyle=url.substring(url.lastIndexOf(".")+1,url.length());
        return fileTyle;
    }
}
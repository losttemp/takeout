package com.baidu.iov.dueros.waimai.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CompareDate {
    public static String formatTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat format;
        format = new SimpleDateFormat("MM月dd日");

        return format.format(date);
    }


}

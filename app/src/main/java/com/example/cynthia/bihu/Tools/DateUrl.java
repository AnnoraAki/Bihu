package com.example.cynthia.bihu.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Cynthia on 2018/3/1.
 */

public class DateUrl {
    public static String getDate(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now;
        Date record;
        try {
            now = new Date(System.currentTimeMillis());
            record =dateFormat.parse(data);
            long l = now.getTime()-record.getTime();
            long day = l/(24 * 60 * 60 * 1000);
            long hour = l/(60 * 60 * 1000) - day * 24;
            long min = l/(60 * 1000) - day * 24 * 60 - hour * 60;
            if (day > 0){
                return "大概"+day+"天前";
            }else if (hour > 0){
                return "大概在"+hour+"小时前";
            }else if (min > 0){
                return "大概在"+min+"分钟前";
            }else {
                return "刚刚";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

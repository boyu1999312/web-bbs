package com.xiaozhuzhijia.webbbs.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {



    /**
     * 获取后几天
     * @param past
     * @return
     */
    public static Date getDate(int past) {

        return date(past, false);
    }
    /**
     *
     * @param past
     * @param isBefore true获取前几天
     * @return
     */
    public static Date getDate(int past, boolean isBefore) {

        return date(past, isBefore);
    }

    /**
     * 获取后几天
     * @param past
     * @return
     */
    public static String getString(int past) {
        Date today = date(past, false);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    /**
     *
     * @param past
     * @param isBefore true获取前几天
     * @return
     */
    public static String getString(int past, boolean isBefore) {
        Date today = date(past, isBefore);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    private static Date date(int past, boolean isBefore){
        Calendar calendar = Calendar.getInstance();
        if(isBefore){
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        }
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        return today;
    }
}

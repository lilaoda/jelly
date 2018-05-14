package lhy.lhylibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateUtils {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 得到系统当前时间：时间格式 2016-08-15 12：00：00
     *
     * @return 2016-08-15 12：00：00
     */
    public static String getTime() {
        return new SimpleDateFormat(dateFormat, Locale.CHINA).format(new Date());
    }

    /**
     * 根据得到系统当前时间：时间格式 2016-08-15 12：00：00
     *
     * @return 2016-08-15 12：00：00
     */
    public static String getTime(Long timestemp) {
        return new SimpleDateFormat(dateFormat, Locale.CHINA).format(new Date(timestemp));
    }

    /**
     * 根据得到系统当前时间：时间格式 2016-08-15 12：00：00
     *
     * @return 2016-08-15 12：00：00
     */
    public static String getTime(Date date) {
        return new SimpleDateFormat(dateFormat, Locale.CHINA).format(date);
    }

    /**
     * 根据给定的时间格式得到系统当前时间
     *
     * @return 2016-08-15 12：00：00
     */
    public static String getTime(String dateFormat) {
        return new SimpleDateFormat(dateFormat, Locale.CHINA).format(new Date());
    }

    public static long getTimestemp() {
        return System.currentTimeMillis();
    }

    /**
     * 时间格式 2016-08-15 12：00：00
     *
     * @return 给定时间的时间戳
     */
    public static long getTimestemp(String time) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat,Locale.CHINA);
        Date date = new Date();
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getWeek() {
        return new SimpleDateFormat("EEEE", Locale.CHINA).format(new Date());
    }
}

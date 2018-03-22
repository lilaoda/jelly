package lhy.lhylibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateUtils {

    public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 得到系统当前时间：时间格式 2016-08-15 12：00：00
     *
     * @return
     */
    public static String getCurrentTime() {
        return new SimpleDateFormat(dateFormat, Locale.CHINA).format(new Date());
    }

    public static long getCurrenTimestemp() {
        return new Date().getTime();
    }

    /**
     * 时间格式 2016-08-15 12：00：00
     *
     * @return
     */
    public static long getCurrenTimestemp(String time) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Date date = new Date();
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 根据得到系统当前时间：时间格式 2016-08-15 12：00：00
     *
     * @return
     */
    public static String getCurrentTime(Long timestemp) {
        return new SimpleDateFormat(dateFormat, Locale.CHINA).format(new Date(timestemp));
    }

    /**
     * 根据得到系统当前时间：时间格式 2016-08-15 12：00：00
     *
     * @return
     */
    public static String getCurrentTime(Date date) {
        return new SimpleDateFormat(dateFormat, Locale.CHINA).format(date);
    }


    public static String getDate(String dateFormat) {
        return new SimpleDateFormat(dateFormat, Locale.CHINA).format(new Date());
    }

    public static String getWeek() {
        return new SimpleDateFormat("EEEE", Locale.CHINA).format(new Date());
    }
}

package com.liziyuan.hope.client.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Date相关公共方法
 *
 * @author zqz
 * @date 2022/6/27
 * @since 1.0.0
 */
public class DateUtils {
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 返回当前的LocalDateTime
     *
     * @return java.time.LocalDateTime
     * @author zqz
     * @date 2022/6/30 13:23
     * @since 1.0.0
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 返回当前时间字符串（格式化表达式：yyyy-MM-dd HH:mm:ss）
     *
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/30 13:23
     * @since 1.0.0
     */
    public static String nowStr() {
        return toDateTimeStr(now(), DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * 返回当前时间字符串
     *
     * @param pattern 指定时间格式化表达式
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/30 13:23
     * @since 1.0.0
     */
    public static String nowStr(String pattern) {
        return toDateTimeStr(now(), pattern);
    }

    /**
     * 返回当前精确到秒的时间戳
     *
     * @param zoneOffset 时区，不填默认为+8
     * @return java.lang.Long
     * @author zqz
     * @date 2022/6/30 13:23
     * @since 1.0.0
     */
    public static Long nowLong(ZoneOffset zoneOffset) {
        LocalDateTime dateTime = now();

        if (zoneOffset == null) {
            return dateTime.toEpochSecond(ZoneOffset.ofHours(8));
        } else {
            return dateTime.toEpochSecond(zoneOffset);
        }
    }

    /**
     * 返回当前精确到毫秒的时间戳
     *
     * @return java.lang.Long
     * @author zqz
     * @date 2022/6/30 13:23
     * @since 1.0.0
     */
    public static Long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 将时间戳转换为LocalDateTime
     *
     * @param second     Long类型的时间戳
     * @param zoneOffset 时区，不填默认为+8
     * @return java.time.LocalDateTime
     * @author zqz
     * @date 2022/6/30 13:23
     * @since 1.0.0
     */
    public static LocalDateTime ofEpochSecond(Long second, ZoneOffset zoneOffset) {
        if (zoneOffset == null) {
            return LocalDateTime.ofEpochSecond(second, 0, ZoneOffset.ofHours(8));
        } else {
            return LocalDateTime.ofEpochSecond(second, 0, zoneOffset);
        }
    }

    /**
     * 格式化LocalDateTime（格式化表达式：yyyy-MM-dd HH:mm:ss）
     *
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/30 13:23
     * @since 1.0.0
     */
    public static String toDateTimeStr(LocalDateTime dateTime) {
        return toDateTimeStr(dateTime, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * 格式化LocalDateTime
     *
     * @param pattern 指定时间格式化表达式
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/30 13:23
     * @since 1.0.0
     */
    public static String toDateTimeStr(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    /**
     * 将时间字符串转化为LocalDateTime
     *
     * @param dateTimeStr 时间字符串
     * @return java.time.LocalDateTime
     * @author zqz
     * @date 2022/6/30 13:48
     * @since 1.0.0
     */
    public static LocalDateTime toDateTime(String dateTimeStr) {
        return toDateTime(dateTimeStr, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * 将时间字符串转化为LocalDateTime
     *
     * @param dateTimeStr 时间字符串
     * @param pattern     指定时间格式化表达式
     * @return java.time.LocalDateTime
     * @author zqz
     * @date 2022/6/30 13:48
     * @since 1.0.0
     */
    public static LocalDateTime toDateTime(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    /**
     * 返回当前日期的LocalDate
     *
     * @return java.time.LocalDate
     * @author zqz
     * @date 2022/6/30 13:37
     * @since 1.0.0
     */
    public static LocalDate currentDate() {
        return LocalDate.now();
    }

    /**
     * 返回当前日期字符串（格式化表达式：yyyy-MM-dd）
     *
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/30 13:42
     * @since 1.0.0
     */
    public static String currentDateStr() {
        return toDateStr(currentDate());
    }

    /**
     * 格式化LocalDate
     *
     * @param date LocalDate
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/30 13:42
     * @since 1.0.0
     */
    public static String toDateStr(LocalDate date) {
        return toDateStr(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * 格式化LocalDate
     *
     * @param date    LocalDate
     * @param pattern 指定日期格式化表达式
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/30 13:42
     * @since 1.0.0
     */
    public static String toDateStr(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    /**
     * 将日期字符串转化为LocalDate
     *
     * @param dateStr 日期字符串
     * @return java.time.LocalDate
     * @author zqz
     * @date 2022/6/30 13:48
     * @since 1.0.0
     */
    public static LocalDate toDate(String dateStr) {
        return toDate(dateStr, DEFAULT_DATE_FORMAT);
    }

    /**
     * 将日期字符串转化为LocalDate
     *
     * @param dateStr 日期字符串
     * @param pattern 指定日期格式化表达式
     * @return java.time.LocalDate
     * @author zqz
     * @date 2022/6/30 13:48
     * @since 1.0.0
     */
    public static LocalDate toDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    /**
     * 返回几天之后的时间
     *
     * @param days 天数
     * @return java.time.LocalDateTime
     * @author zqz
     * @date 2022/6/18 17:36
     * @since 1.0.0
     */
    public static LocalDateTime nextDays(Long days) {
        return now().plusDays(days);
    }

    /**
     * 返回几天之后的时间（精确到秒的时间戳）
     *
     * @param days       天数
     * @param zoneOffset 时区，不填默认为+8
     * @return java.lang.Long
     * @author zqz
     * @date 2022/6/18 17:36
     * @since 1.0.0
     */
    public static Long nextDaysSecond(Long days, ZoneOffset zoneOffset) {
        LocalDateTime dateTime = nextDays(days);

        if (zoneOffset == null) {
            return dateTime.toEpochSecond(ZoneOffset.ofHours(8));
        } else {
            return dateTime.toEpochSecond(zoneOffset);
        }
    }

    /**
     * 将天数转化为秒数
     *
     * @param days 天数
     * @return java.lang.Integer
     * @author zqz
     * @date 2022/6/18 17:45
     * @since 1.0.0
     */
    public static Long dayToSecond(Long days) {
        return days * 86400;
    }

}

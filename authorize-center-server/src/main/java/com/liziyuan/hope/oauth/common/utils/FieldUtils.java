package com.liziyuan.hope.oauth.common.utils;


import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 一些常用方法util
 *
 * @author zqz
 * @version 1.0
 * @date 2020-04-14 21:55
 */
public class FieldUtils {

    /**
     * 判断是否为空的形式
     *
     * @param object Object 对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object object) {
        if (null == object) {
            return true;
        }
        // 如果是字符串，要判断长度是否为空
        if (object instanceof String) {
            return ((String) object).trim().length() == 0;
        }
        if (object instanceof Map) {
            return CollectionUtils.isEmpty(((Map) object));
        }

        if (object instanceof List) {
            return CollectionUtils.isEmpty((List) object);
        }

        if (object instanceof Set) {
            return CollectionUtils.isEmpty(((Set) object));
        }

        return false;
    }

    /**
     * 判断全部参数是否为空
     *
     * @param object 可变参数Object
     * @return 是否为空
     */
    public static boolean isAllEmpty(Object... object) {
        if (isEmpty(object)) {
            return true;
        }
        // 只要有一个非空即为false
        for (Object o : object) {
            if (!isEmpty(o)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断至少有一个参数是否为空
     *
     * @param object 可变参数Object
     * @return 至少有一个参数是否为空
     */
    public static boolean isHaveEmpty(Object... object) {
        if (isEmpty(object)) {
            return true;
        }

        // 有一个为空即为true
        for (Object o : object) {
            if (isEmpty(o)) {
                return true;
            }
        }

        return false;
    }


    /**
     * 去除空格
     *
     * @param source 源字符串
     * @return 去除两侧空格后的字符串
     */
    public static String trim(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        String trim = source.trim();
        if (trim.length() == 0) {
            return null;
        }
        return trim;
    }

    /**
     * 判断某个字符串是否超过某个长度
     *
     * @param source 源字符串
     * @param length 指定长度
     * @return 是否超过某个知道长度
     */
    public static boolean isOverSpecifiedLength(String source, int length) {
        if (length < 0) {
            throw new IllegalArgumentException("the String length must > 0, now set " + length);
        }
        if (isEmpty(source)) {
            return false;
        }
        return source.trim().length() > length;
    }


    /**
     * 判断字符是否满足正则表达式
     *
     * @param source 源字符串
     * @param regex  正则表达式
     * @return 是否满足正则表达式
     */
    public static boolean isMatchRegex(String source, String regex) {
        return Pattern.matches(regex, source);
    }


    /**
     * 从字符列表到 整型 list
     *
     * @param strList strList
     * @return 整型 list
     */
    public static List<Long> coverStr2Long(List<String> strList) {
        if (FieldUtils.isEmpty(strList)) {
            return Collections.emptyList();
        }

        List<Long> longs = new ArrayList<>();
        for (String str : strList) {
            longs.add(Long.parseLong(str));
        }
        return longs;
    }


    /**
     * 判断是否为数字
     *
     * @param str numStr
     * @return 判断是否为数字
     */
    public static boolean isNumeric(String str) {
        // 去除小数点
        str = str.replaceFirst("\\.", "");
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


}

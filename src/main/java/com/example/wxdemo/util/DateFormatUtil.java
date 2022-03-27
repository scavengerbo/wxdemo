package com.example.wxdemo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**********************************************
 *@类描述 日期相关的工具类
 *
 *@版本 V1.0
 ***********************************************/
public class DateFormatUtil {

    public static SimpleDateFormat getDateFormat(DateFormatEnum formatEnum) {
        return new SimpleDateFormat(formatEnum.getFormatStr());
    }

    public static String getCurrDateStr(DateFormatEnum formatEnum) {
        Date nowDate = new Date();
        return getDateFormat(formatEnum).format(nowDate);
    }

    public static String getDefaultCurrDataStr() {
        Date nowDate = new Date();
        return getDateFormat(DateFormatEnum.FORMAT2).format(nowDate);
    }
}

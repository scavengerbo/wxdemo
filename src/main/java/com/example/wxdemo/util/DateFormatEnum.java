package com.example.wxdemo.util;

/**********************************************
 *
 *
 *@类描述  日期格式的枚举类
 *
 *@版本 V1.0
 ***********************************************/
public enum DateFormatEnum {

    FORMAT1("yyyyMMddHHmmSS"),
    FORMAT2("yyyy-MM-dd HH:mm:ss"),
    FORMAT3("yyyy-MM-dd HH:mm:ss.SSS");

    private String formatStr;

    DateFormatEnum(String formatStr) {
        this.formatStr = formatStr;
    }

    public String getFormatStr() {
        return formatStr;
    }
}

package com.example.wxdemo.util;

import lombok.val;
import java.net.URL;

/**
 * 封装get post请求
 * @author basketball
 */
public class NetUtil {

    /**
     * 向指定地址发送get请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        try {
            val urlobj = new URL(url);
            val inputStream = urlobj.openConnection().getInputStream();
            val bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println("发送get请求失败");

        }
        return "";
    }

    /**
     * 向指定地址发送post请求
     * @param url
     * @return
     */
    public static String post(String url,String data) {
        try {
            val urlobj = new URL(url);
            val connection = urlobj.openConnection();
            //设置为可发送数据状态
            connection.setDoOutput(true);
            //获取输出流
            val outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes("UTF-8"));
            outputStream.close();

            //获取输入流
            val inputStream = connection.getInputStream();

            val bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println("发送get请求失败");

        }
        return "";
    }

}

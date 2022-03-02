package com.example.wxdemo.util;

import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class SignUtil {

    private static final String TOKEN = "abc";

    /**
     * 1）将token、timestamp、nonce三个参数进行字典序排序
     * 2）将三个参数字符串拼接成一个字符串进行sha1加密
     * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     *
     * 校验签名
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 布尔值
     */
    public static boolean checkSignature(String signature,String timestamp,String nonce){
        String checktext = null;
        if (StringUtils.isNotBlank(signature)) {
            //对ToKen,timestamp,nonce 按字典排序
            String[] paramArr = new String[]{TOKEN,timestamp,nonce};
            Arrays.sort(paramArr);
            //将排序后的结果拼成一个字符串
            String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                //对接后的字符串进行sha1加密
                byte[] digest = md.digest(content.getBytes());
                checktext = byteToStr(digest);
            } catch (NoSuchAlgorithmException e){
                e.printStackTrace();
            }
        }
        //将加密后的字符串与signature进行对比
        return checktext !=null ? checktext.equals(signature.toUpperCase()) : false;
    }

    /**
     * 将字节数组转化我16进制字符串
     * @param byteArrays 字符数组
     * @return 字符串
     */
    private static String byteToStr(byte[] byteArrays){
        String str = "";
        for (int i = 0; i < byteArrays.length; i++) {
            str += byteToHexStr(byteArrays[i]);
        }
        return str;
    }

    /**
     *  将字节转化为十六进制字符串
     * @param myByte 字节
     * @return 字符串
     */
    private static String byteToHexStr(byte myByte) {
        char[] Digit = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] tampArr = new char[2];
        tampArr[0] = Digit[(myByte >>> 4) & 0X0F];
        tampArr[1] = Digit[myByte & 0X0F];
        String str = new String(tampArr);
        return str;
    }

//    public static XStream createXstream() {
//        return new XStream(new XppDriver() {
//            @Override
//            public HierarchicalStreamWriter createWriter(Writer out) {
//                return new PrettyPrintWriter(out) {
//                    boolean cdata = false;
//                    Class<?> targetClass = null;
//
//                    @Override
//                    public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
//                        super.startNode(name, clazz);
//                        // 业务处理，对于用XStreamCDATA标记的Field，需要加上CDATA标签
//                        if (!name.equals("xml")) {
//                            cdata = needCDATA(targetClass, name);
//                        } else {
//                            targetClass = clazz;
//                        }
//                    }
//
//                    @Override
//                    protected void writeText(QuickWriter writer, String text) {
//                        if (cdata) {
//                            writer.write("<![CDATA[");
//                            writer.write(text);
//                            writer.write("]]>");
//                        } else {
//                            writer.write(text);
//                        }
//                    }
//                };
//            }
//        });
//    }

//    private static boolean needCDATA(Class<?> targetClass, String fieldAlias) {
//        boolean cdata = false;
//        // first, scan self
//        cdata = existsCDATA(targetClass, fieldAlias);
//        if (cdata)
//            return cdata;
//        // if cdata is false, scan supperClass until java.lang.Object
//        Class<?> superClass = targetClass.getSuperclass();
//        while (!superClass.equals(Object.class)) {
//            cdata = existsCDATA(superClass, fieldAlias);
//            if (cdata)
//                return cdata;
//            superClass = superClass.getClass().getSuperclass();
//        }
//        return false;
//    }

//    private static boolean existsCDATA(Class<?> clazz, String fieldAlias) {
//        if ("MediaId".equals(fieldAlias)) {
//            return true; // 特例添加 morning99
//        }
//        // scan fields
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field field : fields) {
//            // 1. exists XStreamCDATA
//            if (field.getAnnotation(WxServiceService.XStreamCDATA.class) != null) {
//                XStreamAlias xStreamAlias = field.getAnnotation(XStreamAlias.class);
//                // 2. exists XStreamAlias
//                if (null != xStreamAlias) {
//                    if (fieldAlias.equals(xStreamAlias.value()))// matched
//                        return true;
//                } else {// not exists XStreamAlias
//                    if (fieldAlias.equals(field.getName()))
//                        return true;
//                }
//            }
//        }
//        return false;
//    }

}
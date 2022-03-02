package com.example.wxdemo.util;

import lombok.val;
import org.junit.Test;

public class TemplateMessageUtil {

    /**
     * 设置所属行业
     */
    public void setIndustry(){

        String url = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", TokenUtil.getAccessToken());
        String setIndustryData = "{\n" +
                "    \"industry_id1\":\"1\",\n" +
                "    \"industry_id2\":\"15\"\n" +
                "}";
        val post = NetUtil.post(url, setIndustryData);
        System.out.println(post);

    }


    /**
     * 获取设置的行业信息
     */
    public String getIndustry(){
        String url = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", TokenUtil.getAccessToken());
        val industry = NetUtil.get(url);
        System.out.println(industry);
        return industry;

    }

    /**
     * 获取模板列表
     */
    private String getAllTemplate(){
        String url = " https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", TokenUtil.getAccessToken());
        val getAllTemplate = NetUtil.get(url);
        System.out.println(getAllTemplate);
        return getAllTemplate;
    }

    /**
     * 发送模板消息
     */
    public static void sendTemplate(String toUser){

        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", TokenUtil.getAccessToken());
        String sendTemplateData = "{\n" +
                "    \"touser\": \"ojPqI5ieWDeNk-l1E12Ib8EDdgfs\",\n" +
                "    \"template_id\": \"rqRobo31P_XIwfJ7ddJ4LR_j2NTdTxWypUqmNey0cQQ\",\n" +
                "    \"url\": \"http://boboweixin.free.idcfengye.com/hello/thymeleaf\",\n" +
                "    \"data\": {\n" +
                "        \"first\": {\n" +
                "            \"value\": \"您好，磅单已处理成功！\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        },\n" +
                "        \"keyword1\": {\n" +
                "            \"value\": \"北京市海淀区\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        },\n" +
                "        \"keyword2\": {\n" +
                "            \"value\": \"2022年2月17日\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        },\n" +
                "        \"remark\": {\n" +
                "            \"value\": \"请及时查收磅单流转更新信息！\",\n" +
                "            \"color\": \"#173177\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        val result = NetUtil.post(url, sendTemplateData);
        System.out.println(result);

    }


    @Test
    public void test(){
//        setIndustry();
//        getIndustry();
        sendTemplate("");
//        getAllTemplate();
    }
}

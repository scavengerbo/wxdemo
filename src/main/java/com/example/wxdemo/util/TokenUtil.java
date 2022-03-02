package com.example.wxdemo.util;

import com.example.wxdemo.bean.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.Test;

import java.util.Map;

/**
 * @author basketball
 */
public class TokenUtil {

    public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    public static final String APPID = "wx5bfe94fcc90cdae0";
    public static final String APPSECRET = "1a8992f2bce2bace0d5aef7ea2c793e2";
    private static AccessToken at;


    /**
     * 获取内部token
     */
    private static void getToken(){
        val url = GET_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        val tokenStr = NetUtil.get(url);
        System.out.println(tokenStr);
        ObjectMapper mapper = new ObjectMapper();
        String access_token = "";
        String expires_in = "";
        try {
            Map map = mapper.readValue(tokenStr, Map.class);
            access_token = (String) map.get("access_token");
            expires_in = String.valueOf(map.get("expires_in"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        at = new AccessToken(access_token, expires_in);
    }

    /**
     * 对外暴露token
     */
    public static String getAccessToken(){
        if (at == null || at.isExpired()){
            getToken();
        }
        System.out.println(at.getAccessToken());
        return at.getAccessToken();
    }


    @Test
    public void test(){
        getToken();
        getAccessToken();
        getAccessToken();
    }
}

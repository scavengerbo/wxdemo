package com.example.wxdemo;

import com.example.wxdemo.bean.Button;
import com.example.wxdemo.bean.ClickButton;
import com.example.wxdemo.bean.SubButton;
import com.example.wxdemo.bean.ViewButton;
import com.example.wxdemo.util.NetUtil;
import com.example.wxdemo.util.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestWx {


    @Test
    public  void  testButton() throws JsonProcessingException {
        val button = new Button();
        button.getButton().add(new ClickButton("一级点击","1"));
        button.getButton().add(new ViewButton("一级跳转","http://www.baidu.com"));
        val sb = new SubButton("有子菜单");
        sb.getSub_button().add(new ClickButton("点击","32"));
        sb.getSub_button().add(new ViewButton("网易新闻","http://news.163.com"));
        button.getButton().add(sb);
        val s = new ObjectMapper().writeValueAsString(button);
        System.out.println(s);

        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", TokenUtil.getAccessToken());
        val post = NetUtil.post(url, s);
        System.out.println(post);

    }



}

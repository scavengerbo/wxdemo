package com.example.wxdemo.controller;

import com.example.wxdemo.util.TemplateMessageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TemplateController {

    @RequestMapping("/hello/thymeleaf")
    public String hello(Model model) {
        return "test";
    }

    @RequestMapping("/user")
    @ResponseBody
    public String getUserById(Integer id) {
        System.out.println("id=" + id);

        TemplateMessageUtil.sendTemplate("11");
        return "id=" + id;
    }


}

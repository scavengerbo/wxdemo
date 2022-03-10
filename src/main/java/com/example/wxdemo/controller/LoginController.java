package com.example.wxdemo.controller;

import com.example.wxdemo.bean.ReturnData;
import com.example.wxdemo.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@Slf4j
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private ILoginService iLoginService;


    @PostMapping("/register")
    public ReturnData register(@RequestBody Map<String, String> params) {
        return ReturnData.success();
    }

    @PostMapping("/verifyNameAndPasswd")
    public ReturnData verifyNameAndPasswd(@RequestBody Map<String, String> params) {

        String name = params.get("name");
        String passwd = params.get("passwd");

        if (StringUtils.isBlank(name) || StringUtils.isBlank(passwd)) {
            return ReturnData.fail("参数校验失败");
        }

        boolean map = iLoginService.verifyByNameAndPasswd(name, passwd);
        return ReturnData.success(map);
    }


}

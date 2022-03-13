package com.example.wxdemo.controller;

import com.example.wxdemo.bean.ReturnData;
import com.example.wxdemo.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

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
    public ReturnData verifyNameAndPasswd(@RequestParam Map<String, String> params) {

        String name = params.get("name");
        String passwd = params.get("passwd");


        if (StringUtils.isBlank(name) || StringUtils.isBlank(passwd)) {
            return ReturnData.fail("参数校验失败");
        }

        Map map = iLoginService.verifyByNameAndPasswd(name, passwd);
        System.out.println(map);
        if (Objects.nonNull(map) && map.size() > 0) {
            return ReturnData.success(map);
        }
        return ReturnData.fail(map);
    }


}

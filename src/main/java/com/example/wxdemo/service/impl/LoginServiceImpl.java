package com.example.wxdemo.service.impl;

import com.example.wxdemo.db.mapper.LoginMapper;
import com.example.wxdemo.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public void register(Map map) {

    }

    @Override
    public boolean verifyByNameAndPasswd(String name, String passwd) {
        Map map = loginMapper.verifyByNameAndPasswd(name, passwd);
        if (Objects.nonNull(map) && map.size() > 0) {
            return true;
        }
        return false;
    }


}

package com.example.wxdemo.service;

import java.util.Map;

public interface ILoginService {

    void register(Map map);

    boolean verifyByNameAndPasswd(String name,String passwd);
}
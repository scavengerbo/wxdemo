package com.example.wxdemo.service;

import java.util.List;
import java.util.Map;

public interface ILoginService {

    void register(Map map);

    Map verifyByNameAndPasswd(String name,String passwd);

    List agencyMatters(String userId);
}

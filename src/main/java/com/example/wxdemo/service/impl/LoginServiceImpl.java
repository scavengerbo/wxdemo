package com.example.wxdemo.service.impl;

import com.example.wxdemo.db.mapper.LoginMapper;
import com.example.wxdemo.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Map verifyByNameAndPasswd(String name, String passwd) {
        Map map = loginMapper.verifyByNameAndPasswd(name, passwd);
//        if (Objects.nonNull(map) && map.size() > 0) {
//            return true;
//        }
//        return false;
        return map;
    }

    @Override
    public List agencyMatters(String userId) {
        List list = loginMapper.agencyMatters(userId);
        return list;
    }

    @Override
    public List workList(String cid, String uid, String deid) {
        List list = loginMapper.workList(cid,uid,deid);
        return list;
    }

    @Override
    public List workTitle(String twpid) {
        List list = loginMapper.workTitle(twpid);
        return list;
    }

    @Override
    public List permUser(String twpid, String userid) {
        List list = loginMapper.permUser(twpid, userid);
        return list;
    }


}

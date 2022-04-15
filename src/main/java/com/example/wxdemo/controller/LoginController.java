package com.example.wxdemo.controller;

import com.example.wxdemo.bean.ReturnData;
import com.example.wxdemo.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
            List agencyMatters = iLoginService.agencyMatters(map.get("userid").toString());
            map.put("agencyMatters", agencyMatters);
            return ReturnData.success(map);
        }
        return ReturnData.fail(map);
    }

    @PostMapping("/workList")
    public ReturnData workList(@RequestParam Map<String,String> params){
        String cid = params.get("cid");
        String uid = params.get("userid");
        String deid = params.get("de_id");
        List list = iLoginService.workList(cid, uid,deid);
        return ReturnData.success(list);
    }

    @PostMapping("/workTitle")
    public ReturnData workTitle(@RequestParam Map<String,String> params){
        String twpid = params.get("twp_id");
        String userid = params.get("userid");
        System.out.println(twpid);
        List list = iLoginService.workTitle(twpid);
        List list1 = iLoginService.permUser(twpid,userid);
        Map<String,Object> map = new HashMap<>();
        map.put("workTitle",list);
        map.put("permUser",list1);
        Map tcw_id = iLoginService.getChildWord(twpid);
        if(!tcw_id.isEmpty()){
            List list2 = iLoginService.childUser(twpid,userid);
            map.put("childflag","Y");
            map.put("childWork",tcw_id);
            map.put("permChild",list2);
        }else {
            map.put("childflag","N");
            map.put("childWork","");
            map.put("permChild","");
        }
        return ReturnData.success(map);
    }
}

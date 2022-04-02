package com.example.wxdemo.controller;

import com.example.wxdemo.bean.ReturnData;
import com.example.wxdemo.service.IWorkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController()
@Slf4j
@RequestMapping("/work")
@ResponseBody
public class WorkController {

    @Autowired
    private IWorkService iWorkService;


    @PostMapping("/sumbitWorkOrder")
    public ReturnData sumbitWorkOrder(@RequestBody Map<String,Object> params) {

        boolean result;
        System.out.println("-----------------"+ params);
        String userId = params.get("userId").toString();
        String twpId = params.get("twpId").toString();
        String status = params.get("status").toString();
        String permUserID = params.get("permUserID").toString();
        String appLevel = params.get("permUserID").toString();
        List<Map> workConts = (List<Map>)params.get("workConts");

        System.out.println(params);
        System.out.println(workConts);

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(twpId) || StringUtils.isBlank(status) ||
                workConts.size() == 0 || StringUtils.isBlank(permUserID) || StringUtils.isBlank(appLevel)) {
            return ReturnData.fail("参数校验失败");
        }

        result = iWorkService.submitWorkOrder(params);
        if (!result) {
            return ReturnData.fail("提交工单失败");
        }
        return ReturnData.success();
    }
}

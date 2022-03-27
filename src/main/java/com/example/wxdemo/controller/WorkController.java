package com.example.wxdemo.controller;

import com.example.wxdemo.bean.ReturnData;
import com.example.wxdemo.service.IWorkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController()
@Slf4j
@RequestMapping("/work")
public class WorkController {

    @Autowired
    private IWorkService iWorkService;


    @PostMapping("/sumbitWorkOrder")
    public ReturnData sumbitWorkOrder(@RequestBody Map<String, Object> params) {

        boolean result;
        String userId = params.get("userId").toString();
        String twpId = params.get("twpId").toString();
        String status = params.get("status").toString();
        List<Map> workConts = (List<Map>) params.get("workConts");

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(twpId) || StringUtils.isBlank(status) ||
                workConts.size() == 0) {
            return ReturnData.fail("参数校验失败");
        }

        result = iWorkService.submitWorkOrder(params);
        if (!result) {
            return ReturnData.fail("提交工单失败");
        }
        return ReturnData.success();
    }
}

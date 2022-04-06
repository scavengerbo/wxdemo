package com.example.wxdemo.controller;

import com.example.wxdemo.bean.ReturnData;
import com.example.wxdemo.service.IWorkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @PostMapping("/approvalWork")
    public ReturnData approvalWork(@RequestParam Map<String,Object> params){
        List<HashMap<String,Object>> list = new ArrayList<>();
        HashMap<String,Object> map = new HashMap<>();
        String approcalLevel = params.get("approcalLevel").toString();
        String wid = params.get("voucher").toString();
        String userid = params.get("userid").toString();
        String nowApprocalLevel = "0";
        Map<String,String> work = iWorkService.getWork(wid);
        map.put("usersmsg",work);
        List<Map> workcont = iWorkService.getWorkCont(wid,nowApprocalLevel);
        map.put("workmsg",workcont);
        map.put("workflag","R");
        map.put("permflag","N");
        map.put("permUser",null);
        list.add(map);
        System.out.println(list);
        while (!approcalLevel.equals(nowApprocalLevel)){
            HashMap<String,Object> map2 = new HashMap<>();
            System.out.println("--------------------------");
            // 获取下一个审批等级
            Map<String,String> getWap = iWorkService.getWap(wid,nowApprocalLevel);
            map2.put("usersmsg",getWap);
            String nextApprovalLevel = iWorkService.nextApprovalLevel(work.get("twp_id"),nowApprocalLevel);
            workcont = iWorkService.getWorkCont(work.get("twp_id"),nextApprovalLevel);
            map2.put("workmsg",workcont);
            map2.put("workflag","R");
            nowApprocalLevel = iWorkService.nextApprovalLevel(work.get("twp_id"),nowApprocalLevel);
            map2.put("permflag","N");
            map2.put("permUser",null);
            list.add(map2);
            //  nowApprocalLevel = next
        }
        // 获取下一个审批等级和填写内容
        HashMap<String,Object> map3 = new HashMap<>();
        Map<String,String> getWap = iWorkService.getWap(wid,nowApprocalLevel);
        map3.put("usersmsg",getWap);
        String nextApprovalLevel = iWorkService.nextApprovalLevel(work.get("twp_id"),nowApprocalLevel);
        List<Map> workTitle = iWorkService.workTitle(work.get("twp_id"),nextApprovalLevel);
        map3.put("workmsg",workTitle);
        map3.put("workflag","W");

        String nnApprovalLevel = iWorkService.nextApprovalLevel(work.get("twp_id"),nextApprovalLevel) == null?"":iWorkService.nextApprovalLevel(work.get("twp_id"),nextApprovalLevel);
        if (!nnApprovalLevel.equals("")){
            System.out.println(work.get("twp_id"));
            System.out.println(userid);
            System.out.println(nnApprovalLevel);
            List<Map> permUser = iWorkService.permUser(work.get("twp_id"),userid,nextApprovalLevel);
            map3.put("permflag","Y");
            map3.put("permUser",permUser);
        }else {
            map3.put("permflag","N");
            map3.put("permUser",null);
        }
        list.add(map3);
        System.out.println(list);
        return ReturnData.success(list);
    }
}

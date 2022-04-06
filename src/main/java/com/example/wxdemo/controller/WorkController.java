package com.example.wxdemo.controller;

import com.example.wxdemo.bean.ReturnData;
import com.example.wxdemo.db.mapper.WorkMapper;
import com.example.wxdemo.service.IWorkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController()
@Slf4j
@RequestMapping("/work")
@ResponseBody
public class WorkController {

    @Autowired
    private IWorkService iWorkService;

    @Resource
    private WorkMapper workMapper;


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
        work.put("ApprocalLevel","0");
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
            String nextApprovalLevel = iWorkService.nextApprovalLevel(work.get("twp_id"),nowApprocalLevel);
            getWap.put("ApprocalLevel",nextApprovalLevel);
            map2.put("usersmsg",getWap);

            workcont = iWorkService.getWorkCont(wid,nextApprovalLevel);
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
        String nextApprovalLevel = iWorkService.nextApprovalLevel(work.get("twp_id"),nowApprocalLevel);
        getWap.put("ApprocalLevel",nextApprovalLevel);
        map3.put("usersmsg",getWap);
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

    @PostMapping("/sumbitPerm")
    public ReturnData sumbitPerm(@RequestBody Map<String,Object> params){
        System.out.println(params);
        String perResult = params.get("permResult").toString();
        Map<String,Object> approval = (Map<String,Object>)params.get("approval");
        Map<String,String> permUser = (Map<String,String>)params.get("permUser");
        Map<String,String> usersmsg = (Map<String,String>)approval.get("usersmsg");
        String wap_id = usersmsg.get("wap_id");
        String w_id = usersmsg.get("w_id");
        List<Map> workmsg = (List<Map>)approval.get("workmsg");
        workmsg.stream().forEach(wc -> {
            wc.put("wid", usersmsg.get("w_id"));
            workMapper.saveWorkContent(wc);
        });
        iWorkService.updateWap(wap_id,params.get("permResult").toString());
        //workMap.put("createTime", DateFormatUtil.getDefaultCurrDataStr());
        if(params.get("permResult").toString().equals("2")){
            iWorkService.updateWork(params.get("permResult").toString(),"2",w_id);
        } else {
            if(approval.get("permflag").toString().equals("Y")){
                Map<String,String> map = new HashMap<>();
                map.put("workId",w_id);
                map.put("permUserID",permUser.get("userid"));
                map.put("appLevel",usersmsg.get("ApprocalLevel"));
                workMapper.saveWorkApproval(map);
            }else{
                iWorkService.updateWork(params.get("permResult").toString(),"2",w_id);
            }
        }


        return ReturnData.success();
    }

    @PostMapping("/myWorkList")
    public ReturnData myWorkList(@RequestParam Map<String,Object> params){

        String userid = params.get("userid").toString();
        List<Map> myWorkList = iWorkService.myWorkList(userid);
        return ReturnData.success(myWorkList);
    }

    @PostMapping("/myWork")
    public ReturnData myWork(@RequestParam Map<String,Object> params){
        List<HashMap<String,Object>> list = new ArrayList<>();
        HashMap<String,Object> map = new HashMap<>();
        String approvalStatus = params.get("approvalStatus").toString();
        String wid = params.get("voucher").toString();
        String twp_id = params.get("twp_id").toString();
//        String userid = params.get("userid").toString();
        String nowApprocalLevel = "0";
        Map<String,String> work = iWorkService.getWork(wid);
        map.put("usersmsg",work);
        List<Map> workcont = iWorkService.getWorkCont(wid,nowApprocalLevel);
        map.put("workmsg",workcont);
        list.add(map);


        if(!approvalStatus.equals("0")){
            Map<String,String> getWap = iWorkService.getWap(wid,nowApprocalLevel);
            String result = getWap.get("result");
            if(result.equals("0")){
                HashMap<String,Object> map2 = new HashMap<>();
                map2.put("usersmsg",getWap);
                map2.put("workmsg","");
                list.add(map2);
            }else{
                do{
                    HashMap<String,Object> map2 = new HashMap<>();
                    Map<String,String> getWap2 = iWorkService.getWap(wid,nowApprocalLevel);
                    map2.put("usersmsg",getWap2);
                    String nextApprovalLevel = iWorkService.nextApprovalLevel(work.get("twp_id"),nowApprocalLevel);
                    List<Map> workcont2 = iWorkService.getWorkCont(wid,nextApprovalLevel);
                    map2.put("workmsg",workcont2);
                    list.add(map2);
                    nowApprocalLevel = nextApprovalLevel;
                    result = getWap2.get("result");
                    if(result.equals("2")||iWorkService.nextApprovalLevel(work.get("twp_id"),nowApprocalLevel).equals("")){
                        break;
                    }
                }while(!result.equals("0"));
            }


        }
        System.out.println(list);
        return ReturnData.success(list);
    }
}

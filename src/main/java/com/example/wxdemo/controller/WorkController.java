package com.example.wxdemo.controller;

import com.example.wxdemo.bean.ReturnData;
import com.example.wxdemo.db.mapper.WorkMapper;
import com.example.wxdemo.service.IWorkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
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
    public ReturnData approvalWork(@RequestParam Map<String,Object> params) throws javax.script.ScriptException {
        Map<String,Object> valmap = new HashMap<>();
        System.out.println("RequestParam");
        List<HashMap<String,Object>> list = new ArrayList<>();
        HashMap<String,Object> map = new HashMap<>();
        String preappr = params.get("preappr").toString();
        String wid = params.get("voucher").toString();
        String userid = params.get("userid").toString();
        String nowApprocalLevel = "0";
        String nowpreappr = "0";
        Map<String,String> work = iWorkService.getWork(wid);
        work.put("nextApprovalLevel",nowApprocalLevel);
        map.put("usersmsg",work);
        List<Map> workcont = iWorkService.getWorkCont(wid,nowApprocalLevel,nowpreappr);
        workcont.forEach(workcontmap -> {
            valmap.put(workcontmap.get("twc_id").toString(),workcontmap.get("vl").toString());
        });
        map.put("workmsg",workcont.size()==0?"":workcont);
        map.put("workflag","R");
        map.put("permflag","N");
        map.put("permUser",null);
        list.add(map);
        System.out.println(list);
        while (!preappr.equals(nowpreappr)){
            HashMap<String,Object> map2 = new HashMap<>();
            System.out.println("--------------------------"+preappr+"###"+nowpreappr );
            // 获取下一个审批等级
            Map<String,String> getWap = iWorkService.getWap(wid,nowpreappr);
            String nextApprovalLevel ;
            if(getWap.get("approcalLevel").equals("-1")){
                nextApprovalLevel = "0";
            }else{
                nextApprovalLevel = iWorkService.nextApprovalLevel(work.get("twp_id"),getWap.get("approcalLevel"));
            }
            String nextappr = iWorkService.nextappr(wid,nowpreappr);
            getWap.put("nextApprovalLevel",nextApprovalLevel);
            map2.put("usersmsg",getWap);
            List<Map> workcont2 = iWorkService.getWorkCont(wid,nextApprovalLevel,nextappr);
            workcont2.forEach(workcontmap -> {
                valmap.put(workcontmap.get("twc_id").toString(),workcontmap.get("vl").toString());
            });
            map2.put("workmsg",workcont2);
            map2.put("workflag","R");
            nowpreappr = iWorkService.nextappr(wid,nowpreappr);
            map2.put("permflag","N");
            map2.put("permUser",null);
            list.add(map2);
            //  nowApprocalLevel = next
        }
        // 获取下一个审批等级和填写内容
        HashMap<String,Object> map3 = new HashMap<>();
        Map<String,String> getWap = iWorkService.getWap(wid,nowpreappr);
        System.out.println(getWap);
        String nextApprovalLevel ;
        if(getWap.get("approcalLevel").equals("-1")){
            nextApprovalLevel = "0";
        }else{
            nextApprovalLevel = iWorkService.nextApprovalLevel(work.get("twp_id"),getWap.get("approcalLevel"));
        }
        getWap.put("nextApprovalLevel",nextApprovalLevel);
        map3.put("usersmsg",getWap);
        List<Map> workTitle = iWorkService.workTitle(work.get("twp_id"),nextApprovalLevel);
        for (int i=0;i<workTitle.size();i++){
            String sum_uuid = workTitle.get(i).get("sum_uuid")==null?"":workTitle.get(i).get("sum_uuid").toString();
            if(!sum_uuid.equals("")){
//                System.out.println("12345678");
//                System.out.println(jsEval(sum_uuid,valmap));
                workTitle.get(i).put("vl",jsEval(sum_uuid,valmap));
            }
        }
        map3.put("workmsg",workTitle);
        map3.put("workflag","W");

         String nnApprovalLevel = iWorkService.nextApprovalLevel(work.get("twp_id"),nextApprovalLevel) == null?"":iWorkService.nextApprovalLevel(work.get("twp_id"),nextApprovalLevel);
         System.out.println("nnApprovalLevel------------"+nnApprovalLevel);
        if (!nnApprovalLevel.equals("")){
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
        System.out.println("sumbitPerm");
        System.out.println(params);
        String perResult = params.get("permResult").toString();
        Map<String,Object> approval = (Map<String,Object>)params.get("approval");
        Map<String,String> permUser = (Map<String,String>)params.get("permUser");
        Map<String,String> usersmsg = (Map<String,String>)approval.get("usersmsg");
        String wap_id = usersmsg.get("wap_id");
        String w_id = usersmsg.get("w_id");
        String approcalLevel = usersmsg.get("approcalLevel");
        String start_appr;
        String start_user;
        List<Map> workmsg = (List<Map>)approval.get("workmsg");
        workmsg.stream().forEach(wc -> {
            wc.put("wid", usersmsg.get("w_id"));
            wc.put("wap_id", usersmsg.get("wap_id"));
            workMapper.saveWorkContent(wc);
        });
        iWorkService.updateWap(wap_id,params.get("permResult").toString());
        //workMap.put("createTime", DateFormatUtil.getDefaultCurrDataStr());
        if(params.get("permResult").toString().equals("2")){
            iWorkService.updateWork2(params.get("permResult").toString(),"4",w_id);
            if (approcalLevel.equals("0")){
                start_appr = "-1";
                start_user= usersmsg.get("workuser");
            } else {
                String preapprocalLevel = iWorkService.preApprovalLevel(usersmsg.get("twp_id"),approcalLevel);
                String preUser = iWorkService.preappUser(w_id,preapprocalLevel);
                start_appr = preapprocalLevel;
                start_user = preUser;
            }
            Map<String,String> map = new HashMap<>();
            map.put("workId",w_id);
            map.put("permUserID",start_user); // start_user
            map.put("appLevel",start_appr); // start_appr
            map.put("wap_id",wap_id);
            map.put("start_appr",usersmsg.get("approcalLevel")); // usersmsg.get("ApprocalLevel")
            map.put("start_user",usersmsg.get("createUser")); // usersmsg.get("createUser")
            map.put("result","0");
            workMapper.saveWorkApproval(map);
        } else {
            System.out.println("permflag----"+approval.get("permflag"));
            if(approval.get("permflag").toString().equals("Y")){
//                System.out.println(params);
                Map<String,String> map = new HashMap<>();
                map.put("workId",w_id);
                map.put("permUserID",permUser.get("userid")); // usersmsg.get("start_appr");
                map.put("appLevel",usersmsg.get("nextApprovalLevel")); // usersmsg.get("start_user");
                map.put("wap_id",wap_id);
                map.put("start_appr",usersmsg.get("approcalLevel")); // usersmsg.get("ApprocalLevel")
                map.put("start_user",usersmsg.get("createUser")); // usersmsg.get("createUser")
                map.put("result","0");
                workMapper.saveWorkApproval(map);
                iWorkService.updateWork2("0","1",w_id);
            }else{
                iWorkService.updateWork(params.get("permResult").toString(),"2",w_id);
                int num = iWorkService.getchildsucc(w_id);
                if(num == 0){
                    iWorkService.updateWork(params.get("permResult").toString(),"5",w_id);
                }

                String par_work = iWorkService.getparwork(w_id) == null?"":iWorkService.getparwork(w_id);
                if (!par_work.equals("")){
                    int num2 = iWorkService.getchildsucc(par_work);
                    if(num2 == 0){
                        iWorkService.updateWork(params.get("permResult").toString(),"5",par_work);
                    }
                }
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

    @PostMapping("/childWorkList")
    public ReturnData childWorkList(@RequestParam Map<String,Object> params){

        String userid = params.get("userid").toString();
        List<Map> myWorkList = iWorkService.childWorkList(userid);
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
        String nowAppr = "0";
        Map<String,String> work = iWorkService.getWork(wid);
        map.put("usersmsg",work);
        List<Map> workcont = iWorkService.getWorkCont(wid,nowApprocalLevel,nowAppr);
        map.put("workmsg",workcont.size()==0?"":workcont);
        list.add(map);


        if(!approvalStatus.equals("0")){
            Map<String,String> getWap = iWorkService.getWap(wid,nowAppr);
            String result = getWap.get("result");
            if(result.equals("0")){
                HashMap<String,Object> map2 = new HashMap<>();
                map2.put("usersmsg",getWap);
                map2.put("workmsg","");
                list.add(map2);
            }else{
                do{
                    HashMap<String,Object> map2 = new HashMap<>();
                    Map<String,String> getWap2 = iWorkService.getWap(wid,nowAppr);
                    map2.put("usersmsg",getWap2);
                    String nextApprovalLevel ;
                    if(getWap2.get("approcalLevel").equals("-1")){
                        nextApprovalLevel = "0";
                    }else{
                        nextApprovalLevel = iWorkService.nextApprovalLevel(work.get("twp_id"),getWap2.get("approcalLevel"));
                    }
                    String nextappr = iWorkService.nextappr(wid,nowAppr);
                    System.out.println(wid+"--"+nextApprovalLevel+"--"+nextappr);
                    List<Map> workcont2 = iWorkService.getWorkCont(wid,nextApprovalLevel,nextappr);
                    map2.put("workmsg",workcont2.size()==0?"":workcont2);
                    list.add(map2);
                    nowAppr = nextappr;
                    result = getWap2.get("result");
                    if((iWorkService.nextappr(wid,nowAppr) == null ? "":iWorkService.nextappr(wid,nowAppr)).equals("")){
                        break;
                    }
                }while(!result.equals("0"));
            }


        }
        System.out.println(list);
        return ReturnData.success(list);
    }

    public static Object jsEval(String formula,Map<String,Object> varMap) throws ScriptException, javax.script.ScriptException {
        formula = new StringSubstitutor(varMap).replace(formula);
        ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
        Object evaluate = jse.eval(formula);
        return new BigDecimal(evaluate.toString()).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
    }
}

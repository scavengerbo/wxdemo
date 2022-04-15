package com.example.wxdemo.service.impl;

import com.example.wxdemo.db.mapper.WorkMapper;
import com.example.wxdemo.service.IWorkService;
import com.example.wxdemo.util.DateFormatUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkServiceImpl implements IWorkService {

    @Resource
    private WorkMapper workMapper;

    @Override
    @Transactional
    public boolean submitWorkOrder(Map map) {

        Map<String, Object> workMap = new HashMap<>();
        workMap.put("creatUser", map.get("userId"));
        workMap.put("createTime", DateFormatUtil.getDefaultCurrDataStr());
        workMap.put("startTime", DateFormatUtil.getDefaultCurrDataStr());
        workMap.put("twpId", map.get("twpId"));
        workMap.put("approvalResult", "0");
        // 0 草稿，1，审批中，2 审批结束，3 删除
        workMap.put("status", map.get("status"));


        //存工单
        int i = workMapper.saveWork(workMap);
        if (i != 1) {
            return false;
        }
        //存工单内容
        List<HashMap> workConts = (List<HashMap>) map.get("workConts");
        workConts.stream().forEach(wc -> {
            wc.put("wid", workMap.get("workId"));
            wc.put("wap_id", "0");
            workMapper.saveWorkContent(wc);
        });

        //存审批信息
        Map<String, Object> workApp = new HashMap<>();
        workApp.put("workId",workMap.get("workId"));
        workApp.put("permUserID",map.get("permUserID"));
        workApp.put("appLevel",map.get("appLevel"));
        workApp.put("wap_id","0");
        workApp.put("start_user",map.get("userId"));
        workApp.put("start_appr","-1");
        workApp.put("result","0");
        workMapper.saveWorkApproval(workApp);

        String childflag = map.get("childflag").toString();
        if(childflag.equals("Y")){
            List<Map> childtwps = workMapper.childtwp(map.get("twpId").toString());
            System.out.println(childtwps);
            childtwps.forEach(childtwp -> {
                Map<String, Object> workMap2 = new HashMap<>();
                workMap2.put("creatUser", map.get("userId"));
                workMap2.put("createTime", DateFormatUtil.getDefaultCurrDataStr());
                workMap2.put("startTime", DateFormatUtil.getDefaultCurrDataStr());
                workMap2.put("twpId", childtwp.get("childtwp"));
                workMap2.put("approvalResult", "0");
                // 0 草稿，1，审批中，2 审批结束，3 删除
                workMap2.put("status", map.get("status"));
                workMap2.put("par_work",workMap.get("workId"));
                workMapper.saveWork(workMap2);

                Map<String, Object> workApp2 = new HashMap<>();
                workApp2.put("workId",workMap2.get("workId"));
                workApp2.put("permUserID",map.get("childpermUserID"));
                workApp2.put("appLevel",map.get("appLevel"));
                workApp2.put("wap_id","0");
                workApp2.put("start_user",map.get("userId"));
                workApp2.put("start_appr","-1");
                workApp2.put("result","3");
                System.out.println(workApp2);
                workMapper.saveWorkApproval(workApp2);
            });
        }

        return true;
    }

    @Override
    public Map<String,String> getWork(String wid) {
        Map work = workMapper.getWork(wid);
        return work;
    }

    @Override
    public List<Map> getWorkCont(String wid, String approcalLevel,String nowpreappr) {
        return workMapper.getWorkCont(wid,approcalLevel,nowpreappr);
    }

    @Override
    public String nextApprovalLevel(String twp_id, String approcalLevel) {
        return workMapper.nextApprovalLevel(twp_id,approcalLevel);
    }

    @Override
    public List<Map> workTitle(String twp_id, String approcalLevel) {
        return workMapper.workTitle(twp_id,approcalLevel);
    }

    @Override
    public Map<String, String> getWap(String wid, String nowApprocalLevel) {
        return workMapper.getWap(wid,nowApprocalLevel);
    }

    @Override
    public List<Map> permUser(String twp_id, String userid, String nnApprovalLevel) {
        return workMapper.permUser(twp_id,userid,nnApprovalLevel);
    }

    @Override
    public void updateWap(String wap_id, String permResult) {
        workMapper.updateWap(wap_id,permResult);
    }

    @Override
    public void updateWork(String permResult, String s,String uuid) {
        workMapper.updateWork(permResult,s,uuid);
    }

    @Override
    public List<Map> myWorkList(String params) {
        return workMapper.myWorkList(params);
    }

    @Override
    public String nextappr(String wid, String nowpreappr) {
        return workMapper.nextappr(wid,nowpreappr);
    }

    @Override
    public void updateWork2(String permResult, String s, String w_id) {
        workMapper.updateWork2(permResult,s,w_id);
    }

    @Override
    public String preApprovalLevel(String twp_id, String approcalLevel) {
        return workMapper.preApprovalLevel(twp_id,approcalLevel);
    }

    @Override
    public String preappUser(String w_id, String preapprocalLevel) {
        return workMapper.preappUser(w_id,preapprocalLevel);
    }

    @Override
    public List<Map> childWorkList(String userid) {
        return workMapper.childWorkList(userid);
    }

    @Override
    public int getchildsucc(String w_id) {
        return workMapper.getchildsucc(w_id);
    }

    @Override
    public String getparwork(String w_id) {
        return workMapper.getparwork(w_id);
    }
}

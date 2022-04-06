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
            workMapper.saveWorkContent(wc);
        });

        //存审批信息
        Map<String, Object> workApp = new HashMap<>();
        workApp.put("workId",workMap.get("workId"));
        workApp.put("permUserID",map.get("permUserID"));
        workApp.put("appLevel",map.get("appLevel"));
        workMapper.saveWorkApproval(workApp);

        return true;
    }

    @Override
    public Map<String,String> getWork(String wid) {
        Map work = workMapper.getWork(wid);
        return work;
    }

    @Override
    public List<Map> getWorkCont(String wid, String approcalLevel) {
        return workMapper.getWorkCont(wid,approcalLevel);
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
}

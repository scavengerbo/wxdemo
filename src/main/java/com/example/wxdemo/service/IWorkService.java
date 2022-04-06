package com.example.wxdemo.service;

import java.util.List;
import java.util.Map;

public interface IWorkService {

    /**
     * 提交工单内容
     * @return
     */
    boolean submitWorkOrder(Map map);

    Map<String,String> getWork(String wid);

    List<Map> getWorkCont(String wid, String approcalLevel);

    String nextApprovalLevel(String twp_id, String approcalLevel);

    List<Map> workTitle(String twp_id, String approcalLevel);

    Map<String, String> getWap(String wid, String nowApprocalLevel);

    List<Map> permUser(String twp_id, String userid, String nnApprovalLevel);

    void updateWap(String wap_id, String permResult);

    void updateWork(String permResult, String s,String uuid);

    List<Map> myWorkList(String userid);
}

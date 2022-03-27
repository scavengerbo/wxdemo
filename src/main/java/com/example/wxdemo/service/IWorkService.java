package com.example.wxdemo.service;

import java.util.Map;

public interface IWorkService {

    /**
     * 提交工单内容
     * @return
     */
    boolean submitWorkOrder(Map map);
}

package com.example.wxdemo.db.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import java.util.Map;

public interface WorkMapper {

    @Options(useGeneratedKeys = true, keyProperty = "workId", keyColumn = "uuid")
    @Insert({"insert into work(createUser, createTime, startTime, twp_id,approvalResult,status) values" +
            "(#{creatUser}, #{createTime, jdbcType=TIMESTAMP},#{startTime, jdbcType=TIMESTAMP}," +
            " #{twpId}, #{approvalResult}, #{status})"})
    int saveWork(Map map);


    @Insert({"insert into work_content(wid, twc_id,twc_value) values" +
            "(#{wid},#{twc_id}, #{vl})"})
    int saveWorkContent(Map workContent);

    @Insert({"INSERT INTO work_approval_proc ( wid, createTime, startTime, approver, result, approcalLevel) " +
            "VALUES (#{workId}, #{createTime, jdbcType=TIMESTAMP}, #{createTime, jdbcType=TIMESTAMP}, " +
            "#{permUserID}, '0', #{appLevel})"})
    int saveWorkApproval(Map map);
}

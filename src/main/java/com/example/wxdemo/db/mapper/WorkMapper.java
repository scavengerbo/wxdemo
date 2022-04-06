package com.example.wxdemo.db.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
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
            "VALUES (#{workId}, sysdate(), sysdate(), " +
            "#{permUserID}, '0', #{appLevel})"})
    int saveWorkApproval(Map map);

    @Select("select a.uuid as w_id, d.workName,cast(d.uuid as char) as twp_id, a.createUser, b.name as username, date_format(a.createTime, '%Y-%m-%d %H:%i:%s') as createTime, '-1' as result\n" +
            "from work a,\n" +
            "     user b,\n" +
            "     template_work_proc d\n" +
            "where a.createUser = b.uuid\n" +
            "  and a.twp_id = d.uuid and a.uuid=#{wid}")
    Map<String,String> getWork(@Param("wid") String wid);

    @Select("select c.twc_value as vl, c.uuid as wc_id, b.name as name, b.type, a.is_null, 1 as readonly\n" +
            "from template_work_content a,\n" +
            "     template_title b,\n" +
            "     work_content c\n" +
            "where a.uuid = c.twc_id\n" +
            "  and a.approvalLevel = #{approcalLevel}\n" +
            "  and c.wid = #{wid}\n" +
            "  and a.tid = b.uuid\n" +
            "  and b.status = 1\n" +
            "order by a.`order`")
    List<Map> getWorkCont(@Param("wid") String wid,@Param("approcalLevel") String approcalLevel);

    @Select("select a.nextApprovalLevel from template_approval_proc a where a.twp_id=#{twp_id} and a.approvalLevel=#{approvalLevel}")
    String nextApprovalLevel(@Param("twp_id") String twp_id,@Param("approvalLevel") String approvalLevel);

    @Select("select '' as vl,a.uuid as twc_id,b.name as name,b.type,a.is_null,case when b.type=1 or b.type=2 then 0 else 1 end as readonly\n" +
            "from template_work_content a,template_title b where a.twp_id=#{twpid} and a.approvalLevel=#{approcalLevel}\n" +
            "and a.tid=b.uuid and b.status=1 order by  a.`order`")
    List<Map> workTitle(@Param("twpid") String twpid,@Param("approcalLevel") String approcalLevel);

    @Select("select a.uuid as wap_id,\n" +
            "       a.wid  as w_id,\n" +
            "       c.workName,\n" +
            "       b.twp_id,\n" +
            "       d.uuid as createUser,\n" +
            "       d.name as username,\n" +
            "       a.endTime,\n" +
            "       a.result\n" +
            "from work_approval_proc a,\n" +
            "     work b,\n" +
            "     template_work_proc c,\n" +
            "     user d\n" +
            "where a.wid = b.uuid\n" +
            "  and b.twp_id = c.uuid\n" +
            "  and a.approver = d.uuid\n" +
            "  and a.wid = #{wid}\n" +
            "  and a.approcalLevel = #{approcalLevel}")
    Map<String,String> getWap(@Param("wid") String wid,@Param("approcalLevel") String approcalLevel);

    @Select("select c.uuid,c.name from template_approval_auth a,permissions b,user c where a.twp_id=#{twpid} and a.approvalLevel=#{approvalLevel}\n" +
            "and a.appr_perm=b.perms and b.type=2 and b.perm_mem=c.uuid and c.uuid!=#{userid}\n" +
            "union\n" +
            "select c.uuid,c.name from template_approval_auth a,permissions b,user c,department d where a.twp_id=#{twpid} and a.approvalLevel=#{approvalLevel}\n" +
            "and a.appr_perm=b.perms and b.type=1 and b.perm_mem=d.uuid and c.de_id=d.uuid and c.uuid!=#{userid}")
    List<Map> permUser(@Param("twpid") String twpid,@Param("userid") String userid,@Param("approvalLevel") String approvalLevel);


}

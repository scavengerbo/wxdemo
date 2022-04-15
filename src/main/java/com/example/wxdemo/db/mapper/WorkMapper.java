package com.example.wxdemo.db.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface WorkMapper {

    @Options(useGeneratedKeys = true, keyProperty = "workId", keyColumn = "uuid")
    @Insert({"insert into work(createUser, createTime, startTime, twp_id,approvalResult,status,par_work) values" +
            "(#{creatUser}, #{createTime, jdbcType=TIMESTAMP},#{startTime, jdbcType=TIMESTAMP}," +
            " #{twpId}, #{approvalResult}, #{status},#{par_work})"})
    int saveWork(Map map);


    @Insert({"insert into work_content(wid, twc_id,twc_value,appr) values" +
            "(#{wid},#{twc_id}, #{vl},#{wap_id})"})
    int saveWorkContent(Map workContent);

    @Insert({"INSERT INTO work_approval_proc ( wid, createTime, startTime, approver, result, approcalLevel,preappr,start_user,start_appr) " +
            "VALUES (#{workId}, sysdate(), sysdate(), " +
            "#{permUserID}, #{result}, #{appLevel},#{wap_id},#{start_user},#{start_appr})"})
    int saveWorkApproval(Map map);

    @Select("select cast(a.uuid as char) as w_id, d.workName,cast(d.uuid as char) as twp_id, a.createUser, " +
            "b.name as username, date_format(a.createTime, '%Y-%m-%d %H:%i:%s') as createTime, '-1' as result\n" +
            "from work a,\n" +
            "     user b,\n" +
            "     template_work_proc d\n" +
            "where a.createUser = b.uuid\n" +
            "  and a.twp_id = d.uuid and a.uuid=#{wid}")
    Map<String,String> getWork(@Param("wid") String wid);

    @Select("select c.twc_value as vl, cast(c.uuid as char) as wc_id, b.name as name, b.type, a.is_null, 1 as readonly\n" +
            "from template_work_content a,\n" +
            "     template_title b,\n" +
            "     work_content c\n" +
            "where a.uuid = c.twc_id\n" +
            "  and a.approvalLevel = #{approcalLevel} and c.appr=#{nowpreappr}\n" +
            "  and c.wid = #{wid}\n" +
            "  and a.tid = b.uuid\n" +
            "  and b.status = 1\n" +
            "order by a.`order`")
    List<Map> getWorkCont(@Param("wid") String wid,@Param("approcalLevel") String approcalLevel,@Param("nowpreappr")String nowpreappr);

    @Select("select ifnull(a.nextApprovalLevel,'') from template_approval_proc a where a.twp_id=#{twp_id} and a.approvalLevel=#{approvalLevel}")
    String nextApprovalLevel(@Param("twp_id") String twp_id,@Param("approvalLevel") String approvalLevel);

    @Select("select ifnull(a.approvalLevel,'') from template_approval_proc a where a.twp_id=#{twp_id} and a.nextApprovalLevel=#{approvalLevel}")
    String preApprovalLevel(@Param("twp_id") String twp_id,@Param("approvalLevel") String approvalLevel);

    @Select("select a.approver from work_approval_proc a where a.wid=#{wid} and a.result='1' and a.approcalLevel=#{approcalLevel} order by a.endTime desc limit 1")
    String preappUser(@Param("wid")String wid,@Param("approcalLevel")String approcalLevel);

    @Select("select a.uuid from work_approval_proc a where a.wid=#{wid} and a.preappr=#{appr}")
    String nextappr(@Param("wid") String wid,@Param("appr") String appr);

    @Select("select '' as vl,a.uuid as twc_id,b.name as name,b.type,a.is_null,case when b.type=1 or b.type=2 then 0 else 1 end as readonly\n" +
            "from template_work_content a,template_title b where a.twp_id=#{twpid} and a.approvalLevel=#{approcalLevel}\n" +
            "and a.tid=b.uuid and b.status=1 order by  a.`order`")
    List<Map> workTitle(@Param("twpid") String twpid,@Param("approcalLevel") String approcalLevel);

    @Select("select cast(a.uuid as char) as wap_id,\n" +
            "       a.wid  as w_id,\n" +
            "       c.workName,\n" +
            "       cast(b.twp_id as char) as twp_id,\n" +
            "       d.uuid as createUser,\n" +
            "       d.name as username,\n" +
            "       date_format(a.endTime, '%Y-%m-%d %H:%i:%s') as createTime,\n" +
            "       case when a.approver=b.createUser then '3' else a.result end as result,a.start_user,a.start_appr,a.approcalLevel,b.createUser as workuser\n" +
            "from work_approval_proc a,\n" +
            "     work b,\n" +
            "     template_work_proc c,\n" +
            "     user d\n" +
            "where a.wid = b.uuid\n" +
            "  and b.twp_id = c.uuid\n" +
            "  and a.approver = d.uuid\n" +
            "  and a.wid = #{wid}\n" +
            "  and a.preappr = #{approcalLevel}")
    Map<String,String> getWap(@Param("wid") String wid,@Param("approcalLevel") String approcalLevel);

    @Select("select cast(c.uuid as char) as uuid,c.name from template_approval_auth a,permissions b,user c where a.twp_id=#{twpid} and a.approvalLevel=#{approvalLevel}\n" +
            "and a.appr_perm=b.perms and b.type=2 and b.perm_mem=c.uuid and c.uuid!=#{userid}\n" +
            "union\n" +
            "select c.uuid,c.name from template_approval_auth a,permissions b,user c,department d where a.twp_id=#{twpid} and a.approvalLevel=#{approvalLevel}\n" +
            "and a.appr_perm=b.perms and b.type=1 and b.perm_mem=d.uuid and c.de_id=d.uuid and c.uuid!=#{userid}")
    List<Map> permUser(@Param("twpid") String twpid,@Param("userid") String userid,@Param("approvalLevel") String approvalLevel);

    @Update("update work_approval_proc a set a.endTime=sysdate(),a.result=#{result} where uuid=#{wap_id}")
    int updateWap(@Param("wap_id")String wap_id,@Param("result")String result);

    @Update("update work set endTime=sysdate(),approvalResult=#{approvalResult},status=#{status} where uuid=#{uuid}")
    int updateWork(@Param("approvalResult") String approvalResult, @Param("status") String status,@Param("uuid")String uuid);

    @Update("update work set approvalResult=#{approvalResult},status=#{status} where uuid=#{uuid}")
    int updateWork2(@Param("approvalResult") String approvalResult, @Param("status") String status,@Param("uuid")String uuid);

    @Select("select b.uuid                                         as voucher,\n" +
            "       c.workName                                     as title,\n" +
            "       b.createUser                                   as apply_user_name,\n" +
            "       b.status                                       as approvalStatus,\n" +
            "       d.name                                         as applicant,\n" +
            "       e.de_name                                         department,\n" +
            "       f.name                                         as company,\n" +
            "       date_format(b.createTime, '%Y-%m-%d %H:%i:%s') as applyTime,\n" +
            "       q.uuid                                         as tcwid,\n" +
            "       q.workName                                     as applyInstruction,\n" +
            "       c.uuid                                         as twp_id\n" +
            "from work b\n" +
            "         join template_work_proc c\n" +
            "              on (b.twp_id = c.uuid)\n" +
            "         join user d on (b.createUser = d.uuid)\n" +
            "         join department e on (d.de_id = e.uuid)\n" +
            "         join company f on (e.c_id = f.uuid)\n" +
            "         left join template_child_work q on (c.uuid = q.twp_id)\n" +
            "where b.starttime <= sysdate()\n" +
            "  and b.createUser = #{userId}\n" +
            "  and b.status != 3\n" +
            "  and c.is_child = 'N'\n" +
            "order by b.createTime desc")
    List<Map> myWorkList(@Param("userId") String userId);

    @Select("select b.uuid                                         as voucher,\n" +
            "       c.workName                                     as title,\n" +
            "       b.createUser                                   as apply_user_name,\n" +
            "       b.status                                       as approvalStatus,\n" +
            "       d.name                                         as applicant,\n" +
            "       e.de_name                                         department,\n" +
            "       f.name                                         as company,\n" +
            "       date_format(b.createTime, '%Y-%m-%d %H:%i:%s') as applyTime,\n" +
            "       q.uuid                                         as tcwid,\n" +
            "       q.workName                                     as applyInstruction,\n" +
            "       c.uuid                                         as twp_id\n" +
            "from work b\n" +
            "         join template_work_proc c\n" +
            "              on (b.twp_id = c.uuid)\n" +
            "         join user d on (b.createUser = d.uuid)\n" +
            "         join department e on (d.de_id = e.uuid)\n" +
            "         join company f on (e.c_id = f.uuid)\n" +
            "         left join template_child_work q on (c.uuid = q.twp_id)\n" +
            "where b.starttime <= sysdate()\n" +
            "  and b.status != 3\n" +
            "  and c.is_child = 'Y'\n" +
            "  and b.par_work = #{userId}\n" +
            "order by b.createTime desc")
    List<Map> childWorkList(@Param("userId") String userId);

    @Select("select c.uuid as childtwp from template_child_work_list a,template_child_work b,template_work_proc c\n" +
            "where a.tcw_id=b.uuid and a.twp_id=c.uuid and b.twp_id=#{twpid}")
    List<Map> childtwp(@Param("twpid") String twpid);

    @Select("select count(1) from work b\n" +
            "where b.par_work=#{wid} and b.status in ('1','2')")
    int getchildsucc(@Param("wid") String wid);

    @Select("select b.par_work from work b,work a\n" +
            "where b.uuid=#{wid} and a.status='2' and a.uuid=b.par_work")
    String getparwork(@Param("wid") String wid);
}

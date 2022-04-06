package com.example.wxdemo.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface LoginMapper {

        @Select("select a.uuid as cid, a.name as cname, b.uuid as userid, b.name as username," +
                "c.uuid as de_id,c.de_name from company a," +
            "user b,department c where a.uuid = c.c_id and c.uuid=b.de_id and c.status=1 and a.status = 1 and " +
                "b.status = 1 and b.name=#{name} and b.passwd=#{passwd} " +
            " and (a.expiryTime > sysdate() or a.expiryTime is null) and (b.expiryTime > sysdate() or b.expiryTime is null)" +
                "and (c.exp_Time > sysdate() or c.exp_Time is null)")
    Map verifyByNameAndPasswd(@Param("name") String name, @Param("passwd") String passwd);

        @Select("select a.wid as voucher,c.workName as title,b.createUser as apply_user_name,b.status as approvalStatus," +
                "d.name as applicant,e.de_name department,f.name as company,date_format(b.createTime, '%Y-%m-%d %H:%i:%s') as applyTime,a.approcalLevel,a.uuid as id,'' as applyInstruction\n" +
                "                from work_approval_proc a,\n" +
                "                      work b,\n" +
                "                     template_work_proc c,\n" +
                "                     user d,department e,company f\n" +
                "                 where a.wid = b.uuid\n" +
                "                   and b.status = '1'\n" +
                "                   and b.starttime <= sysdate()\n" +
                "                   and b.endTime is null\n" +
                "                   and a.approver = #{userId}\n" +
                "and b.twp_id=c.uuid and b.createUser=d.uuid and d.de_id=e.uuid and e.c_id=f.uuid;")
        List<Map> agencyMatters(@Param("userId") String userId);

        @Select("select a.workName,a.uuid as twp_id\n" +
                "from template_work_proc a\n" +
                "where a.cid = #{cid}\n" +
                "  and a.status = 1\n" +
                "  and (exists(select 1\n" +
                "              from permissions b\n" +
                "              where a.view_perm = b.perms and b.type = 1 and b.perm_mem = #{uid} and b.status = 1)\n" +
                "    or exists(select 1\n" +
                "              from permissions b\n" +
                "              where a.view_perm = b.perms and b.type = 2 and b.perm_mem = #{deid} and b.status = 1))")
        List<Map> workList(@Param("cid") String cid, @Param("uid") String uid, @Param("deid") String deid);

        @Select("select '' as vl,a.uuid as twc_id,b.name as name,b.type,a.is_null,case when b.type=1 or b.type=2 then 0 else 1 end as readonly\n" +
                "from template_work_content a,template_title b where a.twp_id=#{twpid} and a.approvalLevel=0\n" +
                "and a.tid=b.uuid and b.status=1 order by  a.`order`")
        List<Map> workTitle(@Param("twpid") String twpid);

        @Select("select c.uuid,c.name from template_approval_auth a,permissions b,user c where a.twp_id=#{twpid} and a.approvalLevel=0\n" +
                "and a.appr_perm=b.perms and b.type=2 and b.perm_mem=c.uuid and c.uuid!=#{userid}\n" +
                "union\n" +
                "select c.uuid,c.name from template_approval_auth a,permissions b,user c,department d where a.twp_id=#{twpid} and a.approvalLevel=0\n" +
                "and a.appr_perm=b.perms and b.type=1 and b.perm_mem=d.uuid and c.de_id=d.uuid and c.uuid!=#{userid}")
        List<Map> permUser(@Param("twpid") String twpid,@Param("userid") String userid);
}

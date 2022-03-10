package com.example.wxdemo.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface LoginMapper {

    @Select("select a.uuid as cid, a.name as cname, b.uuid as userid, b.name as username from company a," +
            "user b where a.uuid = b.cid and a.status = 1 and b.status = 1 and b.name=#{name} and b.passwd=#{passwd} " +
            " and (a.expiryTime < sysdate() or a.expiryTime is null) and (b.expiryTime < sysdate() or b.expiryTime is null)")
    Map verifyByNameAndPasswd(@Param("name") String name, @Param("passwd") String passwd);

}

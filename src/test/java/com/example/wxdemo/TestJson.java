package com.example.wxdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.Test;

import java.util.ArrayList;

public class TestJson {

    @Test
    public void test() throws JsonProcessingException {
        val user = new User();
        user.setId("1");
        user.setName("bobo");

        ObjectMapper objectMapper = new ObjectMapper();
                // 将Java对象序列化为Json字符串
                String objectToJson = objectMapper.writeValueAsString(user);
                System.out.println(objectToJson);

        val user1 = new User();
        user1.setId("2");
        user1.setName("bobo2");



//        byte[]bytes = new byte[32];

        val users = new ArrayList<User>();
        users.add(user);
        users.add(user1);
        String objectToJson1 = objectMapper.writeValueAsString(users);
        System.out.println(objectToJson1);



        String a = new String("刘波");
        val bytes1 = a.getBytes();
        String objectToJson2 = objectMapper.writeValueAsString(bytes1);
        System.out.println(objectToJson2);
    }
}

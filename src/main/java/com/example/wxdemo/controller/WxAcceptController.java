package com.example.wxdemo.controller;

import com.example.wxdemo.bean.BaseMessage;
import com.example.wxdemo.bean.TextMessage;
import com.example.wxdemo.util.SignUtil;
import com.example.wxdemo.util.TemplateMessageUtil;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/wx")
public class WxAcceptController {


    @GetMapping
    public String demo(HttpServletRequest request) {
        val signature = request.getParameter("signature");
        val timestamp = request.getParameter("timestamp");
        val nonce = request.getParameter("nonce");
        val echostr = request.getParameter("echostr");
        System.out.println("hello world");
        System.out.println(signature);
        System.out.println(timestamp);
        System.out.println(nonce);
        System.out.println(echostr);

        //校验验证请求
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            System.out.println("接入成功");
            return echostr;
        } else {
            System.out.println("接入失败");
            return "fail";
        }
    }

//    @PostMapping
//    public void demo(HttpServletRequest request) throws IOException {
//        ServletInputStream inputStream = request.getInputStream();
//        byte[] b= new byte[1024];
//        int len;
//        StringBuilder sb = new StringBuilder();
//        while ((len=inputStream.read(b))!=-1){
//            sb.append(new String(b,0,len));
//        }
//        System.out.println(sb.toString());
//        System.out.println("post");
//    }

//        @RequestMapping(method= RequestMethod.POST,consumes = {MediaType.TEXT_XML_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.TEXT_XML_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Object demo(@RequestBody BaseMessage message) {
        //打印请求的入参
        System.out.println(message.toString());
        //响应出参到微信服务器
//        TextMessage outMessage = new TextMessage();
//        outMessage.setToUserName(message.getFromUserName());
//        outMessage.setFromUserName(message.getToUserName());
//        outMessage.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
//        outMessage.setMsgType(message.getMsgType());
//        outMessage.setContent("你好");
//        System.out.println(outMessage);
        val response = getResponse(message);

        //处理图文消息
        if ("图文".equals(message.getContent())) {

        }
        return response;
    }

    /**
     * 获取accesstoken
     */
    private String getAccessToken() {
        return "";

    }


    /**
     * 处理不同的消息对象
     */
    public Object getResponse(BaseMessage message) {
        Object result = null;
        val msgType = message.getMsgType();
        switch (msgType) {
            case "text":
                break;
            case "image":
                break;
            case "voice":
                break;
            case "video":
                break;
            case "music":
                break;
            case "news":
                break;
            case "event":
                result = dealEvent(message);
                return result;
            default:
                break;
        }
        return null;
    }

    /**
     * 处理接收事件响应
     */
    private Object dealEvent(BaseMessage message) {
        Object resultMessage = null;
        val event = message.getEvent();
        switch (event) {
            case "CLICK":
                resultMessage = dealClick(message);
                return resultMessage;
            case "VIEW":
                dealView();
                break;
            case "subscribe":
                dealSubScribe();
                break;
            default:
                break;

        }
        return resultMessage;


    }

    /**
     * 处理视图菜单
     */
    private void dealView() {

    }

    /**
     * 处理订阅事件
     */
    private void dealSubScribe(){

    }

    /**
     * 处理点击菜单
     */
    private Object dealClick(BaseMessage message) {
        //处理eventKey的内容
        val eventKey = message.getEventKey();
        Object outMessage = null;
        switch(eventKey){
            case "1":
                //处理点击了第一个一级菜单
                //响应出参到微信服务器
                outMessage = new TextMessage("你点了一下一级菜单",message);
//                outMessage.setToUserName(message.getFromUserName());
//                outMessage.setFromUserName(message.getToUserName());
//                outMessage.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
//                outMessage.setMsgType(message.getMsgType());
//                outMessage.setContent("你点了一下一级菜单");
//                outMessage.setEvent(message.getEvent());
//                outMessage.setEventKey(eventKey);
                return outMessage;
            case "32":
                TemplateMessageUtil.sendTemplate(message.getFromUserName());
                break;
            default:
                break;
        }
        return outMessage;

    }

}

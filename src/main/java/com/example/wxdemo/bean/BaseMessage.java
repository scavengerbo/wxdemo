package com.example.wxdemo.bean;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "xml")
public class BaseMessage {


    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;
    @JacksonXmlProperty(localName = "FromUserName")
    private String fromUserName;
    @JacksonXmlProperty(localName = "CreateTime")
    private String createTime;
    @JacksonXmlProperty(localName = "MsgType")
    private String msgType;
    @JacksonXmlProperty(localName = "MsgId")
    private String msgId;

    //文本消息
    @JacksonXmlProperty(localName = "Content")
    private String content;

    //图片消息
    @JacksonXmlProperty(localName = "PicUrl")
    private String picUrl;
    @JacksonXmlProperty(localName = "MediaId")
    private String mediaId;

    //语音消息
    @JacksonXmlProperty(localName = "Format")
    private String format;

    //视频消息
    @JacksonXmlProperty(localName = "ThumbMediaId")
    private String thumbMediaId;

    //地理位置消息
    @JacksonXmlProperty(localName = "Location_X")
    private String location_X;
    @JacksonXmlProperty(localName = "Location_Y")
    private String location_Y;
    @JacksonXmlProperty(localName = "Scale")
    private String scale;
    @JacksonXmlProperty(localName = "Label")
    private String label;

    //链接消息
    @JacksonXmlProperty(localName = "Title")
    private String title;
    @JacksonXmlProperty(localName = "Description")
    private String description;
    @JacksonXmlProperty(localName = "Url")
    private String url;

    //事件消息
    @JacksonXmlProperty(localName = "Event")
    private String event;
    //事件标识
    @JacksonXmlProperty(localName = "EventKey")
    private String eventKey;

    public BaseMessage(BaseMessage base) {
        this.toUserName = base.fromUserName;
        this.fromUserName = base.toUserName;
        this.createTime = String.valueOf(System.currentTimeMillis() / 1000);
    }
}

package com.example.wxdemo.bean;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <xml><ToUserName><![CDATA[gh_5fa42222d0fe]]></ToUserName>
 * <FromUserName><![CDATA[ojPqI5ieWDeNk-l1E12Ib8EDdgfs]]></FromUserName>
 * <CreateTime>1643893735</CreateTime>
 * <MsgType><![CDATA[text]]></MsgType>
 * <Content><![CDATA[2]]></Content>
 * <MsgId>23534897715929075</MsgId>
 * </xml>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "xml")
public class TextMessage extends BaseMessage {

    //    @JacksonXmlProperty(localName = "ToUserName")
//    private String toUserName;
//    @JacksonXmlProperty(localName = "FromUserName")
//    private String fromUserName;
//    @JacksonXmlProperty(localName = "CreateTime")
//    private String createTime;
//    @JacksonXmlProperty(localName = "MsgType")
//    private String msgType;
    @JacksonXmlProperty(localName = "Content")
    private String content;
//    @JacksonXmlProperty(localName = "MsgId")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnore
//    private String msgId;

    public TextMessage(String content, BaseMessage base) {
        super(base);
        this.setMsgType("text");
        this.content = content;
    }
}

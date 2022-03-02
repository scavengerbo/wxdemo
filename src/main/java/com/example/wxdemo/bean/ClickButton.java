package com.example.wxdemo.bean;

import lombok.Data;

@Data
public class ClickButton extends AbstractButton {
    private String type = "click";
    private String key;
    public ClickButton(String name,String key) {
        super(name);
        this.key = key;
    }
}

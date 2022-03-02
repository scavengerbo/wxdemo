package com.example.wxdemo.bean;

import lombok.Data;

@Data
public class ViewButton extends  AbstractButton {
    private String type = "view";
    private String url;

    public ViewButton(String name, String url) {
        super(name);
        this.url = url;
    }
}

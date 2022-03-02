package com.example.wxdemo.bean;

import lombok.Data;

@Data
public abstract class AbstractButton {

    private String name;

    public AbstractButton(String name) {
        this.name = name;
    }
}

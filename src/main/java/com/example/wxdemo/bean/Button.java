package com.example.wxdemo.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Button {

    private List<AbstractButton> button = new ArrayList<>();

}

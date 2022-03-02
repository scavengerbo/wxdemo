package com.example.wxdemo.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author basketball
 */
@Data
public class SubButton extends  AbstractButton {
    private List<AbstractButton> sub_button =  new ArrayList<>();

    public SubButton(String name) {
        super(name);
    }
}

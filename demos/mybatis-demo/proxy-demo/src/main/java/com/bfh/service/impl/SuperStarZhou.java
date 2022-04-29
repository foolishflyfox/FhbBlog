package com.bfh.service.impl;

import com.bfh.service.Service;

/**
 * @author benfeihu
 */
public class SuperStarZhou implements Service {
    @Override
    public void sing() {
        System.out.println("我是周润发，唱歌...");
    }

    @Override
    public String show(int age) {
        System.out.println("周润发 show: " + age);
        return "周";
    }
}

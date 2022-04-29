package com.bfh.service.impl;

import com.bfh.service.Service;

/**
 * @author benfeihu
 */
public class SuperStarLiu implements Service {
    @Override
    public void sing() {
        System.out.println("我是刘德华，在唱歌...");
    }

    @Override
    public String show(int age) {
        System.out.println("刘德华 " + age);
        return "刘";
    }
}

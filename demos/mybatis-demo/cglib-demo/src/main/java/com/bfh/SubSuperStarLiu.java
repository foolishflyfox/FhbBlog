package com.bfh;

/**
 * @author benfeihu
 */
public class SubSuperStarLiu extends SuperStarLiu {
    @Override
    public void sing() {
        System.out.println("预定时间...");
        System.out.println("预定场地...");
        super.sing();
        System.out.println("结束...");
    }
}

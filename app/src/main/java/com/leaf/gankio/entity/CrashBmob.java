package com.leaf.gankio.entity;

import cn.bmob.v3.BmobObject;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/23 10:29
 * @TODO ： 异常
 */

public class CrashBmob extends BmobObject{

    private String carashTime ;
    private String carachInfo ;

    public String getCarashTime() {
        return carashTime;
    }

    public void setCarashTime(String carashTime) {
        this.carashTime = carashTime;
    }

    public String getCarachInfo() {
        return carachInfo;
    }

    public void setCarachInfo(String carachInfo) {
        this.carachInfo = carachInfo;
    }

    public CrashBmob(String carashTime, String carachInfo) {
        this.carashTime = carashTime;
        this.carachInfo = carachInfo;
    }

    public CrashBmob() {
    }
}

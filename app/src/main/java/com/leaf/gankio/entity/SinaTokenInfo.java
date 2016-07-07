package com.leaf.gankio.entity;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/3 22:06
 * @TODO ： sina用户access_token的授权相关信息
 */

public class SinaTokenInfo {


    /**
     * uid : 1073880650
     * appkey : 1352222456
     * scope : null
     * create_at : 1352267591
     * expire_in : 157679471
     *
     * uid	string	授权用户的uid。
       appkey	string	access_token所属的应用appkey。
       scope	string	用户授权的scope权限。
       create_at	string	access_token的创建时间，从1970年到创建时间的秒数。
       expire_in	string	access_token的剩余时间，单位是秒数。
     *
     *
     *
     *
     */

    private String uid;
    private int appkey;
    private Object scope;
    private int create_at;
    private int expire_in;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getAppkey() {
        return appkey;
    }

    public void setAppkey(int appkey) {
        this.appkey = appkey;
    }

    public Object getScope() {
        return scope;
    }

    public void setScope(Object scope) {
        this.scope = scope;
    }

    public int getCreate_at() {
        return create_at;
    }

    public void setCreate_at(int create_at) {
        this.create_at = create_at;
    }

    public int getExpire_in() {
        return expire_in;
    }

    public void setExpire_in(int expire_in) {
        this.expire_in = expire_in;
    }
}

package com.leaf.gankio.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/1 10:10
 * @TODO ： ...
 */

public class PreferenceManager {

    private static final String TAG = "PreferenceManager";
    public static final String PREFERENCE_FILE_NAME = "gankio.configure" ;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor shareEditor;
    private static PreferenceManager preferenceManager = null;
    private Context context ;
    private static final String KEY_UID           = "uid";
    private static final String KEY_ACCESS_TOKEN  = "access_token";
    private static final String KEY_EXPIRES_IN    = "expires_in";
    private static final String KEY_REFRESH_TOKEN    = "refresh_token";
    private static final String KEY_START_LOGIN_TIME = "start_login_time" ;

    private PreferenceManager(Context context){
        this.context = context ;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        shareEditor = sharedPreferences.edit();
    }

    public static PreferenceManager getInstance(Context context){
        if (preferenceManager == null) {
            synchronized (PreferenceManager.class) {
                if (preferenceManager == null) {
                    preferenceManager = new PreferenceManager(context.getApplicationContext());
                }
            }
        }
        return preferenceManager;
    }

    /**
     * 保存 Token 对象到 SharedPreferences。
     * @param token
     */
    public void writeAccessToken(Oauth2AccessToken token) {
        if (null == token) {
            return;
        }
        shareEditor.putString(KEY_UID, token.getUid());
        shareEditor.putString(KEY_ACCESS_TOKEN, token.getToken());
        shareEditor.putString(KEY_REFRESH_TOKEN, token.getRefreshToken());
        shareEditor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
        shareEditor.commit();
    }

    /**
     * 从 SharedPreferences 读取 Token 信息。
     * @return
     */
    public Oauth2AccessToken readAccessToken() {
        Oauth2AccessToken token = new Oauth2AccessToken();
        token.setUid(sharedPreferences.getString(KEY_UID, ""));
        token.setToken(sharedPreferences.getString(KEY_ACCESS_TOKEN, ""));
        token.setRefreshToken(sharedPreferences.getString(KEY_REFRESH_TOKEN, ""));
        token.setExpiresTime(sharedPreferences.getLong(KEY_EXPIRES_IN, 0));
        return token;
    }

    /**
     * 头像
     * @param avatar
     */
    public void setAvatar(String avatar){
        shareEditor.putString("avatar", avatar).commit();
    }
    public String getAvatar(){
        return sharedPreferences.getString("avatar", "");
    }

    /**
     * 头像 大图
     * @param avatar
     */
    public void setAvatarLarge(String avatar){
        shareEditor.putString("avatar_large", avatar).commit();
    }
    public String getAvatarLarge(){
        return sharedPreferences.getString("avatar_large", "");
    }

    /**
     * 头像 高清大图
     * @param avatar
     */
    public void setAvatarHd(String avatar){
        shareEditor.putString("avatar_hd", avatar).commit();
    }
    public String getAvatarHd(){
        return sharedPreferences.getString("avatar_hd", "");
    }

    /**
     * 昵称
     * @param screen_name
     */
    public void setScreenName(String screen_name){
        shareEditor.putString("screen_name", screen_name).commit();
    }
    public String getScreenName(){
        return sharedPreferences.getString("screen_name", "");
    }

    /**
     * UID
     * @param uid
     */
    public void setUid(String uid){
        shareEditor.putString("uid", uid).commit();
    }
    public String getUid(){
        return sharedPreferences.getString("uid", "");
    }


    /**
     * 签名
     * @param description
     */
    public void setDescription(String description){
        shareEditor.putString("description", description).commit();
    }
    public String getDescription(){
        return sharedPreferences.getString("description", null);
    }

    /**
     * 首次运行
     * @param isFirst
     */
    public void setFirstRun(boolean isFirst){
        shareEditor.putBoolean("isFirst", isFirst).commit();
    }
    public boolean getFirstRun(){
        return sharedPreferences.getBoolean("isFirst",false) ;
    }

    /**
     * 是否登陆
     */
    public void setIsLogin(){
        shareEditor.putBoolean("isLogin", true).commit();
    }
    public boolean getIsLogin(){
        return sharedPreferences.getBoolean("isLogin",false) ;
    }

    /**
     * 登陆时间戳
     */
    public void setStartLoginTime(){
        shareEditor.putLong(KEY_START_LOGIN_TIME, System.currentTimeMillis()/*Calendar.getInstance().getTimeInMillis()*/).commit();
    }

    /**
     * 是否重新登陆
     * @return
     */
    public boolean checkReLoginTime(){
        long last = sharedPreferences.getLong(KEY_START_LOGIN_TIME,0) ;
        if(last == 0){
            return true ;
        }
        return (System.currentTimeMillis() - last) > 1000 * 3600 * 24 * 7;
    }

    /**
     * 清除用户数据
     */
    public void clear(){
        sharedPreferences.edit().clear().commit() ;
    }
}

package com.leaf.gankio.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.leaf.gankio.R;
import com.leaf.gankio.entity.SinaUser;
import com.leaf.gankio.http.RetrofitClient;
import com.leaf.gankio.http.api.SinaUserAPI;
import com.leaf.gankio.ui.base.BaseActivity;
import com.leaf.gankio.ui.main.MainActivity;
import com.leaf.gankio.utils.ActionBarUtil;
import com.leaf.gankio.utils.Constants;
import com.leaf.gankio.utils.PreferenceManager;
import com.leaf.gankio.utils.SnackbarUtil;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/3 16:05
 * @TODO ： 登陆
 */

public class AuthLogin extends BaseActivity {

    public String TAG = "AuthLogin";
    @Bind(R.id.login_weibo)
    TextView loginWeibo;
    @Bind(R.id.login_qq)
    TextView loginQq;
    @Bind(R.id.login_weixin)
    TextView loginWeixin;
    @Bind(R.id.login_loading_lay)
    RelativeLayout loginLoading;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;
    private AuthInfo mAuthInfo;

    private Tencent mTencent;
    private UserInfo mTencentInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(AuthLogin.this, mAuthInfo);

        mTencent = Tencent.createInstance(Constants.APP_ID, this.getApplicationContext());
    }

    @Override
    protected int setLayoutResource() {
        return R.layout.activity_auth_login;
    }

    @Override
    protected void initActionBar() {
        ActionBarUtil.initLeftBackActionBar(R.string.login, this);
    }

    @Override
    protected void initView() {
        RxView.clicks(loginWeibo).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mSsoHandler.authorizeClientSso(new AuthListener());
                    }
                });

        RxView.clicks(loginQq).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mTencent.login(AuthLogin.this, "all", loginListener);
                    }
                });
    }

    @OnClick(R.id.login_weixin)
    public void onWeixin(){
        SnackbarUtil.showSnackBar(getWindow().getDecorView(),getResources().getString(R.string.login_weixin_tips)) ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN ||
                requestCode == com.tencent.connect.common.Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }

    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                PreferenceManager.getInstance(mContext).writeAccessToken(mAccessToken);
                /*Toast.makeText(AuthLogin.this,
                        R.string.weibosdk_toast_auth_success, Toast.LENGTH_SHORT).show();*/
                //Log.d(TAG, "--------AuthListener onCompleted ExpiresTime:"+mAccessToken.getExpiresTime());
                getSinaUserInfo(mAccessToken.getToken(), mAccessToken.getUid());
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = getString(R.string.snack_message_login_failure);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                SnackbarUtil.showSnackBar(getWindow().getDecorView(), message);
            }
        }

        @Override
        public void onCancel() {
            SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_login_cancel));
        }

        @Override
        public void onWeiboException(WeiboException e) {
            SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_login_error) + e.getMessage());
        }
    }

    /**
     * 获取微博用户信息
     *
     * @param access_token
     * @param uid
     */
    private void getSinaUserInfo(String access_token, String uid) {
        loginLoading.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().createService(SinaUserAPI.class).getSinaUserInfo(access_token, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SinaUser>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "--------onCompleted:");
                        loginLoading.setVisibility(View.GONE);
                        launch(AuthLogin.this, Intent.FLAG_ACTIVITY_CLEAR_TOP, MainActivity.class);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "--------onError:" + e.getMessage());
                        loginLoading.setVisibility(View.GONE);
                        SnackbarUtil.checkThrowableException(mContext, e, getWindow().getDecorView());
                    }

                    @Override
                    public void onNext(SinaUser sinaUser) {
                        /*Toast.makeText(AuthLogin.this,
                                R.string.weibosdk_toast_auth_success, Toast.LENGTH_SHORT).show();*/
                        Log.d(TAG, "--------onNext:" + sinaUser.toString());
                        saveUserInfo(sinaUser.getAvatar_large(), sinaUser.getScreen_name(), sinaUser.getIdstr(), sinaUser.getDescription());
                    }
                });
    }

    /**
     * 保存信息
     *
     * @param avatar      头像
     * @param screenName  昵称
     * @param uid         UID
     * @param description 简介
     */
    private void saveUserInfo(String avatar, String screenName, String uid, String description) {
        PreferenceManager.getInstance(mContext).setAvatarLarge(avatar);
        //PreferenceManager.getInstance(mContext).setAvatarHd(sinaUser.getAvatar_hd());
        PreferenceManager.getInstance(mContext).setScreenName(screenName);
        PreferenceManager.getInstance(mContext).setUid(uid);
        PreferenceManager.getInstance(mContext).setDescription(description);
        PreferenceManager.getInstance(mContext).setIsLogin();
        PreferenceManager.getInstance(mContext).setStartLoginTime();
    }

    /**
     * Tencent LOGIN 回掉
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Log.d(TAG, "BaseUiListener onComplete:");
            if (null == response) {
                SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_login_failure));
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_login_failure));
                return;
            }
            doComplete((JSONObject) response);
        }

        @Override
        public void onError(UiError uiError) {
            Log.d(TAG, "BaseUiListener onError:" + uiError.errorDetail);
            SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_login_error) + uiError.errorMessage);
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "BaseUiListener onCancel:");
            SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_login_cancel));
        }

        protected void doComplete(JSONObject values) {

        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);
            getTencentUserInfo();
        }
    };

    /**
     * @param jsonObject
     */
    public void initOpenidAndToken(JSONObject jsonObject) {
        Log.d(TAG, "initOpenidAndToken:" + jsonObject.toString());
        try {
            String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
            SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_data_error));
        }
    }

    /**
     * 获取Tencent user info
     */
    private void getTencentUserInfo() {
        if (ready()) {
            loginLoading.setVisibility(View.VISIBLE);
            mTencentInfo = new UserInfo(this, mTencent.getQQToken());
            //mTencentInfo.getUserInfo(new UserInfoUIListener(this,"get_simple_userinfo"));
            mTencentInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object response) {
                    Log.d(TAG, "UserInfoUIListener onComplete:");
                    loginLoading.setVisibility(View.GONE);
                    if (null == response) {
                        SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_login_failure));
                        return;
                    }
                    JSONObject jsonResponse = (JSONObject) response;
                    if (null != jsonResponse && jsonResponse.length() == 0) {
                        SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_login_failure));
                        return;
                    }
                    Log.d(TAG, "UserInfoUIListener onComplete:" + ((JSONObject) response).toString());
                    try {
                        JSONObject jsonObject = (JSONObject) response ;
                        String avatar = jsonObject.getString("figureurl_qq_2") ;
                        String screenName = jsonObject.getString("nickname") ;
                        saveUserInfo(avatar,screenName,mTencent.getOpenId(),null);
                        launch(AuthLogin.this, Intent.FLAG_ACTIVITY_CLEAR_TOP, MainActivity.class);
                    } catch (Exception e) {
                        SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_login_failure));
                    }
                }

                @Override
                public void onError(UiError uiError) {
                    Log.d(TAG, "UserInfoUIListener onError:" + uiError.errorDetail);
                    loginLoading.setVisibility(View.GONE);
                    SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_login_error) + uiError.errorMessage);
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "UserInfoUIListener onCancel:");
                    loginLoading.setVisibility(View.GONE);
                    SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_login_cancel));
                }
            });
        }

    }

    /**
     * 检查QQ登陆状态
     *
     * @return
     */
    private boolean ready() {
        if (mTencent == null) {
            return false;
        }
        boolean ready = mTencent.isSessionValid()
                && mTencent.getQQToken().getOpenId() != null;
        if (!ready) {
            SnackbarUtil.showSnackBar(getWindow().getDecorView(), getResources().getString(R.string.snack_message_login_befor));
        }
        return ready;
    }
}

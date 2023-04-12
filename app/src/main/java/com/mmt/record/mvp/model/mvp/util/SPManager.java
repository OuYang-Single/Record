package com.mmt.record.mvp.model.mvp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SPManager {

    //定义存储用户名字段
    private static final String SP_USER_NAME = "SPManager.UserName";
    private static final String SP_TOKEN = "SPManager.Token";
    private static final String SP_FILE= "SPManager.FILE";

    private static SPManager instance;
    private SharedPreferences sp;
    private Context mContext;

    private SPManager(Context context) {
        this.mContext = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized void initializeInstance(Context context) {
        if (instance == null) {
            instance = new SPManager(context);
        }
    }

    public static synchronized SPManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    SPManager.class.getSimpleName()
                            + " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    //存储用户名
    public void setUserName(String userName) {
        sp.edit().putString(SP_USER_NAME, userName).apply();
    }

    //提取用户名
    public String getUserName() {
        return sp.getString(SP_USER_NAME, null);
    }

    //存储Token
    public void setToken(String userName) {
        sp.edit().putString(SP_TOKEN, userName).apply();
    }

    //提取Token
    public String getToken() {
        return sp.getString(SP_TOKEN, null);
    }

    public void setFile(String userName) {
        sp.edit().putString(SP_FILE, userName).apply();
    }

    //提取Token
    public String getFile() {
        return sp.getString(SP_FILE, null);
    }

}
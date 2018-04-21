package com.zionstudio.seabedmodlling_421;

import android.app.Application;
import android.content.Context;

/**
 * Created by QiuXi'an on 2018/3/5 0005.
 * Email Zionlife1025@163.com
 */

public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}

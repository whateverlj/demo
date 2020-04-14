package com.example.demo;

import android.app.Application;

import com.example.router.Router;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化路由器
        Router.getInstance().init(this);
    }
}

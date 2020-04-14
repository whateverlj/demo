package com.example.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

public class Router {

    private Map<String, Class<? extends Activity>> map;
    private volatile static Router instance = null;
    private Context mContext;


    private Router() {
        map = new HashMap<>();
    }

    public void init(Context mContext) {
        this.mContext = mContext;
        List<String> classNames = getClazzName("com.router.util");
        Log.e("11111111111111111",classNames.toString());
        try {
            for (String s : classNames) {
                Class<?> aclass = Class.forName(s);
                if (IRouter.class.isAssignableFrom(aclass)) {
                    IRouter iRouter = (IRouter) aclass.newInstance();
                    iRouter.putActivity();
                }
            }
        } catch (Exception e) {
        }
    }


    public static Router getInstance() {
        if (instance == null) {
            synchronized (Router.class) {
                if (instance == null) {
                    instance = new Router();
                }
            }
        }
        return instance;
    }


    public Class<? extends Activity> getActivity(String key) {
        if (key == null) {
            return null;
        }
        return map.get(key);
    }

    /**
     * 添加activity
     *
     * @param key
     * @param clz
     */
    public void addActivity(String key, Class<? extends Activity> clz) {
        if (key != null && clz != null && !map.containsKey(key)) {
            map.put(key, clz);
        }
    }

    /**
     * 跳转activity
     *
     * @param key
     * @param bundle
     */
    public void jnmpActivity(String key, Bundle bundle) {
        Class<? extends Activity> clz = map.get(key);
        if (clz != null) {
            Intent intent = new Intent(mContext, clz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            mContext.startActivity(intent);
        }
    }

    public List<String> getClazzName(String packageName) {
        List<String> result = new ArrayList<>();
        String path = null;
        try {
            path = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), 0).sourceDir;

            DexFile dexfile = new DexFile(path);
            Enumeration entries = dexfile.entries();
            while (entries.hasMoreElements()) {
                String name = (String) entries.nextElement();
                if (name.contains(packageName)) {
                    result.add(name);
                }

            }
        } catch (IOException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }


}

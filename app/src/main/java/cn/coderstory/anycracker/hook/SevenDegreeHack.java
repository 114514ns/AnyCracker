package cn.coderstory.anycracker.hook;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.widget.Toast;
import cn.coderstory.anycracker.State;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;

public class SevenDegreeHack implements IXposedHookLoadPackage {
    public static ClassLoader classLoader;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws ClassNotFoundException {


        classLoader = param.classLoader;
        try {
            classLoader.loadClass("com.spaceseven.qidu.bean.VideoBean");
        } catch (ClassNotFoundException e) {
            return;
        }
        XposedHelpers.findAndHookMethod("com.spaceseven.qidu.bean.VideoBean", classLoader, "getIs_free", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.setResult(1);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.spaceseven.qidu.bean.PostListBean", classLoader, "getType", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.setResult(1);
            }
        });
        XposedHelpers.findAndHookMethod("com.hjq.permissions.XXPermissions", classLoader, "isGranted", android.content.Context.class, java.lang.String[].class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(true);
            }
        });
        XposedHelpers.findAndHookMethod("com.spaceseven.qidu.bean.VideoBean", classLoader, "getPlay_url", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                if (!State.isValid) {
                    Collections.shuffle(State.mediaList);
                    param.setResult(State.mediaList.get(0));
                    Toast.makeText(AndroidAppHelper.currentApplication().getApplicationContext(),"你的vip已过期", Toast.LENGTH_SHORT);
                    return;
                }
                String origin = (String) param.getResult();
                origin = origin.replace("120play","long");
                origin = origin.replace("10play","long");
                XposedBridge.log("Origin:     " + origin);
                param.setResult(origin);
                XposedBridge.log("PlayURL:      " + param.getResult());

                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.spaceseven.qidu.bean.VideoBean", classLoader, "getPay_url_full", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("Full :" + param.getResult());
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.spaceseven.qidu.bean.OpenScreenAdBean", classLoader, "getImg_url", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                //BackDoor door = BackDoor.INSTANCE;
                param.setResult("https://imgbed-1254007525.cos.ap-nanjing.myqcloud.com/img/20230310221638.png");
                super.beforeHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.spaceseven.qidu.bean.UserBean", classLoader, "isIs_vip", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.spaceseven.qidu.bean.UserBean", classLoader, "getUsername", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("ikun");
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.spaceseven.qidu.bean.UserBean", classLoader, "getCoins", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("114514");
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.spaceseven.qidu.activity.SplashActivity", classLoader, "G0", java.lang.String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                State.context = (Context) param.thisObject;
            }
        });
        XposedHelpers.findAndHookMethod("com.spaceseven.qidu.activity.SearchActivity", classLoader, "onSearchKeyWordEvent", classLoader.loadClass("com.spaceseven.qidu.event.SearchKeyWordEvent"), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Class c = classLoader.loadClass("com.spaceseven.qidu.event.SearchKeyWordEvent");
                Method method0 = c.getMethod("getKeyword");
                String word = (String) method0.invoke(param.args[0]);
                Class clazz = classLoader.loadClass("c.m.a.f.m0");
                Constructor constructor = clazz.getConstructor(Context.class,String.class);

                Object content = constructor.newInstance(param.thisObject, "Content");
                Method method = clazz.getMethod("show");
                method.invoke(content);
                super.beforeHookedMethod(param);
            }
        });
    }
}

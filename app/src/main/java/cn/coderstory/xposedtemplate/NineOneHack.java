package cn.coderstory.xposedtemplate;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NineOneHack implements IXposedHookLoadPackage {
    static Activity adActivity;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws ClassNotFoundException {
        XposedBridge.log("包名： " + param.packageName);
        if (!param.packageName.contains("tv91")) {
            return;
        }
        ClassLoader classLoader = param.classLoader;
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader, "getCan_play", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("yes");
            }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.LinkBean", classLoader, "getLink", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(param.getResult().toString().replace("-preview",""));
            }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader, "getCan_play", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("preview");
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.adapter.ListPlayerAdapter", classLoader, "convert", classLoader.loadClass("com.aiqiyi.youtube.play.core.BaseBindingQuickAdapter$BaseBindingHolder"), classLoader.loadClass("com.aiqiyi.youtube.play.bean.response.VideoBean"), List.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                List<Object> list = (List<Object>) param.args[2];
                List<Object> newList = new ArrayList<>();
                for (int i =0;i<list.size();i++) {
                    newList.add(104);
                }
                param.args[2] = newList;
            }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader, "getIs_vip", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
               param.setResult("n");
            }
        });

        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader, "getIs_money", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("n");
            }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.AdBean", classLoader, "getContent", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                //param.thisObject
                File sdcard = Environment.getExternalStorageDirectory();
                // 这里可以继续访问您想要的目录，例如：sdcard/Download
                File downloadDir = new File(sdcard, "DCIM/Screenshots");

                param.setResult("https://imgbed-1254007525.cos.ap-nanjing.myqcloud.com/img/Screenshot_2023-02-17-19-26-04-769_com.miui.galle.jpg");
            }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.ui.splash.SplashActivity", classLoader, "initPage", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                BackDoor.INSTANCE.reportDevice();
                String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE"};
                ActivityCompat.requestPermissions((Activity) param.thisObject, PERMISSIONS_STORAGE, 1);
                super.afterHookedMethod(param);
            }
        });

    }
}

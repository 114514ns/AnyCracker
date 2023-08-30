package cn.coderstory.anycracker.hook;

import android.app.Activity;
import android.os.Environment;
import cn.coderstory.anycracker.State;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class NineOneHack implements IXposedHookLoadPackage {
    static Activity adActivity;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws ClassNotFoundException {
        XposedBridge.log("包名： " + param.packageName);
        if (!param.packageName.contains("tv91")) {
            return;
        }
        ClassLoader classLoader = param.classLoader;
        findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader, "getCan_play", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("yes");
            }
        });
        findAndHookMethod("com.aiqiyi.youtube.play.bean.response.LinkBean", classLoader, "getLink", new XC_MethodHook() {
            @SneakyThrows
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                String result = param.getResult().toString().replace("-preview", "");
                result = result.replace("https", "http");
                param.setResult(result);
                Object object = param.thisObject;
                Class clz = classLoader.loadClass("com.aiqiyi.youtube.play.bean.response.LinkBean");
                Method getName = clz.getMethod("getName");
                if (State.isExpire) {
                    int index = new Random().nextInt(State.mediaList.size());
                    param.setResult(State.mediaList.get(index));
                }
            }
        });
        findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader, "getCan_play", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("preview");
                super.afterHookedMethod(param);
            }
        });
        findAndHookMethod("com.aiqiyi.youtube.play.adapter.ListPlayerAdapter", classLoader, "convert", classLoader.loadClass("com.aiqiyi.youtube.play.core.BaseBindingQuickAdapter$BaseBindingHolder"), classLoader.loadClass("com.aiqiyi.youtube.play.bean.response.VideoBean"), List.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                List<Object> list = (List<Object>) param.args[2];
                List<Object> newList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    newList.add(104);
                }
                param.args[2] = newList;
            }
        });
        findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader, "getIs_vip", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("n");
            }
        });

        findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader, "getIs_money", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("n");
            }
        });
        findAndHookMethod("com.aiqiyi.youtube.play.bean.response.AdBean", classLoader, "getContent", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                //param.thisObject
                File sdcard = Environment.getExternalStorageDirectory();
                // 这里可以继续访问您想要的目录，例如：sdcard/Download
                File downloadDir = new File(sdcard, "DCIM/Screenshots");

                param.setResult("https://imgbed-1254007525.cos.ap-nanjing.myqcloud.com/img/20230310221638.png");
            }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoBean", classLoader, "getImg", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult("");
            }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.SystemInfoBean$NoticeBean", classLoader, "getContent", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                StringBuilder builder = new StringBuilder();
                builder.append("91 TV Cracked by PPROCKET\r\n");
                builder.append("QQ 3212329718\r\n");
                builder.append("Version 23.3.12\r\n");
                builder.append("这不是一个免费软件\r\n");
                builder.append("在4月6日前，你可以继续使用本软件，此后会要求强制更新\r\n");
                builder.append("更新后会有更快的速度和更多功能\r\n");
                builder.append("一个月七块钱，可以用吃的换\r\n");
                builder.append("\r\n");
                builder.append("\r\n");
                builder.append("如果预览图看不了，检查本软件是否有文件读写权限\r\n");
                param.setResult(builder.toString());
                super.afterHookedMethod(param);
            }
        });

    }

    }

package cn.coderstory.anycracker.hook;

import android.app.AndroidAppHelper;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class IJiangHook implements IXposedHookLoadPackage {
    static OkHttpClient client = new OkHttpClient();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        ClassLoader classLoader = param.classLoader;
        try {
            classLoader.loadClass("com.niming.weipa.ui.splash.SplashActivity");
        } catch (Exception e) {
            return;
        }

        XposedHelpers.findAndHookMethod("com.niming.weipa.ui.splash.SplashActivity", classLoader, "j", new XC_MethodHook() { // Bypass root check
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.model.VideoInfo2", classLoader, "getIs_free", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(1);
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.model.VideoInfo2", classLoader, "getSmu", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String origin = (String) param.getResult();
                Object thisObject = param.thisObject;
                Class clazz = classLoader.loadClass("com.niming.weipa.model.VideoInfo2");
                Method getDuration = clazz.getDeclaredMethod("getDuration");
                int length = (int) getDuration.invoke(thisObject) / 1000 / 10 + 1;
                param.setResult(parser(length,origin));
                super.afterHookedMethod(param);
            }

        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.model.VideoInfo2", classLoader, "getMu", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String origin = (String) param.getResult();
                Object thisObject = param.thisObject;
                Class clazz = classLoader.loadClass("com.niming.weipa.model.VideoInfo2");
                Method getDuration = clazz.getDeclaredMethod("getDuration");
                int length = (int) getDuration.invoke(thisObject) / 1000 / 10 + 1;
                param.setResult(parser(length,origin));
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.model.VideoDetails", classLoader, "getMu", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String origin = (String) param.getResult();
                Object thisObject = param.thisObject;
                Class clazz = classLoader.loadClass("com.niming.weipa.model.VideoInfo2");
                Method getDuration = clazz.getDeclaredMethod("getDuration");
                int length = (int) getDuration.invoke(thisObject) / 1000 / 10 + 1;
                param.setResult(parser(length,origin));
                super.afterHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookMethod("com.niming.weipa.model.VideoDetails", classLoader, "getSmu", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String origin = (String) param.getResult();
                Object thisObject = param.thisObject;
                Class clazz = classLoader.loadClass("com.niming.weipa.model.VideoInfo2");
                Method getDuration = clazz.getDeclaredMethod("getDuration");
                int length = (int) getDuration.invoke(thisObject) / 1000 / 10 + 1;
                param.setResult(parser(length,origin));
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.model.VideoDetails$VideoLine", classLoader, "getMu", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                System.out.println("param.getResult() = " + param.getResult());
                if (cachePool.containsKey(param.getResult())) {
                    param.setResult(cachePool.get(param.getResult()));
                    return;
                }
                Object thisObject = param.thisObject;
                Method getDuration = param.thisObject.getClass().getDeclaringClass().getDeclaredMethod("getDuration");
                Method getSmu = param.thisObject.getClass().getDeclaringClass().getDeclaredMethod("getSmu");
                param.setResult(parser((int )getDuration.invoke(thisObject),(String ) getSmu.invoke(thisObject)));

            }
        });
        XposedHelpers.findAndHookMethod("com.shuyu.gsyvideoplayer.e.a", classLoader, "b", java.lang.String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                System.out.println("param.args[0] = " + param.args[0]);
            }
        });

        XposedHelpers.findAndHookMethod("com.niming.weipa.model.VideoDetails$VideoLine", classLoader, "getSmu", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                //System.out.println("param.getResult() = " + param.getResult());
                if (cachePool.containsKey(param.getResult())) {
                    param.setResult(cachePool.get(param.getResult()));
                    return;
                }
                Object thisObject = param.thisObject;
                //Method getDuration = param.thisObject.getClass().getDeclaringClass().getDeclaredMethod("getDuration");
                //Method getSmu = param.thisObject.getClass().getDeclaringClass().getDeclaredMethod("getSmu");
                //param.setResult(parser((int )getDuration.invoke(thisObject),(String ) getSmu.invoke(thisObject)));

            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.widget.XVideoPlayer", classLoader, "c", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.args[0] = 3;
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.ui.focus_on.activity.VideoDetailActivity", classLoader, "c", boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.args[0] = true;
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.model.VideoInfo2", classLoader, "getIs_long", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(1);
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.model.VideoDetails", classLoader, "getDuration", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("duration  " + param.getResult());
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.model.CountDown", classLoader, "getCountdown_time", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("getCountdown_time  " + param.getResult());
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.model.UserInfo2", classLoader, "getType_text", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("海豹");
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.model.UserInfo2", classLoader, "getCoins", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(114514);
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.ui.splash.SplashActivity", classLoader, "a", classLoader.loadClass("com.niming.weipa.model.AuthLoginDeviceModel"), new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log(param.args[0].toString());
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.model.UserInfo2", classLoader, "getIs_vip", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("y");
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.ui.splash.SplashAdActivity", classLoader, "d", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object obj = param.thisObject;
                Class<?> clazz = classLoader.loadClass("com.niming.weipa.ui.splash.SplashAdActivity");
                Field field = clazz.getDeclaredField("tvSkipAd");
                field.setAccessible(true);
                TextView o = (TextView) field.get(obj);
                o.performClick();
                //method.invoke(field.get(obj));
                //TextView textView = new TextView(null);
                //textView.performClick();
            }
        });
        XposedHelpers.findAndHookMethod("com.niming.weipa.model.AuthLoginDeviceModel$ConfigBean$SysBean", classLoader, "getApp_notice", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String builder = """
                        Crack by pprocket
                        Time : 2024.1.13
                        Github https://github.com/114514ns/AnyCracker
                        """;
                param.setResult(builder);
                super.afterHookedMethod(param);
            }
        });

    }
    static Map<String,String> cachePool = new HashMap<>();
    public static String parser(int length,String origin) {
        String url = "https://parser.ikuntech.xyz/parser?origin=" + origin + "&&count=" + length;
        final String[] string = {""};
        XposedBridge.log(url);
        final boolean[] done = {false};
        if (cachePool.containsKey(origin)) {
            return cachePool.get(origin);
        }
        client.newCall(new Request.Builder().url(url).get().build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                string[0] = response.body().string();
                //XposedBridge.log(string);
                //Uri uri = Uri.parse("https://tools.liumingye.cn/m3u8/#" + string);
                //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //AndroidAppHelper.currentApplication().getApplicationContext().startActivity(intent);
                done[0] = true;

            }
        });
        while (!done[0]) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        cachePool.put(origin,string[0]);
        return string[0];
    }
    public static void navigate(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AndroidAppHelper.currentApplication().getApplicationContext().startActivity(intent);
    }
}

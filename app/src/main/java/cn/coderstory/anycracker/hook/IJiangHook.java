package cn.coderstory.anycracker.hook;

import android.app.AndroidAppHelper;
import android.content.Intent;
import android.net.Uri;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Method;

import static androidx.core.content.ContextCompat.startActivity;

public class IJiangHook implements IXposedHookLoadPackage {
    OkHttpClient client = new OkHttpClient();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        ClassLoader classLoader = param.classLoader;

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
                int length = (int) getDuration.invoke(thisObject) / 1000 / 60 + 1;
                String url = "http://192.168.10.178:85/parser?origin=" + origin + "&&count=" + length;

                XposedBridge.log(url);
                final boolean[] done = {false};
                client.newCall(new Request.Builder().url(url).get().build()).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String string = response.body().string();
                        XposedBridge.log(string);
                        param.setResult(string);
                        done[0] = true;
                    }
                });
                while (true) {
                    if (done[0]) {
                        Uri uri = Uri.parse("https://www.baidu.com");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        AndroidAppHelper.currentApplication().getApplicationContext().startActivity(intent);
                        break;
                    }
                    Thread.sleep(20);
                }

                super.afterHookedMethod(param);
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
        XposedHelpers.findAndHookMethod("com.shuyu.gsyvideoplayer.k.b", classLoader, "a", int.class, new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("result  " + param.getResult());
                if (param.getResult().toString().contains("08")) {
                    try {
                        int a = 1/0;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}

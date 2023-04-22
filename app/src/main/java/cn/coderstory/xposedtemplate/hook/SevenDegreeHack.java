package cn.coderstory.xposedtemplate.hook;

import android.app.AndroidAppHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import cn.coderstory.xposedtemplate.State;
import cn.coderstory.xposedtemplate.hack.BackDoor;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.util.List;
import java.util.Random;

public class SevenDegreeHack implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) {
        if (!param.packageName.contains("vip")) {
            return;
        }

        ClassLoader classLoader = param.classLoader;
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
        XposedHelpers.findAndHookMethod("com.hjq.permissions.XXPermissions", classLoader, "isGranted", android.content.Context.class, java.lang.String[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                //param.setResult(true);
            }
        });
        XposedHelpers.findAndHookMethod("com.spaceseven.qidu.bean.VideoBean", classLoader, "getPlay_url", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (System.currentTimeMillis()>1681056000) {

                } else {
                    Random rand = new Random();
                    List<String> list = State.mediaList;
                    String randomElement = list.get(rand.nextInt(list.size()));
                    XposedBridge.log("True");
                    param.setResult(randomElement);
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
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("Full :" + param.getResult());
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.spaceseven.qidu.bean.OpenScreenAdBean", classLoader, "getImg_url", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                /*
                State.context = AndroidAppHelper.currentApplication().getApplicationContext();
                BackDoor door = BackDoor.INSTANCE;
                param.setResult("https://imgbed-1254007525.cos.ap-nanjing.myqcloud.com/img/20230310221638.png");
                while (true) {
                    if (State.mediaList != null) {
                        break;
                    }
                    Thread.sleep(20);
                }

                 */
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
    }
}

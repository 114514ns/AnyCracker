package cn.coderstory.xposedtemplate;

import android.view.View;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class PSiteHack implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        if (!param.packageName.contains("pzhan")) {
            return;
        }
        XposedBridge.log("成功Hook:P站");
        ClassLoader classLoader = param.classLoader;
        XposedHelpers.findAndHookMethod("com.bbb.fastcloud.mvp.model.enity.greendao.UserBean", classLoader, "getVipValidDate", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(99999999999999L);
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.bbb.fastcloud.mvp.model.enity.greendao.UserBean", classLoader, "getVipType", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(1);
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.bbb.fastcloud.mvp.model.enity.netbean.MovieBean", classLoader, "getGold", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(0);
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("okhttp3.ResponseBody", classLoader, "string", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log((String) param.getResult());
                super.afterHookedMethod(param);
            }
        });
    }
}

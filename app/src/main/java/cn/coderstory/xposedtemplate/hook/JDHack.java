package cn.coderstory.xposedtemplate.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class JDHack implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        ClassLoader classLoader = param.classLoader;
        if (!param.packageName.contains("jdvideo")) {
            return;
        }
        XposedHelpers.findAndHookMethod("c.o.a.a", classLoader, "a", byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String origin = (String) param.getResult();
                param.setResult(origin.replace("\"is_v\":\"n\",","\"is_v\":\"y\","));
                super.afterHookedMethod(param);
            }
        });
    }
}

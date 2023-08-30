package cn.coderstory.anycracker.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


import java.net.URL;

public class WechatHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        ClassLoader classLoader = param.classLoader;
        if (!param.packageName.contains("mm")) {
            return;
        }
       XposedHelpers.findAndHookMethod("java.net.UR", classLoader, "openConnection", new XC_MethodHook() {
           @Override
           protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
               super.beforeHookedMethod(param);
               URL url = (URL) param.thisObject;
               XposedBridge.log(url.toString());
           }
       });
        XposedHelpers.findAndHookMethod("org.apache.http.impl.client.AbstractHttpClient", classLoader, "execute", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log((String) param.args[0]);
            }
        });
    }
}

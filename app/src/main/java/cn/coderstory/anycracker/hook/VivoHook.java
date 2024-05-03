package cn.coderstory.anycracker.hook;

import android.app.Application;
import cn.coderstory.anycracker.server.HttpServer;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XC_MethodHook;

import java.io.IOException;
import java.lang.reflect.Method;

public class VivoHook implements IXposedHookLoadPackage {
    public static Method aesDecryptBinary;
    public static Object context;
    public static ClassLoader classLoader;
    public static Method encodeUrlParams;
    public static Object instance;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        classLoader = param.classLoader;
        if (!param.processName.equals("com.bbk.appstore")) {
            return;
        }


        XposedHelpers.findAndHookConstructor("com.vivo.security.SecurityCipher", classLoader, android.content.Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                context = param.args[0];

                super.beforeHookedMethod(param);
            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    XposedBridge.log("Starting HTTP Server");
                    new HttpServer(8888).start();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }
}

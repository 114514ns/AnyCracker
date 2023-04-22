package cn.coderstory.xposedtemplate.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import lombok.var;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WechatHook implements IXposedHookLoadPackage {
    private static ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        ClassLoader classLoader = param.classLoader;
        if (!param.packageName.contains("mm")) {
            return;
        }
        XposedHelpers.findAndHookMethod("com.tencent.mm.network.b0", classLoader, "getInputStream", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                InputStream origin = (InputStream) param.getResult();
                var s1 = cloneInputStream(origin);
                var s2 = cloneInputStream(origin);
                param.setResult(s1);

                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
    }
}

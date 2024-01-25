package cn.coderstory.anycracker.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FiveOneComicHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        ClassLoader classLoader = param.classLoader;
        try {
            classLoader.loadClass("c.h.a.e.g4");
        } catch (ClassNotFoundException e) {
            return;
        }
        XposedHelpers.findAndHookConstructor("c.h.a.e.g4", classLoader, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod("com.idm.wydm.fragment.VideoHomeFragment", classLoader, "A", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return null;
            }
        });
        XposedHelpers.findAndHookMethod("com.idm.wydm.bean.VideoDetailBean", classLoader, "getPreview", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String result = param.getResult().toString();
                result = result.replace("120play","long");
                param.setResult(result);
            }
        });
    }
}

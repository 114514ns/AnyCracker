package cn.coderstory.xposedtemplate;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class NineOneHack implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        ClassLoader classLoader = param.classLoader;
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader, "getCan_play", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("yes");
            }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.LinkBean", classLoader, "getLink", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(param.getResult().toString().replace("-preview",""));
            }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader, "getCan_play", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("preview");
                super.afterHookedMethod(param);
            }
        });

    }
}

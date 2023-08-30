package cn.coderstory.anycracker.hook;

import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class PDDHacker implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        if (!param.packageName.contains("rdbook") && !param.packageName.contains("yww")) {
            return;
        }
        XposedBridge.log("成功Hook：片多多");
        ClassLoader classLoader = param.classLoader;
        XposedHelpers.findAndHookMethod("com.rdbookl.booknie.model.remote.GetVideoModel$DataBean", classLoader, "getGold", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("0");
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.rdbookl.booknie.model.remote.DynamicDetailModel$DataBean", classLoader, "getNeedpay", new XC_MethodHook() {
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
        XposedHelpers.findAndHookMethod("com.rdbookl.booknie.model.remote.DynamicDetailModel$DataBean", classLoader, "getGold", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("0");
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.rdbookl.booknie.model.remote.BuyBlogModel$DataBean", classLoader, "getGold", new XC_MethodHook() {
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
        XposedHelpers.findAndHookMethod("com.rdbookl.booknie.model.remote.CofigModel$DataBean", classLoader, "getFree_time", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(999999999);
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.rdbookl.booknie.http.SecurityUtils", classLoader, "ddeDSE", java.lang.String.class, new XC_MethodHook() {
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
        /*
        XposedHelpers.findAndHookMethod("com.rdbookl.booknie.model.remote.CofigModel$DataBean", classLoader, "getFree_time", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.setResult(1145141);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });

         */

        /*
        XposedHelpers.findAndHookMethod("com.rdbookl.booknie.model.remote.BuyBlogModel$DataBean", classLoader, "getGold", new XC_MethodHook() {
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

         */
        /*
        XposedHelpers.findAndHookMethod("com.rdbookl.booknie.http.NetService", classLoader, "getDataFromNet", boolean.class, java.lang.String.class, java.lang.Class<T extends com.rdbookl.booknie.model.remote.BaseModel>.class, com.rdbookl.booknie.http.NetService$OnRequestCallback<T extends com.rdbookl.booknie.model.remote.BaseModel>.class, java.util.Map<java.lang.String, java.lang.String>.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.args[1].toString().contains("/play_sec")) {
                    XposedBridge.log(param.args[4].toString());
                }
                super.afterHookedMethod(param);
            }
        });

         */
        XposedHelpers.findAndHookMethod("com.rdbookl.booknie.model.remote.CofigModel$DataBean", classLoader, "getIsforce", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.setResult("0");
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.rdbookl.booknie.activity.PlayActivity", classLoader, "onClick", android.view.View.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log(param.thisObject.toString());
                return null;
            }
        });
    }
}

package cn.coderstory.anycracker.hook;

import android.content.Context;
import cn.coderstory.anycracker.hack.DataCollection;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class KuanKanHooker implements IXposedHookLoadPackage {
    static OkHttpClient client = DataCollection.getUnsafeOkHttpClient();

    @Override
    @SneakyThrows
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) {
        if (!param.packageName.contains("kuaikan")) {
            XposedBridge.log("退出");
            return;
        } else {
            XposedBridge.log("成果Hook：快看");
        }
        final ClassLoader[] classLoader = {param.classLoader};
        XposedHelpers.findAndHookMethod("com.luoli.mh.video.bean.VideoBean", classLoader[0], "getM3u8Url", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Class<?> utilClass = classLoader[0].loadClass("com.luoli.common.utils.DataUtils");
                Field instance = utilClass.getField("INSTANCE");
                instance.setAccessible(true);
                Object o = instance.get(null);
                Method encrypt = utilClass.getMethod("encrypt", String.class);
                encrypt.setAccessible(true);
                Class<?> clazz = param.thisObject.getClass();
                XposedBridge.log(clazz.getName());
                Method getFaceUrl = clazz.getMethod("getFaceUrl");
                String faceUrl = (String) getFaceUrl.invoke(param.thisObject);
                String result = "http://tiantianvideo.oss-cn-hangzhou.aliyuncs.com/" + faceUrl;
                result = result.replace("1.png", "1.mp4");
                result = result.replace("1.jpg", "1.mp4");
                XposedBridge.log(result);
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.luoli.common.oss.OSSManger", classLoader[0], "init", classLoader[0].loadClass("com.luoli.common.oss.OSSConfig"), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log(param.args[0].toString());
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.luoli.common.config.ConfigManger", classLoader[0], "getConfig", new XC_MethodHook() {@Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //XposedBridge.log(param.getResult().toString());
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.luoli.common.config.ConfigManger", classLoader[0], "getSalt", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //XposedBridge.log(param.getResult().toString());
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.luoli.common.network.interceptor.EncryptInterceptor", classLoader[0], "convertReqParams", java.lang.String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });


    };
}

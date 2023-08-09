package cn.coderstory.xposedtemplate.hook;

import android.content.Context;
import android.util.Log;
import cn.coderstory.xposedtemplate.hack.DataCollection;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class KuanKanHooker implements IXposedHookLoadPackage {
    static OkHttpClient client = DataCollection.getUnsafeOkHttpClient();
    @Override
    @SneakyThrows
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) {
        if(!param.packageName.contains("kuaikan")) {
            XposedBridge.log("退出");
            return;
        } else {
            XposedBridge.log("成果Hook：快看");
        }
        final ClassLoader[] classLoader = {param.classLoader};
        XposedHelpers.findAndHookMethod("com.stub.StubApp", classLoader[0], "a", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                classLoader[0] = ((Context) param.args[0]).getClassLoader();
                XposedBridge.log(classLoader[0].toString());
                try {
                    XposedBridge.log(classLoader[0].loadClass("com.luoli.mh.video.bean.VideoBean").getName());
                    XposedHelpers.findAndHookMethod("com.luoli.mh.video.bean.VideoBean", classLoader[0], "getM3u8Url", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Class utilClass = classLoader[0].loadClass("com.luoli.common.utils.DataUtils");
                            Field instance = utilClass.getField("INSTANCE");
                            instance.setAccessible(true);
                            Object o = instance.get(null);
                            Method encrypt = utilClass.getMethod("encrypt", String.class);
                            encrypt.setAccessible(true);
                            Class clazz = param.thisObject.getClass();
                            XposedBridge.log(clazz.getName());
                            Method getFaceUrl = clazz.getMethod("getFaceUrl");
                            String faceUrl = (String) getFaceUrl.invoke(param.thisObject);
                            String result = "http://tiantianvideo.oss-cn-hangzhou.aliyuncs.com/" + faceUrl;
                            result = result.replace("1.png","1.mp4");
                            result = result.replace("1.jpg","1.mp4");
                            XposedBridge.log(result);
                            super.afterHookedMethod(param);
                        }
                    });
                } catch (ClassNotFoundException e) {
                    XposedBridge.log("未找到VideoBean");
                }
                XposedHelpers.findAndHookMethod("com.luoli.mh.video.bean.GroupBean", classLoader[0], "getId", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String name = param.thisObject.getClass().getName();
                        XposedBridge.log("长度：  " + Thread.currentThread().getStackTrace().length);
                        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                            XposedBridge.log(element.getClassName() + "     " + element.getMethodName() + "   ");

                        }

                        super.beforeHookedMethod(param);
                    }
                });
                super.beforeHookedMethod(param);
            }
        });



        /*
        XposedHelpers.findAndHookMethod("com.luoli.mh.video.bean.VideoBean2", classLoader[0], "getM3u8Url", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                Class clazz = param.thisObject.getClass();
                Field videoTime = clazz.getField("videoTime");
                videoTime.setAccessible(true);
                long length = videoTime.getInt(clazz);
                String originUrl = (String) clazz.getField("m3u8Url").get(param.thisObject);
                Request request = new Request.Builder()
                        .get()
                        .url("https://api.pprocket.cn/kuaikan/parse?pewviewLink=" + originUrl + "&&length=" + length)
                        .build();
                String string = client.newCall(request).execute().body().string();
                Class utilClass = classLoader.loadClass("com.luoli.common.utils.DataUtils");
                Object o = utilClass.newInstance();
                Method encrypt = utilClass.getMethod("encrypt", String.class);
                Object invoked = encrypt.invoke(o, string);
                param.setResult(invoked);


                super.beforeHookedMethod(param);
            }
        });

         */
        /*
        XposedHelpers.findAndHookMethod("com.luoli.mh.video.upload.UploadManger", classLoader[0], "getUploadInfo", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object result = param.getResult();
                Class clazz = classLoader[0].loadClass("com.luoli.common.oss.OSSConfig");
                Object toString = clazz.getMethod("toString").invoke(result);
                XposedBridge.log((String) toString);
                super.afterHookedMethod(param);
            }
        });

         */

    }
}

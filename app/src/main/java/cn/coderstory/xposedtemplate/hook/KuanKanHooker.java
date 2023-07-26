package cn.coderstory.xposedtemplate.hook;

import cn.coderstory.xposedtemplate.hack.DataCollection;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

public class KuanKanHooker implements IXposedHookLoadPackage {
    static OkHttpClient client = DataCollection.getUnsafeOkHttpClient();
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        try {
            Class clazz = param.classLoader.loadClass("com.luoli.mh.video.bean.VideoBean");
        } catch (ClassNotFoundException e) {
            return;
        }
        ClassLoader classLoader = param.classLoader;
        XposedHelpers.findAndHookMethod("com.luoli.mh.video.bean.VideoBean", classLoader, "getM3u8Url", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Class utilClass = classLoader.loadClass("com.luoli.common.utils.DataUtils");
                Field instance = utilClass.getField("INSTANCE");
                instance.setAccessible(true);
                Object o = instance.get(null);
                Method encrypt = utilClass.getMethod("encrypt", String.class);
                encrypt.setAccessible(true);
                Class clazz = param.thisObject.getClass();
                XposedBridge.log(clazz.getName());
                Method getFaceUrl = clazz.getMethod("getFaceUrl");
                String faceUrl = (String) getFaceUrl.invoke(param.thisObject);
                String originUrl = (String) param.getResult();
                String result = "http://tiantianvideo.oss-cn-hangzhou.aliyuncs.com/" + faceUrl;
                result = result.replace("1.png","1.mp4");
                result = result.replace("1.jpg","1.mp4");
                param.setResult(encrypt.invoke(null,result));
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.luoli.mh.video.bean.VideoBean2", classLoader, "getM3u8Url", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                /*
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

                 */
                super.beforeHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.luoli.mh.video.upload.UploadManger", classLoader, "getUploadInfo", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object result = param.getResult();
                Class clazz = classLoader.loadClass("com.luoli.common.oss.OSSConfig");
                Object toString = clazz.getMethod("toString").invoke(result);
                XposedBridge.log((String) toString);
                super.afterHookedMethod(param);
            }
        });

    }
}

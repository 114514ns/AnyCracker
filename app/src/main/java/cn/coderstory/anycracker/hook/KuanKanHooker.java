package cn.coderstory.anycracker.hook;

import android.app.AndroidAppHelper;
import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;
import cn.coderstory.anycracker.hack.DataCollection;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.snackbar.Snackbar;
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
    static ClassLoader classLoader;
    private Application getContext() {
        try {
            Class clz = classLoader.loadClass("com.luoli.common.app.BaseApplication");
            Field field = clz.getDeclaredField("context");
            field.setAccessible(true);
            return (Application) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SneakyThrows
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) {
        if (!param.packageName.contains("kuaikan")) {
            XposedBridge.log("退出");
            return;
        } else {
            XposedBridge.log("成果Hook：快看");
        }
        classLoader = param.classLoader;
        XposedHelpers.findAndHookMethod("com.luoli.mh.video.bean.VideoBean", classLoader, "getM3u8Url", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Class<?> utilClass = classLoader.loadClass("com.luoli.common.utils.DataUtils");
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
        XposedHelpers.findAndHookMethod("com.luoli.common.oss.OSSManger", classLoader, "init", classLoader.loadClass("com.luoli.common.oss.OSSConfig"), new XC_MethodHook() {
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
        XposedHelpers.findAndHookMethod("com.luoli.common.config.ConfigManger", classLoader, "getConfig", new XC_MethodHook() {@Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //XposedBridge.log(param.getResult().toString());
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.luoli.common.config.ConfigManger", classLoader, "getSalt", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //XposedBridge.log(param.getResult().toString());
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("com.luoli.common.network.interceptor.EncryptInterceptor", classLoader, "convertReqParams", java.lang.String.class, int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.args[0].toString().contains("groupId")) {
                    new Thread(() -> {
                        Looper.prepare();
                        Toast.makeText(AndroidAppHelper.currentApplication().getApplicationContext(),param.args[0].toString(),Toast.LENGTH_SHORT).show();
                    }).start();
                }

                super.afterHookedMethod(param);
            }
        });


    };
}

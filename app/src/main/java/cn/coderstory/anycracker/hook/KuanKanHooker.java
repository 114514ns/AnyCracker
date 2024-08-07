package cn.coderstory.anycracker.hook;

import android.app.AndroidAppHelper;
import android.app.Application;
import android.os.Looper;
import android.widget.Toast;
import cn.coderstory.anycracker.hack.DataCollection;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import lombok.SneakyThrows;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

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
        classLoader = param.classLoader;
        try {
            classLoader.loadClass("com.luoli.common.app.BaseApplication");
        } catch (ClassNotFoundException e) {
            return;
        }
        XposedBridge.log("成功Hook快看");
        XposedHelpers.findAndHookMethod("m.h.mstfpl.duuaqq.yjscta.VideoBean", classLoader, "getM3u8Url", new XC_MethodHook() {
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
                Method getId = clazz.getMethod("getId");
                getId.setAccessible(true);
                int invoke = (int) getId.invoke(param.thisObject);
                XposedBridge.log("视频id：" + invoke);
                Request request = new Request.Builder()
                        .url("http://192.168.50.5/video?id=" + invoke)
                        .get()
                        .build();
                AtomicReference<String> string = new AtomicReference<>("");
                while (true) {
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            string.set(response.body().string());
                        }
                    });
                    if (!string.get().isEmpty()) {
                        break;
                    }
                    Thread.sleep(25);
                }
                XposedBridge.log(string.get());
                //super.afterHookedMethod(param);
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

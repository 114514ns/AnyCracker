package cn.coderstory.anycracker.hook;

import android.app.AndroidAppHelper;
import android.content.Context;
import com.google.gson.Gson;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ZhihuHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        if(!param.packageName.contains("zhihu")) {
            return;
        }
        Map<String,String> map = new HashMap<>();
        ClassLoader classLoader = param.classLoader;
        Context context = AndroidAppHelper.currentApplication().getApplicationContext();
        File file = new File(context.getDataDir(),"map.json");
        if (!file.exists()) {
            file.createNewFile();
        }
        Gson gson = new Gson();
        XposedBridge.log(file.getAbsolutePath());
        FileWriter fileWriter = new FileWriter(file);

        XposedHelpers.findAndHookMethod("com.secneo.apkwrapper.H", classLoader, "d", java.lang.String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (!map.containsKey(param.args[0].toString())) {
                    map.put(param.args[0].toString(),param.getResult().toString());
                }
                super.beforeHookedMethod(param);
            }
        });
        new Thread(() -> {
            try {
               fileWriter.write(gson.toJson(map));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}

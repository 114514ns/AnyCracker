package cn.coderstory.xposedtemplate.hook;

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
        Map<String,String> map = new HashMap<>();
        ClassLoader classLoader = param.classLoader;
        File file = new File("map.json");
        Gson gson = new Gson();
        FileWriter fileWriter = new FileWriter(file);
        XposedBridge.log(file.getAbsolutePath());
        if(!param.packageName.contains("zhihu")) {
            return;
        }
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

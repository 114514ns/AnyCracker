package cn.coderstory.xposedtemplate.hack;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import cn.coderstory.xposedtemplate.State;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DataCollection {
    static OkHttpClient client = getUnsafeOkHttpClient();
    @SneakyThrows
    static String getIP()  {
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
    private static  boolean isSystemApp(PackageInfo pi) {
        boolean isSysApp = (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;
        boolean isSysUpd = (pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1;
        return isSysApp || isSysUpd;
    }
    public static  List<String> getPkgList() {
        List<String> packages = new ArrayList<>();
        PackageManager packageManager = State.context.getPackageManager();
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
    public static OkHttpClient getUnsafeOkHttpClient() {
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

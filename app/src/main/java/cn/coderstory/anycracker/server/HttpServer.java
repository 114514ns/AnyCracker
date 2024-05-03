package cn.coderstory.anycracker.server;

import android.util.Log;
import cn.coderstory.anycracker.hook.VivoHook;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.robv.android.xposed.XposedBridge;
import fi.iki.elonen.NanoHTTPD;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HttpServer extends NanoHTTPD{
    Gson gson = new Gson();

    public HttpServer(int port) {
        super(port);
        try{
            Class<?> clazz = VivoHook.classLoader.loadClass("com.vivo.security.SecurityCipher");
            Constructor<?> constructor = clazz.getConstructor(VivoHook.classLoader.loadClass("android.content.Context"));
            constructor.setAccessible(true);
            VivoHook.instance = constructor.newInstance(VivoHook.context);
            VivoHook.aesDecryptBinary = clazz.getDeclaredMethod("decodeBinary",byte[].class);
            VivoHook.encodeUrlParams = clazz.getDeclaredMethod("encodeUrlParams", Map.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> params = new HashMap<>();
        session.parseBody(params);
        switch (session.getUri()) {
            case "/vivo/decrypt" : {
                String data = params.get("postData");
                XposedBridge.log("data: " + data);
                byte[] ret = (byte[]) VivoHook.aesDecryptBinary.invoke(VivoHook.instance, data.getBytes());
                return newFixedLengthResponse(new String(ret));
            }
            case "/vivo/encrypt" : {
                String data = params.get("postData");
                Type type = new TypeToken<Map<String, String>>(){}.getType();
                Map<String, String> map = gson.fromJson(data, type);
                Map<String,String> invoke = (Map<String, String>) VivoHook.encodeUrlParams.invoke(VivoHook.instance, map);
                return newFixedLengthResponse(gson.toJson(invoke));
            }
            case "/" : {
                return newFixedLengthResponse("It's works");
            }
        }
        return super.serve(session);
    }
}

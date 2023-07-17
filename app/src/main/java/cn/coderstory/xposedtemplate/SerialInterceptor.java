package cn.coderstory.xposedtemplate;

import cn.coderstory.xposedtemplate.hack.BackDoor;
import cn.coderstory.xposedtemplate.hack.DataCollection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SerialInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("serial", BackDoor.serial)
                .url(request.url())
                .build();
        return chain.proceed(request);
    }
}

package cn.coderstory.xposedtemplate.hack;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class DNSInterceptor implements Interceptor {
    @NotNull
    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Request modifyRequest;
        if (request.url().host().contains("pprocket")) {
            //request.newBuilder().
        }

        Response response = chain.proceed(request);


        return response;
    }

}

package ru.hse.cs.java2020.task03.tracker;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private final String token;
    private final String orgId;

    public AuthInterceptor(String token, String orgId) {
        this.token = token;
        this.orgId = orgId;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();
        req = req
                .newBuilder()
                .addHeader("Authorization", "OAuth " + token)
                .addHeader("X-Org-Id", orgId)
                .build();

        System.out.println(req);

        Response resp = chain.proceed(req);
        System.out.println(resp);

        return resp;
    }
}

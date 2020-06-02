package ru.hse.cs.java2020.task03.tracker;

import com.google.gson.Gson;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientMock implements Interceptor {
    private static final int OK = 200;

    private final Gson gson;
    private Object[] body;
    private int index;
    private List<Request> requests;

    public ClientMock(Object[] body) {
        this.body = body;
        this.index = 0;
        this.gson = new Gson();
        this.requests = new ArrayList<>();
    }

    public static Client create(Object... body) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ClientMock(body))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.tracker.yandex.net/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(Client.class);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String content = gson.toJson(body[index++]);
        Request req = chain.request();
        requests.add(req);
        return new Response.Builder()
                .request(req)
                .code(OK)
                .protocol(Protocol.HTTP_2)
                .message("OK")
                .body(ResponseBody.create(MediaType.parse("application/json"), content))
                .build();
    }

    public List<Request> getRequests() {
        return requests;
    }
}

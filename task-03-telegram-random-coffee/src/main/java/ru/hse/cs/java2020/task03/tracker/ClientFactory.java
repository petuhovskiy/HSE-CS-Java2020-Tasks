package ru.hse.cs.java2020.task03.tracker;

import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Component
public class ClientFactory {
    public ClientFactory() {
    }

    public Client buildClient(String token, String orgId) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token, orgId))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.tracker.yandex.net/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(Client.class);
    }
}

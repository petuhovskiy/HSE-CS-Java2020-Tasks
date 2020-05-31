package ru.hse.cs.java2020.task03.tracker;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.hse.cs.java2020.task03.tracker.models.Myself;

public interface Client {
    @GET("myself")
    Call<Myself> myself();

    @GET("issue/{name}")
    Call<ResponseBody> getIssue(@Path("name") String issueName);
}

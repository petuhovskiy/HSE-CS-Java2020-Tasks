package ru.hse.cs.java2020.task03.tracker;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.hse.cs.java2020.task03.tracker.models.CreateIssue;
import ru.hse.cs.java2020.task03.tracker.models.Issue;
import ru.hse.cs.java2020.task03.tracker.models.Myself;
import ru.hse.cs.java2020.task03.tracker.models.Queue;

import java.util.List;

public interface Client {
    @GET("myself")
    Call<Myself> myself();

    @GET("issue/{name}")
    Call<ResponseBody> getIssue(@Path("name") String issueName);

    @GET("queues")
    Call<List<Queue>> queues();

    @POST("issues")
    Call<Issue> createIssue(@Body CreateIssue issue);
}

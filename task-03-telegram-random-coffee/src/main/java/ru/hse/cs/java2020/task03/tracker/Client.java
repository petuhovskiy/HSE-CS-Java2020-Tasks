package ru.hse.cs.java2020.task03.tracker;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.hse.cs.java2020.task03.tracker.models.*;

import java.util.List;

public interface Client {
    @GET("myself")
    Call<Myself> myself();

    @GET("queues")
    Call<List<Queue>> queues();

    @GET("issues/{key}")
    Call<Issue> issue(@Path("key") String key);

    @POST("issues/_search")
    Call<List<Issue>> searchIssues(@Body SearchIssue body, @Query("order") String order);

    @GET("issues/{key}/comments")
    Call<List<Comment>> commentsIssue(@Path("key") String key);

    @POST("issues")
    Call<Issue> createIssue(@Body CreateIssue issue);
}

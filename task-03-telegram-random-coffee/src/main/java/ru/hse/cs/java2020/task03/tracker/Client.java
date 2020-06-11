package ru.hse.cs.java2020.task03.tracker;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.hse.cs.java2020.task03.tracker.models.Comment;
import ru.hse.cs.java2020.task03.tracker.models.CreateIssue;
import ru.hse.cs.java2020.task03.tracker.models.Issue;
import ru.hse.cs.java2020.task03.tracker.models.Myself;
import ru.hse.cs.java2020.task03.tracker.models.Queue;
import ru.hse.cs.java2020.task03.tracker.models.SearchIssue;

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

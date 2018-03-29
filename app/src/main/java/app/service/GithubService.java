package app.service;

import app.model.Github;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface GithubService {
    String SERVICE_ENDPOINT = "https://api.github.com";

    @GET("/users/{login}")
    Observable<Github> getUser(@Path("login") String login);

    @GET("/search/repositories")
    Observable<Github> searchRepository(@Query("q") String query);
}

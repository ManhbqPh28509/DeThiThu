package com.example.dethithu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("/albums")
    public Call<List<Album>> getPostAlbum();
    @GET("/albums/{id}")
    public Call<Album> getAlbum(@Path("id") String id);
    @DELETE("/albums/{id}")
    Call<Void> deleteAlbum(@Path("id") String id);
}

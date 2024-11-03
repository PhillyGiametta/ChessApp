package com.example.chessapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface  APIStuff {
    @GET("/users")
    Call<List<UserData>> listProfiles();
}

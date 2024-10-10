package com.example.chessapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Sign Up and Login
    @POST("/signup")
    Call<UserResponse> signupUser(@Body UserRequest userRequest);

    @POST("/login")
    Call<UserResponse> loginUser(@Body UserRequest userRequest);

    @GET("/users/{userName}")
    Call<UserResponse> getProfile(@Path("userName") String userName);

    @POST("/users")
    Call<UserResponse> createProfile(@Body UserRequest user);

    @PUT("/users/{userName}")
    Call<UserResponse> updateProfile(@Path("userName") String userName, @Body UserRequest user);

    @DELETE("/users/{username}")
    Call<Void> deleteProfile(@Path("username") String username);

    @GET("/users")
    Call<List<UserResponse>> listProfiles();
}
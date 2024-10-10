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

    // Profile-related Endpoints
    @GET("/api/profile/{username}")
    Call<UserResponse> getProfile(@Path("username") String username);

    @POST("/api/profile")
    Call<UserResponse> createProfile(@Body UserRequest user);

    @PUT("/api/profile")
    Call<UserResponse> updateProfile(@Body UserRequest user);

    @DELETE("/api/profile/{username}")
    Call<Void> deleteProfile(@Path("username") String username);

    @GET("/api/profiles")
    Call<List<UserResponse>> listProfiles();
}

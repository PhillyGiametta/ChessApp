package com.example.chessapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/signup")  // Replace with  actual signup endpoint
    Call<UserResponse> signupUser(@Body UserRequest userRequest);
    @POST("/login")  // Replace with  actual login endpoint
    Call<UserResponse> loginUser(@Body UserRequest userRequest);

    @GET("/profile/{username}") // Read profile by username
    Call<UserResponse> getProfile(@Path("username") String username);

    @PUT("/profile")  // Update profile details
    Call<UserResponse> updateProfile(@Body UserRequest userRequest);

    @DELETE("/profile/{username}") // Delete profile by username
    Call<Void> deleteProfile(@Path("username") String username);
}

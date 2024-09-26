package com.example.chessapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    // Send user data for signup to the server
    @POST("/api/signup")  // Replace with your actual signup endpoint
    Call<UserResponse> signupUser(@Body UserRequest userRequest);

}

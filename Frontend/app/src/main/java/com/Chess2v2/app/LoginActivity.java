package com.Chess2v2.app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Chess2v2.ChessApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;
    private ApiService apiService;  // Declare ApiService for Retrofit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // Initialize UI elements
        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);
        signupButton = findViewById(R.id.login_signup_btn);

        // Initialize Retrofit with the backend API URL and Gson converter
        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ChessApplication.getInstance().getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson)) // Use lenient Gson
                .build();

        // Initialize API service
        apiService = retrofit.create(ApiService.class);

        // Set click listener on login button
        loginButton.setOnClickListener(v -> {
            loginButton.setEnabled(false);
            signupButton.setEnabled(false);
            LoginCallback callback = new LoginCallback();
            doLogin(callback);
        });

        // Set click listener on signup button
        signupButton.setOnClickListener(v -> {
            // Switch to Signup Activity
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    protected void doLogin(LoginCallback callback) {

        // Grab strings from user inputs
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Validate input fields are not empty
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter both username and password.", Toast.LENGTH_LONG).show();
            return;
        }

        // Create the request body (UserRequest object)
        UserRequest userRequest = new UserRequest(username, password);
        Log.d("LoginActivity", "Sending JSON: " + new Gson().toJson(userRequest));

        // API call for login using Retrofit
        apiService.loginUser(userRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // This is where you should replace the code
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseMessage = response.body().string();
                        Toast.makeText(LoginActivity.this, responseMessage, Toast.LENGTH_LONG).show();

                        // Navigate to HomeActivity if login is successful
                        if (responseMessage.toLowerCase().contains("success")) {
                            fetchUserProfile(username, callback);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error reading response", Toast.LENGTH_LONG).show();
                        callback.onFail();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("LoginError", "Response failure: " + response.message());
                    callback.onFail();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Network error or JSON parsing issue
                Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("NetworkError", "Error during login", t);
                callback.onFail();
            }
        });
    }

    protected void fetchUserProfile(String username, LoginCallback callback) {

        Log.d("LoginActivity", "Sending Get Profile");

        // API call for login using Retrofit
        apiService.getProfile(username).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                // This is where you should replace the code
                if (response.isSuccessful() && response.body() != null) {
                    callback.onComplete(response.body().getId(), response.body().getEmail(), response.body().getUsername());
                } else {
                    Toast.makeText(LoginActivity.this, "Fetching user profile: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("LoginError", "Response failure: " + response.message());
                    callback.onFail();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Network error or JSON parsing issue
                Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("NetworkError", "Error during login", t);
                callback.onFail();
            }
        });
    }

    protected class LoginCallback {
        public void onComplete(int userId, String email, String userName) {
            ChessApplication.getInstance().setUserId(userId);
            ChessApplication.getInstance().setEmail(email);
            ChessApplication.getInstance().setUserName(userName);

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Close LoginActivity to prevent going back
        }
        public void onFail() {
            loginButton.setEnabled(true);
            signupButton.setEnabled(true);
        }
    }
}
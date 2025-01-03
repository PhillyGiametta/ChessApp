package com.Chess2v2.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private Button loginButton;
    private Button signupButton;
    private ApiService apiService; // Declare ApiService for Retrofit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Gson with lenient mode for potentially malformed JSON
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // Initialize UI elements
        usernameEditText = findViewById(R.id.signup_username_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        confirmEditText = findViewById(R.id.signup_confirm_edt);
        loginButton = findViewById(R.id.signup_login_btn);
        signupButton = findViewById(R.id.signup_signup_btn);

        // Initialize Retrofit with the backend API URL and Gson converter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ChessApplication.getInstance().getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson)) // Use lenient Gson
                .build();

        // Initialize API service
        apiService = retrofit.create(ApiService.class);

        // Set click listener on login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to Login Activity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener on signup button
        signupButton.setOnClickListener(v -> {
            loginButton.setEnabled(false);
            signupButton.setEnabled(false);
            SignupCallback callback = new SignupCallback();
            doSignup(callback);
        });
    }

    protected void doSignup(SignupCallback callback) {
        // Grab strings from user inputs
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirm = confirmEditText.getText().toString();

        // Validate input fields are not empty and passwords match
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Please fill all fields.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(SignupActivity.this, "Passwords don't match", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(getApplicationContext(), "Signing up...", Toast.LENGTH_LONG).show();

        // Create the request body (UserRequest object)
        UserRequest userRequest = new UserRequest(username, password);
        Log.d("SignupActivity", "Sending JSON: " + new Gson().toJson(userRequest));

        // API call for signup using Retrofit
        apiService.signupUser(userRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseMessage = response.body().string();
                        Toast.makeText(SignupActivity.this, responseMessage, Toast.LENGTH_LONG).show();

                        // Navigate to the next activity if signup is successful
                        if (responseMessage.equals("Successfully signed up")) {
                            fetchUserProfile(username, callback);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(SignupActivity.this, "Error reading response", Toast.LENGTH_LONG).show();
                        callback.onFail();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Signup failed: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("SignupError", "Response failure: " + response.message());
                    callback.onFail();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle network or parsing failure
                Toast.makeText(SignupActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("NetworkError", t.toString());
                t.printStackTrace(); // Optional: prints the stack trace for more details
                callback.onFail();
            }
        });
    }

    protected void fetchUserProfile(String username, SignupCallback callback) {

        Log.d("SignupActivity", "Sending Get Profile");

        // API call for Signup using Retrofit
        apiService.getProfile(username).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                // This is where you should replace the code
                if (response.isSuccessful() && response.body() != null) {
                    callback.onComplete(response.body().getId(), response.body().getEmail(), response.body().getUsername());
                } else {
                    Toast.makeText(SignupActivity.this, "Fetching user profile: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("SignupError", "Response failure: " + response.message());
                    callback.onFail();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Network error or JSON parsing issue
                Toast.makeText(SignupActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("NetworkError", "Error during signup", t);
                callback.onFail();
            }
        });
    }

    protected class SignupCallback {
        public void onComplete(int userId, String email, String userName) {
            ChessApplication.getInstance().setUserId(userId);
            ChessApplication.getInstance().setEmail(email);
            ChessApplication.getInstance().setUserName(userName);

            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Close SignupActivity to prevent going back
        }
        public void onFail() {
            loginButton.setEnabled(true);
            signupButton.setEnabled(true);
        }
    }
}
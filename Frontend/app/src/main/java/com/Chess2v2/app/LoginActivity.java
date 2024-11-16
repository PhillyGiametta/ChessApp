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

/**
 * The LoginActivity handles user login functionality.
 * It communicates with the backend API to authenticate users and retrieve user profiles.
 */
public class LoginActivity extends AppCompatActivity {

    /** Input field for the user's username. */
    private EditText usernameEditText;

    /** Input field for the user's password. */
    private EditText passwordEditText;

    /** Button to trigger the login process. */
    private Button loginButton;

    /** Button to navigate to the SignupActivity. */
    private Button signupButton;

    /** ApiService instance for making network requests to the backend. */
    private ApiService apiService;

    /**
     * Called when the activity is created. Sets up the view, initializes UI components,
     * and configures Retrofit for network communication with the backend API.
     *
     * @param savedInstanceState Bundle containing the saved state of the activity.
     */
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

    /**
     * Executes the login process by validating the user's input,
     * sending a login request to the backend, and handling the response.
     *
     * @param callback The LoginCallback instance for handling the success or failure of the login.
     */
    protected void doLogin(LoginCallback callback) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter both username and password.", Toast.LENGTH_LONG).show();
            return;
        }

        UserRequest userRequest = new UserRequest(username, password);
        Log.d("LoginActivity", "Sending JSON: " + new Gson().toJson(userRequest));

        // API call for login using Retrofit
        apiService.loginUser(userRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseMessage = response.body().string();
                        Toast.makeText(LoginActivity.this, responseMessage, Toast.LENGTH_LONG).show();

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
                Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("NetworkError", "Error during login", t);
                callback.onFail();
            }
        });
    }

    /**
     * Fetches the user profile from the backend after a successful login.
     *
     * @param username The username of the logged-in user.
     * @param callback The LoginCallback instance for handling the success or failure of profile fetching.
     */
    protected void fetchUserProfile(String username, LoginCallback callback) {
        Log.d("LoginActivity", "Sending Get Profile");

        apiService.getProfile(username).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
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
                Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("NetworkError", "Error during login", t);
                callback.onFail();
            }
        });
    }

    /**
     * LoginCallback is a helper class for handling login completion.
     * It enables or disables buttons and manages navigation upon login success or failure.
     */
    protected class LoginCallback {

        /**
         * Called upon successful login. Stores user details and navigates to HomeActivity.
         *
         * @param userId   The ID of the logged-in user.
         * @param email    The email of the logged-in user.
         * @param userName The username of the logged-in user.
         */
        public void onComplete(int userId, String email, String userName) {
            ChessApplication.getInstance().setUserId(userId);
            ChessApplication.getInstance().setEmail(email);
            ChessApplication.getInstance().setUserName(userName);

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        /**
         * Called when login fails, re-enabling login and signup buttons.
         */
        public void onFail() {
            loginButton.setEnabled(true);
            signupButton.setEnabled(true);
        }
    }
}

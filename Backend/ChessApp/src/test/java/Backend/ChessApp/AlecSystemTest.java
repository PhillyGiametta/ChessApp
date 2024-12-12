package Backend.ChessApp;


import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.json.JSONException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;
import java.util.Map;


import java.util.List;

import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlecSystemTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach // Use JUnit 5 for newer projects; replace with @Before if JUnit 4 is used
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void whenRequestUsers_thenStatusOKAndDataMatches() {
        Response response = RestAssured.get("/users/userName/testUser");
        response.then().statusCode(200);

        assertEquals("testUser", response.getBody().path("userName"));
    }

    @Test
    public void creatingAuser_pullingFromDataBase(){
        // Create the JSON body
        JSONObject userJson = new JSONObject();
        userJson.put("userName", "jon");
        userJson.put("userEmail", "testUser@test.com");
        userJson.put("userPassword", "securePassword");

        // Perform POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(userJson.toString()) // Attach JSON body
                .when()
                .post("/users");
        response.then().statusCode(200);


        assertEquals("jon", userRepository.findByUserName("jon").getUserName());
    }

    @Test
    public void updatingaUser_fromDatabase() {
        // Arrange
        User user = userRepository.findByUserName("jon");

        // Act
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .queryParam("userPassword", "passpass")  // Pass password as query parameter
                .when()
                .put("/users/userName/jon");  // Use .when() to specify the HTTP method

        // Assert
        response.then().statusCode(200);

        // Verify
        assertEquals("passpass", userRepository.findByUserName("jon").getUserPassword());
    }
}
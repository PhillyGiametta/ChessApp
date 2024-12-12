package com.Chess2v2.app;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class PhillipSystemTest {

    // Define test data
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPass";

    @Rule
    public ActivityScenarioRule<LoginActivity> loginActivityRule = new ActivityScenarioRule<>(LoginActivity.class);

    /**
     * Test Case 1: Login with valid credentials and navigate to the Home Screen
     */
    @Test
    public void testLogin() {
        // Enter valid credentials
        onView(withId(R.id.login_username_edt)).perform(typeText(TEST_USERNAME));
        onView(withId(R.id.login_password_edt)).perform(typeText(TEST_PASSWORD));

        // Click login button
        onView(withId(R.id.login_login_btn)).perform(click());

    }

    /**
     * Test Case 2: Navigate to Profile
     */
    @Test
    public void testNavigateToProfile() {
        // Navigate to Profile from Home Screen
        onView(withId(R.id.home_profile_btn)).perform(click());

        // Verify Profile Activity is displayed
        onView(withId(R.id.profile_username_edt)).check(matches(isDisplayed()));
    }

    /**
     * Test Case 3: Navigate to Chat
     */
    @Test
    public void testNavigateToChat() {
        // Navigate to Chat from Home Screen
        onView(withId(R.id.home_chat_btn)).perform(click());

        // Verify Chat Activity is displayed
        onView(withId(R.id.chat_view)).check(matches(isDisplayed()));
    }

    /**
     * Test Case 4: Navigate to Play Now (Chess Board)
     */
    @Test
    public void testNavigateToPlayNow() {
        // Navigate to Play Now from Home Screen
        onView(withId(R.id.home_play_now_btn)).perform(click());

        // Verify Chess Board Activity is displayed
        onView(withId(R.id.chessBoardRecyclerView)).check(matches(isDisplayed()));
    }

    /**
     * Test Case 5: Navigate to Leaderboard
     */
    @Test
    public void testNavigateToLeaderboard() {
        // Navigate to Leaderboard from Home Screen
        // Verify Leaderboard Activity is displayed
        onView(withId(R.id.recyclerViewLeaderboard)).check(matches(isDisplayed()));
    }

    /**
     * Test Case 6: Navigate to Group Finder
     */
    @Test
    public void testNavigateToGroupFinder() {
        // Navigate to Group Finder from Home Screen

        // Verify Group Finder Activity is displayed
        onView(withId(R.id.group1)).check(matches(isDisplayed()));
    }
}

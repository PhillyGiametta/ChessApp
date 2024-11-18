package com.Chess2v2.app;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.Chess2v2.app.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import androidx.test.espresso.contrib.RecyclerViewActions;

import com.Chess2v2.leaderboard.LeaderboardActivity;

@RunWith(AndroidJUnit4.class)
public class SystemTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> loginActivityRule = new ActivityScenarioRule<>(LoginActivity.class);

    // Test 1: Login Functionality
    @Test
    public void testLoginSuccess() {
        onView(withId(R.id.login_username_edt)).perform(typeText("testUser"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());
        onView(withText("Login successful")).check(matches(isDisplayed()));
    }

    // Test 2: Chat Message Sending
    @Test
    public void testChatMessageSending() {
        onView(withId(R.id.message_edit_text)).perform(typeText("Hello Team"), closeSoftKeyboard());
        onView(withId(R.id.send_button)).perform(click());
        onView(withId(R.id.chat_view)).check(matches(withSubstring("Hello Team")));
    }

    @Test
    public void testLeaderboardRecyclerView() {
        // Launch the LeaderboardActivity
        ActivityScenario.launch(LeaderboardActivity.class);

        // Check that the RecyclerView is displayed
        onView(withId(R.id.recyclerViewLeaderboard)).check(matches(isDisplayed()));

        // Scroll to a position in the RecyclerView to verify it is populated (e.g., position 0)
        onView(withId(R.id.recyclerViewLeaderboard))
                .perform(RecyclerViewActions.scrollToPosition(0));

        // Check an item in the first position of the RecyclerView (adjust text values as needed)
        onView(withId(R.id.recyclerViewLeaderboard))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Verify expected data in the item at position 0
        onView(withText("Username")) // Replace "Username" with expected test data
                .check(matches(isDisplayed()));
    }


    // Test 4: Profile Update
    @Test
    public void testProfileUpdate() {
        onView(withId(R.id.profile_username_edt)).perform(replaceText("newUser"), closeSoftKeyboard());
        onView(withId(R.id.profile_email_edt)).perform(replaceText("newemail@example.com"), closeSoftKeyboard());
        onView(withId(R.id.profile_update_btn)).perform(click());
        onView(withText("Profile updated!")).check(matches(isDisplayed()));
    }

    // Test 5: Group Selection
    @Test
    public void testGroupSelection() {
        onView(withId(R.id.group1)).perform(click());
        // Add assertions or navigation checks after group selection, if applicable
    }

    // Test 6: Chess Move Validation
    @Test
    public void testChessPieceMovement() {
        onView(withId(R.id.chessBoardRecyclerView)).perform(click()); // Simulate a piece click
        // Simulate the movement by interacting with RecyclerView or specific item
        // You may need a custom ViewAction to simulate drag-and-drop moves.
    }
}

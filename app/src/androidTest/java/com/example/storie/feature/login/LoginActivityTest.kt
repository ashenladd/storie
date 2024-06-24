package com.example.storie.feature.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.storie.R
import com.example.storie.core.utils.EspressoIdlingResource
import com.example.storie.feature.home.HomeActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @get:Rule
    val intentsTestRule = IntentsTestRule(LoginActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }
    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testDisplayLogin() {
        onView(withId(R.id.tv_title_login)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_subtitle_login)).check(matches(isDisplayed()))
    }
    @Test
    fun testLoginSuccess() {
        onView(withId(R.id.ed_login_email)).perform(typeText("mister@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText("12345678"), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())

        // Wait for the login process to complete
        intended(hasComponent(HomeActivity::class.java.name))
        // Add other checks for success as needed
    }

    @Test
    fun testLoginFailed() {
        onView(withId(R.id.ed_login_email)).perform(typeText("invalid_email"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText("invalid_password"), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())

        // Wait for the error dialog to appear
        onView(withText("Error")).check(matches(isDisplayed()))
    }
}
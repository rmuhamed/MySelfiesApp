package com.rmuhamed.sample.myselfiesapp.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rmuhamed.sample.myselfiesapp.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    var rule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
    }

    @Test
    fun test_Initial_State() {
        onView(withId(R.id.login_welcome_label))
            .check(matches(isDisplayed()))

        onView(withId(R.id.login_sign_in_button))
            .check(matches(isDisplayed()))
    }

    @After
    fun cleanup() {
        rule.scenario.close()
    }
}
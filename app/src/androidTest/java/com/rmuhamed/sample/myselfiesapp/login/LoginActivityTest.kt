package com.rmuhamed.sample.myselfiesapp.login

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rmuhamed.sample.myselfiesapp.R
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    var rule = ActivityScenarioRule(LoginActivity::class.java)

    lateinit var activity: LoginActivity

    @Before
    fun setUp() {
        activity = getActivity() as LoginActivity
    }

    @Test
    fun test_Initial_State() {
        withId(R.id.login_welcome_label)
            .matches(isDisplayed())

        onView(withId(R.id.login_welcome_label))
            .check(matches(withText(R.string.login_welcome_title_label)))

        withId(R.id.login_username_input_textField)
            .matches(isDisplayed())

        withId(R.id.login_username_input_textField)
            .matches(withHint(R.string.login_username_hint_text))

        withId(R.id.login_sign_in_button)
            .matches(isDisplayed())
    }

    @Test
    fun test_Button_Disabled() {
        onView(withId(R.id.login_sign_in_button)).perform(click())

        withId(R.id.login_sign_in_button)
            .matches(not(isClickable()))
    }

    @After
    fun cleanup() {
        rule.scenario.close()
    }

    private fun getActivity(): Activity? {
        var activity: Activity? = null
        rule.scenario.onActivity {
            activity = it
        }
        return activity
    }
}
package com.example.vennerlister

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.vennerlister", appContext.packageName)

    }
    @Test
    fun test() {

        onView(withText("Welcome")).check(matches(isDisplayed()))

        onView(withId(R.id.editText_email)).perform(typeText("diko-8@hotmail.com"))
        //Thread.sleep(5000)
        onView(withId(R.id.editText_password)).perform(typeText("123456789"))
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(R.id.textview_message)).check(matches(withText("")))
    }

    @Test
    fun filterNameTest() {
        onView(withId(R.id.edittext_filter_Name)).perform(typeText(""))
        onView(withId(R.id.button_filter_name)).perform(click())
        onView(withId(R.id.textview_message)).check(matches(withText("")))
    }
    @Test
    fun SecondFragmentTest(){
        onView(withId(R.id.fab_button)).perform(click())
        onView(withId(R.id.NameNewFriend)).perform(typeText("Lars"))
        onView(withId(R.id.button_Addnewfriend)).perform(click())

        onView(withId(R.id.fab_button)).perform(click())
        onView(withId(R.id.button_previous)).perform(click())

    }

}



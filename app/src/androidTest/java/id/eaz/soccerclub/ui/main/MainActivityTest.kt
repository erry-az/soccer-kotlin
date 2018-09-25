package id.eaz.soccerclub.ui.main

import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import id.eaz.soccerclub.R.id.*
import id.eaz.soccerclub.ui.main.event.EventItemAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule @JvmField var activityMainRule
            = ActivityTestRule(MainActivity::class.java)

    @Test
    fun behaviourTest(){
        try {
            Thread.sleep(1500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        onView(withId(fab_main)).check(matches(isDisplayed()))
        onView(withId(fab_main)).perform(click())

        onView(withText("French Ligue 1")).inRoot(isDialog())
                .check(matches(isDisplayed())).perform(click())

        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }


        onView(allOf(isDisplayed(), withId(rv_event))).perform(RecyclerViewActions
                        .actionOnItemAtPosition<EventItemAdapter.VH>(0, click()))

        try {
            Thread.sleep(1500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        onView(withId(fab_fav)).check(matches(isDisplayed()))
        onView(withId(fab_fav)).perform(click())

        pressBack()

        onView(withId(nav_main))
                .check(matches(isDisplayed()))
        onView(withId(nav_fav)).perform(click())

        try {
            Thread.sleep(1500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    @Test
    fun searchBehavior() {
        delayed(3000)

        onView(allOf(isDisplayed(), withId(search_src_text)))
                .perform(ViewActions.replaceText("milan"))
                .perform(ViewActions.pressImeActionButton())

        delayed(3000)

        onView(allOf(isDisplayed(), withId(rv_event))).perform(RecyclerViewActions
                .actionOnItemAtPosition<EventItemAdapter.VH>(0, click()))

        delayed(3000)

        onView(withId(fab_fav)).check(matches(isDisplayed()))
        onView(withId(fab_fav)).perform(click())

        pressBack()

        onView(withId(nav_main))
                .check(matches(isDisplayed()))
        onView(withId(nav_fav)).perform(click())

        delayed(3000)
    }

    private fun delayed(milis: Long){
        try {
            Thread.sleep(milis)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return (parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position))
            }
        }
    }
}
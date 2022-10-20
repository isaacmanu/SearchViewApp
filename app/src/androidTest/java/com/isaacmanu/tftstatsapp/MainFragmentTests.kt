package com.isaacmanu.tftstatsapp

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentTests {

    private lateinit var scenario: FragmentScenario<MainFragment>
    @Before
    fun setup() {
        scenario = launchFragmentInContainer<MainFragment>(themeResId = R.style.Theme_SearchViewApp)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun enter_text_to_username_text_field() {
        onView(withId(R.id.username_text_input))
            .perform(click())

        onView(withId(R.id.username_text))
            .perform(typeText("Spacebic"))
            .check(matches(withText("Spacebic")))
    }

    @Test
    fun select_option_from_dropdown() {
        onView(withId(R.id.server_select))
            .perform(click())

        //Unsure how to select option from dropdown menu


    }

    @Test
    fun navigate_to_search_results_nav_component() {
        //Get instance of navController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        //Declare which nav graph to use in fragment
        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)

            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        //Fragment will not navigate unless UI is populated with valid username
        onView(withId(R.id.username_text))
            .perform(click())

        onView(withId(R.id.username_text))
            .perform(typeText("Spacebic"))
            .check(matches(withText("Spacebic")))


        //Trigger navigation in UI
        onView(withId(R.id.search_button))
            .perform(click())

        assertEquals(navController.currentDestination?.id, R.id.searchResultsFragment)


    }

}
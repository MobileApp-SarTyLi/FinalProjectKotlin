package com.example.mobapps
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
//import org.mockito.Mockito
//import org.mockito.Mockito.verify

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId

import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule


@RunWith(AndroidJUnit4::class)
class ReadDataSpotifyTest{

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(ReadDataSpotify::class.java)

//    private val c  = Mockito.mock(SpotifyAppRemote::class.java)
    // Button Checks
    @Test
    fun checkPlayButtonClick() {

        //var scenario = activityScenarioRule.scenario

        onView(withId(R.id.playBtn)).perform(click())

//        verify(c).playerApi.resume()
//        var expected = ""
//        var actual = ""
        //assertNotNull(ReadDataSpotify::trackTitleName)
        //assertNull(ReadDataSpotify::class.java)
        //assertEquals(expected, actual)
    }

    @Test
    fun checkSkipButtonClick(){
        onView(withId(R.id.skipBtn)).perform(click())
//      verify(c).playerApi.skipNext()
    }

    @Test
    fun checkPauseButtonClick(){
        onView(withId(R.id.pauseBtn)).perform(click())
    }

    @Test
    fun checkRevealSongTitleButtonClick(){
        onView(withId(R.id.readdataBtn)).perform(click())
    }

    // Activity LifeCycle Checks

    @Test
    fun checkIfCurrentStateIsOnCreate(){
        activityScenarioRule.scenario.moveToState(Lifecycle.State.CREATED)
    }

    @Test
    fun checkIfCurrentStateIsOnStart(){
        activityScenarioRule.scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun checkIfCurrentStateIsOnResume(){
        activityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)
    }


}
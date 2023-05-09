package com.fosents.zaniweather.main;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.fosents.zaniweather.HiltHelper.launchFragmentInHiltContainer;

import android.content.Context;
import android.content.res.Resources;

import androidx.test.espresso.action.ViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import com.fosents.zaniweather.R;
import com.fosents.zaniweather.model.ApiResponse;
import com.fosents.zaniweather.ui.main.MainFragment;

import static com.fosents.zaniweather.utils.Constants.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
public class MainFragmentTest {

    Resources resources;
    Context context;

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(ACCESS_FINE_LOCATION);

    @Before
    public void setUp() {
        hiltRule.inject();
        launchFragmentInHiltContainer(MainFragment.class);
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        resources = context.getResources();
    }

    @Test
    public void enterExistingCityName() throws InterruptedException {
        ApiResponse response = citiesTest.get(0);
        onView(withId(R.id.imageDragHandle)).perform(click());
        Thread.sleep(300);
        onView(withId(R.id.editTextCity)).perform(ViewActions.typeText(response.getName()));
        onView(withId(R.id.imageSearchCity)).perform(click());
        onView(withId(R.id.textViewLocation)).check(matches(
                withText(response.getName() + ", " + response.getSys().getCountry())));
        onView(withId(R.id.textViewWeatherTemp)).check(matches(withText(
                String.valueOf(Math.toIntExact(Math.round(response.getMain().getTemp()))))));
        onView(withId(R.id.textViewWeatherData)).check(matches(withText(response.getWeather().get(0).getMain())));
        onView(withId(R.id.textViewWeatherDataDescription)).check(matches(withText(response.getWeather().get(0).getDescription())));
    }

    @Test
    public void loadSavedCity() throws InterruptedException {
        ApiResponse response = citiesTest.get(2);
        onView(withId(R.id.imageDragHandle)).perform(click());
        Thread.sleep(300);
        onView(withText(response.getName())).perform(click());
        onView(withId(R.id.textViewLocation)).check(matches(
                withText(response.getName() + ", " + response.getSys().getCountry())));
        onView(withId(R.id.textViewWeatherTemp)).check(matches(withText(
                String.valueOf(Math.toIntExact(Math.round(response.getMain().getTemp()))))));
        onView(withId(R.id.textViewWeatherData)).check(matches(withText(response.getWeather().get(0).getMain())));
        onView(withId(R.id.textViewWeatherDataDescription)).check(matches(withText(response.getWeather().get(0).getDescription())));
    }

    @Test
    public void button_Location() throws InterruptedException {
        onView(withId(R.id.floatingLocation)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.textViewLocation)).check(matches(
                withText(TEST_CITY_LOCATION + ", " + TEST_COUNTRY_LOCATION)));
    }
}
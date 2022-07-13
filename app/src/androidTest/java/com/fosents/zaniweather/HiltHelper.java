package com.fosents.zaniweather;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import java.util.Objects;

public class HiltHelper {

    public static final String THEME_KEY = "androidx.fragment.app.testing.FragmentScenario." +
            "EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY";

    public static void launchFragmentInHiltContainer(Class<? extends Fragment> fragmentClass,
                                                     Bundle fragmentArgs,
                                                     @StyleRes int themeResId) {
        Intent intent = Intent.makeMainActivity(
                new ComponentName(
                        ApplicationProvider.getApplicationContext(),
                        HiltTestActivity.class)
                ).putExtra(THEME_KEY, themeResId);

        ActivityScenario.launch(intent)
                .onActivity(activity -> {
                    Fragment fragment = ((AppCompatActivity) activity)
                            .getSupportFragmentManager()
                            .getFragmentFactory()
                            .instantiate(Objects.requireNonNull(
                                    fragmentClass.getClassLoader()),
                                    fragmentClass.getName()
                            );
                    if (fragmentArgs != null)
                        fragment.setArguments(fragmentArgs);
                    ((AppCompatActivity) activity).getSupportFragmentManager()
                            .beginTransaction()
                            .add(android.R.id.content, fragment, "")
                            .commitNow();
                });
    }

    public static void launchFragmentInHiltContainer(Class<? extends Fragment> fragmentClass,
                                                     @StyleRes int themeResId) {
        launchFragmentInHiltContainer(fragmentClass, null, themeResId);
    }

    public static void launchFragmentInHiltContainer(Class<? extends Fragment> fragmentClass) {
        launchFragmentInHiltContainer(fragmentClass, null, R.style.Theme_ZaniWeather);
    }
}

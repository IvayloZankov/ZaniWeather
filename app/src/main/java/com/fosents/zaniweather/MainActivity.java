package com.fosents.zaniweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fosents.zaniweather.ui.main.MainFragment;

import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.ExperimentalCoroutinesApi;

@ExperimentalCoroutinesApi
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}
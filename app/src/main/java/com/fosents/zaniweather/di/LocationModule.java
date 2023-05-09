package com.fosents.zaniweather.di;

import android.content.Context;

import com.fosents.zaniweather.data.LocationClient;
import com.fosents.zaniweather.data.local.LocationClientImpl;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ViewModelScoped;

@Module
@InstallIn(ViewModelComponent.class)
public class LocationModule {

    @Provides
    @ViewModelScoped
    public LocationClient provideLocationClient(@ApplicationContext Context context) {
        return new LocationClientImpl(context);
    }
}

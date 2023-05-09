package com.fosents.zaniweather.di;

import com.fosents.zaniweather.data.LocationClient;
import com.fosents.zaniweather.data.local.FakeLocationClient;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.scopes.ViewModelScoped;
import dagger.hilt.testing.TestInstallIn;

@Module
@TestInstallIn(
        components = ViewModelComponent.class,
        replaces = LocationModule.class
)
public class FakeLocationModule {

    @Provides
    @ViewModelScoped
    public LocationClient provideLocationClient() {
        return new FakeLocationClient();
    }
}

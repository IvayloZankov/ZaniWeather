package com.fosents.zaniweather.di;

import com.fosents.zaniweather.data.remote.FakeWeatherApi;
import com.fosents.zaniweather.data.remote.WeatherClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.testing.TestInstallIn;

@Module
@TestInstallIn(
        components = SingletonComponent.class,
        replaces = NetworkModule.class
)
public class FakeNetworkModule {

    @Provides
    @Singleton
    public FakeWeatherApi provideFakeWeatherApi() {
        return new FakeWeatherApi();
    }

    @Provides
    @Singleton
    public WeatherClient provideWeatherClient(FakeWeatherApi weatherApi) {
        return new WeatherClient(weatherApi);
    }
}

package com.fosents.zaniweather.di;

import com.fosents.zaniweather.data.local.DataStoreClient;
import com.fosents.zaniweather.data.local.FakeDataStoreApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.testing.TestInstallIn;

@Module
@TestInstallIn(
        components = SingletonComponent.class,
        replaces = PrefsModule.class
)
public class FakePrefsModule {

    @Provides
    @Singleton
    public FakeDataStoreApi provideFakeDataStoreApi() {
        return new FakeDataStoreApi();
    }

    @Provides
    @Singleton
    public DataStoreClient provideDataStoreClient(FakeDataStoreApi dataStoreApi) {
        return new DataStoreClient(dataStoreApi);
    }
}

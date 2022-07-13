package com.fosents.zaniweather.di;

import android.content.Context;

import com.fosents.zaniweather.data.local.DataStoreApi;
import com.fosents.zaniweather.data.local.DataStoreClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import kotlinx.coroutines.ExperimentalCoroutinesApi;

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent.class)
public class PrefsModule {

    @Provides
    @Singleton
    public DataStoreApi provideDataStoreApi(@ApplicationContext Context context) {
        return new DataStoreApi(context);
    }

    @Provides
    @Singleton
    public DataStoreClient provideDataStoreClient(DataStoreApi dataStoreApi) {
        return new DataStoreClient(dataStoreApi);
    }
}

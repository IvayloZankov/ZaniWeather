package com.fosents.zaniweather.data.local;

import androidx.datastore.preferences.core.Preferences;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import kotlinx.coroutines.ExperimentalCoroutinesApi;

public class DataStoreClient {

    private final DataStoreOperations mDataStoreApi;

    @Inject
    public DataStoreClient(DataStoreOperations dataStoreApi) {
        mDataStoreApi = dataStoreApi;
    }

    @ExperimentalCoroutinesApi
    public Single<Preferences> safeSearchedCities(String cityNew, ArrayList<String> cities) {
        return mDataStoreApi.safeSearchedCities(cityNew, cities);
    }

    @ExperimentalCoroutinesApi
    public Flowable<String> getSavedCities() {
        return mDataStoreApi.getSavedCities();
    }
}

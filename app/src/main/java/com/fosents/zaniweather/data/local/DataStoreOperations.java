package com.fosents.zaniweather.data.local;

import androidx.datastore.preferences.core.Preferences;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface DataStoreOperations {
    Single<Preferences> safeSearchedCities(String cityNew, ArrayList<String> cities);
    Flowable<String> getSavedCities();
}

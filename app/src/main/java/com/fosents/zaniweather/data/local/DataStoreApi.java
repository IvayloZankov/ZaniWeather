package com.fosents.zaniweather.data.local;

import static com.fosents.zaniweather.utils.Constants.PREFS_CITIES_KEY;
import static com.fosents.zaniweather.utils.Constants.PREFS_NAME;

import android.content.Context;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlinx.coroutines.ExperimentalCoroutinesApi;

@ExperimentalCoroutinesApi
public class DataStoreApi implements DataStoreOperations {

    private final RxDataStore<Preferences> mDataStore;

    private final Preferences.Key<String> KEY_CITIES = PreferencesKeys.stringKey(PREFS_CITIES_KEY);

    @Inject
    public DataStoreApi(Context context) {
        mDataStore =
                new RxPreferenceDataStoreBuilder(context, PREFS_NAME).build();
    }

    @Override
    public Single<Preferences> safeSearchedCities(String cityNew, ArrayList<String> cities) {
        if (cities != null)
            cities.add(0, cityNew);
        else {
            cities = new ArrayList<>();
            cities.add(cityNew);
        }
        List<String> lastSearchedCities;
        if (cities.size() > 3)
            lastSearchedCities = cities.subList(0, 3);
        else
            lastSearchedCities = cities;
        StringBuilder builder = new StringBuilder();
        for (String cityName : lastSearchedCities) {
            builder.append(cityName).append(",");
        }
        String citiesForPrefs = builder.substring(0, builder.length() - 1);
        return mDataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(KEY_CITIES, citiesForPrefs);
            return Single.just(mutablePreferences);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<String> getSavedCities() {
        return mDataStore.data().map(prefs ->
                prefs.get(KEY_CITIES) != null ? prefs.get(KEY_CITIES) : "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

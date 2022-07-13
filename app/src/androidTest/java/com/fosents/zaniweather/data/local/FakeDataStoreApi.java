package com.fosents.zaniweather.data.local;

import static com.fosents.zaniweather.utils.Constants.citiesTest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.Preferences;

import com.fosents.zaniweather.model.ApiResponse;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import kotlinx.coroutines.ExperimentalCoroutinesApi;

@ExperimentalCoroutinesApi
public class FakeDataStoreApi implements DataStoreOperations {

    @Inject
    public FakeDataStoreApi() {
    }

    @Override
    public Single<Preferences> safeSearchedCities(String cityNew, ArrayList<String> cities) {
        return Single.just(new Preferences() {
            @Override
            public <T> boolean contains(@NonNull Key<T> key) {
                return false;
            }

            @Nullable
            @Override
            public <T> T get(@NonNull Key<T> key) {
                return null;
            }

            @NonNull
            @Override
            public Map<Key<?>, Object> asMap() {
                return null;
            }
        });
    }

    @Override
    public Flowable<String> getSavedCities() {
        StringBuilder names = new StringBuilder();
        for (ApiResponse response :
                citiesTest) {
            names.append(response.getName());
            names.append(",");
        }
        return Flowable.just(names.substring(0, names.length()));
    }
}

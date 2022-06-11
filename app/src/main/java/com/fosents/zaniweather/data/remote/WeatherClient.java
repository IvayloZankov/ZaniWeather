package com.fosents.zaniweather.data.remote;

import com.fosents.zaniweather.model.ApiResponse;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeatherClient {

    private final WeatherApi mWeatherApi;

    @Inject
    public WeatherClient(WeatherApi weatherApi) {
        mWeatherApi = weatherApi;
    }

    public Single<ApiResponse> getWeatherData(
            String cityName,
            String units,
            String apiKey
    ) {
        return subscribe(mWeatherApi.getWeatherData(cityName, units, apiKey));
    }

    private <T> Single<T> subscribe(Single<T> single) {
        return single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

package com.fosents.zaniweather.data.remote;

import static com.fosents.zaniweather.utils.Constants.*;

import com.fosents.zaniweather.model.ApiResponse;

import io.reactivex.Single;

public class FakeWeatherApi implements WeatherApi {

    @Override
    public Single<ApiResponse> getWeatherData(String cityName, String units, String apiKey) {
        for (int i = 0; i < citiesTest.size(); i++) {
            if (citiesTest.get(i).getName().equalsIgnoreCase(cityName)) {
                return Single.just(citiesTest.get(i));
            }
        }
        return Single.just(new ApiResponse(
                TEST_CITY_1,
                TEST_COUNTRY_1,
                TEST_TEMP_1,
                TEST_WEATHER_1,
                TEST_WEATHER_DESC_1));
    }
}

package com.fosents.zaniweather.data.remote;

import static com.fosents.zaniweather.utils.Constants.OPEN_WEATHER_GET_DATA;

import com.fosents.zaniweather.model.ApiResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET(OPEN_WEATHER_GET_DATA)
    Single<ApiResponse> getWeatherData(@Query("q") String cityName,
                                      @Query("units") String units,
                                      @Query("APPID") String apiKey);
}

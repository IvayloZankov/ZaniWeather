package com.fosents.zaniweather.utils;

import com.fosents.zaniweather.model.ApiResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constants {
    public static final String TEST_CITY_1 = "TestCity";
    public static final String TEST_CITY_2 = "TestCityTwo";
    public static final String TEST_CITY_3 = "TestCity3";
    public static final String TEST_CITY_LOCATION = "TestCityLocation";
    public static final String TEST_COUNTRY_1 = "TestCountry";
    public static final String TEST_COUNTRY_2 = "TestCountryTwo";
    public static final String TEST_COUNTRY_3 = "TestCountry3";
    public static final String TEST_COUNTRY_LOCATION = "LOC";
    public static final double TEST_TEMP_1 = 54.45;
    public static final double TEST_TEMP_2 = -10.30;
    public static final double TEST_TEMP_3 = 24.00;
    public static final String TEST_WEATHER_1 = "Thunderstorm";
    public static final String TEST_WEATHER_2 = "Snow";
    public static final String TEST_WEATHER_3 = "Clouds";
    public static final String TEST_WEATHER_DESC_1 = "Heavy thunderstorms";
    public static final String TEST_WEATHER_DESC_2 = "Light snow";
    public static final String TEST_WEATHER_DESC_3 = "Scattered clouds";

    public static final List<ApiResponse> citiesTest = Collections.unmodifiableList(new ArrayList<ApiResponse>(){{
        add(new ApiResponse(TEST_CITY_1, TEST_COUNTRY_1, TEST_TEMP_1, TEST_WEATHER_1, TEST_WEATHER_DESC_1));
        add(new ApiResponse(TEST_CITY_2, TEST_COUNTRY_2, TEST_TEMP_2, TEST_WEATHER_2, TEST_WEATHER_DESC_2));
        add(new ApiResponse(TEST_CITY_3, TEST_COUNTRY_3, TEST_TEMP_3, TEST_WEATHER_3, TEST_WEATHER_DESC_3));
    }});
}

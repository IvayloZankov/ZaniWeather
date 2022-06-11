package com.fosents.zaniweather.di;

import static com.fosents.zaniweather.utils.Constants.OPEN_WEATHER_BASE_URL;

import com.fosents.zaniweather.data.remote.WeatherClient;
import com.fosents.zaniweather.data.remote.WeatherApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public Retrofit provideRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(OPEN_WEATHER_BASE_URL)
                .client(new OkHttpClient.Builder().addInterceptor(
                        new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public WeatherApi provideWeatherApi(Retrofit retrofit) {
        return retrofit.create(WeatherApi.class);
    }

    @Provides
    @Singleton
    public WeatherClient provideWeatherClient(WeatherApi weatherApi) {
        return new WeatherClient(weatherApi);
    }
}

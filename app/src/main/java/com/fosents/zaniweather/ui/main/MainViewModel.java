package com.fosents.zaniweather.ui.main;

import static com.fosents.zaniweather.utils.Constants.ICON_WEATHER_CLEAR;
import static com.fosents.zaniweather.utils.Constants.ICON_WEATHER_CLOUDS;
import static com.fosents.zaniweather.utils.Constants.ICON_WEATHER_MIST;
import static com.fosents.zaniweather.utils.Constants.ICON_WEATHER_RAIN;
import static com.fosents.zaniweather.utils.Constants.ICON_WEATHER_SNOW;
import static com.fosents.zaniweather.utils.Constants.ICON_WEATHER_SUNNY;
import static com.fosents.zaniweather.utils.Constants.ICON_WEATHER_THUNDERSTORM;
import static com.fosents.zaniweather.utils.Constants.OPEN_WEATHER_UNITS;
import static com.fosents.zaniweather.utils.Constants.STATUS_INVALID_KEY;
import static com.fosents.zaniweather.utils.Constants.STATUS_NOT_FOUND;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.datastore.preferences.core.Preferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fosents.zaniweather.R;
import com.fosents.zaniweather.WeatherViewModel;
import com.fosents.zaniweather.data.local.DataStoreClient;
import com.fosents.zaniweather.data.remote.WeatherClient;
import com.fosents.zaniweather.model.ApiResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import kotlinx.coroutines.ExperimentalCoroutinesApi;

@ExperimentalCoroutinesApi
@HiltViewModel
public class MainViewModel extends WeatherViewModel {

    private final FusedLocationProviderClient fusedLocationClient;

    private final WeatherClient mWeatherClient;
    private final DataStoreClient mDataStoreClient;

    private final MutableLiveData<ApiResponse> mLiveDataWeatherData = new MutableLiveData<>();
    private final MutableLiveData<String> mLiveDataLocation = new MutableLiveData<>();
    private final MutableLiveData<int[]> mLiveDataImages = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mLiveDataRequestPermissions = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> mLiveDataSavedCities = new MutableLiveData<>();

    private String mCityName;

    @Inject
    MainViewModel(@NonNull Application application,
                  WeatherClient weatherClient,
                  DataStoreClient dataStoreClient) {
        super(application);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplication());
        mWeatherClient = weatherClient;
        mDataStoreClient = dataStoreClient;
        getSavedCities();
        getUserLocation();
    }

    private void getSavedCities() {
        mMainCompDisposable.add(mDataStoreClient.getSavedCities().subscribe(s -> {
                    if (!s.isEmpty()) {
                        mLiveDataSavedCities.setValue(
                                new ArrayList<>(Arrays.asList(s.split(",")))
                        );
                    }
                })
        );
    }

    public void saveSearchedCities(String cityNew) {
        ArrayList<String> value = mLiveDataSavedCities.getValue();
        mDataStoreClient.safeSearchedCities(cityNew, value).subscribe(new SingleObserver<Preferences>() {
            @Override
            public void onSubscribe(Disposable d) {
                mMainCompDisposable.add(d);
            }

            @Override
            public void onSuccess(Preferences preferences) {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public LiveData<ApiResponse> getLiveDataWeatherData() {
        return mLiveDataWeatherData;
    }

    public LiveData<String> getLiveDataLocation() {
        return mLiveDataLocation;
    }

    public LiveData<int[]> getLiveDataImages() {
        return mLiveDataImages;
    }

    public MutableLiveData<Boolean> getLiveDataRequestPermissions() {
        return mLiveDataRequestPermissions;
    }

    public LiveData<ArrayList<String>> getLiveDataSavedCities() {
        return mLiveDataSavedCities;
    }

    public void initWeatherDataRequest(boolean isFromUserInput, boolean isFromSavedCity) {
        mLiveDataIsLoading.setValue(true);
        mWeatherClient.getWeatherData(mCityName, OPEN_WEATHER_UNITS, getApplication().getString(R.string.weather_api_key))
                .subscribe(new WeatherObserver<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        mLiveDataWeatherData.setValue(apiResponse);
                        setImageResource(apiResponse.getWeather().get(0).getMain());
                        mLiveDataIsLoading.setValue(false);
                        if (isFromUserInput) {
                            mLiveDataLocation.setValue(
                                    apiResponse.getName() + ", " +
                                            apiResponse.getSys().getCountry());
                            if (!isFromSavedCity)
                                saveSearchedCities(apiResponse.getName());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        String message = e.getMessage();
                        if (message != null && message.contains(STATUS_NOT_FOUND)) {
                            mLiveDataLocation.setValue(
                                    getApplication().getString(R.string.unknown_city));
                        } else if (message != null && message.contains(STATUS_INVALID_KEY)) {
                            mLiveDataLocation.setValue(
                                    getApplication().getString(R.string.invalid_key));
                        } else if (e instanceof IOException) {
                            mLiveDataLocation.setValue(
                                    getApplication().getString(R.string.no_internet));
                        }
                        mLiveDataIsLoading.setValue(false);
                        mLiveDataOnError.setValue(null);
                        mLiveDataWeatherData.setValue(null);
                    }
                });
    }

    public void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(getApplication());
                    List<Address> addresses = new ArrayList<>();
                    try {
                        addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        mCityName = address.getLocality();
                        mLiveDataLocation.setValue(mCityName + ", " + address.getCountryName());
                        initWeatherDataRequest(false, false);
                    }
                }
            });
        } else {
            mLiveDataRequestPermissions.setValue(true);
            mLiveDataIsLoading.setValue(false);
        }
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public void setImageResource(String main) {
        if (main.equalsIgnoreCase(ICON_WEATHER_CLOUDS)) {
            mLiveDataImages.setValue(new int[]{R.drawable.ic_icon_clouds, R.drawable.background_clouds});
        } else if (main.equalsIgnoreCase(ICON_WEATHER_RAIN)) {
            mLiveDataImages.setValue(new int[]{R.drawable.ic_icon_rain, R.drawable.background_rain});
        } else if (main.equalsIgnoreCase(ICON_WEATHER_SUNNY)) {
            mLiveDataImages.setValue(new int[]{R.drawable.ic_icon_sunny, R.drawable.background_sunny});
        } else if (main.equalsIgnoreCase(ICON_WEATHER_CLEAR)) {
            mLiveDataImages.setValue(new int[]{R.drawable.ic_icon_clear, R.drawable.background_clear});
        } else if (main.equalsIgnoreCase(ICON_WEATHER_THUNDERSTORM)) {
            mLiveDataImages.setValue(new int[]{R.drawable.ic_icon_thunderstorm, R.drawable.background_thunderstorm});
        } else if (main.equalsIgnoreCase(ICON_WEATHER_SNOW)) {
            mLiveDataImages.setValue(new int[]{R.drawable.ic_icon_snow, R.drawable.background_snowfall});
        } else if (main.equalsIgnoreCase(ICON_WEATHER_MIST)) {
            mLiveDataImages.setValue(new int[]{R.drawable.ic_icon_mist, R.drawable.background_mist});
        }
    }

    public void resetPermissions() {
        mLiveDataRequestPermissions.setValue(false);
    }
}
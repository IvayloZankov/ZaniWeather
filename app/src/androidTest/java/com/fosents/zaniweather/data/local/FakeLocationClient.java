package com.fosents.zaniweather.data.local;

import static com.fosents.zaniweather.utils.Constants.TEST_CITY_LOCATION;
import static com.fosents.zaniweather.utils.Constants.TEST_COUNTRY_LOCATION;

import android.location.Address;

import androidx.lifecycle.MutableLiveData;

import com.fosents.zaniweather.data.LocationClient;

import java.util.Locale;

public class FakeLocationClient implements LocationClient {

    @Override
    public void getLocation(MutableLiveData<Address> liveData) {
        Address address = new Address(Locale.getDefault());
        address.setLocality(TEST_CITY_LOCATION);
        address.setCountryName(TEST_COUNTRY_LOCATION);
        liveData.setValue(address);
    }
}

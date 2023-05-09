package com.fosents.zaniweather.data.local;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import androidx.lifecycle.MutableLiveData;

import com.fosents.zaniweather.data.LocationClient;
import com.fosents.zaniweather.utils.LocationUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationClientImpl implements LocationClient {

    private final Context mContext;
    private final FusedLocationProviderClient mFusedLocationClient;

    public LocationClientImpl(Context context) {
        mContext = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @SuppressLint("MissingPermission")
    public void getLocation(MutableLiveData<Address> liveData) {
        if (LocationUtils.arePermissions(mContext)) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(mContext);
                    List<Address> addresses = new ArrayList<>();
                    try {
                        addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        String mCityName = address.getLocality();
//                        liveData.setValue(mCityName + ", " + address.getCountryName());
                        liveData.setValue(addresses.get(0));
//                        initWeatherDataRequest(false, false);
                    }
                }
            });
        }
    }
}

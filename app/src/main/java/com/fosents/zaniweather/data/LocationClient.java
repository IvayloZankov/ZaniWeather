package com.fosents.zaniweather.data;

import androidx.lifecycle.MutableLiveData;
import android.location.Address;

public interface LocationClient {

    void getLocation(MutableLiveData<Address> liveData);

}

package com.fosents.zaniweather;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fosents.zaniweather.model.ApiResponse;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class WeatherViewModel extends AndroidViewModel {

    protected CompositeDisposable mMainCompDisposable = new CompositeDisposable();

    protected MutableLiveData<Boolean> mLiveDataIsLoading = new MutableLiveData<>();
    protected MutableLiveData<Throwable> mLiveDataOnError = new MutableLiveData<>();

    public WeatherViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getLiveDataIsLoading() {
        return mLiveDataIsLoading;
    }

    public LiveData<Throwable> getLiveDataOnError() {
        return mLiveDataOnError;
    }

    public abstract class WeatherObserver<T extends ApiResponse> implements SingleObserver<T> {
        @Override
        public void onSubscribe(Disposable d) {
            mMainCompDisposable.add(d);
        }

        @Override
        public void onError(Throwable e) {
            mLiveDataOnError.setValue(e);
            e.printStackTrace();
        }
    }
}

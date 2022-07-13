package com.fosents.zaniweather.ui.main;

import static com.fosents.zaniweather.utils.LocationUtils.arePermissions;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fosents.zaniweather.R;
import com.fosents.zaniweather.model.ApiResponse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.ExperimentalCoroutinesApi;

@ExperimentalCoroutinesApi
@AndroidEntryPoint
public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    private ImageView mImageViewBackground;
    private LinearLayout mLayoutData;
    private TextView mTextViewWeatherData;
    private TextView mTextViewWeatherDataDescription;
    private TextView mTextViewWeatherTemp;
    private ImageView mImageIconWeather;
    private TextView mTextViewLocation;
    private ProgressBar mProgressBarLoading;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BottomSheetBehavior<FrameLayout> mSearchSheet;
    private EditText mEditTextCity;
    private LinearLayout mLayoutSavedCities;
    private ConstraintLayout mEditTextCityContainer;
    private boolean mStateSearchSheet;

    private ActivityResultLauncher<String[]> locationPermissionRequest;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        locationPermissionRequest = registerForActivityResult(new ActivityResultContracts
                        .RequestMultiplePermissions(), result -> {
            if (Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION)))
                mViewModel.getUserLocation();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        initWeatherViews(view);
        initSwipeRefresh(view);
        initCityEditText(view);
        initSearchSheet(view);
        initFloatLocationButton(view);
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.getLiveDataWeatherData().observe(getViewLifecycleOwner(), this::updateWeatherInfo);
        mViewModel.getLiveDataImages().observe(getViewLifecycleOwner(), ints -> {
            mImageIconWeather.setImageResource(ints[0]);
            mImageViewBackground.setImageResource(ints[1]);
        });
        mViewModel.getLiveDataIsLoading().observe(getViewLifecycleOwner(), this::setScreenLoading);
        mViewModel.getLiveDataRequestPermissions().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                requestPermissions();
                mViewModel.resetPermissions();
            }
        });
        mViewModel.getLiveDataLocation().observe(getViewLifecycleOwner(), string ->
                mTextViewLocation.setText(string));
        mViewModel.getLiveDataOnError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null)
                mLayoutData.setVisibility(View.INVISIBLE);
        });
        mViewModel.getLiveDataSavedCities().observe(getViewLifecycleOwner(), cities -> {
            updateSavedCities(cities);
            Log.e("CITIES", cities.toString());
        });
    }

    private void initCityEditText(View view) {
        mEditTextCity = view.findViewById(R.id.editTextCity);
        ImageView imageViewSearch = view.findViewById(R.id.imageSearchCity);
        mEditTextCityContainer =  view.findViewById(R.id.editTextCityContainer);
        mEditTextCity.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                String cityName = mEditTextCity.getText().toString().trim();
                initUserInputWeatherRequest(cityName, false);
                return true;
            }
            return false;
        });
        imageViewSearch.setOnClickListener(v -> {
            String cityName = mEditTextCity.getText().toString().trim();
            initUserInputWeatherRequest(cityName, false);
        });
    }

    private void initUserInputWeatherRequest(String cityName, boolean isFromSavedCity) {
        if (!cityName.isEmpty()) {
            mViewModel.setCityName(cityName);
            mViewModel.initWeatherDataRequest(true, isFromSavedCity);
            InputMethodManager in =
                    (InputMethodManager) requireActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(mEditTextCity.getWindowToken(), 0);
            mSearchSheet.setState(STATE_COLLAPSED);
        }
    }

    private void initSearchSheet(View view) {
        FrameLayout bottomSheetFrame = view.findViewById(R.id.searchSheet);
        mSearchSheet = BottomSheetBehavior.from(bottomSheetFrame);
        mSearchSheet.setPeekHeight(250);
        TransitionDrawable transitionBackground = (TransitionDrawable) bottomSheetFrame.getBackground();
        TransitionDrawable transitionEditText = (TransitionDrawable) mEditTextCity.getBackground();
        mSearchSheet.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == STATE_EXPANDED) {
                    if (!mStateSearchSheet) {
                        mStateSearchSheet = true;
                        transitionEditText.startTransition(300);
                        transitionBackground.startTransition(300);
                        mEditTextCityContainer.setVisibility(View.VISIBLE);
                    }
                }
                else if (newState == STATE_COLLAPSED) {
                        if (mStateSearchSheet) {
                            mStateSearchSheet = false;
                            transitionEditText.reverseTransition(300);
                            transitionBackground.reverseTransition(300);
                            mEditTextCityContainer.setVisibility(View.INVISIBLE);
                            InputMethodManager in =
                                    (InputMethodManager) requireActivity()
                                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(mEditTextCity.getWindowToken(), 0);
                        }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        ImageView mImageDragHandle = view.findViewById(R.id.imageDragHandle);
        mImageDragHandle.setOnClickListener(v -> {
            if (mSearchSheet.getState() == STATE_COLLAPSED)
                mSearchSheet.setState(STATE_EXPANDED);
            else {
                mSearchSheet.setState(STATE_COLLAPSED);
            }
        });
        mLayoutSavedCities = view.findViewById(R.id.layoutSavedCities);
    }

    private void initSwipeRefresh(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            mViewModel.initWeatherDataRequest(false, false);
        });
    }

    private void initWeatherViews(View view) {
        mLayoutData = view.findViewById(R.id.linearLayoutWeatherData);
        mImageViewBackground = view.findViewById(R.id.imageViewWeather);
        mTextViewWeatherTemp = view.findViewById(R.id.textViewWeatherTemp);
        mImageIconWeather = view.findViewById(R.id.imageIconWeather);
        mTextViewWeatherData = view.findViewById(R.id.textViewWeatherData);
        mTextViewWeatherDataDescription = view.findViewById(R.id.textViewWeatherDataDescription);
        mTextViewLocation = view.findViewById(R.id.textViewLocation);
        mProgressBarLoading = view.findViewById(R.id.progressBarLoading);
    }

    private void setScreenLoading(boolean isLoading) {
        if (isLoading) {
            mProgressBarLoading.setVisibility(View.VISIBLE);
            mLayoutData.setAlpha(0.5f);
            mImageViewBackground.setAlpha(0.5f);
        } else {
            mProgressBarLoading.setVisibility(View.GONE);
            mLayoutData.setAlpha(1f);
            mImageViewBackground.setAlpha(1f);
        }
    }

    private void updateWeatherInfo(ApiResponse apiResponse) {
        if (apiResponse != null) {
            mTextViewWeatherTemp.setText(String.valueOf(Math.toIntExact(Math.round(apiResponse.getMain().getTemp()))));
            mTextViewWeatherData.setText(apiResponse.getWeather().get(0).getMain());
            mTextViewWeatherDataDescription.setText(apiResponse.getWeather().get(0).getDescription());
            mLayoutData.setVisibility(View.VISIBLE);
        }
    }

    private void updateSavedCities(ArrayList<String> cities) {
        mLayoutSavedCities.removeAllViews();
        for (String city : cities) {
            TextView view = (TextView) getLayoutInflater().inflate(
                    R.layout.item_saved_city,
                    mLayoutSavedCities, false);
            view.setText(city);
            view.setOnClickListener(v -> {
                mEditTextCity.setText(city);
                initUserInputWeatherRequest(city, true);
            });
            mLayoutSavedCities.addView(view);
        }
    }

    private void initFloatLocationButton(@NonNull View view) {
        FloatingActionButton floatingLocation = view.findViewById(R.id.floatingLocation);
        floatingLocation.setOnClickListener(v -> {
            mViewModel.getUserLocation();
            mSearchSheet.setState(STATE_COLLAPSED);
        });
    }

    private void requestPermissions() {
        if (!arePermissions(requireContext()))
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }
}
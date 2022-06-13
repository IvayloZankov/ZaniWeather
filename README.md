# ZaniWeather

## Setup
Openweathermap Api key is needed in order to use the app. 
You can get it at https://openweathermap.org/api and set it in gradle.properties file as
`weather_api_key="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"`

## About
Simple weather application with Hilt-Dagger, RxJava, Retrofit, MVVM, DataStore.

## Description

### On launch
Ask user for location permission if not granted

#### On Accept
Displays current weather with current location. 

#### On Deny
Stand by for manual city input.

### On location button click
Try to fetch current location and ask again for location permission if not granted. 
In case of success displays current weather with current location. 

### On bottom sheet drawer
Display fields for city input as well as last searched cities if any.

#### On city name text field
Displays weather data if there is a valid city name entered and store it in searched cities.
package com.example.mitsogosample.network;

import com.example.mitsogosample.model.WeatherApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    @GET("onecall")
    Call<WeatherApiResponse> getWeather(@Query("lat") int lat, @Query("lon") int lon,
                                        @Query("units") String units, @Query("appid") String appId);
}

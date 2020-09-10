package com.example.mitsogosample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.databinding.library.BuildConfig;

import com.example.mitsogosample.model.WeatherApiResponse;
import com.example.mitsogosample.network.Api;
import com.example.mitsogosample.room.AppDatabase;
import com.example.mitsogosample.room.entity.DataHistory;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyJobIntentService extends JobIntentService {
    final Handler mHandler = new Handler();
    DataHistory dataHistory;
    private static final String TAG = "MyJobIntentService";

    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 2;
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, MyJobIntentService.class, JOB_ID, intent);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        dataHistory = new DataHistory();
        showToast("Job Execution Started");
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        int minTimeInterval = intent.getIntExtra("minTimeInterval", 5);
        looperFunction(minTimeInterval);
    }

    private void looperFunction(int minTimeInterval) {
        getDetails();
        try {
            Thread.sleep(1000*60*minTimeInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        looperFunction(minTimeInterval);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showToast("Job Execution Finished");
    }
    // Helper for showing tests
    void showToast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyJobIntentService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDetails() {
        String str = android.os.Build.MODEL;
        String osVersion = "";
        try {
            PackageInfo pInfo = MyJobIntentService.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            osVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = BuildConfig.VERSION_NAME;

        BatteryManager bm = (BatteryManager) MyJobIntentService.this.getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        dataHistory.setDeviceName(str);
        dataHistory.setDeviceOs(osVersion +"");
        dataHistory.setBatteryLevel(batLevel + "");
        dataHistory.setWord(versionName);
        Date currentTime = Calendar.getInstance().getTime();
        dataHistory.setTime(currentTime+"");
        dataHistory.setNetworkType(getConnectionType());
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable;
        bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        long megAvailable = bytesAvailable / (1024 * 1024);
        dataHistory.setDeviceStorage("Internal/External Storage "+ megAvailable+ " Mb");
        getWeatherDetails();
    }

    private void getWeatherDetails() {
        int lat = (int)DashboardActivity.LATITUDE;
        int log = (int)DashboardActivity.LONGITUDE;
        //Creating a retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);

//        showToast(lat + "--" + log);

        Call<WeatherApiResponse> call = api.getWeather(9, 76, "metric", "a2c618c6f042ffd85b32056c452bcbc2");

        call.enqueue(new Callback<WeatherApiResponse>() {
            @Override
            public void onResponse(Call<WeatherApiResponse> call, Response<WeatherApiResponse> response) {
                WeatherApiResponse weatherApiResponse = response.body();
                if(response.isSuccessful()){
                    dataHistory.setTemperature(weatherApiResponse.getCurrent().getTemp().toString());
                    dataHistory.setTimeZone(weatherApiResponse.getTimezone());
                    dataHistory.setClimate(weatherApiResponse.getCurrent().getWeather().get(0).getDescription());
                    insertDate(dataHistory);
                }
            }

            @Override
            public void onFailure(Call<WeatherApiResponse> call, Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    public String getConnectionType(){
        String type = null;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected)
        {
            type = activeNetwork.getTypeName();
        }
        return type;
    }

    private void insertDate(final DataHistory dataHistory) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context sharedContext = null;
                try {
                    sharedContext = MyJobIntentService.this.createPackageContext("com.example.test", Context.CONTEXT_IGNORE_SECURITY);
                } catch (PackageManager.NameNotFoundException e) {
                    sharedContext = getApplicationContext();
                    e.printStackTrace();
                }
                AppDatabase.getDatabase(sharedContext).historyDao().insert(dataHistory);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d("debug", "SUccessfuly inserted");
            }
        }.execute();
    }

}

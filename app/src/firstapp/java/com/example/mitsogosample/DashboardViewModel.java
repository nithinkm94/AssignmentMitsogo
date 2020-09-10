package com.example.mitsogosample;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Observable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mitsogosample.room.AppDatabase;
import com.example.mitsogosample.room.entity.DataHistory;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private DataRepository mRepository;
    private MutableLiveData<List<DataHistory>> refreshData;
    private LiveData<List<DataHistory>> listLiveData;
    private Observable<String> test ;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataRepository(application);
        listLiveData = mRepository.getAllData();
        refreshData = new MutableLiveData<>();
    }

    LiveData<List<DataHistory>> getListLiveData() {
        Log.d("updatedDb", "1size= ");
        return listLiveData;
    }

    public LiveData<List<DataHistory>> getRefreshData() {
        return refreshData;
    }

    public void getData() {
        new AsyncTask<String, String, List<DataHistory>>() {
            @Override
            protected List<DataHistory> doInBackground(String... strings) {

                return  mRepository.getRefreshData();
            }

            @Override
            protected void onPostExecute(List<DataHistory> dataHistories) {
                super.onPostExecute(dataHistories);
                Log.d("updatedDb", "Success -- size = " + dataHistories.size());
                refreshData.setValue(dataHistories);
            }
        }.execute();
    }

}

package com.example.mitsogosample;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mitsogosample.room.AppDatabase;
import com.example.mitsogosample.room.dao.HistoryDao;
import com.example.mitsogosample.room.entity.DataHistory;

import java.util.List;

public class DataRepository {
    private HistoryDao historyDao;
    private LiveData<List<DataHistory>> listLiveData;

    private LiveData<List<DataHistory>> refreshData;

    DataRepository(Application application) {
        Context sharedContext = null;
        try {
            sharedContext =  application.createPackageContext( "com.example.mitsogosample.secondapp", Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            sharedContext = application;
            e.printStackTrace();
        }
        AppDatabase db = AppDatabase.getDatabase(sharedContext);
        historyDao = db.historyDao();
        listLiveData = historyDao.getLiveData();
    }

    LiveData<List<DataHistory>> getAllData() {
        Log.d("updatedDb", "2size= ");
        return listLiveData;
    }

    List<DataHistory> getRefreshData() {
        Log.d("updatedDb", "refreshData= ");
        return historyDao.getRefreshData();
    }

}

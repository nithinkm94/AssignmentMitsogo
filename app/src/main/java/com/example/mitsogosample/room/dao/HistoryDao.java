package com.example.mitsogosample.room.dao;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mitsogosample.room.entity.DataHistory;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DataHistory dataHistory);

    @Query("SELECT * from data_history")
    List<DataHistory> getRefreshData();

    @Query("SELECT * from data_history")
    LiveData<List<DataHistory>> getLiveData();

}

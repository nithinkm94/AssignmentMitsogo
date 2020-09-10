package com.example.mitsogosample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mitsogosample.room.AppDatabase;
import com.example.mitsogosample.room.entity.DataHistory;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    DashboardViewModel dashboardViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        RecyclerView recyclerView = findViewById(R.id.rv_history);
        final DataHistoryAdapter adapter = new DataHistoryAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        dashboardViewModel.getListLiveData().observe(this, new Observer<List<DataHistory>>() {
            @Override
            public void onChanged(@Nullable final List<DataHistory> dataHistories) {
                // Update the cached copy of the words in the adapter.
                Log.d("updatedDb", "onChanged= ");
                adapter.updateData(dataHistories);
            }
        });

        dashboardViewModel.getRefreshData().observe(this, new Observer<List<DataHistory>>() {
            @Override
            public void onChanged(@Nullable final List<DataHistory> dataHistories) {
                // Update the cached copy of the words in the adapter.
                Log.d("updatedDb", "2onChanged= ");
                adapter.updateData(dataHistories);
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.reload:
                dashboardViewModel.getData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    }
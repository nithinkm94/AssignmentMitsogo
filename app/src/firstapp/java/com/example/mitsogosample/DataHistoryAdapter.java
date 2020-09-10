package com.example.mitsogosample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mitsogosample.databinding.DataHistoryList;
import com.example.mitsogosample.room.entity.DataHistory;

import java.util.List;

public class DataHistoryAdapter extends RecyclerView.Adapter<DataHistoryAdapter.MyViewHolder> {

    private List<DataHistory> dataHistories;
    private final LayoutInflater mInflater;

    DataHistoryAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DataHistoryList dataHistoryList = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(dataHistoryList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataHistory dataHistory = dataHistories.get(position);
        holder.dataHistoryList.tvTime.setText(dataHistory.getTime());
        holder.dataHistoryList.tvClimate.setText("Climate : "+ dataHistory.getClimate());
        holder.dataHistoryList.textView5.setText("Temp : "+dataHistory.getTemperature());
        holder.dataHistoryList.tvBattery.setText("Battery : %"+dataHistory.getBatteryLevel());
        holder.dataHistoryList.tvNetwork.setText("Network : "+dataHistory.getNetworkType());
        holder.dataHistoryList.tvDeviceName.setText("Device : "+dataHistory.getDeviceName());
        holder.dataHistoryList.tvOs.setVisibility(View.GONE);
        holder.dataHistoryList.tvInternal.setText(dataHistory.getDeviceStorage());
        holder.dataHistoryList.tvExternal.setVisibility(View.GONE);



    }

    void updateData(List<DataHistory> dataHistories) {
        this.dataHistories = dataHistories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (dataHistories != null)
            return dataHistories.size();
        else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        DataHistoryList dataHistoryList;

        public MyViewHolder(@NonNull DataHistoryList itemView) {
            super(itemView.getRoot());
            this.dataHistoryList = itemView;
        }
    }
}

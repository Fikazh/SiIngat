package com.example.siingat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TodayAdapter extends ArrayAdapter<Today> {
    public TodayAdapter(Context context, List<Today> today)
    {
        super (context, 0, today);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Today today = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.daily_cell, parent, false);
        }

        TextView dailyCellName = convertView.findViewById(R.id.dailyCellName);
        TextView dailyCellTime = convertView.findViewById(R.id.dailyCellTime);

        String dailyName = today.getName();
        String dailyTime = CalendarUtils.formattedTime(today.getTime());

        dailyCellName.setText(dailyName);
        dailyCellTime.setText(dailyTime);

        return convertView;
    }
}
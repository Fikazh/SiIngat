package com.example.siingat;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DailyAdapter extends ArrayAdapter<Daily> {
    public DailyAdapter (Context context, List<Daily> dailies)
    {
        super (context, 0, dailies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Daily daily = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.daily_cell, parent, false);
        }

        TextView dailyCellName = convertView.findViewById(R.id.dailyCellName);
        TextView dailyCellTime = convertView.findViewById(R.id.dailyCellTime);

        String dailyName = daily.getName();
        String dailyTime = CalendarUtils.formattedTime(daily.getTime());

        dailyCellName.setTextSize(18);
        dailyCellTime.setTextSize(18);

        dailyCellName.setText(dailyName);
        dailyCellTime.setText(dailyTime);

        return convertView;
    }
}
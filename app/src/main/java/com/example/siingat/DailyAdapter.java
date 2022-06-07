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

        TextView dailyCellTV = convertView.findViewById(R.id.dailyCellTV);

        String dailyTitle = daily.getName() + " " + CalendarUtils.formattedTime(daily.getTime());
        dailyCellTV.setText(dailyTitle);

        return convertView;
    }
}
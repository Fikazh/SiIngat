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

public class FutureEventAdapter extends ArrayAdapter<Event> {
    public FutureEventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.future_event_cell, parent, false);

        TextView futureEventCellName = convertView.findViewById(R.id.futureEventCellName);
        TextView futureEventCellTime = convertView.findViewById(R.id.futureEventCellTime);
        TextView futureEventCellDate = convertView.findViewById(R.id.futureEventCellDate);

        String name = event.getName();
        String time = CalendarUtils.formattedTime(event.getTime());
        String date = CalendarUtils.formattedDate(event.getDate());

        futureEventCellName.setText(name);
        futureEventCellTime.setText(time);
        futureEventCellDate.setText(date);

        return convertView;
    }
}

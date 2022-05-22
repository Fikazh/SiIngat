package com.example.siingat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventDescET;
    private TextView eventDateET, eventTimeET;

    final Calendar myCalendar = Calendar.getInstance();
    int hour, minute;

    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        eventDateET.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventEditActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void initWidgets()
    {
        eventDescET = findViewById(R.id.eventDescET);
        eventDateET = findViewById(R.id.eventDateET);
        eventTimeET = findViewById(R.id.eventTimeET);
    }

    private void updateLabel(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        eventDateET.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void timePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;

                time = String.format("%02d:%02d", hour, minute);
                LocalTime localTime = LocalTime.parse(time);
                eventTimeET.setText(CalendarUtils.formattedTime(localTime));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, onTimeSetListener, hour, minute, false);
        timePickerDialog.show();
    }

    public void saveEventAction(View view)
    {
        String eventName = eventDescET.getText().toString();

        String eventDatetoString = eventDateET.getText().toString();
        LocalDate eventDate = LocalDate.parse(eventDatetoString);

        LocalTime eventTime = LocalTime.parse(time);

        Event newEvent = new Event(eventName, eventDate, eventTime);
        Event.eventsList.add(newEvent);
        finish();
    }
}
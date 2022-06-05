package com.example.siingat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

public class DailyEditActivity extends AppCompatActivity
{
    private EditText dailyDescET;
    private TextView dailyTimeET;
    private CheckBox dailyPriorityCheck;
    boolean isPriority;

    int hour, minute;

    String time, date;

    private NumberPicker numberPicker;
    private String selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_edit);
        initWidgets();

        DaysObject.initDaysObject();
        numberPicker.setMaxValue(DaysObject.getDaysArrayList().size()-1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(DaysObject.daysObjectNames());

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                selectedDay = DaysObject.getDaysArrayList().get(newValue).getName();
            }
        });

    }

    private void initWidgets()
    {
        dailyDescET = findViewById(R.id.dailyDescET);
        dailyTimeET = findViewById(R.id.dailyTimeET);
        dailyPriorityCheck = findViewById(R.id.dailyPriorityCB);
        numberPicker = findViewById(R.id.numberPicker);
    }

    public void timePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;

                time = String.format("%02d:%02d", hour, minute);
                LocalTime localTime = LocalTime.parse(time);
                dailyTimeET.setText(CalendarUtils.formattedTime(localTime));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, onTimeSetListener, hour, minute, false);
        timePickerDialog.show();
    }

    public void onCheckboxClicked(View view) {
        if (dailyPriorityCheck.isChecked()){
            dailyPriorityCheck.setText("Prioritized");
            isPriority = true;
        }
        else {
            dailyPriorityCheck.setText("Set as priority?");
            isPriority = false;
        }
    }

    public void saveDailyAction(View view) {

        String dailyName = dailyDescET.getText().toString();

        LocalTime dailyTime = LocalTime.parse(time);

        Event newDaily = new Event(selectedDay, dailyName, dailyTime, isPriority);
        Event.eventsList.add(newDaily);
        finish();
    }

    public void dailyBackAction(View view) {
        //Masih buat coba-coba
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        date = sdf.format(c.getTime());

        Toast.makeText(getApplicationContext(),"Today is: " + date,Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK);
        //Masih buat coba-coba
        finish();
    }
}
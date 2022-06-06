package com.example.siingat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DailyEditActivity extends AppCompatActivity {
    //SQLite
    private Database database;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore dbFire;

    //Local Java
    private User usr;

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

        //Initial Database
        database = new Database(this);

        //Firebase Auth Initial
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        dbFire = FirebaseFirestore.getInstance();

        DaysObject.initDaysObject();
        numberPicker.setMaxValue(DaysObject.getDaysArrayList().size() - 1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(DaysObject.daysObjectNames());

        selectedDay = "Monday";
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                selectedDay = DaysObject.getDaysArrayList().get(newValue).getName();
            }
        });

    }

    private void initWidgets() {
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
        if (dailyPriorityCheck.isChecked()) {
            dailyPriorityCheck.setText("Prioritized");
            isPriority = true;
        } else {
            dailyPriorityCheck.setText("Set as priority?");
            isPriority = false;
        }
    }

    public void saveDailyAction(View view) {

        String dailyName = dailyDescET.getText().toString();

        LocalTime dailyTime = LocalTime.parse(time);

        Daily newDaily = new Daily(selectedDay, dailyName, dailyTime, isPriority);
        Daily.dailiesList.add(newDaily);

        //SQLite
        try {

            // Create a Dailies object for firebase
            Map<String, Object> dailies = new HashMap<>();
            dailies.put("Day", newDaily.getDay());
            dailies.put("Time", newDaily.getTime());
            dailies.put("Description", newDaily.getName());
            dailies.put("Priority", newDaily.isPriority());

            // Add a new document with a generated ID
            dbFire.collection("users")
                    .document(currentUser.getUid())
                    .collection("Dailies")
                    .add(dailies)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Firestore", "DocumentSnapshot successfully written!");
                            try {
                                SQLiteDatabase db = database.getWritableDatabase();
                                db.execSQL("insert into Dailies(ID_DAILY, UID, Day, Time, Description, Priority) values ('" +
                                        documentReference.getId() + "','" +
                                        currentUser.getUid() + "','" +
                                        newDaily.getDay()+ "','"+
                                        newDaily.getTime().toString()+"','"+
                                        newDaily.getName()+"','" +
                                        newDaily.isPriority()+"')");
                                Log.d("Sqlite", "Stored Data Success");
                            }catch (Exception e){
                                Log.d("Sqlite", "Data has been stored");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Firestore", "Error adding document", e);
                        }
                    });


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Data Error", Toast.LENGTH_LONG).show();
        }
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void dailyBackAction(View view) {
        //Masih buat coba-coba
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        date = sdf.format(c.getTime());

        Toast.makeText(getApplicationContext(), "Today is: " + date, Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK);
        //Masih buat coba-coba
        finish();
    }

}
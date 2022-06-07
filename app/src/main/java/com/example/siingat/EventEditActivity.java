package com.example.siingat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventEditActivity extends AppCompatActivity {
    //SQLite
    private Database database;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore dbFire;

    //Local Java
    private User usr;

    private EditText eventDescET;
    private TextView eventDateET, eventTimeET;
    private CheckBox priorityCheck;
    boolean isPriority;

    final Calendar myCalendar = Calendar.getInstance();
    int hour, minute;

    String time;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();

        //Initial Database
        database = new Database(this);

        //Firebase Auth Initial
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        dbFire = FirebaseFirestore.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        eventDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventEditActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void initWidgets() {
        eventDescET = findViewById(R.id.eventDescET);
        eventDateET = findViewById(R.id.eventDateET);
        eventTimeET = findViewById(R.id.eventTimeET);
        priorityCheck = findViewById(R.id.priorityCB);
    }

    private void updateLabel() {
        String sFormat = "yyyy-MM-dd";
        SimpleDateFormat shortDateFormat = new SimpleDateFormat(sFormat, Locale.US);
        date = shortDateFormat.format(myCalendar.getTime());

        String myFormat = "EEEE, d MMM yyyy";
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

    public void onCheckboxClicked(View view) {
        if (priorityCheck.isChecked()) {
            priorityCheck.setText("Prioritized");
            isPriority = true;
        } else {
            priorityCheck.setText("Set as priority?");
            isPriority = false;
        }
    }

    public void saveEventAction(View view) {
        String eventName = eventDescET.getText().toString();
        if (eventName.isEmpty()) {
            eventDescET.setError("Cannot be Empty");
        } else if (eventName.length() >= 30) {
            eventDescET.setError("Maximum 30 characters");
        } else if (eventDateET.getText().toString().isEmpty()) {
            eventDateET.setError("Cannot be Empty");
        } else if (eventTimeET.getText().toString().isEmpty()) {
            eventTimeET.setError("Cannot be Empty");
        } else {
            LocalDate eventDate = LocalDate.parse(date);

            LocalTime eventTime = LocalTime.parse(time);

            Event newEvent = new Event(eventName, eventDate, eventTime, isPriority);
            Event.eventsList.add(newEvent);

            try {
                // Create a Dailies object for firebase
                Map<String, Object> dailies = new HashMap<>();
                dailies.put("Description", newEvent.getName());
                dailies.put("Date", newEvent.getDate().toString());
                dailies.put("Time", newEvent.getTime().toString());
                dailies.put("Priority", newEvent.isPriority());

                // Add a new document with a generated ID
                dbFire.collection("users")
                        .document(currentUser.getUid())
                        .collection("Events")
                        .add(dailies)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("Firestore", "DocumentSnapshot successfully written!");
                                try {
                                    //SQLite
                                    SQLiteDatabase db = database.getWritableDatabase();
                                    db.execSQL("insert into Events(ID_EVENT, UID, Date, Time, Description, Priority) values ('" +
                                            documentReference.getId() + "','" +
                                            currentUser.getUid() + "','" +
                                            newEvent.getDate().toString() + "','" +
                                            newEvent.getTime().toString() + "','" +
                                            newEvent.getName() + "','" +
                                            newEvent.isPriority() + "')");
                                    Log.d("Sqlite", "Stored Data Success");
                                } catch (Exception e) {
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
            finish();
        }
    }

    public void backAction(View view) {
        finish();
    }
}
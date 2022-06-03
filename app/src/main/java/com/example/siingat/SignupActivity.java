package com.example.siingat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    //SQLite
    private Database database;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore dbFire;

    //Local Java
    private User usr;

    private Button btnUnfocus, btnNext;
    int selectedBtn;
    private EditText etName, etBirth;
    final Calendar myCalendar = Calendar.getInstance();
    private Button[] btnGender = new Button[3];
    private int[] btnIdGender = {R.id.btn_male_gender, R.id.btn_female_gender, R.id.btn_other_gender};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initial Database
        database = new Database(this);

        //Firebase Auth Initial
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        dbFire = FirebaseFirestore.getInstance();

        etName = findViewById(R.id.et_name);

        for (int i = 0; i < btnGender.length; i++) {
            btnGender[i] = (Button) findViewById(btnIdGender[i]);
            btnGender[i].setOnClickListener(this);
        }

        btnUnfocus = btnGender[0];
        selectedBtn = -1;

        etBirth = (EditText) findViewById(R.id.et_birth);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        etBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignupActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        etBirth.setText(dateFormat.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_male_gender:
                selectedBtn = 0;
                Log.d("Btn", "ID SELECTED : " + btnGender[selectedBtn].getText().toString());
                setFocus(btnUnfocus, btnGender[selectedBtn]);
                break;

            case R.id.btn_female_gender:
                selectedBtn = 1;
                Log.d("Btn", "ID SELECTED : " + btnGender[selectedBtn].getText().toString());
                setFocus(btnUnfocus, btnGender[selectedBtn]);
                break;

            case R.id.btn_other_gender:
                selectedBtn = 2;
                Log.d("Btn", "ID SELECTED : " + btnGender[selectedBtn].getText().toString());
                setFocus(btnUnfocus, btnGender[selectedBtn]);
                break;
            case R.id.btn_next:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    etName.setError("Name Cannot be Empty");
                    return;
                } else if (etName.length() >= 23) {
                    etName.setError("Maximum 22 characters");
                    return;
                } else if (selectedBtn == -1){
                    Toast.makeText(getApplicationContext(),"Gender Cannot be Empty",Toast.LENGTH_LONG).show();
                    return;
                }else if (TextUtils.isEmpty(etBirth.getText().toString())) {
                    etBirth.setError("Birth Cannot be Empty");
                    return;
                } else {
                    try {
                        usr = new User(currentUser.getUid().toString(), etName.getText().toString(),
                                btnGender[selectedBtn].getText().toString(), etBirth.getText().toString());

                        try {
                            SQLiteDatabase db = database.getWritableDatabase();
                            db.execSQL("insert into Users(UID, Name, Gender, Birth) values ('" +
                                    usr.getUID() + "','" +
                                    usr.getName() + "','" +
                                    usr.getGender() + "','" +
                                    usr.getBirth() + "')");
                            Log.d("Sqlite", "Stored Data Success");
                        } catch (Exception e) {
                            Log.d("Sqlite", "Data has been stored");
                        }

                        try{
                            //SQLite initial
                            SQLiteDatabase db = database.getWritableDatabase();

                            //Get Data from SQLite
                            Cursor c = db.rawQuery("UPDATE Users set Name = '" + usr.getName().trim() + "', " +
                                    "Gender = '" + usr.getGender().trim() + "', " +
                                    "Birth = '" + usr.getBirth() + "' " +
                                    "where TRIM(UID)='" + currentUser.getUid().toString().trim() + "'", null);
                            c.moveToNext();
                            Log.d("Sqlite", "Update data SQLite Success");
                        } catch (Exception e){
                            Log.d("Sqlite", "Update data SQLite failed");
                        }


                        // Create a new user with a first and last name
                        Map<String, Object> user = new HashMap<>();
                        user.put("Name", usr.getName());
                        user.put("Gender", usr.getGender());
                        user.put("Birth", usr.getBirth());

                        // Add a new document with a generated ID
                        dbFire.collection("users")
                                .document(usr.getUID())
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Firestore", "Error adding document", e);
                                    }
                                });
                        Toast.makeText(getApplicationContext(), "Data tersimpan", Toast.LENGTH_LONG).show();
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Data Error", Toast.LENGTH_LONG).show();
                    } finally {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    break;
                }
        }
    }

    private void setFocus(Button btn_unfocus, Button btn_focus) {
        btn_unfocus.setTextColor(Color.rgb(0, 0, 0));
        btn_unfocus.setBackgroundColor(Color.rgb(224, 224, 224));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(64, 123, 255));
        this.btnUnfocus = btn_focus;
    }
}
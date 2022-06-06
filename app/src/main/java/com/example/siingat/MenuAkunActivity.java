package com.example.siingat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MenuAkunActivity extends AppCompatActivity implements View.OnClickListener {

    //SQLite
    private Database database;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore dbFire;

    //Local Java
    private User usr;

    //Komponen
    private TextView tvName, Theme;
    private Button btnUpdate, btnLogout;
    private EditText etName, etBirth;
    private TextView tvGender, tvEmail;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_akun);

        //firebase initial
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        dbFire = FirebaseFirestore.getInstance();

        //SQLite initial
        database = new Database(this);
        SQLiteDatabase db = database.getReadableDatabase();

        //Get Data from SQLite
        Cursor c = db.rawQuery("SELECT * FROM Users WHERE TRIM(UID) = '" + currentUser.getUid().toString().trim() + "'", null);
        c.moveToNext();
        Log.d("Data select", "UID : " + c.getString(c.getColumnIndex("UID")));

        //User object initial
        usr = new User();

        //Komponen
        btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);
        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
        etBirth = (EditText) findViewById(R.id.et_birth);
        etName = (EditText) findViewById(R.id.name_field);
        tvName = findViewById(R.id.nameAkun);
        tvGender = findViewById(R.id.gender_field);
        tvEmail = findViewById(R.id.email_field);

        //Untuk set textview dan edit text
        usr.setName(c.getString(c.getColumnIndex("Name")));
        usr.setBirth(c.getString(c.getColumnIndex("Birth")));
        usr.setGender(c.getString(c.getColumnIndex("Gender")));

        tvName.setText("Hi, " + usr.getName());
        etName.setText(usr.getName());
        etBirth.setText(usr.getBirth());
        tvGender.setText(usr.getGender());
        tvEmail.setText(currentUser.getEmail());


        //Untuk Tanggal
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
                new DatePickerDialog(MenuAkunActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    //UpdateLabel Tanggal
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        etBirth.setText(dateFormat.format(myCalendar.getTime()));
    }

    //Untuk Log Out
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                FirebaseAuth.getInstance().signOut();
                Log.d("Logout", "Logout Firebase success");
                Toast.makeText(getApplicationContext(), "Logout Success", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                break;
            case R.id.btn_update:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    etName.setError("Name Cannot be Empty");
                    return;
                } else if (etName.length() >= 23) {
                    etName.setError("Maximum 22 characters");
                    return;
                } else if(TextUtils.isEmpty(etBirth.getText().toString())){
                    etBirth.setError("Birth Cannot be Empty");
                    return;
                }else {
                    String name = etName.getText().toString();
                    String birth = etBirth.getText().toString();

                    DocumentReference updateDbfireRef = dbFire.collection("users").document(currentUser.getUid().toString());
                    updateDbfireRef
                            .update("Name", name,
                                    "Birth", birth)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //SQLite initial
                                    SQLiteDatabase db = database.getWritableDatabase();

                                    //Get Data from SQLite
                                    Cursor c = db.rawQuery("UPDATE Users set Name = '" + name.trim() + "', " +
                                            "Birth = '" + birth.trim() + "' " +
                                            "where TRIM(UID)='" + currentUser.getUid().toString().trim() + "'", null);
                                    c.moveToNext();
                                    Log.d("Update Profile", "DocumentSnapshot successfully updated!");
                                    Toast.makeText(getApplicationContext(),"Update Success",Toast.LENGTH_LONG).show();
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Log.w("Update Profile", "Error updating document", e);
                                    Toast.makeText(getApplicationContext(),"Update Failed",Toast.LENGTH_LONG).show();
                                }
                            });
                }
                break;
        }
    }
}
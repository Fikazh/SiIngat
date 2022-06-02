package com.example.siingat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private TextView tvName,Theme;
    private Button btn_update, btn_logout;
    private EditText etName, etBirth;
    private TextView gender, email;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_akun);

        //firebase initial
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //SQLite initial
        database = new Database(this);
        SQLiteDatabase db = database.getReadableDatabase();
        //Get Data from SQLite
        Cursor c = db.rawQuery("SELECT * FROM Users WHERE TRIM(UID) = '"+ currentUser.getUid().toString().trim() +"'", null);
        c.moveToNext();
        Log.d("Data select","UID : " + c.getString(c.getColumnIndex("UID")));

        //User object initial
        usr = new User();

        //Komponen
        btn_update = findViewById(R.id.btn_update);
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        etBirth = (EditText) findViewById(R.id.et_birth);
        etName = (EditText) findViewById(R.id.name_field);
        tvName = findViewById(R.id.nameAkun);
        gender = findViewById(R.id.gender_field);

        //Untuk set textview dan edit text
        usr.setName(c.getString(c.getColumnIndex("Name")));
        usr.setBirth(c.getString(c.getColumnIndex("Birth")));
        usr.setGender(c.getString(c.getColumnIndex("Gender")));

        tvName.setText("Hi, " + usr.getName());
        etName.setText(usr.getName());
        etBirth.setText(usr.getBirth());
        gender.setText(usr.getGender());



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

        //Button Update Profile
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Listener UNTUK UPDATE PROFILE
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
        switch (v.getId()){
            case R.id.btn_logout:
                FirebaseAuth.getInstance().signOut();
                Log.d("Logout","Logout Firebase success");
                Toast.makeText(getApplicationContext(),"Logout Success",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                break;
        }
    }
}
package com.example.siingat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnUnfocus, btnNext;
    private EditText etBirth;
    final Calendar myCalendar = Calendar.getInstance();
    private Button[] btnGender = new Button[3];
    private int[] btnIdGender = {R.id.btn_male_gender, R.id.btn_female_gender, R.id.btn_other_gender};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        for(int i = 0; i < btnGender.length; i++){
            btnGender[i] = (Button) findViewById(btnIdGender[i]);
            btnGender[i].setOnClickListener(this);
        }

        btnUnfocus = btnGender[0];

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
        etBirth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignupActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
    }

    private void updateLabel(){
        String myFormat = "dd/MM/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        etBirth.setText(dateFormat.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_male_gender :
                setFocus(btnUnfocus, btnGender[0]);
                break;

            case R.id.btn_female_gender :
                setFocus(btnUnfocus, btnGender[1]);
                break;

            case R.id.btn_other_gender :
                setFocus(btnUnfocus, btnGender[2]);
                break;
            case R.id.btn_next :
                Intent introIntent = new Intent(SignupActivity.this, IntroActivity.class);
                startActivity(introIntent);
                break;
        }
    }

    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(0,0,0));
        btn_unfocus.setBackgroundColor(Color.rgb(224, 224, 224));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(64, 123, 255));
        this.btnUnfocus = btn_focus;
    }
}
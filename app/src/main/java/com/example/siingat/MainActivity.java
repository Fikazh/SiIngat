package com.example.siingat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore dbFire;

    //Local Java
    private User usr;

    //Database
    private Database database;

    private TextView date, tvName;
    private FloatingActionButton addPlus;
    private BottomNavigationView bottomNavigationView;
    private CircleImageView civProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        civProfile = findViewById(R.id.avatar);
        civProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MenuAkunActivity.class);
                startActivity(i);
            }
        });

        tvName = findViewById(R.id.name);
        usr.setName(c.getString(c.getColumnIndex("Name")));
        tvName.setText("Hi, " + usr.getName());

        date = findViewById(R.id.date);
        Date currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance(DateFormat.LONG).format(currentTime);
        date.setText(formattedDate);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        addPlus = findViewById(R.id.fab);
        addPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog function goes here
                showDialog();
            }
        });
    }

    private void showDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.fab_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button addDaily = dialog.findViewById(R.id.fabDaily);
        Button addEvent = dialog.findViewById(R.id.fabEvent);

        dialog.show();

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, EventEditActivity.class));
            }
        });
    }
}
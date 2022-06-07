package com.example.siingat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private NavController navController;

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
        Cursor c = db.rawQuery("SELECT * FROM Users WHERE TRIM(UID) = '" + currentUser.getUid().toString().trim() + "'", null);
        c.moveToNext();
        Log.d("Data select", "UID : " + c.getString(c.getColumnIndex("UID")));

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
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        addPlus = findViewById(R.id.fab);
        addPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        //if SQLite with UID not null run this
        SQLiteDatabase dbread = database.getReadableDatabase();

        //Backup Cloud to SQLite
        Cursor cc = db.rawQuery("SELECT * FROM Dailies WHERE TRIM(UID) = '" + currentUser.getUid().trim() + "'", null);
        cc.moveToNext();
        if(cc.getCount() == 0){
            try {
                dbFire = FirebaseFirestore.getInstance();
                CollectionReference docRef = dbFire.collection("users").document(currentUser.getUid()).collection("Dailies");
                docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> document = task.getResult().getDocuments();
                            for (int i = 0; i < document.size(); i++) {
                                //default, ISO_LOCAL_DATE
                                LocalTime localtime = LocalTime.parse(document.get(i).getData().get("Time").toString());
                                String strPriority = document.get(i).getData().get("Priority").toString();

                                //Boolean converter
                                Boolean blPriority = false;
                                if (strPriority.equals("1")) {
                                    blPriority = true;
                                } else if (strPriority.equals("0")) {
                                    blPriority = false;
                                } else {
                                    Log.d("Boolean Convert", "failed");
                                }

                                Event newDaily = new Event(document.get(i).getData().get("Day").toString(),
                                        document.get(i).getData().get("Description").toString(),
                                        localtime,
                                        blPriority);

                                SQLiteDatabase db = database.getWritableDatabase();
                                db.execSQL("insert into Dailies(ID_DAILY, UID, Day, Time, Description, Priority) values ('" +
                                        document.get(i).getId() + "','" +
                                        currentUser.getUid() + "','" +
                                        newDaily.getDay() + "','" +
                                        newDaily.getTime().toString() + "','" +
                                        newDaily.getName() + "','" +
                                        newDaily.isPriority() + "')");
                            }
                        }
                    }
                });
                Log.d("Sqlite", "Stored Data Dailies Success");
            }catch (Exception ex){
                Log.d("Sqlite", "Stored Data Dailies Failed");
            }
        }else{
            Log.d("Sqlite", "Data Dailies has Stored");
        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.fab_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button addDaily = dialog.findViewById(R.id.fabDaily);
        Button addEvent = dialog.findViewById(R.id.fabEvent);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.BOTTOM;
        wmlp.y = 250;

        dialog.show();

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, EventEditActivity.class));
            }
        });

        addDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, DailyEditActivity.class);
                startActivityForResult(intent, 10001);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK)) {
            Toast.makeText(getApplicationContext(), "Activity Finished", Toast.LENGTH_SHORT).show();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
    }
}
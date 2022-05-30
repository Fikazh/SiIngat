package com.example.siingat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MenuAkunActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_mainmenu, btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_akun);

        btn_mainmenu = findViewById(R.id.btn_mainMenu);
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);

        btn_mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuAkunActivity.this, MainActivity.class));
            }
        });
    }


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
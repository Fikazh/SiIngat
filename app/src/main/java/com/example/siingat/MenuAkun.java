package com.example.siingat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuAkun extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_akun);

        Button btn_mainmenu = findViewById(R.id.btn_mainMenu);
        Button btn_logout = findViewById(R.id.btn_logout);

        btn_mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuAkun.this, MainActivity.class));
            }
        });
    }


}
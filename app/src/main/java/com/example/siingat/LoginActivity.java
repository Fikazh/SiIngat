package com.example.siingat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnGlLogin, btnFbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnGlLogin = (Button) findViewById(R.id.btn_gl);
        btnGlLogin.setOnClickListener(this);
        btnFbLogin = (Button) findViewById(R.id.btn_fb);
        btnFbLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_gl:
                Intent glIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(glIntent);
                break;
            case R.id.btn_fb:
                break;
        }
    }
}
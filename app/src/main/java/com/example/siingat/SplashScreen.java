package com.example.siingat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    Animation topAnim, leftAnim, rightAnim, fadeInAnim;
    TextView Welcome;
    ImageView logo, circleShape;
    View leftLine, rightLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_animation);
        rightAnim = AnimationUtils.loadAnimation(this, R.anim.right_animation);
        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fadein_animation);

        //Hooks
        Welcome = findViewById(R.id.welcome);
        leftLine = findViewById(R.id.line_left);
        rightLine = findViewById(R.id.line_right);
        circleShape = findViewById(R.id.circle_shape);
        logo = findViewById(R.id.splash_logo);

        Welcome.setAnimation(topAnim);
        leftLine.setAnimation(leftAnim);
        rightLine.setAnimation(rightAnim);
        circleShape.setAnimation(fadeInAnim);
        logo.setAnimation(fadeInAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1500);
    }
}
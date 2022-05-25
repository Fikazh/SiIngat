package com.example.siingat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashScreen extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;

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
                // Check if user is signed in (non-null) and update UI accordingly.
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser!=null){
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(getApplicationContext(), IntroActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 1500);

        //Firebase Auth Initial
        mAuth = FirebaseAuth.getInstance();

//        printHashKey(this);
    }

    private static void printHashKey(Context pContext) {
        try{
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(),0));
                Log.i("TAG","PrintHashKey() Hash Key : " + hashKey);
            }
        }catch (NoSuchAlgorithmException e){
            Log.e("TAG", "PrintHashKey()", e);
        }catch (Exception e){
            Log.e("TAG", "PrintHashKey()",e);
        }
    }
}
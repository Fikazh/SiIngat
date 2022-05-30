package com.example.siingat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore dbFire;

    Animation topAnim, leftAnim, rightAnim, fadeInAnim;
    TextView Welcome;
    ImageView logo, circleShape;
    View leftLine, rightLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Firebase Auth Initial
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        dbFire = FirebaseFirestore.getInstance();

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
                if (currentUser!=null){
                    DocumentReference docRef = dbFire.collection("users").document(currentUser.getUid().toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("Firestore doc splsh", "DocumentSnapshot data: " + document.getData());
                                    Intent i = new Intent(getApplicationContext(), cobaLogin.class);
                                    startActivity(i);
                                } else {
                                    Log.d("Firestore doc splsh", "No such document");
                                    Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                                    startActivity(i);
                                }
                            } else {
                                Log.d("Firestore doc splsh", "get failed with ", task.getException());
                            }
                        }
                    });
//                    Log.d("Check Doc","name of doc : " + docRef.getId());
                }else{
                    Intent i = new Intent(getApplicationContext(), IntroActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 1500);

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
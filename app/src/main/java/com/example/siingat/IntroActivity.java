package com.example.siingat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        Intro1Fragment mIntro1Fragment = new Intro1Fragment;
        mFragmentTransaction.add(R.id.intro_frame_container, mIntro1Fragment, Intro1Fragment.class.getSimpleName());
        Log.d("MyFlexibleFragment", "Fragment Name :"+Intro1Fragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }
}
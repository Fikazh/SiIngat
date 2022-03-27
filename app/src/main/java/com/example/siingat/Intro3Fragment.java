package com.example.siingat;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Intro3Fragment extends Fragment implements View.OnClickListener {

    public Intro3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro3, container, false);
        Button btnNext3 = (Button) view.findViewById(R.id.btn_intro3);
        btnNext3.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_intro3){
            Intent homeIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(homeIntent);
        }
    }
}
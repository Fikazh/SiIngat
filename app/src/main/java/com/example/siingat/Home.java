package com.example.siingat;

import android.animation.LayoutTransition;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements View.OnClickListener {

    CardView event, daily;
    LinearLayoutCompat eventDetails, dailyDetails;
    ConstraintLayout eventLayout, dailyLayout;
    ImageView eventDown, dailyDown;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        daily = view.findViewById(R.id.daily);
        daily.setOnClickListener(this);
        dailyDetails = view.findViewById(R.id.daily_details);
        dailyLayout = view.findViewById(R.id.clDaily);
        dailyDown = view.findViewById(R.id.daily_down);
        dailyLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        event = view.findViewById(R.id.event);
        event.setOnClickListener(this);
        eventDetails = view.findViewById(R.id.event_details);
        eventLayout = view.findViewById(R.id.clEvent);
        eventDown = view.findViewById(R.id.event_down);
        eventLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        return view;
    }


    @Override
    public void onClick(View view) {
        int v;
        switch (view.getId()) {
            case R.id.daily:
                v = (dailyDetails.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

                TransitionManager.beginDelayedTransition(dailyLayout, new AutoTransition());
                dailyDetails.setVisibility(v);

                if (dailyDetails.getVisibility() == View.VISIBLE){
                    dailyDown.setVisibility(View.GONE);
                }
                else
                    dailyDown.setVisibility(View.VISIBLE);
                break;

            case R.id.event:
                v = (eventDetails.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

                TransitionManager.beginDelayedTransition(eventLayout, new AutoTransition());
                eventDetails.setVisibility(v);

                if (eventDetails.getVisibility() == View.VISIBLE){
                    eventDown.setVisibility(View.GONE);
                }
                else
                    eventDown.setVisibility(View.VISIBLE);
                break;
        }
    }
}
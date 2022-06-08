package com.example.siingat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private ListView dailyListView, eventListView;

    List<String> daysList;
    List<Daily> childList;
    Map<String, List<Daily>> dailyCollection;
    ExpandableListView expandableListView;
    ExpandableListAdapter dailyExpandableListAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
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
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        dailyListView = view.findViewById(R.id.todayListView);
        setTodayAdapter();

        eventListView = view.findViewById(R.id.eventListView);
        setFutureEventAdapter();

        expandableListView = view.findViewById(R.id.homeDailies);
        setExpandableDailyAdapter();

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Object selected = dailyExpandableListAdapter.getChild(i,i1);
                return true;
            }
        });

        return view;
    }

    private void setTodayAdapter() {
        ArrayList<Daily> dailyEvents = Daily.dailyForToday();
        DailyAdapter dailyAdapter = new DailyAdapter(getActivity().getApplicationContext(), dailyEvents);
        dailyListView.setAdapter(dailyAdapter);
    }

    private void setFutureEventAdapter()
    {
        ArrayList<Event> futureEvents = Event.eventsForLater();
        FutureEventAdapter futureEventAdapter = new FutureEventAdapter(getActivity().getApplicationContext(), futureEvents);
        eventListView.setAdapter(futureEventAdapter);
    }

    private void setExpandableDailyAdapter() {
        createGroupList();
        createCollection();
        dailyExpandableListAdapter = new DailyExpandableListAdapter(getActivity().getApplicationContext(), daysList, dailyCollection);
        expandableListView.setAdapter(dailyExpandableListAdapter);
    }

    private void createGroupList() {
        daysList = new ArrayList<>();
        daysList.add("Monday");
        daysList.add("Tuesday");
        daysList.add("Wednesday");
        daysList.add("Thursday");
        daysList.add("Friday");
        daysList.add("Saturday");
        daysList.add("Sunday");
    }

    private void createCollection() {
        dailyCollection = new HashMap<String, List<Daily>>();

        for(String day : daysList){
            loadChild(Daily.dailiesList, day);
            dailyCollection.put(day, childList);
        }
    }

    private void loadChild(ArrayList<Daily> dailies, String day) {
        childList = new ArrayList<>();

        Collections.sort(dailies, new Comparator<Daily>() {
            @Override
            public int compare(Daily d1, Daily d2) {
                return d1.getTime().compareTo(d2.getTime());
            }
        });

        for(Daily daily : dailies) {
            if(daily.getDay().equals(day))
                childList.add(daily);
        }
    }

    @Override
    public void onClick(View view) {
        int v;
        switch (view.getId()) {

        }
    }
}
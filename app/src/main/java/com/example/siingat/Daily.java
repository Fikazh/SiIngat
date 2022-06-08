package com.example.siingat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Daily {
    public static ArrayList<Daily> dailiesList = new ArrayList<>();

    public static ArrayList<Daily> dailyForToday()
    {
        LocalDate date = LocalDate.now();
        String today = date.format(DateTimeFormatter.ofPattern("EEEE"));

        ArrayList<Daily> dailies = new ArrayList<>();

        for(Daily daily : dailiesList)
        {
            if(daily.getDay().equals(today))
                dailies.add(daily);
        }

        Collections.sort(dailies, new Comparator<Daily>() {

            @Override
            public int compare(Daily d1, Daily d2) {
                return d1.getTime().compareTo(d2.getTime());
            }
        });

        return dailies;
    }

    private String name, day;
    private LocalTime time;
    private boolean priority;

    //Daily constructor
    public Daily(String name, String day,LocalTime time, boolean priority)
    {
        this.day = day;
        this.name = name;
        this.time = time;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }
}
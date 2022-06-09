package com.example.siingat;

import android.util.Log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Today {
    public static ArrayList<Today> todayList = new ArrayList<>();


    public static ArrayList<Today> dailyEventToday(ArrayList<Daily> dailiesList, ArrayList<Event> eventsList) {
        ArrayList<Today> dailyEvents = new ArrayList<>();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();


        //add to dailyEvents form dailiesList and eventsList
        for (Daily daily : dailiesList) {
            Today dailyToday = new Today(daily.getName(), daily.getDay(), daily.getTime(), daily.isPriority());

            String today = date.format(DateTimeFormatter.ofPattern("EEEE"));

            if (daily.getDay().equals(today)) {
                if (daily.getTime().isAfter(time)) {
                    dailyEvents.add(dailyToday);
                }
            }
        }

        for (Event event : eventsList) {
            Today eventToday = new Today(event.getName(), event.getDate(), event.getTime(), event.isPriority());
            if (event.getDate().equals(date)) {
                if (event.getTime().isAfter(time)) {
                    dailyEvents.add(eventToday);
                }
            }
        }

        Collections.sort(dailyEvents, new Comparator<Today>() {
            @Override
            public int compare(Today td1, Today td2) {
                return td1.getTime().compareTo(td2.getTime());
            }
        });
        for (int i = 0; i < dailyEvents.size(); i++){
            todayList.add(dailyEvents.get(i));
        }

        return dailyEvents;
    }

    private String name, day;
    private LocalDate date;
    private LocalTime time;
    private boolean priority;

    //Daily constructor
    public Today(String name, String day, LocalTime time, boolean priority) {
        this.day = day;
        this.name = name;
        this.time = time;
        this.priority = priority;
    }

    //Event constructor
    public Today(String name, LocalDate date, LocalTime time, boolean priority) {
        this.name = name;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

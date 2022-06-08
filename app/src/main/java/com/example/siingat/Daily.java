package com.example.siingat;
import java.time.LocalTime;
import java.util.ArrayList;

public class Daily {
    public static ArrayList<Daily> dailiesList = new ArrayList<>();
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
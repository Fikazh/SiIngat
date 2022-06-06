package com.example.siingat;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Event
{
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    public static ArrayList<Event> dailyForDay()
    {
        LocalDate date = LocalDate.now();
        String today = date.format(DateTimeFormatter.ofPattern("EEEE"));

        ArrayList<Event> dailies = new ArrayList<>();

        for(Event daily : eventsList)
        {
            if(daily.getDay().equals(today))
                dailies.add(daily);
        }

        return dailies;
    }


    private String name, day;
    private LocalDate date;
    private LocalTime time;
    private boolean isPriority;

    //Event constructor
    public Event(String name, LocalDate date, LocalTime time)
    {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    //Daily constructor
    public Event(String day, String name, LocalTime time, boolean priority)
    {
        this.day = day;
        this.name = name;
        this.time = time;
        this.isPriority = priority;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isPriority() {
        return isPriority;
    }

    public void setPriority(boolean priority) {
        isPriority = priority;
    }
}

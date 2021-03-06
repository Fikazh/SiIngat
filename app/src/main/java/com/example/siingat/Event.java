package com.example.siingat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Event {
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();

        for (Event event : eventsList) {
            if (event.getDate().equals(date))
                events.add(event);
        }

        Collections.sort(events, new Comparator<Event>() {

            @Override
            public int compare(Event e1, Event e2) {
                return e1.getTime().compareTo(e2.getTime());
            }
        });

        return events;
    }

    public static ArrayList<Event> eventsForLater(){
        ArrayList<Event> events = new ArrayList<>();
        LocalDate localDate = LocalDate.now();

        for (Event event : eventsList) {
            if(event.getDate().isAfter(localDate))
                events.add(event);
        }

        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event ev1, Event ev2) {
                return ev1.getTime().compareTo(ev2.getTime());
            }
        });

        Collections.sort(events, new Comparator<Event>() {

            @Override
            public int compare(Event e1, Event e2) {
                return e1.getDate().compareTo(e2.getDate());
            }
        });

        return events;
    }

    private String name;
    private LocalDate date;
    private LocalTime time;
    private boolean priority;


    //Event constructor
    public Event(String name, LocalDate date, LocalTime time, boolean priority) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
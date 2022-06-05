package com.example.siingat;

import java.util.ArrayList;

public class DaysObject {
    private static ArrayList<DaysObject> daysArrayList;

    private int id;
    private String name;

    public DaysObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static void initDaysObject(){
        daysArrayList = new ArrayList<>();

        DaysObject monday = new DaysObject(0, "Monday");
        daysArrayList.add(monday);

        DaysObject tuesday = new DaysObject(1, "Tuesday");
        daysArrayList.add(tuesday);

        DaysObject wednesday = new DaysObject(2, "Wednesday");
        daysArrayList.add(wednesday);

        DaysObject thursday = new DaysObject(3, "Thursday");
        daysArrayList.add(thursday);

        DaysObject friday = new DaysObject(4, "Friday");
        daysArrayList.add(friday);

        DaysObject saturday = new DaysObject(5, "Saturday");
        daysArrayList.add(saturday);

        DaysObject sunday = new DaysObject(6, "Sunday");
        daysArrayList.add(sunday);
    }

    public static ArrayList<DaysObject> getDaysArrayList() {
        return daysArrayList;
    }

    public static String[] daysObjectNames(){
        String[] names = new String[daysArrayList.size()];
        for(int i = 0; i< daysArrayList.size(); i++){
            names[i] = daysArrayList.get(i).name;
        }

        return names;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

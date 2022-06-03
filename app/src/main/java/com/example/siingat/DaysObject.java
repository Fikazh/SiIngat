package com.example.siingat;

import java.util.ArrayList;

public class DaysObject {
    private static ArrayList<DaysObject> DaysArrayList;

    int id;
    String name;

    public DaysObject(int id, String name) {
        this.id = id;
        this.name = name;
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

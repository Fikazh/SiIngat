package com.example.siingat;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String UID, Name, Gender, Birth;


    public User(String UID, String name, String gender, String birth) {
        this.UID = UID;
        this.Name = name;
        this.Gender = gender;
        this.Birth = birth;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public String getBirth() {
        return Birth;
    }

    public void setBirth(String birth) {
        this.Birth = birth;
    }
}

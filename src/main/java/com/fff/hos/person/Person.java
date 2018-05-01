package com.fff.hos.person;

import com.google.gson.annotations.SerializedName;

public class Person {
    @SerializedName("email")
    private String Email;

    @SerializedName("displayname")
    private String DisplayName;

    @SerializedName("age")
    private int Age;

    @SerializedName("interests")
    private String Interests;

    @SerializedName("description")
    private String Description;

    @SerializedName("location")
    private String Location;

    @SerializedName("activities")
    private String Activities;

    @SerializedName("influence")
    private int Influence;

    public Person() {

    }

    public Person(String email, String displayName, int age, String interests, String description, String location, String activities, int influence) {
        Email = email;
        DisplayName = displayName;
        Age = age;
        Interests = interests;
        Description = description;
        Location = location;
        Activities = activities;
        Influence = influence;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getInterests() {
        return Interests;
    }

    public void setInterests(String interests) {
        Interests = interests;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getActivities() {
        return Activities;
    }

    public void setActivities(String activities) {
        Activities = activities;
    }

    public int getInfluence() {
        return Influence;
    }

    public void setInfluence(int influence) {
        Influence = influence;
    }
}

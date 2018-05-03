package com.fff.hos.person;

import com.google.gson.annotations.SerializedName;

public class Person {
    @SerializedName("email")
    private String Email;

    @SerializedName("displayname")
    private String DisplayName;

    @SerializedName("age")
    private int Age;

    @SerializedName("gender")
    private String Gender;

    @SerializedName("interests")
    private String Interests;

    @SerializedName("description")
    private String Description;

    @SerializedName("location")
    private String Location;

    @SerializedName("joinactivities")
    private String JoinActivities;

    @SerializedName("holdactivities")
    private String HoldActivities;

    @SerializedName("goodmember")
    private int GoodMember;

    @SerializedName("goodleader")
    private int GoodLeader;

    @SerializedName("online")
    private int Online;

    public Person() {

    }

    public Person(String email, String displayName, int age, String gender, String interests, String description
            , String location, String joinActivities, String holdActivities, int goodMember, int goodLeader, int online) {
        Email = email;
        DisplayName = displayName;
        Age = age;
        Gender = gender;
        Interests = interests;
        Description = description;
        Location = location;
        JoinActivities = joinActivities;
        HoldActivities = holdActivities;
        GoodMember = goodMember;
        GoodLeader = goodLeader;
        Online = online;
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

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
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

    public String getJoinActivities() {
        return JoinActivities;
    }

    public void setJoinActivities(String joinActivities) {
        JoinActivities = joinActivities;
    }

    public String getHoldActivities() {
        return HoldActivities;
    }

    public void setHoldActivities(String holdActivities) {
        HoldActivities = holdActivities;
    }

    public int getGoodMember() {
        return GoodMember;
    }

    public void setGoodMember(int goodMember) {
        GoodMember = goodMember;
    }

    public int getGoodLeader() {
        return GoodLeader;
    }

    public void setGoodLeader(int goodLeader) {
        GoodLeader = goodLeader;
    }

    public int getOnline() {
        return Online;
    }

    public void setOnline(int online) {
        Online = online;
    }
}

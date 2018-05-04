package com.fff.hos.person;

import com.google.gson.annotations.SerializedName;

public class Person {

    @SerializedName("email")
    private String Email;
    public static String DB_EMAIL = "email";
    public static String DB_USERPASSWORD = "userpassword";
    public static String DB_DISPLAYNAME = "displayname";

    @SerializedName("displayname")
    private String DisplayName;
    public static String DB_AGE = "age";

    @SerializedName("age")
    private int Age;
    public static String DB_GENDER = "gender";

    @SerializedName("gender")
    private String Gender;
    public static String DB_INTERESTS = "interests";

    @SerializedName("interests")
    private String Interests;
    public static String DB_DESCRIPTION = "description";

    @SerializedName("description")
    private String Description;
    public static String DB_LOCATION = "location";

    @SerializedName("location")
    private String Location;
    public static String DB_JOINACTIVITIES = "joinactivities";

    @SerializedName("joinactivities")
    private String JoinActivities;
    public static String DB_HOLDACTIVITIES = "holdactivities";

    @SerializedName("holdactivities")
    private String HoldActivities;
    public static String DB_GOODMEMBER = "goodmember";

    @SerializedName("goodmember")
    private int GoodMember;
    public static String DB_GOODLEADER = "goodleader";

    @SerializedName("goodleader")
    private int GoodLeader;
    public static String DB_ONLINE = "online";

    @SerializedName("online")
    private int Online;
    @SerializedName("userpassword")
    private String UserPassword;

    public Person() {

    }

    public Person(String email, String userPassword, String displayName, int age, String gender, String interests, String description, String location, String joinActivities, String holdActivities, int goodMember, int goodLeader, int online) {
        Email = email;
        UserPassword = userPassword;
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

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
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

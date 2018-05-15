package com.fff.hos.data;

import com.google.gson.annotations.SerializedName;

public class Person {

    @SerializedName("email")
    private String Email;

    @SerializedName("userpassword")
    private String UserPassword;

    @SerializedName("displayname")
    private String DisplayName;

    @SerializedName("age")
    private Integer Age;

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
    private Integer GoodMember;

    @SerializedName("goodleader")
    private Integer GoodLeader;

    @SerializedName("online")
    private Integer Online;

    @SerializedName("newuserpassword")
    private String NewUserPassword;

    public Person() {

    }

    public Person(String email, String userPassword, String displayName, Integer age, String gender, String interests, String description, String location, String joinActivities, String holdActivities, Integer goodMember, Integer goodLeader, Integer online, String newUserPassword) {
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
        NewUserPassword = newUserPassword;
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

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
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

    public Integer getGoodMember() {
        return GoodMember;
    }

    public void setGoodMember(Integer goodMember) {
        GoodMember = goodMember;
    }

    public Integer getGoodLeader() {
        return GoodLeader;
    }

    public void setGoodLeader(Integer goodLeader) {
        GoodLeader = goodLeader;
    }

    public Integer getOnline() {
        return Online;
    }

    public void setOnline(Integer online) {
        Online = online;
    }

    public String getNewUserPassword() {
        return NewUserPassword;
    }

    public void setNewUserPassword(String newUserPassword) {
        NewUserPassword = newUserPassword;
    }
}

package com.fff.hos.data;

import com.google.gson.annotations.SerializedName;

public class Person {

    @SerializedName("id")
    private String Id;

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

    @SerializedName("saveactivities")
    private String SaveActivities;

    @SerializedName("good")
    private Integer Good;

    @SerializedName("nogood")
    private Integer NoGood;

    @SerializedName("online")
    private Integer Online;

    @SerializedName("anonymous")
    private Integer Anonymous;

    @SerializedName("newuserpassword")
    private String NewUserPassword;

    @SerializedName("verifycode")
    private String VerifyCode;

    public Person() {

    }

    public Person(String id, String email, String userPassword, String displayName, Integer age, String gender, String interests, String description, String location, String saveActivities, Integer good, Integer noGood, Integer online, Integer anonymous, String newUserPassword, String verifyCode) {
        Id = id;
        Email = email;
        UserPassword = userPassword;
        DisplayName = displayName;
        Age = age;
        Gender = gender;
        Interests = interests;
        Description = description;
        Location = location;
        SaveActivities = saveActivities;
        Good = good;
        NoGood = noGood;
        Online = online;
        Anonymous = anonymous;
        NewUserPassword = newUserPassword;
        VerifyCode = verifyCode;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getSaveActivities() {
        return SaveActivities;
    }

    public void setSaveActivities(String saveActivities) {
        SaveActivities = saveActivities;
    }

    public Integer getGood() {
        return Good;
    }

    public void setGood(Integer good) {
        Good = good;
    }

    public Integer getNoGood() {
        return NoGood;
    }

    public void setNoGood(Integer noGood) {
        NoGood = noGood;
    }

    public Integer getOnline() {
        return Online;
    }

    public void setOnline(Integer online) {
        Online = online;
    }

    public Integer getAnonymous() {
        return Anonymous;
    }

    public void setAnonymous(Integer anonymous) {
        Anonymous = anonymous;
    }

    public String getNewUserPassword() {
        return NewUserPassword;
    }

    public void setNewUserPassword(String newUserPassword) {
        NewUserPassword = newUserPassword;
    }

    public String getVerifyCode() {
        return VerifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        VerifyCode = verifyCode;
    }
}

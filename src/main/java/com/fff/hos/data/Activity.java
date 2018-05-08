package com.fff.hos.data;

import com.google.gson.annotations.SerializedName;

public class Activity {

    @SerializedName("id")
    private String Id;

    @SerializedName("publisheremail")
    private String PublisherEmail;

    @SerializedName("displayname")
    private String DisplayName;

    @SerializedName("datebegin")
    private String DateBegin;

    @SerializedName("dateend")
    private String DateEnd;

    @SerializedName("location")
    private String Location;

    @SerializedName("status")
    private String Status;

    @SerializedName("image")
    private String Image;

    @SerializedName("description")
    private String Description;

    @SerializedName("tags")
    private String Tags;

    @SerializedName("goodactivity")
    private Integer GoodActivity;

    @SerializedName("attention")
    private Integer Attention;

    @SerializedName("attendees")
    private String Attendees;

    public Activity() {

    }

    public Activity(String id, String publisherEmail, String displayName, String dateBegin, String dateEnd, String location, String status, String image, String description, String tags, Integer goodActivity, Integer attention, String attendees) {
        Id = id;
        PublisherEmail = publisherEmail;
        DisplayName = displayName;
        DateBegin = dateBegin;
        DateEnd = dateEnd;
        Location = location;
        Status = status;
        Image = image;
        Description = description;
        Tags = tags;
        GoodActivity = goodActivity;
        Attention = attention;
        Attendees = attendees;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPublisherEmail() {
        return PublisherEmail;
    }

    public void setPublisherEmail(String publisherEmail) {
        PublisherEmail = publisherEmail;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getDateBegin() {
        return DateBegin;
    }

    public void setDateBegin(String dateBegin) {
        DateBegin = dateBegin;
    }

    public String getDateEnd() {
        return DateEnd;
    }

    public void setDateEnd(String dateEnd) {
        DateEnd = dateEnd;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public Integer getGoodActivity() {
        return GoodActivity;
    }

    public void setGoodActivity(Integer goodActivity) {
        GoodActivity = goodActivity;
    }

    public Integer getAttention() {
        return Attention;
    }

    public void setAttention(Integer attention) {
        Attention = attention;
    }

    public String getAttendees() {
        return Attendees;
    }

    public void setAttendees(String attendees) {
        Attendees = attendees;
    }
}

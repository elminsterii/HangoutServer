package com.fff.hos.data;

import com.google.gson.annotations.SerializedName;

public class Activity {

    public static String DB_ID = "id";
    public static String DB_DISPLAYNAME = "displayname";
    public static String DB_DATEBEGIN = "datebegin";
    public static String DB_DATEEND = "dateend";
    public static String DB_LOCATION = "location";
    public static String DB_STATUS = "status";
    public static String DB_IMAGE = "image";
    public static String DB_DESCRIPTION = "description";
    public static String DB_TAGS = "tags";
    public static String DB_GOODACTIVITY = "goodactivity";
    @SerializedName("id")
    private String Id;
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
    private byte[] Image;
    @SerializedName("description")
    private String Description;
    @SerializedName("tags")
    private String Tags;
    @SerializedName("goodactivity")
    private String GoodActivity;

    public Activity() {

    }

    public Activity(String id, String displayName, String dateBegin, String dateEnd, String location, String status, byte[] image, String description, String tags, String goodActivity) {
        Id = id;
        DisplayName = displayName;
        DateBegin = dateBegin;
        DateEnd = dateEnd;
        Location = location;
        Status = status;
        Image = image;
        Description = description;
        Tags = tags;
        GoodActivity = goodActivity;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
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

    public String getGoodActivity() {
        return GoodActivity;
    }

    public void setGoodActivity(String goodActivity) {
        GoodActivity = goodActivity;
    }
}

package com.fff.hos.data;

import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("id")
    private String Id;

    @SerializedName("ts")
    private String Ts;

    @SerializedName("publisheremail")
    private String PublisherEmail;

    @SerializedName("displayname")
    private String DisplayName;

    @SerializedName("activityid")
    private String ActivityId;

    @SerializedName("content")
    private String Content;

    public Comment() {
    }

    public Comment(String id, String ts, String publisherEmail, String displayName, String activityId, String content) {
        Id = id;
        Ts = ts;
        PublisherEmail = publisherEmail;
        DisplayName = displayName;
        ActivityId = activityId;
        Content = content;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTs() {
        return Ts;
    }

    public void setTs(String ts) {
        Ts = ts;
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

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}

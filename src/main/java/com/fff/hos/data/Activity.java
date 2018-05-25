package com.fff.hos.data;

import com.google.gson.annotations.SerializedName;

public class Activity implements Cloneable {

    public enum ACTIVITY_STATUS {
        ST_ACTIVITY_NOT_START
        ,ST_ACTIVITY_READY_START
        ,ST_ACTIVITY_STARTING
        ,ST_ACTIVITY_DONE
    }

    @SerializedName("id")
    private String Id;

    @SerializedName("publisheremail")
    private String PublisherEmail;

    @SerializedName("publisheruserpassword")
    private String PublisherUserPassword;

    @SerializedName("publishbegin")
    private String PublishBegin;

    @SerializedName("publishend")
    private String PublishEnd;

    @SerializedName("largeactivity")
    private Integer LargeActivity;

    @SerializedName("earlybird")
    private Integer EarlyBird;

    @SerializedName("displayname")
    private String DisplayName;

    @SerializedName("datebegin")
    private String DateBegin;

    @SerializedName("dateend")
    private String DateEnd;

    @SerializedName("location")
    private String Location;

    @SerializedName("status")
    private Integer Status;

    @SerializedName("description")
    private String Description;

    @SerializedName("tags")
    private String Tags;

    @SerializedName("good")
    private Integer Good;

    @SerializedName("nogood")
    private Integer NoGood;

    @SerializedName("attention")
    private Integer Attention;

    @SerializedName("attendees")
    private String Attendees;

    public Activity() {

    }

    public Activity(String id, String publisherEmail, String publisherUserPassword, String publishBegin, String publishEnd, Integer largeActivity, Integer earlyBird, String displayName, String dateBegin, String dateEnd, String location, Integer status, String description, String tags, Integer good, Integer noGood, Integer attention, String attendees) {
        Id = id;
        PublisherEmail = publisherEmail;
        PublisherUserPassword = publisherUserPassword;
        PublishBegin = publishBegin;
        PublishEnd = publishEnd;
        LargeActivity = largeActivity;
        EarlyBird = earlyBird;
        DisplayName = displayName;
        DateBegin = dateBegin;
        DateEnd = dateEnd;
        Location = location;
        Status = status;
        Description = description;
        Tags = tags;
        Good = good;
        NoGood = noGood;
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

    public String getPublisherUserPassword() {
        return PublisherUserPassword;
    }

    public void setPublisherUserPassword(String publisherUserPassword) {
        PublisherUserPassword = publisherUserPassword;
    }

    public String getPublishBegin() {
        return PublishBegin;
    }

    public void setPublishBegin(String publishBegin) {
        PublishBegin = publishBegin;
    }

    public String getPublishEnd() {
        return PublishEnd;
    }

    public void setPublishEnd(String publishEnd) {
        PublishEnd = publishEnd;
    }

    public Integer getLargeActivity() {
        return LargeActivity;
    }

    public void setLargeActivity(Integer largeActivity) {
        LargeActivity = largeActivity;
    }

    public Integer getEarlyBird() {
        return EarlyBird;
    }

    public void setEarlyBird(Integer earlyBird) {
        EarlyBird = earlyBird;
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

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
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

    public boolean checkMembersStillHaveValue() {
        return (PublisherEmail != null && !PublisherEmail.isEmpty())
                || (LargeActivity != null)
                || (EarlyBird != null)
                || (DisplayName != null && !DisplayName.isEmpty())
                || (Tags != null && !Tags.isEmpty());
    }

    @Override
    public boolean equals(Object obj) {
        boolean bIsEqual = true;

        Activity actTarget = (Activity)obj;
        if(!this.EarlyBird.equals(actTarget.getEarlyBird()))
            bIsEqual = false;
        if(!this.Status.equals(actTarget.getStatus()))
            bIsEqual = false;

        return bIsEqual;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

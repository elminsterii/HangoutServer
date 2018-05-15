package com.fff.hos.data.deserializer;

import com.fff.hos.data.Activity;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ActivityDeserializer implements JsonDeserializer<Activity> {
    @Override
    public Activity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) jsonElement;

        JsonElement jeId = jsonObject.get("id");
        JsonElement jePublisherEmail = jsonObject.get("publisheremail");
        JsonElement jePublisherUserPassword = jsonObject.get("publisheruserpassword");
        JsonElement jePublishBegin = jsonObject.get("publishbegin");
        JsonElement jePublishEnd = jsonObject.get("publishend");
        JsonElement jeLargeActivity = jsonObject.get("largeactivity");
        JsonElement jeEarlyBird = jsonObject.get("earlybird");
        JsonElement jeDisplayName = jsonObject.get("displayname");
        JsonElement jeDateBegin = jsonObject.get("datebegin");
        JsonElement jeDateEnd = jsonObject.get("dateend");
        JsonElement jeLocation = jsonObject.get("location");
        JsonElement jeStatus = jsonObject.get("status");
        JsonElement jeDescription = jsonObject.get("description");
        JsonElement jeTags = jsonObject.get("tags");
        JsonElement jeGoodActivity = jsonObject.get("goodactivity");
        JsonElement jeAttention = jsonObject.get("attention");
        JsonElement jeAttendees = jsonObject.get("attendees");

        Activity activity = new Activity();
        activity.setId(jeId.getAsString());
        activity.setPublisherEmail(jePublisherEmail == null ? null : jePublisherEmail.getAsString());
        activity.setPublisherUserPassword(jePublisherUserPassword == null ? null : jePublisherUserPassword.getAsString());
        activity.setPublishBegin(jePublishBegin == null ? null : jePublishBegin.getAsString());
        activity.setPublishEnd(jePublishEnd == null ? null : jePublishEnd.getAsString());
        activity.setLargeActivity(jeLargeActivity == null ? null : jeLargeActivity.getAsInt());
        activity.setEarlyBird(jeEarlyBird == null ? null : jeEarlyBird.getAsInt());
        activity.setDisplayName(jeDisplayName == null ? null : jeDisplayName.getAsString());
        activity.setDateBegin(jeDateBegin == null ? null : jeDateBegin.getAsString());
        activity.setDateEnd(jeDateEnd == null ? null : jeDateEnd.getAsString());
        activity.setLocation(jeLocation == null ? null : jeLocation.getAsString());
        activity.setStatus(jeStatus == null ? null : jeStatus.getAsString());
        activity.setDescription(jeDescription == null ? null : jeDescription.getAsString());
        activity.setTags(jeTags == null ? null : jeTags.getAsString());
        activity.setGoodActivity(jeGoodActivity == null ? null : jeGoodActivity.getAsInt());
        activity.setAttention(jeAttention == null ? null : jeAttention.getAsInt());
        activity.setAttendees(jeAttendees == null ? null : jeAttendees.getAsString());

        return activity;
    }
}

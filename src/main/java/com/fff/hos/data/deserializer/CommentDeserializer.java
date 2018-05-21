package com.fff.hos.data.deserializer;

import com.fff.hos.data.Comment;
import com.google.gson.*;

import java.lang.reflect.Type;

public class CommentDeserializer implements JsonDeserializer<Comment> {
    @Override
    public Comment deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) jsonElement;

        JsonElement jeId = jsonObject.get("id");
        JsonElement jeTs = jsonObject.get("ts");
        JsonElement jePublisherEmail = jsonObject.get("publisheremail");
        JsonElement jeDisplayName = jsonObject.get("displayname");
        JsonElement jeActivityId = jsonObject.get("activityid");
        JsonElement jeContent = jsonObject.get("content");

        Comment comment = new Comment();
        comment.setId(jeId == null ? null : jeId.getAsString());
        comment.setTs(jeTs == null ? null : jeTs.getAsString());
        comment.setPublisherEmail(jePublisherEmail == null ? null : jePublisherEmail.getAsString());
        comment.setDisplayName(jeDisplayName == null ? null : jeDisplayName.getAsString());
        comment.setActivityId(jeActivityId == null ? null : jeActivityId.getAsString());
        comment.setContent(jeContent == null ? null : jeContent.getAsString());

        return comment;
    }
}

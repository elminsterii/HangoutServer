package com.fff.hos.data.deserializer;

import com.fff.hos.data.Person;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PersonDeserializer implements JsonDeserializer<Person> {
    @Override
    public Person deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) jsonElement;

        JsonElement jeEmail = jsonObject.get("email");
        JsonElement jeUserPassword = jsonObject.get("userpassword");
        JsonElement jeDisplayName = jsonObject.get("displayname");
        JsonElement jeAge = jsonObject.get("age");
        JsonElement jeInterests = jsonObject.get("interests");
        JsonElement jeDescription = jsonObject.get("description");
        JsonElement jeLocation = jsonObject.get("location");
        JsonElement jeJoinActivities = jsonObject.get("joinactivities");
        JsonElement jeHoldActivities = jsonObject.get("holdactivities");
        JsonElement jeGoodMember = jsonObject.get("goodmember");
        JsonElement jeGoodLeader = jsonObject.get("goodleader");
        JsonElement jeOnline = jsonObject.get("online");
        JsonElement jeNewUserPassword = jsonObject.get("newuserpassword");

        Person person = new Person();
        person.setEmail(jeEmail.getAsString());
        person.setUserPassword(jeUserPassword == null ? null : jeUserPassword.getAsString());
        person.setDisplayName(jeDisplayName == null ? null : jeDisplayName.getAsString());
        person.setAge(jeAge == null ? null : jeAge.getAsInt());
        person.setInterests(jeInterests == null ? null : jeInterests.getAsString());
        person.setDescription(jeDescription == null ? null : jeDescription.getAsString());
        person.setLocation(jeLocation == null ? null : jeLocation.getAsString());
        person.setJoinActivities(jeJoinActivities == null ? null : jeJoinActivities.getAsString());
        person.setHoldActivities(jeHoldActivities == null ? null : jeHoldActivities.getAsString());
        person.setGoodMember(jeGoodMember == null ? null : jeGoodMember.getAsInt());
        person.setGoodLeader(jeGoodLeader == null ? null : jeGoodLeader.getAsInt());
        person.setOnline(jeOnline == null ? null : jeOnline.getAsInt());
        person.setUserPassword(jeNewUserPassword == null ? null : jeNewUserPassword.getAsString());

        return person;
    }
}

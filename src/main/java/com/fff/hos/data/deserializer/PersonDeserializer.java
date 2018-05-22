package com.fff.hos.data.deserializer;

import com.fff.hos.data.Person;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PersonDeserializer implements JsonDeserializer<Person> {
    @Override
    public Person deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) jsonElement;

        JsonElement jeId = jsonObject.get("id");
        JsonElement jeEmail = jsonObject.get("email");
        JsonElement jeUserPassword = jsonObject.get("userpassword");
        JsonElement jeDisplayName = jsonObject.get("displayname");
        JsonElement jeAge = jsonObject.get("age");
        JsonElement jeInterests = jsonObject.get("interests");
        JsonElement jeDescription = jsonObject.get("description");
        JsonElement jeLocation = jsonObject.get("location");
        JsonElement jeSaveActivities = jsonObject.get("saveactivities");
        JsonElement jeGood = jsonObject.get("good");
        JsonElement jeNoGood = jsonObject.get("nogood");
        JsonElement jeOnline = jsonObject.get("online");
        JsonElement jeAnonymous = jsonObject.get("anonymous");
        JsonElement jeNewUserPassword = jsonObject.get("newuserpassword");

        Person person = new Person();
        person.setId(jeId == null ? null : jeId.getAsString());
        person.setEmail(jeEmail.getAsString());
        person.setUserPassword(jeUserPassword == null ? null : jeUserPassword.getAsString());
        person.setDisplayName(jeDisplayName == null ? null : jeDisplayName.getAsString());
        person.setAge(jeAge == null ? null : jeAge.getAsInt());
        person.setInterests(jeInterests == null ? null : jeInterests.getAsString());
        person.setDescription(jeDescription == null ? null : jeDescription.getAsString());
        person.setLocation(jeLocation == null ? null : jeLocation.getAsString());
        person.setSaveActivities(jeSaveActivities == null ? null : jeSaveActivities.getAsString());
        person.setGood(jeGood == null ? null : jeGood.getAsInt());
        person.setNoGood(jeNoGood == null ? null : jeNoGood.getAsInt());
        person.setOnline(jeOnline == null ? null : jeOnline.getAsInt());
        person.setAnonymous(jeAnonymous == null ? null : jeAnonymous.getAsInt());
        person.setUserPassword(jeNewUserPassword == null ? null : jeNewUserPassword.getAsString());

        return person;
    }
}

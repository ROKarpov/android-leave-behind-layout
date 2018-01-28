package com.romankarpov.leavebehindlayout.demo.services;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.romankarpov.leavebehindlayout.demo.model.Contact;


public class ContactDeserializer implements com.google.gson.JsonDeserializer<Contact> {
    private final static String NAME_NAME = "name";
    private final static String SURNAME_NAME = "surname";
    private final static String GENDER_NAME = "gender";
    private final static String REGION_NAME = "region";
    private final static String AGE_NAME = "age";
    private final static String TITLE_NAME = "title";
    private final static String PHONE_NAME = "phone";
    private final static String BIRTHDAY_NAME = "birthday";
    private final static String BIRTHDAY_DMY_FORMAT_NAME = "dmy";
    private final static String BIRTHDAY_MDY_FORMAT_NAME = "mdy";
    private final static String BIRTHDAY_TIMESTAMP_NAME = "raw";
    private final static String EMAIL_NAME = "email";
    private final static String PHOTO_NAME = "photo";

    private final static String BIRTHDAY_PARSE_ERROR_MSG = "The birthday cannot be parsed.";

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Contact deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        Contact contact = new Contact();
        contact.setName(jsonObject.get(NAME_NAME).getAsString());
        contact.setSurname(jsonObject.get(SURNAME_NAME).getAsString());
        contact.setGender(jsonObject.get(GENDER_NAME).getAsString());
        contact.setRegion(jsonObject.get(REGION_NAME).getAsString());
        contact.setAge(jsonObject.get(AGE_NAME).getAsInt());
        contact.setTitle(jsonObject.get(TITLE_NAME).getAsString());
        contact.setPhone(jsonObject.get(PHONE_NAME).getAsString());
        contact.setEmail(jsonObject.get(EMAIL_NAME).getAsString());
        contact.setPhotoUri(jsonObject.get(PHOTO_NAME).getAsString());

        try {
            final JsonObject birthdayObject =  jsonObject.get(BIRTHDAY_NAME).getAsJsonObject();
            final String dmyBirthday = birthdayObject.get(BIRTHDAY_DMY_FORMAT_NAME).getAsString();
            contact.setBirthday(mDateFormat.parse(dmyBirthday));
        }
        catch (ParseException pe) {
            throw new JsonParseException(BIRTHDAY_PARSE_ERROR_MSG, pe);
        }

        return contact;
    }
}

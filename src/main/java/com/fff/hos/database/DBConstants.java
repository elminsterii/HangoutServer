package com.fff.hos.database;

class DBConstants {

    //Person constants
    static final String TABLE_NAME_PERSON = "persons";
    static final String PERSON_COL_ID = "id";
    static final String PERSON_COL_TS = "ts";
    static final String PERSON_COL_EMAIL = "email";
    static final String PERSON_COL_USERPASSWORD = "userpassword";
    static final String PERSON_COL_DISPLAYNAME = "displayname";
    static final String PERSON_COL_AGE = "age";
    static final String PERSON_COL_GENDER = "gender";
    static final String PERSON_COL_INTERESTS = "interests";
    static final String PERSON_COL_DESCRIPTION = "description";
    static final String PERSON_COL_LOCATION = "location";
    static final String PERSON_COL_JOINACTIVITIES = "joinactivities";
    static final String PERSON_COL_HOLDACTIVITIES = "holdactivities";
    static final String PERSON_COL_GOODMEMBER = "goodmember";
    static final String PERSON_COL_GOODLEADER = "goodleader";
    static final String PERSON_COL_ONLINE = "online";

    //Activity constants
    static final String TABLE_NAME_ACTIVITY = "activities";
    static final String ACTIVITY_COL_ID = "id";
    static final String ACTIVITY_COL_TS = "ts";
    static final String ACTIVITY_COL_PUBLISHEREMAIL = "publisheremail";
    static final String ACTIVITY_COL_PUBLISHBEGIN = "publishbegin";
    static final String ACTIVITY_COL_PUBLISHEND = "publishend";
    static final String ACTIVITY_COL_LARGEACTIVITY = "largeactivity";
    static final String ACTIVITY_COL_EARLYBIRD = "earlybird";
    static final String ACTIVITY_COL_DISPLAYNAME = "displayname";
    static final String ACTIVITY_COL_DATEBEGIN = "datebegin";
    static final String ACTIVITY_COL_DATEEND = "dateend";
    static final String ACTIVITY_COL_LOCATION = "location";
    static final String ACTIVITY_COL_STATUS = "status";
    static final String ACTIVITY_COL_DESCRIPTION = "description";
    static final String ACTIVITY_COL_TAGS = "tags";
    static final String ACTIVITY_COL_GOODACTIVITY = "goodactivity";
    static final String ACTIVITY_COL_ATTENTION = "attention";
    static final String ACTIVITY_COL_ATTENDEES = "attendees";

    //Comment constants
    static final String TABLE_NAME_COMMENT = "comments";
    static final String COMMENT_COL_ID = "id";
    static final String COMMENT_COL_TS = "ts";
    static final String COMMENT_COL_PUBLISHEREMAIL = "publisheremail";
    static final String COMMENT_COL_DISPLAYNAME = "displayname";
    static final String COMMENT_COL_ACTIVITYID = "activityid";
    static final String COMMENT_COL_CONTENT = "content";
}

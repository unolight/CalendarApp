package com.mixerbox.mixerboxcalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {

    Date startDate, endDate;
    int alertTime;
    String name, type;
    boolean alert;

    Event(String startDate, String name) throws ParseException {
        this.startDate = DB.dateParser.parse(startDate);
        this.name = name;
    }

}
